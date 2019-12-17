package com.example.tcc_mobile;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.tcc_mobile.classes.Categoria;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class signup_prestador extends AppCompatActivity {

    ImageView imageView;
    int cont;
    SharedPreferences prefs;
    String token;
    int id;
    List<String> spinnerArray = new ArrayList<>();
    Spinner categoria_spinner;
    EditText celular;
    EditText cpf;
    EditText endereco;
    String celular_text;
    String cpf_text;
    String endereco_text;
    String categoria_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_prestador);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        prefs = getSharedPreferences("user_info", MODE_PRIVATE);
        token = prefs.getString("token", "No name defined");
        id = prefs.getInt("id", 0);
        Log.e("token ", token + " ID " + id);
        new Get_Categorias().execute();

    }

    public void setLista(){
        //spinnerArray.add("Diarista/Limpeza");
        //spinnerArray.add("Construção/Civil");
        categoria_spinner = findViewById(R.id.spinner_categoria);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(signup_prestador.this, android.R.layout.simple_spinner_item, spinnerArray);
        categoria_spinner.setAdapter(spinnerAdapter);
        celular = findViewById(R.id.celular);
        cpf = findViewById(R.id.cpf);
        endereco = findViewById(R.id.endereco);

    }

    public void getFromGallery(View view){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 0);
    }
    public void saveImage(View view) {
        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        }else {

            String image_name = "Image_" + cont++;

            String root = Environment.getExternalStorageDirectory().toString() + File.separator + "DCIM";
            File myDir = new File(root);
            myDir.mkdirs();
            String fname = image_name + ".jpg";
            File file = new File(myDir, fname);
            if (file.exists()) file.delete();
            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
                MediaStore.Images.Media.insertImage(getContentResolver()
                        ,file.getAbsolutePath(),file.getName(),file.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void cadastrar_prestador(View view) {

        celular_text = celular.getText().toString();
        cpf_text = cpf.getText().toString();
        endereco_text = endereco.getText().toString();
        categoria_text = categoria_spinner.getSelectedItem().toString();
        new Cadastro_Prestador().execute();
    }

    private class Cadastro_Prestador extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            JSONObject json = new JSONObject();
            try {
                json.put("id", id);
                json.put("token", token);
                json.put("celular", celular_text);
                json.put("cpf_cnpj", cpf_text);
                json.put("endereço", endereco_text);
                //json.put("categoria_outro", );
                json.put("categoria_servico", categoria_text);

                URL url = new URL("http://webservices.pythonanywhere.com/register_user_prestador");
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
                                    "Usuario ou senha incorretos! " ,
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Intent intent = new Intent(signup_prestador.this, index_prestador.class);
                    startActivity(intent);

                }

            } catch (IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Usuario ou senha incorretos! ",
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


    private class Get_Categorias extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setLista();


        }

        @Override
        protected Void doInBackground(Void... voids) {
            JSONObject json = new JSONObject();
            try {
                json.put("id", id);
                json.put("token", token);

                URL url = new URL("http://webservices.pythonanywhere.com/get_categorias");
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
                    categoria.setCategoria(finalResult.getJSONObject(i).getString("categoria"));
                    spinnerArray.add(finalResult.getJSONObject(i).getString("categoria"));
                }


            } catch (ProtocolException ex) {
                Log.e("protocol", ex.getMessage());
            } catch (IOException ex) {
                Log.e("io", ex.getMessage());
            } catch (JSONException ex) {
                Log.e("json", ex.getMessage());
            }
            return null;
        }
    }
}
