package com.example.tcc_mobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tcc_mobile.R;
import com.example.tcc_mobile.classes.User;
import com.example.tcc_mobile.interfaces.Actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Users_Adapter extends RecyclerView.Adapter{
    private List<User> lista_users = new ArrayList<>();
    private Actions actions;
    private int posicaoRemovidoRecentemente;
    private User filmeRemovidoRecentemente;

    public Users_Adapter(List<User> lista_users, Actions actions) {
        this.lista_users = lista_users;
        this.actions = actions;
    }

    public List<User> getLista_users() {
        return lista_users;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row_users, viewGroup, false);
        UserViewHolder holder = new UserViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int i) {
        UserViewHolder holder = (UserViewHolder) viewHolder;
        holder.full_name.setText(lista_users.get(i).get_full_name());
        holder.username.setText(lista_users.get(i).getUsername());
        String text = String.valueOf(lista_users.get(i).getCategoria());
        holder.categoria.setText(text);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actions.edit(viewHolder.getAdapterPosition());
            }
        });
    }

    public void remover(int position){
        posicaoRemovidoRecentemente = position;
        filmeRemovidoRecentemente = lista_users.get(position);
        lista_users.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,this.getItemCount());
        actions.undo();
    }

    public void restaurar(){
        lista_users.add(posicaoRemovidoRecentemente,filmeRemovidoRecentemente);
        notifyItemInserted(posicaoRemovidoRecentemente);
    }

    public void inserir(User users){
        lista_users.add(users);
        //notifyItemInserted(getItemCount());
    }

    public void mover(int fromPosition, int toPosition){
        if (fromPosition < toPosition)
            for (int i = fromPosition; i < toPosition; i++)
                Collections.swap(lista_users, i, i+1);
        else
            for (int i = fromPosition; i > toPosition; i--)
                Collections.swap(lista_users, i, i-1);
        notifyItemMoved(fromPosition,toPosition);
    }

    @Override
    public int getItemCount() {
        return lista_users.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        TextView full_name;
        TextView username;
        TextView categoria;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setTag(this);
            full_name =  itemView.findViewById(R.id.full_name);
            username = itemView.findViewById(R.id.username);
            categoria = itemView.findViewById(R.id.categoria_user);
        }
    }
}
