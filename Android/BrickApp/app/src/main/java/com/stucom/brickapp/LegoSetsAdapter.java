package com.stucom.brickapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.stucom.brickapp.model.LegoSet;
import com.stucom.brickapp.model.LegoSetsApiResponse;

public class LegoSetsAdapter extends RecyclerView.Adapter<LegoSetsAdapter.ViewHolder> {

    private Context context;
    private LegoSetsApiResponse legoSetsApiResponse;

    public LegoSetsAdapter(Context context, LegoSetsApiResponse legoSetsApiResponse) {
        this.context = context;
        this.legoSetsApiResponse = legoSetsApiResponse;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lego_set, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LegoSet legoSet = legoSetsApiResponse.getResults().get(position);
        holder.setLegoSet(legoSet);
    }

    @Override
    public int getItemCount() {
        return legoSetsApiResponse.getResults().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivLegoSetImage;
        TextView tvLegoSetName;
        LegoSet legoSet;

        public ViewHolder(@NonNull View view) {
            super(view);
            ivLegoSetImage = view.findViewById(R.id.ivLegoSetImage);
            tvLegoSetName = view.findViewById(R.id.tvLegoSetName);
        }

        public LegoSet getLegoSet() { return legoSet; }
        public void setLegoSet(LegoSet legoSet) {
            this.legoSet = legoSet;
            Picasso.with(context).load(legoSet.getSetImgUrl()).into(ivLegoSetImage);
            tvLegoSetName.setText(legoSet.getName());
        }
    }
}
