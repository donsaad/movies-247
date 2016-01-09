package com.donsaad.movies247.networking;

/**
 * Created by donsaad on 1/7/2016.
 */
public interface OnDataFetchListener {

    void onDataFetched(String data);
    void onDataError(int errorCode);

}
