package com.example.tcc_mobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tcc_mobile.R;
import com.example.tcc_mobile.classes.Categoria;
import com.example.tcc_mobile.interfaces.Actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Categorias_Adapter extends RecyclerView.Adapter{
        private List<Categoria> lista_categorias = new ArrayList<>();
        private Actions actions;
        private int posicaoRemovidoRecentemente;
        private Categoria filmeRemovidoRecentemente;

        public Categorias_Adapter(List<Categoria> lista_categorias, Actions actions) {
            this.lista_categorias = lista_categorias;
            this.actions = actions;
        }

        public List<Categoria> getLista_categorias() {
            return lista_categorias;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row_categorias, viewGroup, false);
            CategoriaViewHolder holder = new CategoriaViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int i) {
            CategoriaViewHolder holder = (CategoriaViewHolder) viewHolder;
            holder.imagem_text.setImageBitmap(lista_categorias.get(i).getImagem());
            holder.categoria_text.setText(lista_categorias.get(i).getCategoria());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actions.edit(viewHolder.getAdapterPosition());
                }
            });
        }

        public void remover(int position){
            posicaoRemovidoRecentemente = position;
            filmeRemovidoRecentemente = lista_categorias.get(position);
            lista_categorias.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position,this.getItemCount());
            actions.undo();
        }

        public void restaurar(){
            lista_categorias.add(posicaoRemovidoRecentemente,filmeRemovidoRecentemente);
            notifyItemInserted(posicaoRemovidoRecentemente);
        }

        public void inserir(Categoria categorias){
            lista_categorias.add(categorias);
            //notifyItemInserted(getItemCount());
        }

        public void mover(int fromPosition, int toPosition){
            if (fromPosition < toPosition)
                for (int i = fromPosition; i < toPosition; i++)
                    Collections.swap(lista_categorias, i, i+1);
            else
                for (int i = fromPosition; i > toPosition; i--)
                    Collections.swap(lista_categorias, i, i-1);
            notifyItemMoved(fromPosition,toPosition);
        }

        @Override
        public int getItemCount() {
            return lista_categorias.size();
        }

        public static class CategoriaViewHolder extends RecyclerView.ViewHolder {

            TextView categoria_text;
            ImageView imagem_text;

            public CategoriaViewHolder(@NonNull View itemView) {
                super(itemView);
                itemView.setTag(this);
                imagem_text = itemView.findViewById(R.id.imagem_categoria);
                categoria_text = itemView.findViewById(R.id.categoria_name);

            }
        }
    }

