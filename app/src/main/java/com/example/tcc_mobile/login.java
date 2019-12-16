package com.example.tcc_mobile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

public class login extends AppCompatActivity {
    EditText username;
    EditText password;
    String user;
    String pass;
    ProgressDialog dialog;
    String token;
    int id;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //   VERIFICAR SE USUARIO ESTA SALVO NO SHARED PREFERENCES
        prefs = getSharedPreferences("user_info", MODE_PRIVATE);
        token = prefs.getString("token", "No name defined");
        id = prefs.getInt("id", 0);
        if(id != 0)
            new login.Request_User().execute();



    }

    public void register_user(View view) {
        Intent intent = new Intent(login.this, signup.class);
        startActivity(intent);
    }

    public void verifyUser(View view) {
        username = findViewById(R.id.user);
        password = findViewById(R.id.pass);
        user = username.getText().toString();
        pass = password.getText().toString();
        if (username.length() == 0 || password.length() == 0){
            Toast.makeText(getApplicationContext(), "Defina um usuario ou senha!", Toast.LENGTH_SHORT).show();
        }else {
            try { new Authenticate().execute(); }

            catch(Exception e) {
                Toast.makeText(getApplicationContext(), "Usuario ou senha incorretos!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private class Request_User extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(login.this, R.style.styleProgressDialog);
            dialog.setTitle("Carregando");
            dialog.setMessage("Verificando o usuario...");
            dialog.show();
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            JSONObject json = new JSONObject();
            try {
                json.put("token", token);
                json.put("id", id);
                URL url = new URL("http://192.168.0.105:8000/api/get_info");
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
                    Log.e("aaaaaaaaaa", finalResult.toString());
                    if (finalResult.getString("categoria").equals( "Consumidor")){
                        Intent intent = new Intent(login.this, index.class);
                        startActivity(intent);
                    }else{
                        startActivity(new Intent(login.this, index_prestador.class));
                    }


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
    private class Authenticate extends AsyncTask<Void, Void, Void>  {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(login.this, R.style.styleProgressDialog);
            dialog.setTitle("Carregando");
            dialog.setMessage("Verificando o usuario...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            JSONObject json = new JSONObject();
            try {
                json.put("username", user);
                json.put("password", pass);

                URL url = new URL("http://192.168.0.105:8000/api/login");
                final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                //String userpass = username + ":" + password;
                //String autorizacao = "Basic " + Base64.encodeToString(userpass.getBytes(), Base64.DEFAULT);
                //connection.setRequestProperty("Authorization", autorizacao);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
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
                final int responseCode = connection.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_NOT_FOUND )  {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),
                                                "Usuario ou senha incorretos! " ,
                                                Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                            if (responseCode == HttpURLConnection.HTTP_OK) {
                                String line = null;
                                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                                StringBuilder builder = new StringBuilder();
                                for (line = null; (line = br.readLine()) != null; ) {
                                    builder.append(line).append("\n");
                                }
                                JSONTokener tokener = new JSONTokener(builder.toString());
                                JSONObject finalResult = new JSONObject(tokener);
                                Log.e("result", finalResult.toString());
                                User user = new User();
                                user.setToken(finalResult.getString("token"));
                                user.setID(finalResult.getInt("id"));
                                user.setFirst_name(finalResult.getString("first_name"));
                                user.setLast_name(finalResult.getString("last_name"));
                                user.setUsername(finalResult.getString("username"));
                                user.setEmail(finalResult.getString("email"));
                                //user.setCategoria(finalResult.getString("categoria"))
                                user.setCategoria_user(finalResult.getString("categoria_user"));
                                Log.e("CATEGORIA", finalResult.getString("categoria_user"));
                                editor = getSharedPreferences("user_info", MODE_PRIVATE).edit();
                                editor.putString("token", user.getToken());
                                editor.putInt("id", user.getID());
                                editor.apply();
                                if (finalResult.getString("categoria_user").equals("Consumidor")){
                                    Intent intent = new Intent(login.this, index.class);
                                    startActivity(intent);
                                }else{
                                    startActivity(new Intent(login.this, index_prestador.class));
                                }
                            }

            } catch (IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Usuario ou senha incorretos! ",
                                Toast.LENGTH_LONG).show();
                    }
                });
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
