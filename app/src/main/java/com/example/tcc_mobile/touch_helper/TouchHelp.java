package com.example.tcc_mobile.touch_helper;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tcc_mobile.adapters.Categorias_Adapter;
import com.example.tcc_mobile.adapters.Demandas_Adapter;
import com.example.tcc_mobile.adapters.Users_Adapter;

public class TouchHelp extends ItemTouchHelper.SimpleCallback {

    private Demandas_Adapter adapter;
    Categorias_Adapter adapter_categoria;
    Users_Adapter adapter_user;

    public TouchHelp(Demandas_Adapter adapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT);
        this.adapter = adapter;
    }
    public TouchHelp(Categorias_Adapter adapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT);
        this.adapter_categoria = adapter;
    }
    public TouchHelp(Users_Adapter adapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT);
        this.adapter_user = adapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        adapter.mover(viewHolder.getAdapterPosition(),viewHolder1.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        adapter.remover(viewHolder.getAdapterPosition());
    }


}