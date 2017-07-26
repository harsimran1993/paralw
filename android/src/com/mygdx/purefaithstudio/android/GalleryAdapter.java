package com.mygdx.purefaithstudio.android;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mygdx.purefaithstudio.Config;

import java.util.ArrayList;

/**
 * Created by harsimran singh on 26-07-2017.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private ArrayList<ImageItem> mData = new ArrayList();
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public GalleryAdapter(Context context, ArrayList<ImageItem> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        Log.i("harsim","adapter constructor");
    }
    @Override
    public GalleryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.lwitem, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        Log.i("harsim","created views");
        Log.i("harsim","loaded adapter view");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(GalleryAdapter.ViewHolder holder, int position) {
        ImageItem item =mData.get(position);
        holder.imageTitle.setText(item.getTitle());
        holder.image.setImageResource(item.getImage());
        holder.madeBy.setText(item.getMadeBy());
        holder.downloads.setText(""+item.getDownloads());
        if(position * 10 < (Config.points + Config.fps)){
            holder.lock.setVisibility(View.GONE);
        }
        else{
            holder.lock.setVisibility(View.VISIBLE);
        }

        Log.i("harsim","loadeddata");
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView imageTitle,madeBy,downloads;
        ImageView image,lock;

        public ViewHolder(View itemView) {
            super(itemView);
            imageTitle = itemView.findViewById(R.id.text);
            image = itemView.findViewById(R.id.image);
            lock = itemView.findViewById(R.id.lock);
            madeBy = itemView.findViewById(R.id.madeBy);
            downloads = itemView.findViewById(R.id.downloads);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public ImageItem getItem(int id) {
        return mData.get(id);
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
