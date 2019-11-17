package com.example.tcc_mobile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tcc_mobile.adapters.Demandas_Adapter;
import com.example.tcc_mobile.classes.Demandas;
import com.example.tcc_mobile.classes.User;
import com.example.tcc_mobile.interfaces.Actions;
import com.example.tcc_mobile.views.box_message;
import com.example.tcc_mobile.views.create_demanda;
import com.example.tcc_mobile.views.demandas;
import com.example.tcc_mobile.views.servicos;
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
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.tcc_mobile.R.id.nav_gallery;

public class index extends AppCompatActivity implements Actions {

    TextView text_result;
    TextView title;
    TextView name;
    TextView email;
    private String token;
    private int id;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    ProgressDialog dialog;
    User user = new User();
    List<Demandas> listademandas = new ArrayList<>();
    Demandas_Adapter adapter;
    RecyclerView recyclerView;




    private AppBarConfiguration mAppBarConfiguration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);DrawerLayout drawer = findViewById(R.id.drawer_layout);


        NavigationView navigationView = findViewById(R.id.nav_view);

        //  ------ SHARED PREFERENCES -----
        prefs = getSharedPreferences("user_info", MODE_PRIVATE);
        token = prefs.getString("token", "No name defined");
        id = prefs.getInt("id", 0);
        Log.e("token ", token + " ID " + id);
        new index.Request_User().execute();



        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        // ----  MENU LATERAL -----
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.home:
                        Intent intent = new Intent(getApplicationContext(), index.class);
                        startActivity(intent);
                        break;
                    case R.id.criar_demanda:
                        intent = new Intent(getApplicationContext(), create_demanda.class);
                        startActivity(intent);
                        break;
                    case R.id.demandas:
                        intent = new Intent(getApplicationContext(), demandas.class);
                        startActivity(intent);
                        break;
                    case R.id.box:
                        intent = new Intent(getApplicationContext(), box_message.class);
                        startActivity(intent);
                        break;
                    case R.id.servicos:
                        intent = new Intent(getApplicationContext(), servicos.class);
                        startActivity(intent);
                        break;
                    case R.id.configuration:

                        break;
                    case R.id.logout:

                        break;
                }
                return false;
            }
        });



        title = findViewById(R.id.texto_titulo);
        name = navigationView.findViewById(R.id.name);
        email = navigationView.findViewById(R.id.email);

        //Bundle bundle = getIntent().getExtras();
        //user = bundle.getParcelable("user");

        try {
            Log.e("EMAILLLLLLLL", user.getEmail());
            String texto = "Bem vindo, " + user.get_full_name();
            title.setText(texto);
            name.setText(texto);
            email.setText(texto);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void perfil_user(MenuItem item) {

    }

    public void configuration_user(MenuItem item) {
    }

    public void logout(MenuItem item) {
        editor.clear();
        editor.apply();
        Intent intent = new Intent(index.this, login.class);
        startActivity(intent);
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

    private class Request_User extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(index.this, R.style.styleProgressDialog);
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
                    Log.e("222222", finalResult.toString());
                    User user = new User();
                    user.setFirst_name(finalResult.getString("first_name"));
                    user.setLast_name(finalResult.getString("last_name"));
                    user.setUsername(finalResult.getString("username"));
                    user.setEmail(finalResult.getString("email"));
                    user.setCategoria(finalResult.getString("categoria"));
                    user.setCategoria_user(finalResult.getString("categoria_user"));

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

    private class Index_Mobile extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            JSONObject json = new JSONObject();
            try {
                json.put("token", token);
                json.put("id", id);
                URL url = new URL("http://192.168.0.104:8000/index_mobile");
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

}
