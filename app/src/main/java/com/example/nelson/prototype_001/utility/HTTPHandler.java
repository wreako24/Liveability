package com.example.nelson.prototype_001.utility;

/**
 * Created by Nelson on 2/9/2017.
 */

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by Ravi Tamada on 01/09/16.
 * www.androidhive.info
 */
public class HTTPHandler {

    private static final String TAG = HTTPHandler.class.getSimpleName();

    public HTTPHandler() {
    }

    public String makeServiceCall(String reqUrl,String type) {
        String response = null;
        try {
            URL url = new URL(reqUrl);
            Log.e(TAG, reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            if(type.equals("bus")) {
                conn.setRequestProperty("AccountKey", "2c+SU5sT6QhqV8+MOOAIsA==");
                conn.setRequestProperty("accept", "application/json");
            }else if(type.equals("taxi")){
                conn.setRequestProperty("api-key","VVhNJxFUkbk8Emu5tEOGEyd3D5RRWf84");
            }else if(type.equals("places")){

            }
            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);
            Log.e(TAG, response);
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}