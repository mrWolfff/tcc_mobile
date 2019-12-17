package com.example.tcc_mobile.views;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tcc_mobile.R;
import com.example.tcc_mobile.classes.Demandas;
import com.example.tcc_mobile.classes.Propostas;
import com.example.tcc_mobile.classes.Servicos;

public class concluir_servico extends AppCompatActivity {

    SharedPreferences prefs;
    String token;
    int id;
    Servicos servico = new Servicos();
    Demandas demanda = new Demandas();
    Propostas proposta = new Propostas();
    private RatingBar ratingBar;
    private TextView avaliacao;
    private Button btnSubmit;
    EditText sugestao_critica;
    String sugest_crit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concluir_servico);
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
        //Log.e("id_servico_demanda", String.valueOf(servico.getDemanda()));
        addListenerOnRatingBar();
        addListenerOnButton();
        sugestao_critica = findViewById(R.id.sugestao_critica);

    }

    public void addListenerOnRatingBar() {
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        avaliacao = findViewById(R.id.receive_rating);

        //se o valor de avaliação mudar,
        //exiba o valor de avaliação atual no resultado (textview) automaticamente
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float valor, boolean fromUser) {
                avaliacao.setText(String.valueOf(valor));
            }
        });
    }

    public void addListenerOnButton() {
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        //se o botão for clicado, exiba o valor de avaliação corrente.
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(concluir_servico.this,
                        String.valueOf(ratingBar.getRating()),
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void ok(View view) {
        if(sugestao_critica.length() > 0){
            sugest_crit = sugestao_critica.getText().toString();

        }
    }
}
