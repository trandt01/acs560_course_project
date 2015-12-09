package com.dtran.testtemplate1;

import android.os.AsyncTask;
import java.net.HttpURLConnection;
import java.net.URL;

public class SetLift extends AsyncTask<URL, String, String> {
    @Override
    protected String doInBackground(URL... urls) {
        HttpURLConnection urlConnection = null;
        String message = null;
        try {
            int count = urls.length;
            if (count > 0) {
                for (int i = 0; i < urls.length; i++) {
                    urlConnection = (HttpURLConnection) urls[i].openConnection();
                    urlConnection.connect();

                    message = urlConnection.getResponseMessage();
                }
            }
        }
        catch(Exception e){
        }
        finally{
            urlConnection.disconnect();
        }
        return null;
    }
}
