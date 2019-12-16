package com.example.tcc_mobile.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tcc_mobile.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class create_demanda extends AppCompatActivity {
    EditText titulo_text;
    EditText descricao_text;
    SharedPreferences prefs;
    String token;
    int id;
    Date currentTime;
    List<String> spinnerArray = new ArrayList<>();
    ProgressDialog dialog;
    String descricao;
    String titulo;
    String categoria;
    Spinner categoria_spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_demanda);
        Toolbar toolbar = findViewById(R.id.toolbar_create_demanda);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //  ------ SHARED PREFERENCES -----
        prefs = getSharedPreferences("user_info", MODE_PRIVATE);
        token = prefs.getString("token", "No name defined");
        id = prefs.getInt("id", 0);
        Log.e("token ", token + " ID " + id);
        try {
            spinnerArray.add("Diarista/Limpeza");
            spinnerArray.add("Construção/Civil");
            categoria_spinner = findViewById(R.id.categoria);
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(create_demanda.this, android.R.layout.simple_spinner_item, spinnerArray);
            categoria_spinner.setAdapter(spinnerAdapter);
            titulo_text = findViewById(R.id.titulo);
            descricao_text = findViewById(R.id.descricao);
            titulo = titulo_text.getText().toString();
            descricao = descricao_text.getText().toString();
            categoria = categoria_spinner.getSelectedItem().toString();
        }catch (Exception e){
            Log.e("erro", e.getMessage());
        }
    }

    public void create_demanda_post(View view) {
        new Create_Demanda().execute();
    }

    private class Create_Demanda extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(create_demanda.this, R.style.styleProgressDialog);
            dialog.setTitle("Nova demanda!");
            dialog.setMessage("Criando a demanda...");
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
                json.put("id", id);
                json.put("token", token);
                json.put("titulo", titulo);
                json.put("descricao", descricao);
                json.put("categoria", categoria);

                URL url = new URL("http://192.168.0.105:8000/create_demanda");
                final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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
                if(responseCode == HttpURLConnection.HTTP_NOT_FOUND){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Não foi possivel criar a demanda! ",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Demanda Criada! ",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                    startActivity(new Intent(create_demanda.this, demandas.class));
                }
            } catch (ProtocolException ex) {
                Log.e("protocol", ex.getMessage());
            } catch (IOException ex) {
                Log.e("io", ex.getMessage());
            } catch (JSONException ex) {
                Log.e("json", ex.getMessage());
            }
            return null;
        }
    }


}
