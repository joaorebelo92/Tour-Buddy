package com.example.joorebelo.finalproject.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joorebelo.finalproject.BuildConfig;
import com.example.joorebelo.finalproject.FavoritesActivity;
import com.example.joorebelo.finalproject.R;
import com.example.joorebelo.finalproject.SearchPlaceActivity;
import com.example.joorebelo.finalproject.model.RankedPlaces;

import java.util.ArrayList;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolder> {
    private ArrayList<RankedPlaces> places;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    Context context;
    String whoAsk;

    public PlacesAdapter(Context context, ArrayList<RankedPlaces> places, String whoAsk) {
        this.mInflater = LayoutInflater.from(context);
        this.places = places;
        this.context = context;
        this.whoAsk = whoAsk;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.search_place_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //String animal = mData.get(position);

        holder.txtPlaceName.setText(places.get(position).getPlaceName());
        holder.txtCity.setText(context.getString(R.string.strCity) + ": " + places.get(position).getCity());
        holder.txtCountry.setText(context.getString(R.string.strCountry) + ": " + places.get(position).getCountry());
        holder.imgPlace.setImageURI(Uri.parse("android.resource://"+ BuildConfig.APPLICATION_ID+"/drawable/" + places.get(position).getImageLink()));

        if (places.get(position).isFavorite()){
            holder.imgFavotite.setImageResource(R.drawable.ic_favorite_places);
        }else
        {
            holder.imgFavotite.setImageResource(R.drawable.ic_action_favorite_empty);
        }
        holder.ratingBar.setRating( (float) places.get(position).getRank());

        holder.txtCountRate.setText("(" + context.getString(R.string.strRatedBy) + " " + places.get(position).getCountRate() + " " + context.getString(R.string.strUsers) + ")");
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return places.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView txtCountry, txtCity, txtPlaceName, txtCountRate;
        ImageView imgPlace, imgFavotite;
        RatingBar ratingBar;

        ViewHolder(View itemView) {
            super(itemView);
            txtCountry = itemView.findViewById(R.id.txtCountry);
            txtCity = itemView.findViewById(R.id.txtCity);
            txtPlaceName = itemView.findViewById(R.id.txtPlaceName);
            imgPlace = itemView.findViewById(R.id.imgPlace);
            imgFavotite = itemView.findViewById(R.id.imgFavotite);
            txtCountRate = itemView.findViewById(R.id.txtCountRate);

            ratingBar = itemView.findViewById(R.id.ratingBar);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null){
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            //add to favorites
            //Toast.makeText(context, "Item: " + places.get(getAdapterPosition()).getPlaceName(), Toast.LENGTH_LONG).show();
            if(places.get(getAdapterPosition()).isFavorite()){
                imgFavotite.setImageResource(R.drawable.ic_action_favorite_empty);
                places.get(getAdapterPosition()).setFavorite(false);
                Toast.makeText(context, R.string.strRemovedFavorite, Toast.LENGTH_SHORT).show();
            }else{
                imgFavotite.setImageResource(R.drawable.ic_favorite_places);
                places.get(getAdapterPosition()).setFavorite(true);
                Toast.makeText(context, R.string.strAddedFavorite, Toast.LENGTH_SHORT).show();
            }
            if(places.size()>0){
                if (whoAsk.equals("search")){
                    SearchPlaceActivity.updateFavotites(places.get(getAdapterPosition()).getPlaceId(), places.get(getAdapterPosition()).isFavorite());
                }else{
                    FavoritesActivity.updateFavotites(places.get(getAdapterPosition()).getPlaceId(), places.get(getAdapterPosition()).isFavorite());
                }
            }else {
                Toast.makeText(context, R.string.strFavoritesEmpty, Toast.LENGTH_LONG).show();
            }

            return true;
        }
    }

    public RankedPlaces getItem(int id) {
        return places.get(id);
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
