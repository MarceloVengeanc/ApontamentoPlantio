package com.exataid.apontamentoplantio.telas.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.exataid.apontamentoplantio.R;
import com.exataid.apontamentoplantio.banco.modelos.CargaDeMuda;

import java.util.List;

public class ConsultaCargaDeMudaAdapter extends RecyclerView.Adapter<ConsultaCargaDeMudaAdapter.ViewHolder> {
    private List<CargaDeMuda> localDataSet;
    Context ctx;
    private Resources res;

    public ConsultaCargaDeMudaAdapter(List<CargaDeMuda> localDataSet, Context ctx, Resources res) {
        this.localDataSet = localDataSet;
        this.ctx = ctx;
        this.res = res;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtCodFazenda, txtNomeFazenda, txtCodTalhao, txtCodProcedencia, txtNomeProcedencia, txtCarga, txtPesoMedio;

        public TextView getTxtCodFazenda() {
            return txtCodFazenda;
        }

        public TextView getTxtNomeFazenda() {
            return txtNomeFazenda;
        }

        public TextView getTxtCodTalhao() {
            return txtCodTalhao;
        }

        public TextView getTxtCodProcedencia() {
            return txtCodProcedencia;
        }

        public TextView getTxtNomeProcedencia() {
            return txtNomeProcedencia;
        }

        public TextView getTxtCarga() {
            return txtCarga;
        }

        public TextView getTxtPesoMedio() {
            return txtPesoMedio;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtCodFazenda = itemView.findViewById(R.id.txtCodFaz);
            txtNomeFazenda = itemView.findViewById(R.id.txtNomeFazenda);
            txtCodTalhao = itemView.findViewById(R.id.txtCodTalhao);
            txtCarga = itemView.findViewById(R.id.txtNumCargas);
            txtCodProcedencia = itemView.findViewById(R.id.txtCodProcedencia);
            txtNomeProcedencia = itemView.findViewById(R.id.txtNomeProcedencia);
            txtPesoMedio = itemView.findViewById(R.id.txtPesoMedio);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_lista_consulta_carga_de_muda, parent, false);
        return new ConsultaCargaDeMudaAdapter.ViewHolder(v);
    }

    @SuppressLint("StringFormatMatches")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position % 2 != 0) {
            holder.itemView.setBackgroundColor(ctx.getColor(R.color.temaCinza));
        } else {
            holder.itemView.setBackgroundColor(ctx.getColor(R.color.temaBranco));
        }
        holder.getTxtCodFazenda().setText(res.getString(R.string.plantio, localDataSet.get(position).getCodFazenda() + "-" + localDataSet.get(position).getNomeFazenda()));
        holder.getTxtCodTalhao().setText(res.getString(R.string.plantio, localDataSet.get(position).getCodTalhao()));
        holder.getTxtCodProcedencia().setText(res.getString(R.string.plantio, localDataSet.get(position).getCodProcedencia()));
        holder.getTxtNomeProcedencia().setText(res.getString(R.string.plantio, localDataSet.get(position).getNomeProcedencia()));
        holder.getTxtCarga().setText(res.getString(R.string.plantio, localDataSet.get(position).getCarga()));
        String formato = ("%.2f");
        @SuppressLint("DefaultLocale") String peso = String.format(formato, localDataSet.get(position).getPeso()).replace(",", ".");
        holder.getTxtPesoMedio().setText(res.getString(R.string.plantio, peso));
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
