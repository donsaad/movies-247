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
 * Adapter to fetch posters and populate them.
 */
public class ReviewsListAdapter extends ArrayAdapter {

    private Context mContext;
    private List<String> reviews = new ArrayList<>();

    public ReviewsListAdapter(Context context, int resource, List<String> reviews) {
        super(context, resource, reviews);

        mContext = context;
        this.reviews = reviews;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView content;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_reviews, parent, false);
        }
        content = (TextView) convertView.findViewById(R.id.tv_review);
        content.setText(reviews.get(position));

        return convertView;
    }
}
