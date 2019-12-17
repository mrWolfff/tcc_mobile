package com.example.tcc_mobile.views;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tcc_mobile.R;
import com.example.tcc_mobile.classes.User;

public class enviar_message extends AppCompatActivity {

    SharedPreferences prefs;
    int id;
    String token;
    User user = new User();
    EditText mensagem;
    String msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_message);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mensagem = findViewById(R.id.mensagem);
    }

    public void enviar(View view) {
        if(mensagem.length() == 0){

        }else{
            msg = mensagem.getText().toString();
            //new Send_Message().execute();
        }

    }
}
