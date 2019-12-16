package com.example.tcc_mobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tcc_mobile.R;
import com.example.tcc_mobile.classes.Servicos;
import com.example.tcc_mobile.interfaces.Actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Servicos_Adapter extends RecyclerView.Adapter{
        private List<Servicos> lista_servicos = new ArrayList<>();
        private Actions actions;
        private int posicaoRemovidoRecentemente;
        private Servicos filmeRemovidoRecentemente;

        public Servicos_Adapter(List<Servicos> lista_servicos, Actions actions) {
            this.lista_servicos = lista_servicos;
            this.actions = actions;
        }

        public List<Servicos> getLista_servicos() {
            return lista_servicos;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row_servicos, viewGroup, false);
            ServicosViewHolder holder = new ServicosViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int i) {
            ServicosViewHolder holder = (ServicosViewHolder) viewHolder;
            holder.proposta.setText(lista_servicos.get(i).getProposta_string());
            holder.data_servico.setText(lista_servicos.get(i).getData_servico());
            holder.status.setText(lista_servicos.get(i).getStatus());
            holder.user.setText(lista_servicos.get(i).getUser_string());
            //holder.demanda.setText(lista_servicos.get(i).getDemanda());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actions.edit(viewHolder.getAdapterPosition());
                }
            });
        }

        public void remover(int position){
            posicaoRemovidoRecentemente = position;
            filmeRemovidoRecentemente = lista_servicos.get(position);
            lista_servicos.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position,this.getItemCount());
            actions.undo();
        }

        public void restaurar(){
            lista_servicos.add(posicaoRemovidoRecentemente,filmeRemovidoRecentemente);
            notifyItemInserted(posicaoRemovidoRecentemente);
        }

        public void inserir(Servicos servicos){
            lista_servicos.add(servicos);
            //notifyItemInserted(getItemCount());
        }

        public void mover(int fromPosition, int toPosition){
            if (fromPosition < toPosition)
                for (int i = fromPosition; i < toPosition; i++)
                    Collections.swap(lista_servicos, i, i+1);
            else
                for (int i = fromPosition; i > toPosition; i--)
                    Collections.swap(lista_servicos, i, i-1);
            notifyItemMoved(fromPosition,toPosition);
        }

        @Override
        public int getItemCount() {
            return lista_servicos.size();
        }

        public static class ServicosViewHolder extends RecyclerView.ViewHolder {

            TextView proposta;
            TextView data_servico;
            TextView status;
            TextView user;
            //TextView demanda;

            public ServicosViewHolder(@NonNull View itemView) {
                super(itemView);
                itemView.setTag(this);
                proposta =  itemView.findViewById(R.id.proposta_servico);
                user = itemView.findViewById(R.id.user_servico);
                data_servico =  itemView.findViewById(R.id.data_servico_text);
                status =  itemView.findViewById(R.id.status_servico);
                //demanda = itemView.findViewById(R.id.demanda);
            }
        }

    }
