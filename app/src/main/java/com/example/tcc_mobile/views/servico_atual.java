package com.example.tcc_mobile.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tcc_mobile.R;
import com.example.tcc_mobile.classes.Demandas;
import com.example.tcc_mobile.classes.Propostas;
import com.example.tcc_mobile.classes.Servicos;

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

public class servico_atual extends AppCompatActivity {

    int id;
    String token;
    SharedPreferences prefs;
    int position;
    Servicos servico = new Servicos();
    Propostas proposta = new Propostas();
    Demandas demanda = new Demandas();

    TextView status_result;
    TextView proposta_result;
    TextView valor_result;
    TextView data_inicio_result;
    TextView data_fim_result;
    TextView usuario_result;
    TextView titulo_result;
    TextView descricao_result;
    TextView categoria_result;
    TextView usuario_demanda_result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servico_atual);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        status_result = findViewById(R.id.status_result);
        proposta_result = findViewById(R.id.proposta_result);
        valor_result = findViewById(R.id.valor_result);
        data_inicio_result = findViewById(R.id.data_inicio_result);
        data_fim_result = findViewById(R.id.data_fim_result);
        usuario_result = findViewById(R.id.usuario_result);
        titulo_result = findViewById(R.id.titulo_demanda_result);
        descricao_result = findViewById(R.id.descricao_demanda_result);
        categoria_result = findViewById(R.id.categoria_result);
        usuario_demanda_result = findViewById(R.id.usuario_demanda_result);


        prefs = getSharedPreferences("user_info", MODE_PRIVATE);
        token = prefs.getString("token", "No name defined");
        id = prefs.getInt("id", 0);
        Log.e("token ", token + " ID " + id);

        Bundle bundle = getIntent().getExtras();
        servico = bundle.getParcelable("servico");
        position = (int) bundle.getSerializable("position");
        Log.e("id_servico_demanda", String.valueOf(servico.getDemanda()));

        new Get_Servicos_Atual().execute();

    }

    public void setTextServicos(){
        status_result.setText(servico.getStatus());
    }
    public void setTextPropostas(){
        proposta_result.setText(proposta.getProposta());
        valor_result.setText(String.valueOf(proposta.getValor()));
        data_inicio_result.setText(proposta.getData_inicio());
        data_fim_result.setText(proposta.getData_fim());
        usuario_result.setText(proposta.getUser_proposta_string());
    }
    public void setTextDemandas(){
        titulo_result.setText(demanda.getTitulo());
        descricao_result.setText(demanda.getDescricao());
        categoria_result.setText(demanda.getCategoria_string());
        usuario_demanda_result.setText(demanda.getUser_demanda_string());
    }

    public void concluir_servico(View view) {
        Intent intent = new Intent(this, concluir_servico.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("servico", servico.getId());
        bundle.putSerializable("demanda", demanda.getId());
        bundle.putSerializable("proposta", proposta.getId());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void cancelar_servico(View view) {
        Intent intent = new Intent(this, cancelar_servico.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("servico", servico);
        bundle.putParcelable("demanda", demanda);
        bundle.putParcelable("proposta", proposta);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private class Get_Servicos_Atual extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setTextServicos();
            new Get_Proposta_Servico().execute();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            JSONObject json = new JSONObject();
            try {
                json.put("token", token);
                json.put("id", id);
                json.put("id_servico", servico.getId());
                URL url = new URL("http://webservices.pythonanywhere.com/get_servico_atual");
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
                    Log.e("servico", finalResult.toString());
                    //servico.setStatus(finalResult.getString("status"));
                    servico.setProposta(finalResult.getInt("proposta"));
                    //servico.setDemanda(finalResult.getInt("demanda"));
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

    private class Get_Proposta_Servico extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setTextPropostas();
            new Get_Demanda_Servico().execute();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            JSONObject json = new JSONObject();
            try {
                json.put("token", token);
                json.put("id", id);
                json.put("id_proposta", servico.getProposta());
                URL url = new URL("http://webservices.pythonanywhere.com/get_proposta_atual");
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
                    Log.e("proposta", finalResult.toString());
                    proposta.setId(finalResult.getInt("id"));
                    proposta.setProposta(finalResult.getString("proposta"));
                    proposta.setValor(finalResult.getInt("valor"));
                    proposta.setData_inicio(finalResult.getString("data_inicio"));
                    proposta.setData_fim(finalResult.getString("data_fim"));
                    proposta.setUser_proposta_string(finalResult.getString("user_proposta"));
                    proposta.setDemanda(finalResult.getInt("demanda"));
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


    private class Get_Demanda_Servico extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setTextDemandas();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            JSONObject json = new JSONObject();
            try {
                json.put("token", token);
                json.put("id", id);
                json.put("id_demanda", proposta.getDemanda());
                URL url = new URL("http://webservices.pythonanywhere.com/get_demanda_atual");
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
                    Log.e("demanda", finalResult.toString());
                    demanda.setTitulo(finalResult.getString("titulo"));
                    demanda.setDescricao(finalResult.getString("descricao"));
                    demanda.setCategoria_string(finalResult.getString("categoria"));
                    demanda.setUser_demanda_string(finalResult.getString("user_demanda"));
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
