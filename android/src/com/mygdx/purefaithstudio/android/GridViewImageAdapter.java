package com.mygdx.purefaithstudio.android;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mygdx.purefaithstudio.Config;

import java.util.ArrayList;

public class GridViewImageAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    private ArrayList<ImageItem> data = new ArrayList();

    public GridViewImageAdapter(Context context, int layoutResourceId, ArrayList<ImageItem> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageTitle = (TextView) row.findViewById(R.id.text);
            holder.image = (ImageView) row.findViewById(R.id.image);
            holder.lock=(ImageView) row.findViewById(R.id.lock);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        ImageItem item =data.get(position);
        holder.imageTitle.setText(item.getTitle());
        holder.image.setImageResource(item.getImage());
        if(position * 10 < (Config.points + Config.fps)){
            holder.lock.setVisibility(View.GONE);
        }
        else{
            holder.lock.setVisibility(View.VISIBLE);
        }
        return row;
    }

    static class ViewHolder {
        TextView imageTitle;
        ImageView image,lock;
    }
}