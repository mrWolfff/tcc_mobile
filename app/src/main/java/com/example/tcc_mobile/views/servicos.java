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
import androidx.viewpager.widget.ViewPager;

import com.example.tcc_mobile.R;
import com.example.tcc_mobile.adapters.Servicos_Adapter;
import com.example.tcc_mobile.adapters.TabAdapter;
import com.example.tcc_mobile.classes.Demandas;
import com.example.tcc_mobile.classes.Servicos;
import com.example.tcc_mobile.fragments.Tab_Servicos_Ativos;
import com.example.tcc_mobile.fragments.Tab_Servicos_Cancelados;
import com.example.tcc_mobile.fragments.Tab_Servicos_Concluidos;
import com.example.tcc_mobile.interfaces.Actions;
import com.google.android.material.tabs.TabLayout;

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

public class servicos extends AppCompatActivity implements Actions {

    ViewPager viewPager;
    TabLayout tabLayout;
    TabAdapter tabAdapter;
    SharedPreferences prefs;
    String token;
    int id;
    List<Servicos>lista_servicos = new ArrayList<>();
    List<Servicos>lista_servicos_cancelados = new ArrayList<>();
    List<Servicos>lista_servicos_concluidos = new ArrayList<>();
    Servicos_Adapter adapter;
    RecyclerView recyclerView;
    RecyclerView recyclerView_concluido;
    int proposta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicos);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.viewPager_servicos);
        tabLayout = findViewById(R.id.tabLayout_servicos);
        tabAdapter = new TabAdapter(getSupportFragmentManager());
        tabAdapter.addFragment(new Tab_Servicos_Ativos(), "Ativos");
        tabAdapter.addFragment(new Tab_Servicos_Cancelados(), "Cancelados");
        tabAdapter.addFragment(new Tab_Servicos_Concluidos(), "Concluídos");
        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);







        prefs = getSharedPreferences("user_info", MODE_PRIVATE);
        token = prefs.getString("token", "No name defined");
        id = prefs.getInt("id", 0);
        Log.e("token ", token + " ID " + id);
        new Get_Servicos().execute();
    }

    @Override
    public void undo() {

    }

    @Override
    public void toast(Demandas demandas) {

    }

    @Override
    public void edit(int position) {
        Intent intent = new Intent(this, servico_atual.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("servico", adapter.getLista_servicos().get(position));
        bundle.putSerializable("position", position);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    private void setRecyclerView(){
        adapter = new Servicos_Adapter(lista_servicos, this);
        recyclerView =  findViewById(R.id.servicos_ativos_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        //ItemTouchHelper touchHelper = new ItemTouchHelper(new TouchHelp(adapter));
        //touchHelper.attachToRecyclerView(recyclerView);
    }
    private void setRecyclerViewCancelados(){
        adapter = new Servicos_Adapter(lista_servicos_cancelados, this);
        recyclerView = findViewById(R.id.servicos_cancelados_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(servicos.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        //ItemTouchHelper touchHelper = new ItemTouchHelper(new TouchHelp(adapter));
        //touchHelper.attachToRecyclerView(recyclerView);
    }
    private void setRecyclerViewConcluidos(){
        adapter = new Servicos_Adapter(lista_servicos_concluidos, this);
        recyclerView_concluido =  findViewById(R.id.concluido_recyclerView);
        try {
            recyclerView_concluido.setLayoutManager(new LinearLayoutManager(this));
            recyclerView_concluido.setItemAnimator(new DefaultItemAnimator());
            recyclerView_concluido.setAdapter(adapter);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private class Get_Servicos extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            setRecyclerView();
            setRecyclerViewCancelados();
            setRecyclerViewConcluidos();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            JSONObject json = new JSONObject();
            try {
                json.put("token", token);
                json.put("id", id);
                URL url = new URL("http://192.168.0.105:8000/get_servicos");
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
                        Servicos servico = new Servicos();
                        //servico.setDemanda(finalResult.getJSONObject(i).getInt("demanda"));
                        servico.setStatus(finalResult.getJSONObject(i).getString("status"));
                        servico.setData_servico(finalResult.getJSONObject(i).getString("data_servico"));
                        servico.setUser_proposta_string(finalResult.getJSONObject(i).getString("user"));
                        servico.setProposta_string(finalResult.getJSONObject(i).getString("proposta"));
                        String nova = finalResult.getJSONObject(i).getString("user");
                        servico.setId(finalResult.getJSONObject(i).getInt("id"));
                        Log.e("user", nova);
                        servico.setUser_string(nova);
                        Log.e("proposta printando", servico.getProposta_string());
                        if(servico.getStatus().equals("Ativo")){
                            lista_servicos.add(servico);
                        }
                        if(servico.getStatus().equals("Cancelado")  || servico.getStatus().equals("Cancelado sem Confirmação")){
                            lista_servicos_cancelados.add(servico);
                        }
                        if(servico.getStatus().equals("Concluído")  || servico.getStatus().equals("Concluído sem Confirmação")){
                            lista_servicos_concluidos.add(servico);
                        }

                        Log.e("proposta: ", String.valueOf(servico.getProposta()));
                        //Log.e("categoria: ", String.valueOf(servico.getDemanda()));
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

