package com.donsaad.movies247;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by donsaad on 12/17/2015.
 * Adapter to fetch posters and populate them.
 */
public class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieRecyclerAdapter.CardViewHolder> {

    private Context mContext;
    private List<Movie> movieList = new ArrayList<>();

    public MovieRecyclerAdapter(Context context, List<Movie> list) {
        this.mContext = context;
        this.movieList = list;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.card_item_movies, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, final int position) {
        Picasso.with(mContext).load(movieList.get(position).getPoster()).into(holder.mImageHolder);
        holder.mTitle.setText(movieList.get(position).getTitle());

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageHolder;
        TextView mTitle;

        public CardViewHolder(View itemView) {
            super(itemView);
            this.mImageHolder = (ImageView) itemView.findViewById(R.id.img_holder);
            this.mTitle = (TextView) itemView.findViewById(R.id.tv_title);
            mImageHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtra(MoviesActivity.MOVIE_POSTER_PATH_KEY, movieList.get(getAdapterPosition()).getPoster());
                    intent.putExtra(MoviesActivity.MOVIE_OVERVIEW_KEY, movieList.get(getAdapterPosition()).getOverview());
                    intent.putExtra(MoviesActivity.MOVIE_VOTE_AVG_KEY, movieList.get(getAdapterPosition()).getVoteAverage());
                    intent.putExtra(MoviesActivity.MOVIE_RELEASE_KEY, movieList.get(getAdapterPosition()).getReleaseDate());
                    intent.putExtra(MoviesActivity.MOVIE_TITLE_KEY, movieList.get(getAdapterPosition()).getTitle());
                    mContext.startActivity(intent);
                }
            });
        }

    }

}
