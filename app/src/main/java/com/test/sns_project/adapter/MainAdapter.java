package com.test.sns_project.adapter;

import android.app.Activity;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.test.sns_project.PostInfo;
import com.test.sns_project.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Pattern;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private ArrayList<PostInfo> mDataset;
    private Activity activity;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;

        ViewHolder(CardView view) {
            super(view);
            cardView = view;
        }
    }
    public MainAdapter(Activity activity, ArrayList<PostInfo> myDataset) {
        mDataset = myDataset;
        this.activity = activity;
    }
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        final  ViewHolder galleryViewHolder = new ViewHolder(cardView);
        cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
            }
        });
        return galleryViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position) {

        CardView cardView = holder.cardView;
        TextView titleTextView = cardView.findViewById(R.id.titleTextView);
        titleTextView.setText(mDataset.get(position).getTitle());

        TextView createdAtTextView = cardView.findViewById(R.id.createTextView);
        createdAtTextView.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(mDataset.get(position).getCreatedAt()));

        LinearLayout contentsLayout = cardView.findViewById(R.id.contentsLayout);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        ArrayList<String> contentsList = mDataset.get(position).getContents();
        for(int i = 0; i<contentsList.size(); i++){
            String contents = contentsList.get(i);
            if(Patterns.WEB_URL.matcher(contents).matches()){
                ImageView imageView = new ImageView(activity);
                imageView.setLayoutParams(layoutParams);
                contentsLayout.addView(imageView);
                Glide.with(activity).load(contents).override(1000).into(imageView);
            }else{
                TextView textView = new TextView(activity);
                textView.setLayoutParams(layoutParams);
                textView.setText(contents);
                contentsLayout.addView(textView);
            }
        }
    }
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}