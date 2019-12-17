package com.example.tcc_mobile.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tcc_mobile.R;
import com.example.tcc_mobile.classes.Demandas;
import com.example.tcc_mobile.classes.Message_Session;

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

public class demanda_atual extends AppCompatActivity {
    SharedPreferences prefs;
    int id;
    String token;
    TextView texto_titulo;
    TextView texto_descricao;
    TextView texto_categoria;
    TextView texto_usuario;
    Demandas demanda = new Demandas();
    int position;
    String msg;
    Message_Session message_session = new Message_Session();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demanda_atual);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prefs = getSharedPreferences("user_info", MODE_PRIVATE);
        token = prefs.getString("token", "No name defined");
        id = prefs.getInt("id", 0);
        Log.e("token ", token + " ID " + id);

        Bundle bundle = getIntent().getExtras();
        demanda = bundle.getParcelable("demanda");
        position = (int) bundle.getSerializable("position");
        Log.e("id_demanda", String.valueOf(demanda.getId()));

        texto_titulo = findViewById(R.id.texto_titulo);
        texto_descricao = findViewById(R.id.texto_descricao);
        texto_categoria = findViewById(R.id.texto_categoria);
        texto_usuario = findViewById(R.id.texto_usuario);

        texto_titulo.setText(demanda.getTitulo());
        texto_descricao.setText(demanda.getDescricao());
        texto_categoria.setText(demanda.getCategoria_string());
        texto_usuario.setText(demanda.getUser_demanda_string());


        //new Get_Demandas().execute();
    }

    public void enviar_mensagem(View view) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder alert = new AlertDialog.Builder(demanda_atual.this);
                alert.setTitle("Enviar mensagem");
                final EditText input = new EditText(demanda_atual.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alert.setView(input);
                // alert.setMessage("Message");

                alert.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Log.e("alou", "lasdsdasda" + input.getText().toString());
                        if(input.length() > 0){
                            msg = input.getText().toString();
                            new Send_Message().execute();
                        }
                        //Your action here
                    }
                });
                alert.setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        });

                alert.show();
            }
        });
    }

    private class Send_Message extends AsyncTask<Void,Void,Void> {
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
                json.put("message", msg);
                json.put("to_user", demanda.getUser_demanda_string());
                URL url = new URL("http://webservices.pythonanywhere.com/send_message_view");
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

                    message_session.setFrom_user_string(finalResult.getString("from_user"));
                    message_session.setTo_user_string(finalResult.getString("to_user"));
                    message_session.setId(finalResult.getInt("id"));

                    Intent intent = new Intent(demanda_atual.this, message.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("message_session", message_session);
                    intent.putExtras(bundle);
                    startActivity(intent);



                    Log.e("deu boa", "deu boa");
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
