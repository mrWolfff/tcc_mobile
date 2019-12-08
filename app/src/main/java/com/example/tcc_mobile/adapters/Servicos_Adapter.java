//package com.example.tcc_mobile.adapters;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.tcc_mobile.R;
//import com.example.tcc_mobile.classes.Servicos;
//import com.example.tcc_mobile.interfaces.Actions;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//public class Servicos_Adapter extends RecyclerView.Adapter{
//        private List<Servicos> lista_servicos = new ArrayList<>();
//        private Actions actions;
//        private int posicaoRemovidoRecentemente;
//        private Servicos filmeRemovidoRecentemente;
//
//        public Servicos_Adapter(List<Servicos> lista_servicos, Actions actions) {
//            this.lista_servicos = lista_servicos;
//            this.actions = actions;
//        }
//
//        public List<Servicos> getLista_servicos() {
//            return lista_servicos;
//        }
//
//        @NonNull
//        @Override
//        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row_servicos, viewGroup, false);
//            com.example.tcc_mobile.adapters.Demandas_Adapter.DemandaViewHolder holder = new com.example.tcc_mobile.adapters.Demandas_Adapter.DemandaViewHolder(view);
//            return holder;
//        }
//
//        //@Override
////        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int i) {
////            ServicosViewHolder holder = (ServicosViewHolder) viewHolder;
////            //holder.categoria_text.setText(String.valueOf(lista_servicos.get(i).getCategoria()));
////            //holder.descricao_text.setText(lista_servicos.get(i).getDescricao());
////            //holder.user_text.setText(String.valueOf(lista_servicos.get(i).getUser_demanda()));
////            //holder.data_text.setText(lista_servicos.get(i).getData());
////            //holder.itemView.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    actions.edit(viewHolder.getAdapterPosition());
////                }
////            });
////        }
//
//        public void remover(int position){
//            posicaoRemovidoRecentemente = position;
//            filmeRemovidoRecentemente = lista_servicos.get(position);
//            lista_servicos.remove(position);
//            notifyItemRemoved(position);
//            notifyItemRangeChanged(position,this.getItemCount());
//            actions.undo();
//        }
//
//        public void restaurar(){
//            lista_servicos.add(posicaoRemovidoRecentemente,filmeRemovidoRecentemente);
//            notifyItemInserted(posicaoRemovidoRecentemente);
//        }
//
//        public void inserir(Servicos servicos){
//            lista_servicos.add(servicos);
//            //notifyItemInserted(getItemCount());
//        }
//
//        public void mover(int fromPosition, int toPosition){
//            if (fromPosition < toPosition)
//                for (int i = fromPosition; i < toPosition; i++)
//                    Collections.swap(lista_servicos, i, i+1);
//            else
//                for (int i = fromPosition; i > toPosition; i--)
//                    Collections.swap(lista_servicos, i, i-1);
//            notifyItemMoved(fromPosition,toPosition);
//        }
//
//        @Override
//        public int getItemCount() {
//            return lista_servicos.size();
//        }
//
//        public static class ServicosViewHolder extends RecyclerView.ViewHolder {
//
//            TextView titulo_text;
//            TextView categoria_text;
//            TextView descricao_text;
//            TextView user_text;
//            TextView data_text;
//
//            public ServicosViewHolder(@NonNull View itemView) {
//                super(itemView);
//                itemView.setTag(this);
//                titulo_text =  itemView.findViewById(R.id.titulo_text);
//                categoria_text = itemView.findViewById(R.id.categoria_text);
//                descricao_text = itemView.findViewById(R.id.descricao_text);
//                user_text = itemView.findViewById(R.id.user_text);
//                data_text = itemView.findViewById(R.id.data_text);
//            }
//        }
//
//    }
