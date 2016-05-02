package co.com.acktos.ubimoviltaxista.presentation;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import co.com.acktos.ubimoviltaxista.R;
import co.com.acktos.ubimoviltaxista.app.Config;
import co.com.acktos.ubimoviltaxista.controllers.ServicesController;
import co.com.acktos.ubimoviltaxista.models.Service;
import co.com.acktos.ubimoviltaxista.models.State;
import co.com.acktos.ubimoviltaxista.presentation.adapters.ServicesAdapter;
import co.com.acktos.ubimoviltaxista.presentation.adapters.StatesAdapter;


public class TrackingActivity extends AppCompatActivity
        implements StatesAdapter.OnRecyclerViewClickListener, OnMapReadyCallback {


    private List<State> mStates;
    private List<State> mAvailableStates;
    private Service mService;
    private RecyclerView mStatesView;
    private RecyclerView.LayoutManager mLayoutManager;
    private StatesAdapter mRecyclerAdapter;
    private ServicesController servicesController;
    private MaterialDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        servicesController = new ServicesController(this);

        Intent intent = getIntent();
        mService = (Service) intent.getSerializableExtra(Config.KEY_SERVICE);

        SupportMapFragment mDriverMap = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.tracking_map);
        mDriverMap.getMapAsync(this);
    }

    @Override

    protected void onResume() {

        super.onResume();
        setupStatesSheet();
    }

    private void setupStatesSheet() {

        mStates = getStates();
        String stateId = getStateIdFromTag(mService.getState());

        Log.i(Config.DEBUG_TAG, "setupStatesSheet: stateId:" + stateId);

        mAvailableStates = getAvailableStates(findState(stateId));
        mStatesView = (RecyclerView) findViewById(R.id.recycler_states);
        assert mStatesView != null;
        mStatesView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mStatesView.setLayoutManager(mLayoutManager);

        mRecyclerAdapter = new StatesAdapter(this, mAvailableStates, this);
        mStatesView.setAdapter(mRecyclerAdapter);

    }

    private void updateStatesSheet(State state) {

        mAvailableStates = getAvailableStates(state);
        mRecyclerAdapter.swap(mAvailableStates);
    }

    private List<State> getStates() {

        mStates = new ArrayList<>();
        mStates.add(
                new State(Config.ID_STATE_ACCEPTED, Config.STATE_ACCEPTED, R.drawable.ic_car_24dp, new String[]{"3", "4", "5", "6"}));
        mStates.add(
                new State(Config.ID_STATE_ON_THE_WAY, Config.STATE_ON_THE_WAY, R.drawable.ic_on_the_way_24dp, new String[]{"4", "5", "6"}));
        mStates.add(
                new State(Config.ID_STATE_ARRIVED, Config.STATE_ARRIVED, R.drawable.ic_arrived_24dp, new String[]{"5", "6"}));
        mStates.add(
                new State(Config.ID_STATE_ON_BOARD, Config.STATE_ON_BOARD, R.drawable.ic_on_board_24dp, new String[]{"6"}));
        mStates.add(
                new State(Config.ID_STATE_COMPLETED, Config.STATE_COMPLETED, R.drawable.ic_completed_24dp, new String[]{"6"}));

        return mStates;
    }

    public State findState(String stateId) {

        State state = null;

        for (State itemState : mStates) {

            if (stateId.equals(itemState.getId())) {
                state = itemState;
            }
        }
        return state;
    }

    private List<State> getAvailableStates(State state) {

        List<State> availableStates = new ArrayList<>();

        for (String stateId : state.getAvailable_states()) {

            availableStates.add(findState(stateId));
        }

        return availableStates;
    }

    public static String getStateIdFromTag(String tag) {

        switch (tag) {

            case Config.STATE_ACCEPTED:
                return Config.ID_STATE_ACCEPTED;
            case Config.STATE_ON_THE_WAY:
                return Config.ID_STATE_ON_THE_WAY;
            case Config.STATE_ARRIVED:
                return Config.ID_STATE_ARRIVED;
            case Config.ID_STATE_ON_BOARD:
                return Config.ID_STATE_ON_BOARD;
            case Config.STATE_COMPLETED:
                return Config.ID_STATE_COMPLETED;
            default:
                return Config.ID_STATE_ACCEPTED;
        }

    }

    @Override
    public void onRecyclerViewClick(View v, int position) {

        updateStateOnBackend(mAvailableStates.get(position));
        launchProgressDialog();
    }

    private void updateStateOnBackend(final State state) {

        final Snackbar mSnackBar = Snackbar
                .make(
                        mStatesView,
                        R.string.msg_update_service_state_failed,
                        Snackbar.LENGTH_LONG);


        mSnackBar.setAction(R.string.accept, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSnackBar.dismiss();

            }
        });

        servicesController.updateServiceState(
                mService.getId(),
                state.getId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {

                        if (mProgressDialog != null) {
                            mProgressDialog.dismiss();
                        }

                        if (result.equals(Config.SUCCESS_CODE)) {

                            updateStatesSheet(state);
                            mSnackBar.setText(R.string.msg_update_service_state_success);
                            mSnackBar.show();

                        } else {

                            mSnackBar.setText(R.string.msg_update_service_state_failed);
                            mSnackBar.show();
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        if (mProgressDialog != null) {
                            mProgressDialog.dismiss();
                        }
                        mSnackBar.setText(R.string.msg_update_service_state_failed);
                        mSnackBar.show();
                    }
                });
    }

    private void launchProgressDialog() {

        mProgressDialog = new MaterialDialog.Builder(this)
                .title(R.string.msg_progress_update_state)
                .content(R.string.plase_wait)
                .progress(true, 0)
                .widgetColorRes(R.color.color_ubimovil_grey_800)
                .autoDismiss(false)
                .show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        googleMap.setPadding(0,50,16,80);

        LatLng sydney = new LatLng(-33.867, 151.206);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));

        googleMap.addMarker(new MarkerOptions()
                .title("Sydney")
                .snippet("The most populous city in Australia.")
                .position(sydney));



    }

}

