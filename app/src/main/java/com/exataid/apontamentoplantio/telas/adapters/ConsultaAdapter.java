package com.exataid.apontamentoplantio.telas.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.exataid.apontamentoplantio.APDatabase;
import com.exataid.apontamentoplantio.R;
import com.exataid.apontamentoplantio.banco.modelos.ApontamentoPlantio;
import com.exataid.apontamentoplantio.banco.modelos.CargaDeMuda;
import com.exataid.apontamentoplantio.utils.CliqueRecycler;
import com.exataid.apontamentoplantio.utils.OnOneOffClickListener;
import com.exataid.apontamentoplantio.utils.U_Data_Hora;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ConsultaAdapter extends RecyclerView.Adapter<ConsultaAdapter.ViewHolder> {

    private List<ApontamentoPlantio> listaApontamentoPlantio;
    private List<CargaDeMuda> listaCargaDeMudas;
    private Context ctx;
    private Resources res;
    private CliqueRecycler cliqueRecycler;

    public void setCliqueRecycler(CliqueRecycler cliqueRecycler) {
        this.cliqueRecycler = cliqueRecycler;
    }

    public ConsultaAdapter(List<ApontamentoPlantio> listaApontamentoPlantio, Context ctx, Resources res) {
        this.listaApontamentoPlantio = listaApontamentoPlantio;
        this.ctx = ctx;
        this.res = res;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtData, txtCodFazenda, txtNomeFazenda, txtTalhao, txtConcluido, txtSisPlan, txtTipoPlantio, txtVariedade, txtEnviado, txtArea, txtLote;
        Button btnConsultaCarga;
        private ConsultaCargaDeMudaAdapter consultaCargaDeMudaAdapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtData = itemView.findViewById(R.id.txtDataRegistro);
            txtCodFazenda = itemView.findViewById(R.id.txtCodFaz);
            txtNomeFazenda = itemView.findViewById(R.id.txtNomeFazenda);
            txtTalhao = itemView.findViewById(R.id.txtCodTalhao);
            txtSisPlan = itemView.findViewById(R.id.txtSisPlan);
            txtVariedade = itemView.findViewById(R.id.txtVariedade);
            txtArea = itemView.findViewById(R.id.txtArea);
            txtConcluido = itemView.findViewById(R.id.txtConcluido);
            txtEnviado = itemView.findViewById(R.id.txtEnviado);
            txtLote = itemView.findViewById(R.id.txtLote);
            txtTipoPlantio = itemView.findViewById(R.id.txtTipoPlantio);
            btnConsultaCarga = itemView.findViewById(R.id.btnConsultaCarga);

        }

        public TextView getTxtData() {
            return txtData;
        }

        public TextView getTxtCodFazenda() {
            return txtCodFazenda;
        }

        public TextView getTxtNomeFazenda() {
            return txtNomeFazenda;
        }

        public TextView getTxtTalhao() {
            return txtTalhao;
        }

        public TextView getTxtConcluido() {
            return txtConcluido;
        }

        public TextView getTxtSisPlan() {
            return txtSisPlan;
        }

        public TextView getTxtTipoPlantio() {
            return txtTipoPlantio;
        }

        public TextView getTxtVariedade() {
            return txtVariedade;
        }

        public TextView getTxtEnviado() {
            return txtEnviado;
        }

        public TextView getTxtArea() {
            return txtArea;
        }

        public TextView getTxtLote() {
            return txtLote;
        }

        public Button getBtnConsultaCarga() {
            return btnConsultaCarga;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_lista_consulta, parent, false
        );
        return new ViewHolder(v);
    }

    @SuppressLint("StringFormatMatches")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if (position % 2 != 0) {
            holder.itemView.setBackgroundColor(ctx.getColor(R.color.temaCinza));
        } else {
            holder.itemView.setBackgroundColor(ctx.getColor(R.color.temaBranco));
        }

        holder.getTxtData().setText(res.getString(R.string.plantio, U_Data_Hora.formatarData(listaApontamentoPlantio.get(position).getDataPlantio(), U_Data_Hora.YYYY_MM_DD, U_Data_Hora.DDMMYYYY)));
        holder.getTxtCodFazenda().setText(res.getString(R.string.plantio, listaApontamentoPlantio.get(position).getCodFazenda() + "- " + listaApontamentoPlantio.get(position).getNomeFazenda()));
        holder.getTxtTalhao().setText(res.getString(R.string.plantio, listaApontamentoPlantio.get(position).getCodTalhao()));
        holder.getTxtConcluido().setText(res.getString(R.string.plantio, listaApontamentoPlantio.get(position).getConcluido()));
        holder.getTxtSisPlan().setText(res.getString(R.string.plantio, listaApontamentoPlantio.get(position).getNomeSisPlantio()));
        holder.getTxtTipoPlantio().setText(res.getString(R.string.plantio, listaApontamentoPlantio.get(position).getNomeTipoPlantio()));
        holder.getTxtVariedade().setText(res.getString(R.string.plantio, listaApontamentoPlantio.get(position).getCodVariedade() + "- " + listaApontamentoPlantio.get(position).getNomeVariedade()));
        holder.getTxtLote().setText(res.getString(R.string.plantio, listaApontamentoPlantio.get(position).getLote()));
        @SuppressLint("DefaultLocale") String area = String.format("%.2f", listaApontamentoPlantio.get(position).getAreaPlantada()).replace(",",".");
        holder.getTxtArea().setText(res.getString(R.string.plantio, area));
        holder.getTxtEnviado().setText(res.getString(R.string.plantio, listaApontamentoPlantio.get(position).getEnviado()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cliqueRecycler != null) {
                    cliqueRecycler.onItemClick(position);
                }
            }
        });
        holder.getBtnConsultaCarga().setOnClickListener(new OnOneOffClickListener() {
            private int acrescenta = 0;

            @Override
            public void onSingleClick(View v) {
                ApontamentoPlantio apontamentoPlantio = listaApontamentoPlantio.get(position);
                try {
                    listaCargaDeMudas = APDatabase.getInstance(ctx).getCargaDeMudaDAO().buscarPorApontamento(apontamentoPlantio.getId()).get();
                    if (listaCargaDeMudas != null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

                        LayoutInflater inflater = LayoutInflater.from(ctx);
                        View view1 = inflater.inflate(R.layout.alert_consulta_de_cargas, null);
                        RecyclerView rvConsultaCarga;
                        Button btnFechar;
                        rvConsultaCarga = view1.findViewById(R.id.rvCargasDeMudas);
                        btnFechar = view1.findViewById(R.id.btnFechar);

                        builder.setView(view1);
                        AlertDialog alertDialog = builder.create();

//                        //CONFIGURA RECYCLER
                        holder.consultaCargaDeMudaAdapter = new ConsultaCargaDeMudaAdapter(listaCargaDeMudas, ctx, res);
                        //configurarRecycler
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ctx.getApplicationContext());
                        rvConsultaCarga.setLayoutManager(layoutManager);
                        if (acrescenta == 0) {
                            rvConsultaCarga.addItemDecoration(new DividerItemDecoration(ctx.getApplicationContext(), LinearLayout.VERTICAL));
                            acrescenta++;
                        }
                        rvConsultaCarga.setAdapter(holder.consultaCargaDeMudaAdapter);

                        btnFechar.setOnClickListener(new OnOneOffClickListener() {
                            @Override
                            public void onSingleClick(View v) {
                                alertDialog.dismiss();
                                acrescenta = 0;
                            }
                        });
                        alertDialog.show();

                    }
                } catch (ExecutionException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return listaApontamentoPlantio.size();
    }
}
