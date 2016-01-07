package com.donsaad.movies247;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * AsyncTask parameter is the endpoint to fetch data from it
 * and it return the JSON response as a String
 */
public class DataFetchTask extends AsyncTask<String, Void, String> {

    public final String LOG_TAG = DataFetchTask.class.getSimpleName();


    private OnDataFetchListener onDataFetchListener;

    public void setOnDataFetchListener(OnDataFetchListener listener) {
        onDataFetchListener = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        // verifying parameters
        if (params.length == 0) {
            return null;
        }
        HttpURLConnection httpURLConnection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(params[0] + MoviesActivity.API_KEY);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            InputStream inputStream = httpURLConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer buffer = new StringBuffer();

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // no data
                return null;
            }

            return buffer.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Connection Error: ", e);
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        if (s != null) {
            onDataFetchListener.onDataFetched(s);
        } else {
            onDataFetchListener.onDataError(0);
        }

    } // end of onPostExecute

} // end of DataFetchTask
