package com.example.tcc_mobile.views;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tcc_mobile.R;
import com.example.tcc_mobile.classes.Demandas;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class edit_demanda extends AppCompatActivity {
    int position;
    Demandas demanda = new Demandas();
    EditText titulo_edit;
    EditText descricao_edit;
    TextView categoria_edit;
    TextView user_edit;
    TextView titulo_demanda;
    ProgressDialog dialog;
    SharedPreferences prefs;
    String token;
    int id;
    String titulo_string;
    String descricao_string;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_demanda);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //  ------ SHARED PREFERENCES -----
        prefs = getSharedPreferences("user_info", MODE_PRIVATE);
        token = prefs.getString("token", "No name defined");
        id = prefs.getInt("id", 0);
        Log.e("token ", token + " ID " + id);

        Bundle bundle = getIntent().getExtras();
        demanda = bundle.getParcelable("demanda");
        position = (int) bundle.getSerializable("position");
        Log.e("id_demanda", String.valueOf(demanda.getId()));

        titulo_edit = findViewById(R.id.titulo_edit);
        categoria_edit = findViewById(R.id.categoria_edit);
        descricao_edit = findViewById(R.id.descricao_edit);
        user_edit = findViewById(R.id.user_edit);
        titulo_demanda = findViewById(R.id.titulo_demanda);

        try {
            String aux = "Demanda: " + demanda.getTitulo();
            titulo_demanda.setText(aux);
            titulo_edit.setText(demanda.getTitulo());
            categoria_edit.setText(String.valueOf(demanda.getCategoria()));
            descricao_edit.setText(demanda.getDescricao());
            user_edit.setText(String.valueOf(demanda.getUser_demanda()));

        } catch (Exception e) {
            Log.e("atributos: ", e.getMessage());
        }

    }


    public void on_button_delete(View view) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder alert = new AlertDialog.Builder(edit_demanda.this);
                alert.setTitle("Deletar!");
                alert.setMessage("Voce tem certeza que quer deletar a demanda?");

                alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        new Delete_Demanda().execute();
                    }
                });
                alert.setNegativeButton("Não",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        });

                alert.show();
            }
        });

    }

    public void on_button_ok(View view) {
        titulo_string = titulo_edit.getText().toString();
        descricao_string = descricao_edit.getText().toString();
        new Editar_Demanda().execute();
    }

    private class Editar_Demanda extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(edit_demanda.this, R.style.styleProgressDialog);
            dialog.setTitle("Editando");
            dialog.setMessage("Editando a demanda...");
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
                json.put("id_demanda", String.valueOf(demanda.getId()));
                json.put("titulo", titulo_string);
                json.put("descricao", descricao_string);

                URL url = new URL("http://192.168.0.105:8000/edit_demanda");
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
                if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Não foi possivel editar a demanda! ",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Demanda Editada! ",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
                Intent intent = new Intent(edit_demanda.this, demandas.class);
                startActivity(intent);
            } catch (ProtocolException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            return null;
        }
    }

    private class Delete_Demanda extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(edit_demanda.this, R.style.styleProgressDialog);
            dialog.setTitle("Deletando");
            dialog.setMessage("Deletando a demanda...");
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
                json.put("id_demanda", String.valueOf(demanda.getId()));

                URL url = new URL("http://192.168.0.105:8000m/delete_demanda");
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
                if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Não foi possivel excluir a demanda! ",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Demanda Excluida! ",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
                Intent intent = new Intent(edit_demanda.this, demandas.class);
                startActivity(intent);
            } catch (ProtocolException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            return null;
        }

    }
}
