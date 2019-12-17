package com.example.tcc_mobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class signup extends AppCompatActivity {
    List<String> spinnerArray = new ArrayList<>();
    Spinner spinner;
    EditText ultimo_nome;
    EditText username;
    EditText nome;
    EditText email;
    EditText senha;
    EditText confirme_senha;
    String first_name;
    String last_name;
    String username_user;
    String password;
    String email_text;
    String categoria;
    String password_confirm;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Toolbar toolbar = findViewById(R.id.toolbar_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nome = findViewById(R.id.first_name);
        ultimo_nome = findViewById(R.id.last_name);
        username = findViewById(R.id.username_signup);
        email = findViewById(R.id.email);
        senha = findViewById(R.id.senha);
        confirme_senha = findViewById(R.id.confirm_senha);

        spinnerArray.add("Prestador");
        spinnerArray.add("Consumidor");
        spinner = findViewById(R.id.spinner_signup);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(signup.this, android.R.layout.simple_spinner_item, spinnerArray);
        spinner.setAdapter(spinnerAdapter);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    public void cadastrar(View view) {
        if(nome.length() == 0 || senha.length() == 0 || confirme_senha.length() == 0 || username.length() == 0 || ultimo_nome.length() == 0 || email.length() == 0){
            Toast.makeText(getApplicationContext(), "Complete todos os dados!", Toast.LENGTH_SHORT).show();
        }else{
            if(senha.getText().toString().equals(confirme_senha.getText().toString())){
                first_name = nome.getText().toString();
                last_name = ultimo_nome.getText().toString();
                username_user = username.getText().toString();
                password = senha.getText().toString();
                email_text = email.getText().toString();
                categoria = spinner.getSelectedItem().toString();
                password_confirm = confirme_senha.getText().toString();
                new Cadastro().execute();
            }else{
                Toast.makeText(getApplicationContext(), "Senha e Confirmação de senha são diferentes!" , Toast.LENGTH_SHORT).show();
            }
        }

    }

    private class Cadastro extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            JSONObject json = new JSONObject();
            try {
                json.put("first_name", first_name);
                json.put("last_name", last_name);
                json.put("username", username_user);
                json.put("password1", password);
                json.put("categoria", categoria);
                json.put("password2", password_confirm);
                json.put("email", email_text);

                URL url = new URL("http://webservices.pythonanywhere.com/register_user");
                final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                //String userpass = username + ":" + password;
                //String autorizacao = "Basic " + Base64.encodeToString(userpass.getBytes(), Base64.DEFAULT);
                //connection.setRequestProperty("Authorization", autorizacao);
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
                if(responseCode == HttpURLConnection.HTTP_NOT_FOUND )  {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Usuario  ja existe! " ,
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String line = null;
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder builder = new StringBuilder();
                    for (line = null; (line = br.readLine()) != null; ) {
                        builder.append(line).append("\n");
                    }
                    JSONTokener tokener = new JSONTokener(builder.toString());
                    JSONObject finalResult = new JSONObject(tokener);
                    Log.e("result", finalResult.toString());
                    User user = new User();
                    user.setToken(finalResult.getString("token"));
                    user.setID(finalResult.getInt("id"));
                    //user.setCategoria(finalResult.getString("categoria"))


                    editor = getSharedPreferences("user_info", MODE_PRIVATE).edit();
                    editor.putString("token", user.getToken());
                    editor.putInt("id", user.getID());
                    editor.apply();
                    if (finalResult.getString("categoria").equals("Consumidor")){
                        Intent intent = new Intent(signup.this, index.class);
                        startActivity(intent);
                    }else{
                        startActivity(new Intent(signup.this, signup_prestador.class));
                    }
                }

            } catch (IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Usuario ja existe! ",
                                Toast.LENGTH_LONG).show();
                    }
                });
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
