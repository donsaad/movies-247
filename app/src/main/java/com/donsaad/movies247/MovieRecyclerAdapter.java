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
 * Adapter to fetch data and populate it in main screen.
 */
public class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieRecyclerAdapter.CardViewHolder> {

    private Context mContext;
    private List<Movie> moviesList = new ArrayList<>();

    public MovieRecyclerAdapter(Context context, List<Movie> list) {
        this.mContext = context;
        this.moviesList = list;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.card_item_movies, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, final int position) {
        Picasso.with(mContext).load(moviesList.get(position).getPoster()).into(holder.mImageHolder);
        holder.mTitle.setText(moviesList.get(position).getTitle());

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
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
                    intent.putExtra(Movie.MOVIE_POSTER_PATH_KEY, moviesList.get(getAdapterPosition()).getPoster());
                    intent.putExtra(Movie.MOVIE_OVERVIEW_KEY, moviesList.get(getAdapterPosition()).getOverview());
                    intent.putExtra(Movie.MOVIE_VOTE_AVG_KEY, moviesList.get(getAdapterPosition()).getVoteAverage());
                    intent.putExtra(Movie.MOVIE_RELEASE_KEY, moviesList.get(getAdapterPosition()).getReleaseDate());
                    intent.putExtra(Movie.MOVIE_TITLE_KEY, moviesList.get(getAdapterPosition()).getTitle());
                    mContext.startActivity(intent);
                }
            });
        }

    }

}
