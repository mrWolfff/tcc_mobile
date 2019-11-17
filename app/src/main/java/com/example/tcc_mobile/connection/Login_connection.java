package com.example.tcc_mobile.connection;

import android.os.AsyncTask;
import android.util.Log;

import com.example.tcc_mobile.classes.User;
import com.example.tcc_mobile.interfaces.Retrofit_Interface;

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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;





public class Login_connection extends AsyncTask<Void, Void, String>{
    private String address;
    private String username;
    private String password;
    private JSONObject json;
    private String response = "";
    User user;

    public String getResponse(){
        return this.response;
    }
    public void setResponse(String response){
        this.response = response;
    }


    public Login_connection(String url, String username, String password, JSONObject json){
        this.address = url;
        this.username = username;
        this.password = password;
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

    public String getCsrfFromUrl(String url) {
        final String[] token = {""};
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Retrofit_Interface jsonHolder = retrofit.create(Retrofit_Interface.class);
        Call call = jsonHolder.getToken();
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if(!response.isSuccessful()){
                    Log.e("code", String.valueOf(response.code()));
                    //text_result.setText("Code: " + response.code());
                }
                token[0] = (String) response.body();
            }
            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("erro", t.getMessage());
            }
        });
        return token[0];
    }

    @Override
    public String doInBackground(Void... voids) {
        String responseString = "";
        try {
            json.put("username", username);
            json.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //String token = getCsrfFromUrl("http://192.168.0.106:8000/api/");
        try {
            URL url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            //String userpass = username + ":" + password;
            //String autorizacao = "Basic " + Base64.encodeToString(userpass.getBytes(), Base64.DEFAULT);
            //connection.setRequestProperty("Authorization", autorizacao);

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
                Log.e("oi", finalResult.getString("token"));

                User user = new User();
                user.setToken(finalResult.getString("token"));
                user.setID(finalResult.getInt("id"));
                user.setFirst_name(finalResult.getString("first_name"));
                user.setLast_name(finalResult.getString("last_name"));
                user.setUsername(finalResult.getString("username"));
                user.setEmail(finalResult.getString("email"));
                //user.setCategoria(finalResult.getString("categoria"));
                user.setCategoria_user(finalResult.getString("categoria_user"));
                setUser(user);

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
        return responseString;
    }



    private void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

}
