package io.donsaad.movies247.reviews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.donsaad.movies247.utils.Constants;

/**
 * Created by donsaad on 1/9/2016.
 * Parser class to parse JSON string of reviews
 */
public class ReviewParser {

    public ArrayList<Review> parseJson(String s) throws JSONException {

        JSONArray array = new JSONObject(s).getJSONArray(Constants.TRAILER_JSON_KEY);
        JSONObject object = null;
        ArrayList<Review> reviews = new ArrayList<>();
        Review review;
        for (int i = 0, length = array.length(); i < length; i++) {
            review = new Review();
            object = array.getJSONObject(i);
            review.setContent(object.getString(Constants.REVIEW_CONTENT_KEY));
            review.setAuthor(object.getString(Constants.REVIEW_AUTHOR_KEY));
            review.setUrl(object.getString(Constants.REVIEW_URL_KEY));
            // TODO: 1/9/2016 set other data
            reviews.add(review);
        }
        return reviews;
    }
}
