package com.example.tcc_mobile.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tcc_mobile.R;
import com.example.tcc_mobile.adapters.Propostas_Adapter;
import com.example.tcc_mobile.classes.Demandas;
import com.example.tcc_mobile.classes.Propostas;
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
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class propostas_consumidor extends AppCompatActivity implements Actions {

    SharedPreferences prefs;
    String token;
    int id;
    List<Propostas>lista_proposta = new ArrayList<>();
    Propostas_Adapter adapter;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propostas);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prefs = getSharedPreferences("user_info", MODE_PRIVATE);
        token = prefs.getString("token", "No name defined");
        id = prefs.getInt("id", 0);
        Log.e("token ", token + " ID " + id);

        new Get_Propostas().execute();


    }

    private void setRecyclerView(){
        adapter = new Propostas_Adapter(lista_proposta, this);
        recyclerView =  findViewById(R.id.item_recycler_proposta);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        //ItemTouchHelper touchHelper = new ItemTouchHelper(new TouchHelp(adapter));
        //touchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void undo() {

    }

    @Override
    public void toast(Demandas demandas) {

    }

    @Override
    public void edit(int position) {
        Intent intent = new Intent(this, proposta_detail.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("proposta", adapter.getLista_propostas().get(position));
        bundle.putSerializable("position", position);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    private class Get_Propostas extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setRecyclerView();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            JSONObject json = new JSONObject();
            try {
                json.put("token", token);
                json.put("id", id);
                URL url = new URL("http://webservices.pythonanywhere.com/get_propostas");
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
                        Propostas proposta = new Propostas();
                        proposta.setProposta(finalResult.getJSONObject(i).getString("proposta"));
                        proposta.setValor(finalResult.getJSONObject(i).getInt("valor"));
                        proposta.setData(finalResult.getJSONObject(i).getString("data"));
                        proposta.setData_inicio(finalResult.getJSONObject(i).getString("data_inicio"));
                        proposta.setData_fim(finalResult.getJSONObject(i).getString("data_fim"));
                        proposta.setUser_proposta(finalResult.getJSONObject(i).getInt("user_proposta"));
                        proposta.setTo_user_proposta(finalResult.getJSONObject(i).getInt("to_user_proposta"));
                        lista_proposta.add(proposta);
                        Log.e("titulo: ", proposta.getProposta());
                        Log.e("categoria: ", String.valueOf(proposta.getDemanda()));
                        Log.e("descricao: ", proposta.getData());
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

}
