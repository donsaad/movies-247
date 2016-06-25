package io.donsaad.movies247.trailers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.donsaad.movies247.utils.Constants;

/**
 * Created by donsaad on 1/9/2016.
 * Parser class to parse JSON string of trailer
 */
public class TrailerParser {

    public ArrayList<Trailer> parseJson(String s) throws JSONException {

        JSONArray array = new JSONObject(s).getJSONArray(Constants.TRAILER_JSON_KEY);
        JSONObject object = null;
        ArrayList<Trailer> trailers = new ArrayList<>();
        Trailer trailer;
        for (int i = 0, length = array.length(); i < length; i++) {
            trailer = new Trailer();
            object = array.getJSONObject(i);
            trailer.setKey(object.getString(Constants.TRAILER_VIDEO_KEY));
            trailer.setName(object.getString(Constants.TRAILER_NAME_KEY));
            // TODO: 1/9/2016 set other data
            trailers.add(trailer);
        }
        return trailers;
    }
}
