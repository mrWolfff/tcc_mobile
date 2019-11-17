package com.example.tcc_mobile;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tcc_mobile.adapters.Demandas_Adapter;

public class TouchHelp extends ItemTouchHelper.SimpleCallback {

    private Demandas_Adapter adapter;

    public TouchHelp(Demandas_Adapter adapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT);
        this.adapter = adapter;
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