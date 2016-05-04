package co.com.acktos.ubimoviltaxista.presentation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.List;

import co.com.acktos.ubimoviltaxista.R;
import co.com.acktos.ubimoviltaxista.controllers.CreditsController;
import co.com.acktos.ubimoviltaxista.models.Car;

public class BalanceActivity extends AppCompatActivity {


    //Components
    CreditsController creditsController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);


        //Initialize components
        creditsController=new CreditsController(this);
        creditsController.getCredits(new Response.Listener<List<Car>>() {
            @Override
            public void onResponse(List<Car> cars) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

    }
}
