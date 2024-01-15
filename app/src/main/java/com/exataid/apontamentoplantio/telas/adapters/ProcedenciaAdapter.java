package com.exataid.apontamentoplantio.telas.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.exataid.apontamentoplantio.R;
import com.exataid.apontamentoplantio.banco.modelos.Procedencia;
import com.exataid.apontamentoplantio.utils.OnItemClickListener;

import java.util.List;

public class ProcedenciaAdapter extends RecyclerView.Adapter<ProcedenciaAdapter.ViewHolder> {
    private final List<Procedencia> localDataSet;
    private final OnItemClickListener<Procedencia> listener;

    public ProcedenciaAdapter(List<Procedencia> localDataSet, OnItemClickListener<Procedencia> listener) {
        this.localDataSet = localDataSet;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView descNomeProcedencia, codProcedencia;

        public TextView getDescNomeProcedencia() {
            return descNomeProcedencia;
        }

        public TextView getCodProcedencia() {
            return codProcedencia;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            descNomeProcedencia = itemView.findViewById(R.id.txtNomeFazenda);
            codProcedencia = itemView.findViewById(R.id.txtCodFazenda);
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
        holder.getDescNomeProcedencia().setText(String.valueOf(localDataSet.get(position).getNomeProcedencia()));
        holder.getCodProcedencia().setText(String.valueOf(localDataSet.get(position).getCodProcedencia()));
        holder.itemView.setOnClickListener(v ->
                listener.onItemClick(localDataSet.get(position)));
    }


    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
