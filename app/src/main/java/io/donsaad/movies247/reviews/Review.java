package io.donsaad.movies247.reviews;

import io.donsaad.movies247.moviedetails.MovieDetailsListAdapter;
import io.donsaad.movies247.moviedetails.ListItem;

/**
 * Created by donsaad on 12/25/2015.
 * Review data model
 */
public class Review implements ListItem {

    private String id;
    private String author;
    private String content;
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int getViewType() {
        return MovieDetailsListAdapter.RowType.REVIEW_ITEM.ordinal();
    }
}
