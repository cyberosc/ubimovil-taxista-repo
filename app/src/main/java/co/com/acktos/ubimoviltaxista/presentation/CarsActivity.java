package co.com.acktos.ubimoviltaxista.presentation;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.List;

import co.com.acktos.ubimoviltaxista.R;
import co.com.acktos.ubimoviltaxista.android.DividerItemDecoration;
import co.com.acktos.ubimoviltaxista.app.Config;
import co.com.acktos.ubimoviltaxista.controllers.CarsController;
import co.com.acktos.ubimoviltaxista.models.Car;
import co.com.acktos.ubimoviltaxista.presentation.adapters.CarsAdapter;

public class CarsActivity extends AppCompatActivity implements CarsAdapter.OnRecyclerViewClickListener{


    //Attributes
    List<Car> cars;

    //Components
    CarsController carsControllers;

    //UI References
    private Spinner spinnerCars;
    private Button btnSelectCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cars);


        //Initialize UI
        spinnerCars = (Spinner) findViewById(R.id.spinner_cars);
        btnSelectCar= (Button) findViewById(R.id.btn_select_car);

        //Initialize components
        carsControllers=new CarsController(this);

        carsControllers.getCars(new Response.Listener<List<Car>>() {
            @Override
            public void onResponse(List<Car> carsList) {

                Log.i(Config.DEBUG_TAG, "on response get cars:"+carsList.size());
                cars=carsList;
                ArrayAdapter adapter =
                        new ArrayAdapter(CarsActivity.this, android.R.layout.simple_spinner_item, carsList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinnerCars.setAdapter(adapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        btnSelectCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(cars!=null){
                    final Car car=cars.get(spinnerCars.getSelectedItemPosition());

                    Log.i(Config.DEBUG_TAG, "selected car:"+car.getPlate());

                    new MaterialDialog.Builder(CarsActivity.this)
                            .content(R.string.msg_confirm_set_car)
                            .positiveText(R.string.accept)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {

                                    carsControllers.setCar(car.getId(), new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String s) {

                                            launchServicesActivity();
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError volleyError) {


                                        }
                                    });

                                }
                            })
                            .show();
                }

            }
        });


    }


    private void launchServicesActivity(){

        Intent i=new Intent(this,MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    @Override
    public void onRecyclerViewClick(View v, final int position) {


    }
}
