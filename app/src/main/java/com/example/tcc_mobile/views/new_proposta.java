package com.example.tcc_mobile.views;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tcc_mobile.R;
import com.example.tcc_mobile.classes.Message_Session;
import com.example.tcc_mobile.classes.User;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class new_proposta extends AppCompatActivity {

    EditText data_texto_inicio;
    EditText data_texto_fim;
    String data;
    EditText valor_texto;
    EditText descricao_texto;
    String categoria;
    Spinner demanda_spinner;
    List<String> spinnerArray = new ArrayList<>();
    User user = new User();
    SharedPreferences prefs;
    String token;
    int id;
    String descricao;
    String data_inicio;
    String data_fim;
    String demanda;
    String valor;
    String session_id;
    Message_Session message_session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_proposta);
        Toolbar toolbar = findViewById(R.id.toolbar_new_proposta);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        user = bundle.getParcelable("user");
        message_session = bundle.getParcelable("message_session");
        Log.e("session", String.valueOf(message_session.getId()));

        data_texto_inicio = findViewById(R.id.data_inicio_text);
        data_texto_fim = findViewById(R.id.data_fim);
        valor_texto = findViewById(R.id.valor_proposta);
        descricao_texto = findViewById(R.id.descricao_proposta);

        prefs = getSharedPreferences("user_info", MODE_PRIVATE);
        token = prefs.getString("token", "No name defined");
        id = prefs.getInt("id", 0);
        Log.e("token ", token + " ID " + id);
        demanda_spinner = findViewById(R.id.spinner_proposta);
        new Get_Demandas_User().execute();

    }

    public void set_adapter_spinner(){
        try {
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(new_proposta.this, android.R.layout.simple_spinner_item, spinnerArray);
            demanda_spinner.setAdapter(spinnerAdapter);
        }catch (Exception e){

        }
    }

    public void date_picker_inicio(View view) {
        final View dialogView = View.inflate(this, R.layout.date_time_piker, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePicker datePicker =  dialogView.findViewById(R.id.date_picker);
                Calendar calendar = new GregorianCalendar(
                        datePicker.getYear(),
                        datePicker.getMonth(),
                        datePicker.getDayOfMonth());
                Date date = Calendar.getInstance().getTime();
                DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
                data = dateFormat.format(date);
                data_texto_inicio.setText(data);
                alertDialog.dismiss();
            }});
        alertDialog.setView(dialogView);
        alertDialog.show();
    }

    public void date_picker_fim(View view) {
        final View dialogView = View.inflate(this, R.layout.date_time_piker, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePicker datePicker =  dialogView.findViewById(R.id.date_picker);
                Calendar calendar = new GregorianCalendar(
                        datePicker.getYear(),
                        datePicker.getMonth(),
                        datePicker.getDayOfMonth());
                Date date = Calendar.getInstance().getTime();
                DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
                data_texto_inicio.setText(dateFormat.format(date));
                alertDialog.dismiss();
            }});
        alertDialog.setView(dialogView);
        alertDialog.show();
    }

    public void send_proposta(View view) {
        if(data_texto_fim.getText().toString().equals("") && data_texto_inicio.getText().toString().equals("") && valor_texto.getText().toString().equals("") && demanda_spinner.equals("")){
            valor = valor_texto.getText().toString();
            data_inicio = data_texto_inicio.getText().toString();
            data_fim = data_texto_fim.getText().toString();
            demanda = demanda_spinner.getSelectedItem().toString();


        }else{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Campos vazios! " ,
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private class Send_Proposta extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            JSONObject json = new JSONObject();
            try {
                json.put("id", id);
                json.put("token", token);
                json.put("valor", valor);
                json.put("data_inicio", data_inicio);
                json.put("data_fim", data_fim);
                json.put("demanda", demanda);
                json.put("descricao", descricao);
                json.put("session", session_id);

                URL url = new URL("http://192.168.0.105:8000/get_demandas_user");
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
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Intent intent = new Intent(new_proposta.this, propostas_consumidor.class);
                    startActivity(intent);
                }
            } catch (ProtocolException ex) {
                Log.e("protocol", ex.getMessage());
            } catch (IOException ex) {
                Log.e("io", ex.getMessage());
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            return null;
        }
    }


    private class Get_Demandas_User extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            set_adapter_spinner();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            JSONObject json = new JSONObject();
            try {
                json.put("id", id);
                json.put("token", token);
                json.put("session", message_session.getId());

                URL url = new URL("http://192.168.0.105:8000/get_demandas_user");
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
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String line = null;
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder builder = new StringBuilder();
                    for (line = null; (line = br.readLine()) != null; ) {
                        builder.append(line).append("\n");
                    }
                    JSONTokener tokener = new JSONTokener(builder.toString());
                    JSONArray finalResult = new JSONArray(tokener);
                    Log.e("222222", finalResult.toString());
                    for(int i=0; i < finalResult.length(); i++) {
                        spinnerArray.add(finalResult.getJSONObject(i).getString("titulo"));
                    }

                }
            } catch (ProtocolException ex) {
                Log.e("protocol", ex.getMessage());
            } catch (IOException ex) {
                Log.e("io", ex.getMessage());
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            return null;
        }
    }

}
