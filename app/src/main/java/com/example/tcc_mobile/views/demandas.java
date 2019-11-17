package com.example.tcc_mobile.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tcc_mobile.R;
import com.example.tcc_mobile.TouchHelp;
import com.example.tcc_mobile.adapters.Demandas_Adapter;
import com.example.tcc_mobile.classes.Demandas;
import com.example.tcc_mobile.classes.User;
import com.example.tcc_mobile.interfaces.Actions;

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
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class demandas extends AppCompatActivity implements Actions {
    private String token;
    private int id;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    ProgressDialog dialog;
    User user = new User();
    List<Demandas> listademandas = new ArrayList<>();
    Demandas_Adapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demandas);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //  ------ SHARED PREFERENCES -----  //
        prefs = getSharedPreferences("user_info", MODE_PRIVATE);
        token = prefs.getString("token", "No name defined");
        id = prefs.getInt("id", 0);
        Log.e("token ", token + " ID " + id);
        new demandas.Request_User().execute();
        new demandas.Get_Demandas().execute();
        setRecyclerView();

    }

    private void setRecyclerView(){
        adapter = new Demandas_Adapter(listademandas, this);
        recyclerView =  findViewById(R.id.itemRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(new TouchHelp(adapter));
        touchHelper.attachToRecyclerView(recyclerView);
    }


    @Override
    public void undo() {

    }

    @Override
    public void toast(Demandas demandas) {

    }

    @Override
    public void edit(int position) {
        Intent intent = new Intent(this, edit_demanda.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("demanda", adapter.getLista_demandas().get(position));
        bundle.putSerializable("position", position);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private class Get_Demandas extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }

        @Override
        protected Void doInBackground(Void... voids) {
            JSONObject json = new JSONObject();
            try {
                json.put("token", token);
                json.put("id", id);
                URL url = new URL("http://192.168.0.104:8000/get_demandas");
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
                    JSONArray finalResult = new JSONArray(tokener);
                    Log.e("result", finalResult.toString());
                    for(int i = 0; i < finalResult.length() ; i++){
                        Demandas demanda = new Demandas();
                        demanda.setId(finalResult.getJSONObject(i).getInt("id"));
                        demanda.setTitulo(finalResult.getJSONObject(i).getString("titulo"));
                        demanda.setCategoria(finalResult.getJSONObject(i).getInt("categoria"));
                        demanda.setDescricao(finalResult.getJSONObject(i).getString("descricao"));
                        demanda.setUser_demanda(finalResult.getJSONObject(i).getInt("user_demanda"));
                        demanda.setData(finalResult.getJSONObject(i).getString("data"));
                        listademandas.add(demanda);
                        Log.e("titulo: ", demanda.getTitulo());
                        Log.e("categoria: ", String.valueOf(demanda.getCategoria()));
                        Log.e("descricao: ", demanda.getDescricao());
                    }
                }
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class Request_User extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(demandas.this, R.style.styleProgressDialog);
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
                URL url = new URL("http://192.168.0.104:8000/api/get_info");
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
                    user.setFirst_name(finalResult.getString("first_name"));
                    user.setLast_name(finalResult.getString("last_name"));
                    user.setUsername(finalResult.getString("username"));
                    user.setEmail(finalResult.getString("email"));
                    user.setCategoria_user(finalResult.getString("categoria_user"));

                    editor = getSharedPreferences("user_info", MODE_PRIVATE).edit();
                    editor.putString("token", user.getToken());
                    editor.putInt("id", user.getID());
                    editor.apply();
                } else {
                    Toast.makeText(getApplicationContext(), "Usuario ou senha incorretos!", Toast.LENGTH_SHORT).show();
                }
            } catch (MalformedURLException e) {
                Log.e("connection_error_url", e.getMessage());
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Usuario ou senha incorretos!", Toast.LENGTH_SHORT).show();
                Log.e("connection_error_io", e.getMessage());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
