package com.exataid.apontamentoplantio.telas.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.exataid.apontamentoplantio.R;
import com.exataid.apontamentoplantio.banco.modelos.SistemaPlantio;
import com.exataid.apontamentoplantio.utils.OnItemClickListener;

import java.util.List;

public class SistemaPlantioAdapter extends RecyclerView.Adapter<SistemaPlantioAdapter.ViewHolder> {
    private final List<SistemaPlantio> localDataSet;
    private final OnItemClickListener<SistemaPlantio> listener;

    public SistemaPlantioAdapter(List<SistemaPlantio> localDataSet, OnItemClickListener<SistemaPlantio> listener) {
        this.localDataSet = localDataSet;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView descNomeSisPlantio, codSisPlantio;

        public TextView getDescNomeSisPlantio() {
            return descNomeSisPlantio;
        }

        public TextView getCodSisPlantio() {
            return codSisPlantio;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            descNomeSisPlantio = itemView.findViewById(R.id.txtNomeFazenda);
            codSisPlantio = itemView.findViewById(R.id.txtCodFazenda);
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_lista_talhoes, parent, false
        );
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getDescNomeSisPlantio().setText(String.valueOf(localDataSet.get(position).getNomeSistemaPlantio()));
        holder.getCodSisPlantio().setText(String.valueOf(localDataSet.get(position).getCodSistemaPlantio()));
        holder.itemView.setOnClickListener(v ->
                listener.onItemClick(localDataSet.get(position)));
    }


    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
