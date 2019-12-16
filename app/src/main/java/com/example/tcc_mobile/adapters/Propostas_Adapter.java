package com.example.tcc_mobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tcc_mobile.R;
import com.example.tcc_mobile.classes.Propostas;
import com.example.tcc_mobile.interfaces.Actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Propostas_Adapter extends RecyclerView.Adapter{
    private List<Propostas> lista_propostas = new ArrayList<>();
    private Actions actions;
    private int posicaoRemovidoRecentemente;
    private Propostas filmeRemovidoRecentemente;

    public Propostas_Adapter(List<Propostas> lista_propostas, Actions actions) {
        this.lista_propostas = lista_propostas;
        this.actions = actions;
    }

    public List<Propostas> getLista_propostas() {
        return lista_propostas;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row_propostas, viewGroup, false);
        PropostasViewHolder holder = new PropostasViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int i) {
        PropostasViewHolder holder = (PropostasViewHolder) viewHolder;
        holder.proposta.setText(lista_propostas.get(i).getProposta());
        holder.valor.setText(String.valueOf(lista_propostas.get(i).getValor()));
        holder.data.setText(String.valueOf(lista_propostas.get(i).getData()));
        holder.data_inicio.setText(lista_propostas.get(i).getData_inicio());
        holder.data_fim.setText(String.valueOf(lista_propostas.get(i).getData_fim()));
        holder.demanda.setText(String.valueOf(lista_propostas.get(i).getDemanda()));
        holder.user_proposta.setText(String.valueOf(lista_propostas.get(i).getUser_proposta()));
        holder.to_user_proposta.setText(String.valueOf(lista_propostas.get(i).getTo_user_proposta()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actions.edit(viewHolder.getAdapterPosition());
            }
        });
    }

    public void remover(int position){
        posicaoRemovidoRecentemente = position;
        filmeRemovidoRecentemente = lista_propostas.get(position);
        lista_propostas.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,this.getItemCount());
        actions.undo();
    }

    public void restaurar(){
        lista_propostas.add(posicaoRemovidoRecentemente,filmeRemovidoRecentemente);
        notifyItemInserted(posicaoRemovidoRecentemente);
    }

    public void inserir(Propostas propostas){
        lista_propostas.add(propostas);
        //notifyItemInserted(getItemCount());
    }

    public void mover(int fromPosition, int toPosition){
        if (fromPosition < toPosition)
            for (int i = fromPosition; i < toPosition; i++)
                Collections.swap(lista_propostas, i, i+1);
        else
            for (int i = fromPosition; i > toPosition; i--)
                Collections.swap(lista_propostas, i, i-1);
        notifyItemMoved(fromPosition,toPosition);
    }

    @Override
    public int getItemCount() {
        return lista_propostas.size();
    }

    public static class PropostasViewHolder extends RecyclerView.ViewHolder {

        TextView proposta;
        TextView data;
        TextView data_inicio;
        TextView data_fim;
        TextView user_proposta;
        TextView to_user_proposta;
        TextView demanda;
        TextView valor;


        public PropostasViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setTag(this);
            proposta =  itemView.findViewById(R.id.proposta_proposta);
            data = itemView.findViewById(R.id.data_proposta);
            valor = itemView.findViewById(R.id.valor_proposta_item);
            user_proposta = itemView.findViewById(R.id.user_proposta);
            data_inicio = itemView.findViewById(R.id.data_inicio_proposta);
            data_fim = itemView.findViewById(R.id.data_fim_proposta);
            to_user_proposta = itemView.findViewById(R.id.to_user_proposta);
            demanda = itemView.findViewById(R.id.demanda_proposta);
        }
    }
}
