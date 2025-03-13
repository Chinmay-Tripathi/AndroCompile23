package com.example.androcompile23;

import android.os.AsyncTask;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class HttpClient extends AsyncTask<Void, Void, String> {

    private String url;
    private HashMap<String, String> data;
    private ResponseListener listener;
    private boolean isError = false;  // To track errors

    public interface ResponseListener {
        void onResponse(String response);
        void onError(String error);
    }

    public HttpClient(String url, HashMap<String, String> data, ResponseListener listener) {
        this.url = url;
        this.data = data;
        this.listener = listener;
    }

    @Override
    protected String doInBackground(Void... params) {
        HttpURLConnection connection = null;
        try {
            // Set up the connection
            URL urlObj = new URL(url);
            connection = (HttpURLConnection) urlObj.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            // Send JSON data
            JSONObject jsonData = new JSONObject(data);
            OutputStream os = connection.getOutputStream();
            os.write(jsonData.toString().getBytes("UTF-8"));
            os.flush();
            os.close();

            // Get the response from the server
            int responseCode = connection.getResponseCode();
            InputStream inputStream;

            if (responseCode >= 200 && responseCode < 300) {
                inputStream = connection.getInputStream();  // Successful response
            } else {
                inputStream = connection.getErrorStream();  // Error response
                isError = true;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            return response.toString();  // Return full response body

        } catch (Exception e) {
            isError = true;
            return "Error: " + e.getMessage();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (listener != null) {
            if (isError) {
                listener.onError(result);
            } else {
                listener.onResponse(result);
            }
        }
    }
}
