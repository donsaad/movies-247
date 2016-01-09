package com.donsaad.movies247.trailers;

/**
 * Created by donsaad on 12/25/2015.
 * Trailer data model
 */
public class Trailer {

    public static final String TRAILER_JSON_KEY = "results";
    public static final String TRAILER_ID_KEY = "id";
    public static final String TRAILER_VIDEO_KEY = "key";
    public static final String TRAILER_NAME_KEY = "name";

    private String id;
    private String key;
    private String name;
    private String site;
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
