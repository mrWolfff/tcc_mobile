package com.example.tcc_mobile.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.tcc_mobile.R;
import com.example.tcc_mobile.adapters.Messages_Adapter;
import com.example.tcc_mobile.classes.Demandas;
import com.example.tcc_mobile.classes.Message_Session;
import com.example.tcc_mobile.classes.Messages;
import com.example.tcc_mobile.classes.User;
import com.example.tcc_mobile.interfaces.Actions;
import com.google.android.material.navigation.NavigationView;

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
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class message extends AppCompatActivity implements Actions {

    EditText message;
    String text;
    ImageButton sendMessage;
    ListView message_list;
    SharedPreferences prefs;
    String token;
    int id;
    Message_Session message_session;
    int position;
    NavigationView navigationView;
    AppBarConfiguration mAppBarConfiguration;
    Messages_Adapter adapter = new Messages_Adapter(message.this);
    NavController navController;
    int cont_there = 0;
    boolean status;
    int message_session_id;
    User request_user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar toolbar = findViewById(R.id.toolbar_message);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //toolbar.setTitle("Chat com USUARIO TAL");


        prefs = getSharedPreferences("user_info", MODE_PRIVATE);
        token = prefs.getString("token", "No name defined");
        id = prefs.getInt("id", 0);
        Log.e("token ", token + " ID " + id);

        Bundle bundle = getIntent().getExtras();
        message_session = bundle.getParcelable("message_session");
        //position = (int) bundle.getSerializable("position");
        Log.e("sessao ", String.valueOf(message_session.getId()));

        message = findViewById(R.id.message);
        sendMessage = findViewById(R.id.send_message);
        message_list = findViewById(R.id.messages_view);
        status = true;
        new Request_User().execute();
        new Get_Messages().execute();



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_message, menu);
        return true;
    }

    private void setRecyclerView(){
//        adapter = new Categorias_Adapter(message_list, message.this);
//        message_list =  findViewById(R.id.index_recyclerView);
//        message_list.setLayoutManager(new LinearLayoutManager(this));
//        message_list.setItemAnimator(new DefaultItemAnimator());
//        message_list.setAdapter(adapter);
//        //ItemTouchHelper touchHelper = new ItemTouchHelper(new TouchHelp(adapter));
//        //touchHelper.attachToRecyclerView(recyclerView);
    }


    public void send_message_chat(View view) {
        text = message.getText().toString();
        if (text.length() > 0) {
            new Send_Message().execute();
        }
    }

    @Override
    public void undo() {

    }
    @Override
    public void toast(Demandas demandas) {

    }
    @Override
    public void edit(int position) {

    }

    public void new_proposta(MenuItem item) {
        if(request_user.getCategoria_user().equals("Prestador")) {
            status = false;
            Intent intent = new Intent(this, new_proposta.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("message_session", message_session);
            intent.putExtras(bundle);
            startActivity(intent);
        }else{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Voce nao pode criar propostas! " ,
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void perfil_user(MenuItem item) {
    }

    public void logout(MenuItem item) {
    }

    private class Request_User extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            JSONObject json = new JSONObject();
            try {
                json.put("token", token);
                json.put("id", id);
                URL url = new URL("http://webservices.pythonanywhere.com/api/get_info");
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
                    request_user.setID(finalResult.getInt("id"));
                    request_user.setCategoria_user(finalResult.getString("categoria"));
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

    private class Get_Messages extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

                new Timer().scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        new Get_Messages_Loop().execute();
                    }
                }, 0, 5000);

        }

        @Override
        protected Void doInBackground(Void... voids) {
            JSONObject json = new JSONObject();
            try {
                json.put("token", token);
                json.put("id", id);
                json.put("message_session", message_session.getId());
                URL url = new URL("http://webservices.pythonanywhere.com/get_messages");
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
                        final Messages message = new Messages();
                        message.setSession(finalResult.getJSONObject(i).getInt("session"));
                        message.setMessage(finalResult.getJSONObject(i).getString("message"));
                        message.setFrom_user(finalResult.getJSONObject(i).getInt("from_user"));
                        message.setTo_user(finalResult.getJSONObject(i).getInt("to_user"));
                        message.setData(finalResult.getJSONObject(i).getString("data"));
                        message.setRequest_user(id);
                        Log.e("request_user", String.valueOf(message.getRequest_user()));
                        Log.e("from_user", String.valueOf(message.getFrom_user()));
                        Log.e("to_user", String.valueOf(message.getTo_user()));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.add(message);
                                message_list.setAdapter(adapter);
                                message_list.setSelection(message_list.getCount() - 1);
                            }
                        });
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

    private class Send_Message extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    message.getText().clear();
                }
            });
        }

        @Override
        protected Void doInBackground(Void... voids) {
            JSONObject json = new JSONObject();
            try {
                json.put("token", token);
                json.put("id", id);
                json.put("message", text);
                json.put("message_session", message_session.getId());
                URL url = new URL("http://webservices.pythonanywhere.com/send_message");
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



    private class Get_Messages_Loop extends AsyncTask<Void,Void,Void> {
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
                json.put("message_session", message_session.getId());
                json.put("cont_there", cont_there);
                URL url = new URL("http://webservices.pythonanywhere.com/get_messages_loop");
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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.clear();
                            adapter.notifyDataSetChanged();
                        }
                    });
                    for(int i = 0; i < finalResult.length() ; i++){
                        final Messages message = new Messages();
                        message.setSession(finalResult.getJSONObject(i).getInt("session"));
                        message.setMessage(finalResult.getJSONObject(i).getString("message"));
                        message.setFrom_user(finalResult.getJSONObject(i).getInt("from_user"));
                        message.setTo_user(finalResult.getJSONObject(i).getInt("to_user"));
                        message.setData(finalResult.getJSONObject(i).getString("data"));
                        message.setRequest_user(id);
                        Log.e("request_user", String.valueOf(message.getRequest_user()));
                        Log.e("from_user", String.valueOf(message.getFrom_user()));
                        Log.e("to_user", String.valueOf(message.getTo_user()));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.add(message);
                                message_list.setAdapter(adapter);
                                message_list.setSelection(message_list.getCount() - 1);
                            }
                        });
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

}
