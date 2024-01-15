package com.exataid.apontamentoplantio.telas.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.exataid.apontamentoplantio.APDatabase;
import com.exataid.apontamentoplantio.R;
import com.exataid.apontamentoplantio.banco.modelos.CargaDeMuda;
import com.exataid.apontamentoplantio.utils.CliqueRecycler;

import java.text.DecimalFormat;
import java.util.List;

public class CargaDeMudaAdapter extends RecyclerView.Adapter<CargaDeMudaAdapter.ViewHolder> {

    private final List<CargaDeMuda> localDataSet;

    private final Context ctx;
    private final Resources res;
    private CliqueRecycler cliqueRecycler;

    public void setCliqueRecycler(CliqueRecycler cliqueRecycler) {
        this.cliqueRecycler = cliqueRecycler;
    }

    public CargaDeMudaAdapter(List<CargaDeMuda> localDataSet, Context ctx, Resources res) {
        this.localDataSet = localDataSet;
        this.ctx = ctx;
        this.res = res;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtCodFazenda, txtNomeFazenda, txtCodTalhao, txtCodProcedencia, txtNomeProcedencia, txtCarga, txtPesoMedio;
        ImageView btnDelete;

        public ImageView getBtnDelete() {
            return btnDelete;
        }

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
            btnDelete = itemView.findViewById(R.id.btnLixeira);
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
        View registros = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lista_carga_de_muda, parent, false);
        return new ViewHolder(registros);

    }

    @SuppressLint("StringFormatMatches")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.getTxtCodFazenda().setText(res.getString(R.string.plantio, localDataSet.get(position).getCodFazenda()));
        holder.getTxtNomeFazenda().setText(res.getString(R.string.plantio, localDataSet.get(position).getNomeFazenda()));
        holder.getTxtCodTalhao().setText(res.getString(R.string.plantio, localDataSet.get(position).getCodTalhao()));
        holder.getTxtCodProcedencia().setText(res.getString(R.string.plantio, localDataSet.get(position).getCodProcedencia()));
        holder.getTxtNomeProcedencia().setText(res.getString(R.string.plantio, localDataSet.get(position).getNomeProcedencia()));
        holder.getTxtCarga().setText(res.getString(R.string.plantio, localDataSet.get(position).getCarga()));
        @SuppressLint("DefaultLocale") String pesoFormat = String.format("%.2f", localDataSet.get(position).getPeso()).replace(",",".");
        holder.getTxtPesoMedio().setText(res.getString(R.string.plantio, pesoFormat));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cliqueRecycler != null) {
                    cliqueRecycler.onItemClick(position);
                }
            }
        });
        holder.getBtnDelete().setOnClickListener(new View.OnClickListener() {

            @SuppressLint({"SetTextI18n", "MissingInflatedId"})
            @Override
            public void onClick(View view) {
                APDatabase db = APDatabase.getInstance(ctx.getApplicationContext());
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                LayoutInflater inflater = LayoutInflater.from(ctx);

                View view1 = inflater.inflate(R.layout.alert_aviso, null);
                builder.setView(view1);

                //DECLARA VARIAVEIS
                Button btnOk, btnCancela;
                TextView msg;
                btnOk = view1.findViewById(R.id.btn_OkAlert2);
                btnCancela = view1.findViewById(R.id.btn_CancelaAlert);
                msg = view1.findViewById(R.id.txtmsg);
                btnCancela.setVisibility(View.VISIBLE);
                msg.setText("Deseja remover a carga de muda " + (position + 1) + " da lista?");

                builder.setView(view1);
                AlertDialog alertDialog = builder.create();
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CargaDeMuda cargaRemovida = localDataSet.remove(position);
                        if (cargaRemovida != null && cargaRemovida.getId() != 0) {
                            db.getCargaDeMudaDAO().excluir(cargaRemovida);
                        }
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, localDataSet.size());
                        alertDialog.dismiss();

                    }
                });
                btnCancela.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
