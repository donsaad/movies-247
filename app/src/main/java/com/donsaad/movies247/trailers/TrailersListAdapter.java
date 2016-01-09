package com.donsaad.movies247.trailers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.donsaad.movies247.R;

import java.util.ArrayList;

/**
 * Created by donsaad on 1/9/2016.
 */
public class TrailersListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Trailer> trailers;

    public TrailersListAdapter(Context context, ArrayList<Trailer> trailers) {
        this.mContext = context;
        this.trailers = trailers;
    }

    @Override
    public int getCount() {
        return trailers.size();
    }

    @Override
    public Object getItem(int position) {
        return trailers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_trailers, parent, false);
            holder.view = (TextView) convertView.findViewById(R.id.tv_trailer);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.view.setText("Trailer #" + (position + 1));
        return convertView;
    }

    private class ViewHolder {
        private TextView view;
    }

}
