package com.example.tcc_mobile.connection;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class Service_connection extends AsyncTask<Void, Void,Void>{
        private String address;
        private JSONObject json;
        private String response = "";


        public Service_connection(String url, JSONObject json){
            this.address = url;
            this.json = json;
        }

        public String post(final JSONObject data) {
            try {
                final RequestBody body = RequestBody
                        .create(MediaType.parse("application/json"), data.toString());
                final Request request = new Request.Builder()
                        .url("http://192.168.0.106:8000/api/login")
                        .post(body)
                        .addHeader("Accept", "application/json")
                        .build();
                final OkHttpClient client = new OkHttpClient();
                final okhttp3.Response response = client.newCall(request).execute();
                return response.body().toString();
            } catch (Exception e) {
                Log.e("Your tag", "Error", e);
            }

            return null;
        }


        @Override
        public Void doInBackground(Void... voids) {
            String responseString = "";
            try {
                URL url = new URL(address);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept","application/json");
                connection.setDoOutput(true);
                connection.setDoInput(true);
                //connection.setRequestProperty("X-CSRFToken", token);
                OutputStream outputStream = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                writer.write(json.toString());
                writer.flush();
                writer.close();
                outputStream.close();
                connection.connect();
                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String line = null;
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder builder = new StringBuilder();
                    for (line = null; (line = br.readLine()) != null;) {
                        builder.append(line).append("\n");
                    }
                    JSONTokener tokener = new JSONTokener(builder.toString());
                    JSONObject finalResult = new JSONObject(tokener);
                    Log.e("result", finalResult.toString());
                }
                if(responseCode == HttpURLConnection.HTTP_NOT_FOUND){
                    response = "erro";
                }
            } catch (MalformedURLException e) {
                Log.e("connection_error_url", e.getMessage());
            } catch (IOException e) {
                response = "erro";
                Log.e("connection_error_io", e.getMessage());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
}

