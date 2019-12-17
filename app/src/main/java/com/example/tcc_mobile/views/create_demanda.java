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
import com.example.tcc_mobile.classes.Categoria;

import org.json.JSONArray;
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
    List<Categoria>lista_categoria = new ArrayList<>();


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
        titulo_text = findViewById(R.id.titulo);
        descricao_text = findViewById(R.id.descricao_texto);
        new Get_Categorias().execute();

    }

    public void setLista(){
        //spinnerArray.add("Diarista/Limpeza");
        //spinnerArray.add("Construção/Civil");
        categoria_spinner = findViewById(R.id.categoria);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(create_demanda.this, android.R.layout.simple_spinner_item, spinnerArray);
        categoria_spinner.setAdapter(spinnerAdapter);



    }

    public void create_demanda_post(View view) {
        titulo = titulo_text.getText().toString();
        descricao = descricao_text.getText().toString();
        categoria = categoria_spinner.getSelectedItem().toString();
        new Create_Demanda().execute();
    }


    private class Get_Categorias extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setLista();


        }

        @Override
        protected Void doInBackground(Void... voids) {
            JSONObject json = new JSONObject();
            try {
                json.put("id", id);
                json.put("token", token);

                URL url = new URL("http://webservices.pythonanywhere.com/get_categorias");
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
                String line = null;
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder builder = new StringBuilder();
                for (line = null; (line = br.readLine()) != null; ) {
                    builder.append(line).append("\n");
                }
                JSONTokener tokener = new JSONTokener(builder.toString());
                JSONArray finalResult = new JSONArray(tokener);
                Log.e("result", finalResult.toString());
                for(int i = 0; i < finalResult.length() ; i++){
                    Categoria categoria = new Categoria();
                    categoria.setCategoria(finalResult.getJSONObject(i).getString("categoria"));
                    categoria.setId(finalResult.getJSONObject(i).getInt("id"));
                    spinnerArray.add(finalResult.getJSONObject(i).getString("categoria"));
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
            startActivity(new Intent(create_demanda.this, demandas.class));
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

                URL url = new URL("http://webservices.pythonanywhere.com/create_demanda");
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
