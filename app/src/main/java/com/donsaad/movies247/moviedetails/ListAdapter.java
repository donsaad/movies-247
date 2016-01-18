package com.donsaad.movies247.moviedetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.donsaad.movies247.R;
import com.donsaad.movies247.reviews.Review;
import com.donsaad.movies247.trailers.Trailer;

import java.util.ArrayList;

/**
 * Created by donsaad on 1/18/2016.
 * Adapter for trailers and reviews
 */
public class ListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<ListItem> items;

    public enum RowType {
        TRAILER_ITEM, REVIEW_ITEM
    }

    public ListAdapter(Context context, ArrayList<ListItem> items) {
        this.mContext = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public ListItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getViewType();
    }

    @Override
    public int getViewTypeCount() {
        return RowType.values().length;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        int rowType = getItemViewType(position);

        if (convertView == null) {
            holder = new ViewHolder();
            if (rowType == RowType.TRAILER_ITEM.ordinal()) {
                convertView = LayoutInflater.from(mContext)
                        .inflate(R.layout.list_item_trailer, parent, false);
                holder.textView = (TextView) convertView.findViewById(R.id.tv_trailer);
            } else {
                convertView = LayoutInflater.from(mContext)
                        .inflate(R.layout.list_item_review, parent, false);
                holder.textView = (TextView) convertView.findViewById(R.id.tv_review);
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (rowType == RowType.REVIEW_ITEM.ordinal())
            holder.textView.setText(((Review) items.get(position)).getContent());
        else
            holder.textView.setText(((Trailer) items.get(position)).getName());

        return convertView;
    }

    private static class ViewHolder {
        TextView textView;
    }

}
