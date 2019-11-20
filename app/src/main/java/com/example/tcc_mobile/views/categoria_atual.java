package com.example.tcc_mobile.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tcc_mobile.R;
import com.example.tcc_mobile.adapters.Users_Adapter;
import com.example.tcc_mobile.classes.Categoria;
import com.example.tcc_mobile.classes.Demandas;
import com.example.tcc_mobile.classes.User;
import com.example.tcc_mobile.interfaces.Actions;

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
import java.util.ArrayList;
import java.util.List;

public class categoria_atual extends AppCompatActivity implements Actions {
    TextView title_categoria;
    SharedPreferences prefs;
    String token;
    int id;
    Categoria categoria = new Categoria();
    User user = new User();
    List<User> listausers = new ArrayList<>();
    Users_Adapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria_atual);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //  ------ SHARED PREFERENCES -----
        prefs = getSharedPreferences("user_info", MODE_PRIVATE);
        token = prefs.getString("token", "No name defined");
        id = prefs.getInt("id", 0);
        Log.e("token ", token + " ID " + id);

        title_categoria = findViewById(R.id.title_categoria);
        Bundle bundle = getIntent().getExtras();
        categoria = bundle.getParcelable("categoria");
        Log.e("categoria", categoria.getCategoria());
        String text = "Usuarios da categoria: "+ categoria.getCategoria();
        title_categoria.setText(text);
        new Get_User_Categoria().execute();
        setRecyclerView();

    }

    private void setRecyclerView() {
        adapter = new Users_Adapter(listausers, this);
        recyclerView = findViewById(R.id.itemRecyclerView_user);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        //ItemTouchHelper touchHelper = new ItemTouchHelper(new TouchHelp(adapter));
        //touchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void undo() {

    }

    @Override
    public void toast(Demandas demandas) {

    }

    @Override
    public void edit(int position) {
        Intent intent = new Intent(this, user_detail.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", adapter.getLista_users().get(position));
        intent.putExtras(bundle);
        startActivity(intent);

    }

    private class Get_User_Categoria extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            JSONObject json = new JSONObject();
            try {
                json.put("token", token);
                json.put("id", id);
                json.put("categoria", categoria.getCategoria());
                URL url = new URL("http://192.168.0.104:8000/get_user_categoria");
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
                        User user = new User();
                        user.setFirst_name(finalResult.getJSONObject(i).getString("first_name"));
                        user.setLast_name(finalResult.getJSONObject(i).getString("last_name"));
                        user.setUsername(finalResult.getJSONObject(i).getString("username"));
                        user.setCategoria(finalResult.getJSONObject(i).getInt("categoria_servico"));
                        listausers.add(user);
                        Log.e("nome: ", user.get_full_name());
                        Log.e("categoria: ", String.valueOf(user.getCategoria()));
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
