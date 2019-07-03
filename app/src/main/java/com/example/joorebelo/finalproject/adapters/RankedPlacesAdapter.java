package com.example.joorebelo.finalproject.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.joorebelo.finalproject.BuildConfig;
import com.example.joorebelo.finalproject.R;
import com.example.joorebelo.finalproject.model.RankedPlaces;

import java.util.ArrayList;
import java.util.List;

public class RankedPlacesAdapter extends RecyclerView.Adapter<RankedPlacesAdapter.ViewHolder> {
    private ArrayList<RankedPlaces> rankedPlaces;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    Context context;

    public RankedPlacesAdapter(Context context, ArrayList<RankedPlaces> rankedPlaces) {
        this.mInflater = LayoutInflater.from(context);
        this.rankedPlaces = rankedPlaces;
        this.context = context;
    }
/*
    private List<String> getCityNames(ArrayList<RankedPlaces> rankedPlaces) {
        List<String> a = new ArrayList<>();
        for (int i = 0; i < rankedPlaces.size(); i++) {
            a.add(rankedPlaces.get(i).getPlaceName());
        }
        return a;
    }
*/
    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycleview_place, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //String animal = mData.get(position);

        holder.myTextView.setText(rankedPlaces.get(position).getPlaceName());
        holder.img_place.setImageURI(Uri.parse("android.resource://"+ BuildConfig.APPLICATION_ID+"/drawable/" + rankedPlaces.get(position).getImageLink()));
        holder.ratingBar.setRating( (float) rankedPlaces.get(position).getRank());
        holder.txtCountRate.setText("(" + context.getString(R.string.strRatedBy) + " " + rankedPlaces.get(position).getCountRate() + " " + context.getString(R.string.strUsers) + ")");
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return rankedPlaces.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView, txtCountRate;
        ImageView img_place;
        RatingBar ratingBar;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.place_name);
            img_place = itemView.findViewById(R.id.img_place);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            txtCountRate = itemView.findViewById(R.id.txtCountRate);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public RankedPlaces getItem(int id) {
        return rankedPlaces.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
