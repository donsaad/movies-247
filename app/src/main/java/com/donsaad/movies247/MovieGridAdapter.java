package com.donsaad.movies247;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by donsaad on 12/17/2015.
 * Adapter to fetch data and populate it in main screen.
 */
public class MovieGridAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Movie> moviesList = new ArrayList<>();

    public MovieGridAdapter(Context context, ArrayList<Movie> movies) {
        this.mContext = context;
        this.moviesList = movies;
    }

    @Override
    public int getCount() {
        return moviesList.size();
    }

    @Override
    public Object getItem(int position) {
        return moviesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.card_item_movies, parent, false);
            holder.mImageHolder = (ImageView) convertView.findViewById(R.id.img_holder);
            holder.mTitle = (TextView) convertView.findViewById(R.id.tv_card_title);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mTitle.setText(moviesList.get(position).getTitle());
        Picasso.with(mContext).load(moviesList.get(position).getPoster()).into(holder.mImageHolder);

        return convertView;
    }

    public class ViewHolder {
        ImageView mImageHolder;
        TextView mTitle;
    }

}
