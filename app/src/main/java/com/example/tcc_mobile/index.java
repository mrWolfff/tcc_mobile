package com.example.tcc_mobile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tcc_mobile.adapters.Categorias_Adapter;
import com.example.tcc_mobile.classes.Categoria;
import com.example.tcc_mobile.classes.Demandas;
import com.example.tcc_mobile.classes.User;
import com.example.tcc_mobile.interfaces.Actions;
import com.example.tcc_mobile.views.box_message;
import com.example.tcc_mobile.views.categoria_atual;
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

    TextView title;
    TextView name;
    TextView email;
    String token;
    int id;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    ProgressDialog dialog;
    User user = new User();
    List<Categoria> listacategorias = new ArrayList<>();
    Categorias_Adapter adapter;
    RecyclerView recyclerView;
    NavigationView navigationView;
    NavController navController;


    private AppBarConfiguration mAppBarConfiguration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        //  ------ SHARED PREFERENCES -----
        prefs = getSharedPreferences("user_info", MODE_PRIVATE);
        token = prefs.getString("token", "No name defined");
        id = prefs.getInt("id", 0);
        Log.e("token ", token + " ID " + id);

        //

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
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
        View nav = navigationView.getHeaderView(0);
        title = findViewById(R.id.texto_titulo);
        name = nav.findViewById(R.id.nav_name);
        email = nav.findViewById(R.id.nav_email);



        new Request_User().execute();



    }

    private void setRecyclerView(){
        adapter = new Categorias_Adapter(listacategorias, this);
        recyclerView =  findViewById(R.id.index_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        //ItemTouchHelper touchHelper = new ItemTouchHelper(new TouchHelp(adapter));
        //touchHelper.attachToRecyclerView(recyclerView);
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder alert = new AlertDialog.Builder(index.this);
                alert.setTitle("Logout!");
                alert.setMessage("Voce tem certeza que deseja sair?");

                alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        prefs = getSharedPreferences("user_info", MODE_PRIVATE);
                        editor = prefs.edit();
                        editor.clear();
                        editor.apply();
                        Intent intent = new Intent(index.this, login.class);
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

    @Override
    public void undo() {
    }
    @Override
    public void toast(Demandas demandas) {
    }
    @Override
    public void edit(int position) {
        Intent intent = new Intent(this, categoria_atual.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("categoria", adapter.getLista_categorias().get(position));
        bundle.putSerializable("position", position);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private class Get_Categorias extends AsyncTask<Void,Void,Void>{

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
                URL url = new URL("http://192.168.0.108:8000/get_categorias");
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
                        Categoria categoria = new Categoria();
                        categoria.setId(finalResult.getJSONObject(i).getInt("id"));
                        categoria.setCategoria(finalResult.getJSONObject(i).getString("categoria"));
                        listacategorias.add(categoria);
                        Log.e("titulo: ", categoria.getCategoria());

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

    private class Request_User extends AsyncTask<Void,Void,Void> {


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //Log.e("USER PEGOU", user.getEmail());

            new Get_Categorias().execute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            JSONObject json = new JSONObject();
            try {
                json.put("token", token);
                json.put("id", id);
                URL url = new URL("http://192.168.0.108:8000/api/get_info");
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
                    //user.setToken(finalResult.getString("token"));
                    user.setFirst_name(finalResult.getString("first_name"));
                    user.setLast_name(finalResult.getString("last_name"));
                    user.setUsername(finalResult.getString("username"));
                    user.setEmail(finalResult.getString("email"));
                    user.setCategoria_user(finalResult.getString("categoria"));


                    Log.e("CATEGORIA", user.getCategoria_user());



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
