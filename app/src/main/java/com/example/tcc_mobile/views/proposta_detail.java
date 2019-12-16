package com.example.tcc_mobile.views;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tcc_mobile.R;
import com.example.tcc_mobile.classes.Propostas;

public class proposta_detail extends AppCompatActivity {

    SharedPreferences prefs;
    String token;
    int id;
    int position;
    Propostas proposta;
    TextView proposta_text;
    TextView valor_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proposta_detail);
        Toolbar toolbar = findViewById(R.id.toolbar_proposta_detail);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        prefs = getSharedPreferences("user_info", MODE_PRIVATE);
        token = prefs.getString("token", "No name defined");
        id = prefs.getInt("id", 0);
        Log.e("token ", token + " ID " + id);

        Bundle bundle = getIntent().getExtras();
        proposta = bundle.getParcelable("proposta");
        position = (int) bundle.getSerializable("position");
        Log.e("proposta", String.valueOf(proposta.getProposta()));

        proposta_text = findViewById(R.id.textView_proposta);
        valor_text = findViewById(R.id.textView_valor);
        proposta_text.setText(proposta.getProposta());
        valor_text.setText(String.valueOf(proposta.getValor()));

    }

}
