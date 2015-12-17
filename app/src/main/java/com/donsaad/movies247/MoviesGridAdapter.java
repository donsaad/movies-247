package com.donsaad.movies247;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by donsaad on 12/17/2015.
 * Adapter to fetch posters and populate them.
 */
public class MoviesGridAdapter extends ArrayAdapter {

    private Context mContext;
    private List<String> posters = new ArrayList<>();

    public MoviesGridAdapter(Context context, int resource, List<String> posters) {
        super(context, resource, posters);

        mContext = context;
        this.posters = posters;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item_movies, parent, false);
        }

        Picasso.with(mContext).load(posters.get(position)).into((ImageView) convertView);

        return convertView;
    }
}
