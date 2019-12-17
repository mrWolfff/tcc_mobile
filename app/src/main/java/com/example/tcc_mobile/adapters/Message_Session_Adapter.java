package com.example.tcc_mobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tcc_mobile.R;
import com.example.tcc_mobile.classes.Message_Session;
import com.example.tcc_mobile.interfaces.Actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Message_Session_Adapter extends RecyclerView.Adapter{

        private List<Message_Session> lista_message_session = new ArrayList<>();
        private Actions actions;
        private int posicaoRemovidoRecentemente;
        private Message_Session filmeRemovidoRecentemente;

        public Message_Session_Adapter(List<Message_Session> lista_message_session, Actions actions) {
            this.lista_message_session = lista_message_session;
            this.actions = actions;
        }

        public List<Message_Session> getLista_message_session() {
            return lista_message_session;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row_message_session, viewGroup, false);
            Message_SessionViewHolder holder = new Message_SessionViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int i) {
            Message_SessionViewHolder holder = (Message_SessionViewHolder) viewHolder;
            holder.from_user.setText(lista_message_session.get(i).getFrom_user_string());
            holder.to_user.setText(lista_message_session.get(i).getTo_user_string());
            holder.imageView.setImageBitmap(lista_message_session.get(i).getImagem());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actions.edit(viewHolder.getAdapterPosition());
                }
            });
        }

        public void remover(int position){
            posicaoRemovidoRecentemente = position;
            filmeRemovidoRecentemente = lista_message_session.get(position);
            lista_message_session.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position,this.getItemCount());
            actions.undo();
        }

        public void restaurar(){
            lista_message_session.add(posicaoRemovidoRecentemente,filmeRemovidoRecentemente);
            notifyItemInserted(posicaoRemovidoRecentemente);
        }

        public void inserir(Message_Session message_session){
            lista_message_session.add(message_session);
            //notifyItemInserted(getItemCount());
        }

        public void mover(int fromPosition, int toPosition){
            if (fromPosition < toPosition)
                for (int i = fromPosition; i < toPosition; i++)
                    Collections.swap(lista_message_session, i, i+1);
            else
                for (int i = fromPosition; i > toPosition; i--)
                    Collections.swap(lista_message_session, i, i-1);
            notifyItemMoved(fromPosition,toPosition);
        }

        @Override
        public int getItemCount() {
            return lista_message_session.size();
        }

        public static class Message_SessionViewHolder extends RecyclerView.ViewHolder {

            TextView from_user;
            TextView to_user;
            ImageView imageView;

            public Message_SessionViewHolder(@NonNull View itemView) {
                super(itemView);
                itemView.setTag(this);
                from_user = itemView.findViewById(R.id.from_user_text);
                to_user = itemView.findViewById(R.id.to_user_text_categoria);
                imageView = itemView.findViewById(R.id.imagem_user);

            }
        }
    }

