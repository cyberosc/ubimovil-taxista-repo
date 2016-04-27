package co.com.acktos.ubimoviltaxista.presentation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import co.com.acktos.ubimoviltaxista.R;
import co.com.acktos.ubimoviltaxista.app.Config;
import co.com.acktos.ubimoviltaxista.models.State;
import co.com.acktos.ubimoviltaxista.presentation.adapters.ServicesAdapter;
import co.com.acktos.ubimoviltaxista.presentation.adapters.StatesAdapter;

public class TrackingActivity extends AppCompatActivity implements StatesAdapter.OnRecyclerViewClickListener{


    private List<State> mStates;
    private RecyclerView mStatesView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        setupStatesSheet();

    }


    private void setupStatesSheet(){

        mStates=getStates();
        mStatesView = (RecyclerView) findViewById(R.id.recycler_states);
        assert mStatesView != null;
        mStatesView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mStatesView.setLayoutManager(mLayoutManager);

        mRecyclerAdapter = new StatesAdapter(this, mStates, this);
        mStatesView.setAdapter(mRecyclerAdapter);

    }

    private List<State> getStates(){


        List<State> states=new ArrayList<>();
        states.add(new State("1",Config.STATE_ACCEPTED,R.drawable.ic_arrow_up_black_24dp));
        states.add(new State("1",Config.STATE_ON_THE_WAY,R.drawable.ic_arrow_up_black_24dp));
        states.add(new State("1",Config.STATE_ARRIVED,R.drawable.ic_arrow_up_black_24dp));
        states.add(new State("1",Config.STATE_ON_BOARD,R.drawable.ic_arrow_up_black_24dp));
        states.add(new State("1",Config.STATE_COMPLETED,R.drawable.ic_arrow_up_black_24dp));

        return states;

    }

    @Override
    public void onRecyclerViewClick(View v, int position) {

    }
}
