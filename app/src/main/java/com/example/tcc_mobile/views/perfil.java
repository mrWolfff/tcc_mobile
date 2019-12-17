package com.example.tcc_mobile.views;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tcc_mobile.HttpHandler;
import com.example.tcc_mobile.R;
import com.example.tcc_mobile.classes.User;

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

public class perfil extends AppCompatActivity {

    User user = new User();
    SharedPreferences prefs;
    String token;
    int id;
    TextView nome;
    TextView email;
    RatingBar rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prefs = getSharedPreferences("user_info", MODE_PRIVATE);
        token = prefs.getString("token", "No name defined");
        id = prefs.getInt("id", 0);
        Log.e("token ", token + " ID " + id);
        nome = findViewById(R.id.nome_perfil);
        email = findViewById(R.id.email_perfil);
        rating = findViewById(R.id.ratingBar_perfil);
        new Request_User().execute();



    }

    public void setTextViews(){
        nome.setText(user.get_full_name());
        email.setText(user.getEmail());
        rating.setRating(user.getAvaliacao());
        rating.invalidate();
        rating.setIsIndicator(true);
    }


    private class Request_User extends AsyncTask<Void,Void,Void> {


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setTextViews();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            JSONObject json = new JSONObject();
            try {
                json.put("token", token);
                json.put("id", id);
                URL url = new URL("http://webservices.pythonanywhere.com/api/get_info");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true);
                connection.setDoInput(true);
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
                    for (line = null; (line = br.readLine()) != null; ) {
                        builder.append(line).append("\n");
                    }
                    JSONTokener tokener = new JSONTokener(builder.toString());
                    JSONObject finalResult = new JSONObject(tokener);
                    Log.e("222222", finalResult.toString());
                    //user.setToken(finalResult.getString("token"));
                    user.setFirst_name(finalResult.getString("first_name"));
                    user.setLast_name(finalResult.getString("last_name"));
                    user.setUsername(finalResult.getString("username"));
                    user.setEmail(finalResult.getString("email"));
                    user.setAvaliacao((float) finalResult.getDouble("avaliacao"));
                    //user.setCategoria_user(finalResult.getString("categoria"));
                    user.setImagem(new HttpHandler().getBitmap("http://192.168.0.105:8000"+finalResult.getString("imagem")));

                }
            } catch (MalformedURLException e) {
                Log.e("connection_error_url", e.getMessage());
            } catch (IOException e) {
                Log.e("connection_error_io", e.getMessage());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
