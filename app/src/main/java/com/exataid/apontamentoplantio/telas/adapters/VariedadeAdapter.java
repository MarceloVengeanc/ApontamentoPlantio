package com.exataid.apontamentoplantio.telas.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.exataid.apontamentoplantio.R;
import com.exataid.apontamentoplantio.banco.modelos.Variedade;
import com.exataid.apontamentoplantio.utils.OnItemClickListener;

import java.util.List;

public class VariedadeAdapter extends RecyclerView.Adapter<VariedadeAdapter.ViewHolder> {
    private final List<Variedade> localDataSet;
    private final OnItemClickListener<Variedade> listener;

    public VariedadeAdapter(List<Variedade> localDataSet, OnItemClickListener<Variedade> listener) {
        this.localDataSet = localDataSet;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView descNomeVariedade, codVariedade;

        public TextView getDescNomeVariedade() {
            return descNomeVariedade;
        }

        public TextView getCodVariedade() {
            return codVariedade;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            descNomeVariedade = itemView.findViewById(R.id.txtNomeFazenda);
            codVariedade = itemView.findViewById(R.id.txtCodFazenda);
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
        holder.getDescNomeVariedade().setText(String.valueOf(localDataSet.get(position).getNomeVariedade()));
        holder.getCodVariedade().setText(String.valueOf(localDataSet.get(position).getCodVariedade()));
        holder.itemView.setOnClickListener(v ->
                listener.onItemClick(localDataSet.get(position)));
    }


    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
