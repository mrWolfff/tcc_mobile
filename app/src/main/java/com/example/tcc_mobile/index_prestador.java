package com.example.tcc_mobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tcc_mobile.adapters.Demandas_Adapter;
import com.example.tcc_mobile.classes.Demandas;
import com.example.tcc_mobile.classes.User;
import com.example.tcc_mobile.interfaces.Actions;
import com.example.tcc_mobile.touch_helper.TouchHelp;
import com.example.tcc_mobile.views.box_message;
import com.example.tcc_mobile.views.demanda_atual;
import com.example.tcc_mobile.views.perfil;
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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class index_prestador extends AppCompatActivity implements Actions {
    String token;
    SharedPreferences prefs;
    int id;
    User user = new User();
    TextView text_titulo;
    List<Demandas> listademandas = new ArrayList<>();
    Demandas_Adapter adapter;
    RecyclerView recyclerView;
    NavigationView navigationView;
    AppBarConfiguration mAppBarConfiguration;
    NavController navController;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_prestador);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout_prestador);
        navigationView = findViewById(R.id.nav_view_prestador);
        mAppBarConfiguration = new AppBarConfiguration.Builder()
                .setDrawerLayout(drawer)
                .build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_prestador);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        // ----  MENU LATERAL -----
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.home_prestador:
                        Intent intent = new Intent(getApplicationContext(), index_prestador.class);
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
                        perfil();
                        break;
                    case R.id.logout:
                        logout_();
                        break;
                }
                return false;
            }
        });



        //  ------ SHARED PREFERENCES -----
        prefs = getSharedPreferences("user_info", MODE_PRIVATE);
        token = prefs.getString("token", "No name defined");
        id = prefs.getInt("id", 0);
        Log.e("token ", token + " ID " + id);
        new Request_User().execute();
        try{
            Log.e("user", user.getCategoria_user());
            text_titulo = findViewById(R.id.text_titulo);
            String text = "Demanda da categoria " + user.getCategoria_user();
            text_titulo.setText(text);
        }catch (Exception e){
            e.printStackTrace();
        }
        new Get_Demanda_Index().execute();


    }

    public void perfil(){
        Intent intent = new Intent(this, perfil.class);
        startActivity(intent);
    }

    public void logout_(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder alert = new AlertDialog.Builder(index_prestador.this);
                alert.setTitle("Logout!");
                alert.setMessage("Voce tem certeza que deseja sair?");

                alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        prefs = getSharedPreferences("user_info", MODE_PRIVATE);
                        editor = prefs.edit();
                        editor.clear();
                        editor.apply();
                        Intent intent = new Intent(index_prestador.this, login.class);
                        startActivity(intent);
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

    private void setRecyclerView(){
        adapter = new Demandas_Adapter(listademandas, this);
        recyclerView =  findViewById(R.id.prestador_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(new TouchHelp(adapter));
        touchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void undo() {

    }

    @Override
    public void toast(Demandas demandas) {

    }

    @Override
    public void edit(int position) {
        Intent intent = new Intent(this, demanda_atual.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("demanda", adapter.getLista_demandas().get(position));
        bundle.putSerializable("position", position);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private class Get_Demanda_Index extends AsyncTask<Void,Void,Void>{

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
                URL url = new URL("http://webservices.pythonanywhere.com/index_mobile");
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
                        demanda.setDescricao(finalResult.getJSONObject(i).getString("descricao"));
                        demanda.setData(finalResult.getJSONObject(i).getString("data"));
                        demanda.setUser_demanda_string(finalResult.getJSONObject(i).getString("user_demanda"));
                        demanda.setCategoria_string(finalResult.getJSONObject(i).getString("categoria"));
                        listademandas.add(demanda);
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


    private class Request_User extends AsyncTask<Void, Void, Void> {
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
                    Log.e("222222", finalResult.toString());
                    user.setFirst_name(finalResult.getString("first_name"));
                    user.setLast_name(finalResult.getString("last_name"));
                    user.setUsername(finalResult.getString("username"));
                    user.setEmail(finalResult.getString("email"));
                    user.setCategoria_user(finalResult.getString("categoria"));

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
}
