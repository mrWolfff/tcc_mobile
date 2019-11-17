package com.example.tcc_mobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tcc_mobile.R;
import com.example.tcc_mobile.classes.Demandas;
import com.example.tcc_mobile.interfaces.Actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Demandas_Adapter extends RecyclerView.Adapter{
        private List<Demandas> lista_demandas = new ArrayList<>();
        private Actions actions;
        private int posicaoRemovidoRecentemente;
        private Demandas filmeRemovidoRecentemente;

        public Demandas_Adapter(List<Demandas> lista_demandas, Actions actions) {
            this.lista_demandas = lista_demandas;
            this.actions = actions;
        }

        public List<Demandas> getLista_demandas() {
            return lista_demandas;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row_demandas, viewGroup, false);
            DemandaViewHolder holder = new DemandaViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int i) {
            DemandaViewHolder holder = (DemandaViewHolder) viewHolder;
            holder.titulo_text.setText(lista_demandas.get(i).getTitulo());
            holder.categoria_text.setText(String.valueOf(lista_demandas.get(i).getCategoria()));
            holder.descricao_text.setText(lista_demandas.get(i).getDescricao());
            holder.user_text.setText(String.valueOf(lista_demandas.get(i).getUser_demanda()));
            holder.data_text.setText(lista_demandas.get(i).getData());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actions.edit(viewHolder.getAdapterPosition());
                }
            });
        }

        public void remover(int position){
            posicaoRemovidoRecentemente = position;
            filmeRemovidoRecentemente = lista_demandas.get(position);
            lista_demandas.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position,this.getItemCount());
            actions.undo();
        }

        public void restaurar(){
            lista_demandas.add(posicaoRemovidoRecentemente,filmeRemovidoRecentemente);
            notifyItemInserted(posicaoRemovidoRecentemente);
        }

        public void inserir(Demandas demandas){
            lista_demandas.add(demandas);
            //notifyItemInserted(getItemCount());
        }

        public void mover(int fromPosition, int toPosition){
            if (fromPosition < toPosition)
                for (int i = fromPosition; i < toPosition; i++)
                    Collections.swap(lista_demandas, i, i+1);
            else
                for (int i = fromPosition; i > toPosition; i--)
                    Collections.swap(lista_demandas, i, i-1);
            notifyItemMoved(fromPosition,toPosition);
        }

        @Override
        public int getItemCount() {
            return lista_demandas.size();
        }

        public static class DemandaViewHolder extends RecyclerView.ViewHolder {

            TextView titulo_text;
            TextView categoria_text;
            TextView descricao_text;
            TextView user_text;
            TextView data_text;

            public DemandaViewHolder(@NonNull View itemView) {
                super(itemView);
                itemView.setTag(this);
                titulo_text =  itemView.findViewById(R.id.titulo_text);
                categoria_text = itemView.findViewById(R.id.categoria_text);
                descricao_text = itemView.findViewById(R.id.descricao_text);
                user_text = itemView.findViewById(R.id.user_text);
                data_text = itemView.findViewById(R.id.data_text);
            }
        }
}
