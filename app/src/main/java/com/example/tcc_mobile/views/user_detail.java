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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tcc_mobile.R;
import com.example.tcc_mobile.classes.Message_Session;
import com.example.tcc_mobile.classes.User;

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

public class user_detail extends AppCompatActivity {
    SharedPreferences prefs;
    String token;
    int id;
    User user = new User();
    User request_user = new User();
    TextView title_detail;
    TextView full_name_info;
    TextView username_info;
    TextView categoria_info;
    String msg;
    Message_Session message_session = new Message_Session();
    int id_user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //  ------ SHARED PREFERENCES -----
        prefs = getSharedPreferences("user_info", MODE_PRIVATE);
        token = prefs.getString("token", "No name defined");
        id = prefs.getInt("id", 0);
        Log.e("token ", token + " ID " + id);

        title_detail = findViewById(R.id.title_detail);
        full_name_info = findViewById(R.id.full_name_info);
        username_info = findViewById(R.id.username_info);
        categoria_info = findViewById(R.id.categoria_info);


        Bundle bundle = getIntent().getExtras();
        user = bundle.getParcelable("user");
        id_user = (int)bundle.getSerializable("id");
        Log.e("user", String.valueOf(user.getCategoria()));

        String text = "Informações do usuario";
        title_detail.setText(text);
        text = "Nome Completo: "+ user.get_full_name();
        full_name_info.setText(text);
        text = "Username: "+ user.getUsername();
        username_info.setText(text);
        text = "Categoria: " + user.getCategoria();
        categoria_info.setText(text);

        new Request_User().execute();
    }

    public void send_message(View view) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
            AlertDialog.Builder alert = new AlertDialog.Builder(user_detail.this);
            alert.setTitle("Enviar mensagem");
            final EditText input = new EditText(user_detail.this);
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
                        Log.e("mensagem", msg);
                        new Send_Message().execute();
                    }
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
                    Log.e("finalresult", finalResult.toString());
                    request_user.setID(finalResult.getInt("id"));
                    request_user.setFirst_name(finalResult.getString("first_name"));
                    request_user.setLast_name(finalResult.getString("last_name"));
                    request_user.setUsername(finalResult.getString("username"));
                    request_user.setEmail(finalResult.getString("email"));
                    Log.e("user", String.valueOf(request_user.getID()));
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
                json.put("to_user", id_user);
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
                    Intent intent = new Intent(user_detail.this, message.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("message_session", message_session);
                    intent.putExtras(bundle);
                    startActivity(intent);




                    Log.e("deu boa", "deu boa");
                }else{
                    Log.e("deu ruim", "deu ruim");
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
