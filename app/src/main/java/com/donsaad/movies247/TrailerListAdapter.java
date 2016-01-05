package com.donsaad.movies247;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by donsaad on 12/17/2015.
 */
public class TrailerListAdapter extends ArrayAdapter {

    private Context mContext;
    private List<String> keys = new ArrayList<>();

    public TrailerListAdapter(Context context, int resource, List<String> keys) {
        super(context, resource, keys);

        mContext = context;
        this.keys = keys;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView link;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_trailers, parent, false);
        }
        link = (TextView) convertView.findViewById(R.id.tv_trailer_key);

        link.setText(keys.get(position));

        return convertView;
    }
}
