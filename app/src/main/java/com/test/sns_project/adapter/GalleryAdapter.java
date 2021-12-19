package com.test.sns_project.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.test.sns_project.R;

import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private ArrayList<String> mDataset;
    private Activity activity;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;

        ViewHolder(CardView view) {
            super(view);
            cardView = view;
        }
    }
    public GalleryAdapter(Activity activity, ArrayList<String> myDataset) {
        mDataset = myDataset;
        this.activity = activity;
    }
    @Override
    public GalleryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery, parent, false);
        final  ViewHolder galleryViewHolder = new ViewHolder(cardView);
        cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent resultIntent = new Intent();
                resultIntent.putExtra("profilePath",mDataset.get(galleryViewHolder.getAdapterPosition()));
                activity.setResult(Activity.RESULT_OK,resultIntent);
                activity.finish();
            }
        });
        return galleryViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position) {

        CardView cardView = holder.cardView;
        ImageView imageView = cardView.findViewById(R.id.imageView);
        Glide.with(activity).load(mDataset.get(position)).centerCrop().override(300).into(imageView);
    }
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}