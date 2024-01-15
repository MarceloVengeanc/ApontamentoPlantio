package com.exataid.apontamentoplantio.telas.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.exataid.apontamentoplantio.R;
import com.exataid.apontamentoplantio.banco.modelos.TipoPlantio;
import com.exataid.apontamentoplantio.utils.OnItemClickListener;

import java.util.List;

public class TipoAdapter extends RecyclerView.Adapter<TipoAdapter.ViewHolder> {
    private final List<TipoPlantio> localDataSet;
    private final OnItemClickListener<TipoPlantio> listener;

    public TipoAdapter(List<TipoPlantio> localDataSet, OnItemClickListener<TipoPlantio> listener) {
        this.localDataSet = localDataSet;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView descNomeTipo, codTipo;

        public TextView getDescNomeTipo() {
            return descNomeTipo;
        }

        public TextView getCodTipo() {
            return codTipo;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            descNomeTipo = itemView.findViewById(R.id.txtNomeFazenda);
            codTipo = itemView.findViewById(R.id.txtCodFazenda);
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
        holder.getDescNomeTipo().setText(String.valueOf(localDataSet.get(position).getNomeTipo()));
        holder.getCodTipo().setText(String.valueOf(localDataSet.get(position).getCodTipo()));
        holder.itemView.setOnClickListener(v ->
                listener.onItemClick(localDataSet.get(position)));
    }


    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
