package com.example.tcc_mobile.views;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.tcc_mobile.R;
import com.example.tcc_mobile.adapters.TabAdapter;
import com.example.tcc_mobile.fragments.Tab_Servicos_Ativos;
import com.example.tcc_mobile.fragments.Tab_Servicos_Cancelados;
import com.example.tcc_mobile.fragments.Tab_Servicos_Concluidos;
import com.google.android.material.tabs.TabLayout;

public class servicos extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;
    TabAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicos);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.viewPager_servicos);
        tabLayout = findViewById(R.id.tabLayout_servicos);
        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new Tab_Servicos_Ativos(), "Ativos");
        adapter.addFragment(new Tab_Servicos_Cancelados(), "Cancelados");
        adapter.addFragment(new Tab_Servicos_Concluidos(), "Concluídos");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

}

