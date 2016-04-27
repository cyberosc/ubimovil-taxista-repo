package co.com.acktos.ubimoviltaxista.presentation.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import co.com.acktos.ubimoviltaxista.R;
import co.com.acktos.ubimoviltaxista.app.Config;
import co.com.acktos.ubimoviltaxista.models.Car;
import co.com.acktos.ubimoviltaxista.models.State;

/**
 * Created by Acktos on 2/24/16.
 */
public class StatesAdapter extends RecyclerView.Adapter<StatesAdapter.ViewHolder> {

    private List<State> states;
    private static OnRecyclerViewClickListener onRecyclerViewClickListener;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mStateNameView;
        public ImageView mStateIconView;


        public ViewHolder(View itemView) {

            super(itemView);

            mStateNameView=(TextView)itemView.findViewById(R.id.state_item_name);
            mStateIconView=(ImageView)itemView.findViewById(R.id.state_item_icon);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            Log.i(Config.DEBUG_TAG, "click on car:" + this.getLayoutPosition());
            onRecyclerViewClickListener.onRecyclerViewClick(view, this.getLayoutPosition());

        }
    }

    public StatesAdapter(Context context,List<State> states,OnRecyclerViewClickListener onRecyclerViewClick){

        this.context=context;
        this.states=states;
        this.onRecyclerViewClickListener=onRecyclerViewClick;
    }


    @Override
    public int getItemCount() {
        return states.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.state_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        viewHolder.mStateNameView.setText(states.get(i).getName());

        Picasso.with(context)
                .load(states.get(i).getIconResource())
                .placeholder(R.drawable.ic_car_24dp)
                .into(viewHolder.mStateIconView);
    }

    public interface OnRecyclerViewClickListener
    {

        void onRecyclerViewClick(View v, int position);
    }


}
