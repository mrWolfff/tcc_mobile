package com.example.tcc_mobile;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class signup extends AppCompatActivity {
    List<String> spinnerArray = new ArrayList<>();
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Toolbar toolbar = findViewById(R.id.toolbar_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        spinnerArray.add("Diarista/Limpeza");
        spinnerArray.add("Construção/Civil");
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



}
