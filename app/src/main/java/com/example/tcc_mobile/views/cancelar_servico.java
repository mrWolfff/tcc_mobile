package com.example.tcc_mobile.views;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tcc_mobile.R;
import com.example.tcc_mobile.classes.Demandas;
import com.example.tcc_mobile.classes.Propostas;
import com.example.tcc_mobile.classes.Servicos;

public class cancelar_servico extends AppCompatActivity {

    SharedPreferences prefs;
    String token;
    int id;
    Servicos servico = new Servicos();
    Demandas demanda = new Demandas();
    Propostas proposta = new Propostas();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancelar_servico);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prefs = getSharedPreferences("user_info", MODE_PRIVATE);
        token = prefs.getString("token", "No name defined");
        id = prefs.getInt("id", 0);
        Log.e("token ", token + " ID " + id);

        Bundle bundle = getIntent().getExtras();
        servico = bundle.getParcelable("servico");
        proposta = bundle.getParcelable("proposta");
        demanda = bundle.getParcelable("demanda");
        Log.e("id_servico_demanda", String.valueOf(servico.getDemanda()));
    }

}
