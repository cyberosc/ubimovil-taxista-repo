package co.com.acktos.ubimoviltaxista.presentation;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.List;

import co.com.acktos.ubimoviltaxista.R;
import co.com.acktos.ubimoviltaxista.app.Config;
import co.com.acktos.ubimoviltaxista.controllers.CreditsController;
import co.com.acktos.ubimoviltaxista.models.Car;

public class BalanceActivity extends AppCompatActivity {


    //Components
    CreditsController creditsController;
    MaterialDialog mProgressDialog;

    //UI references
    private EditText mCreditCode;


    //Attributes
    private boolean isPendingTransaction=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);

        //Initialize UI
        mCreditCode =(EditText) findViewById(R.id.txt_credit_code);


        //Initialize components
        creditsController=new CreditsController(this);


    }


    public void onPurchaseCredit(View view){

        String code=mCreditCode.getText().toString();
        code=code.trim();

        if(!isPendingTransaction){

            launchProgressDialog();
            isPendingTransaction=true;

            creditsController.purchaseCredit(code, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    String title;
                    String message;

                    if(response.equals(Config.SUCCESS_CODE)){

                        title=getString(R.string.msg_title_success_purchase_credit);
                        message=getString(R.string.msg_success_purchase_credit);

                    }else{

                        title=getString(R.string.msg_title_failed_purchase_credit);
                        message=getString(R.string.msg_failed_purchase_credit);
                    }
                    isPendingTransaction=false;
                    dismissProgressDialog();

                    showResultMessageDialog(title,message);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    isPendingTransaction = false;
                    dismissProgressDialog();
                }
            });
        }


    }

    private void showResultMessageDialog(String title,String message){

        new MaterialDialog.Builder(this)
                .title(title)
                .content(message)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dismissProgressDialog();
                    }
                })
                .show();
    }

    private void launchProgressDialog() {

        mProgressDialog = new MaterialDialog.Builder(this)
                .content(R.string.plase_wait)
                .progress(true, 0)
                .widgetColorRes(R.color.color_ubimovil_grey_800)
                .autoDismiss(false)
                .show();
    }

    private void dismissProgressDialog(){

        if(mProgressDialog!=null){
            mProgressDialog.dismiss();
        }
    }
}
