package com.exataid.apontamentoplantio.telas;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.exataid.apontamentoplantio.APDatabase;
import com.exataid.apontamentoplantio.R;
import com.exataid.apontamentoplantio.banco.modelos.ApontamentoPlantio;
import com.exataid.apontamentoplantio.banco.modelos.CargaDeMuda;
import com.exataid.apontamentoplantio.banco.modelos.Fazendas;
import com.exataid.apontamentoplantio.telas.adapters.ConsultaAdapter;
import com.exataid.apontamentoplantio.telas.listas.ListaConsultaFazenda;
import com.exataid.apontamentoplantio.utils.CliqueRecycler;
import com.exataid.apontamentoplantio.utils.ConfigGerais;
import com.exataid.apontamentoplantio.utils.OnOneOffClickListener;
import com.exataid.apontamentoplantio.utils.U_Data_Hora;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ConsultaActivity extends AppCompatActivity implements CliqueRecycler {

    private TextView txtListaConsulta;
    private ImageView btnDataInicial, btnDataFinal, btnConsultaFazenda;
    private Button btnFiltrar;
    private EditText editDataInicial, editDataFInal, editConsultaFazenda;
    private CheckBox cbNaoEnviado;
    Resources res;
    private List<ApontamentoPlantio> listaApontamentoPlantioFiltrada;
    private List<Fazendas> listaFazenda;
    private List<ApontamentoPlantio> listaApontamentoPlantioSalvos;
    private List<ApontamentoPlantio> listaApontamentoPlantioVazia = new ArrayList<>();
    private List<CargaDeMuda> listaExcluirCarga;
    private RecyclerView rvConsulta;
    private ConsultaAdapter consultaAdapter;
    private Fazendas codFazenda;
    private ApontamentoPlantio apontamentoPlantio;
    private String dataEscolhida = "";
    private int acrescenta = 0;
    private boolean datafinal = false, dataInicial = false, editar = false, btnFazendaPressionado = false;
    ActivityResultLauncher<Intent> aguardaResultado = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        if (data.hasExtra("resultadoBuscaFazendas")) {
                            setBuscaFazenda((Fazendas) data.getSerializableExtra("resultadoBuscaFazendas"));
                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);

        carregaBotoes();
        carregaListas();
        carregarTela();
        onCliqueFiltraData();
        cliqueFiltrar();
        setEditConsultaFazenda();
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void chamaCalendario() {
        ViewGroup viewGroup = findViewById(android.R.id.content);

        AlertDialog.Builder builder = new AlertDialog.Builder(ConsultaActivity.this);
        View view = LayoutInflater.from(ConsultaActivity.this).inflate(R.layout.alert_calendario_dialog, viewGroup, false);
        Button btnOk = view.findViewById(R.id.btn_Okdata);
        DatePicker dataPicker = view.findViewById(R.id.dtpicker);
        dataPicker.setMinDate(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(6));
        builder

                .setView(view);
        AlertDialog alertDialog = builder.create();

        btnOk.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                String mes1 = "";
                int dia = dataPicker.getDayOfMonth();
                int mes = dataPicker.getMonth() + 1;
                int ano = dataPicker.getYear();

                Calendar dataCompleta = Calendar.getInstance();
                dataCompleta.set(ano, mes, dia);

                if (mes < 10 && dia < 10) {
                    dataEscolhida = "0" + dia + "/0" + mes + "/" + ano;
                } else if (mes < 10) {
                    dataEscolhida = dia + "/0" + mes + "/" + ano;
                } else if (dia < 10) {
                    dataEscolhida = "0" + dia + "/" + mes + "/" + ano;
                } else {
                    dataEscolhida = dia + "/" + mes + "/" + ano;
                }
                Log.i(TAG, dataEscolhida);
                Log.i(TAG, "Cria Intent");
                if (dataInicial) {
                    editDataInicial.setText(dataEscolhida);
                    dataInicial = false;
                } else if (datafinal) {
                    editDataFInal.setText(dataEscolhida);
                    datafinal = false;
                }
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void chamarCondicoesFiltrar() {
        Date dataInicial = U_Data_Hora.formatarData(String.valueOf(editDataInicial.getText()), U_Data_Hora.DDMMYYYY);
        Date dataFinal = U_Data_Hora.formatarData(String.valueOf(editDataFInal.getText()), U_Data_Hora.DDMMYYYY);

        filtrarApontamentosPorData(dataInicial, dataFinal);

        filtrarPorFazenda();
        filtrarPorEnviado();
    }

    private boolean dataInconsistente() {
        return U_Data_Hora.diferenciarTempo(editDataInicial.getText().toString(), U_Data_Hora.DDMMYYYY,
                editDataFInal.getText().toString(), U_Data_Hora.DDMMYYYY, U_Data_Hora.DD) < 0;
    }

    public void cliqueFiltrar() {
        btnFiltrar.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (dataInconsistente()) {
                    alertAviso("Data inicial à frente da Data final.", false);
                } else {
                    chamarCondicoesFiltrar();
                }
            }
        });
    }

    private void filtrarApontamentosPorData(Date dataInicial, Date dataFinal) {
        if (dataInicial != null && !editDataInicial.getText().toString().isEmpty()
                && dataFinal != null && !editDataFInal.getText().toString().isEmpty()) {
            try {
                String carregaDataInicial = U_Data_Hora.formatarData(
                        U_Data_Hora.formatarData(
                                String.valueOf(editDataInicial.getText()),
                                U_Data_Hora.DDMMYYYY),
                        U_Data_Hora.YYYY_MM_DD);
                String carregaDataFinal = U_Data_Hora.formatarData(
                        U_Data_Hora.formatarData(
                                String.valueOf(editDataFInal.getText()),
                                U_Data_Hora.DDMMYYYY),
                        U_Data_Hora.YYYY_MM_DD);
                listaApontamentoPlantioFiltrada = APDatabase
                        .getInstance(this)
                        .getApontamentoPlantioDAO()
                        .buscarApontamentosPorData(ConfigGerais.UsuarioLogado.getNome(),
                                carregaDataInicial
                                , carregaDataFinal
                        ).get();
                listaApontamentoPlantioSalvos = listaApontamentoPlantioFiltrada;
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void filtrarPorFazenda() {
        if (!editConsultaFazenda.getText().toString().equalsIgnoreCase("")) {
            if (listaApontamentoPlantioSalvos.stream().anyMatch(u -> u.getNomeFazenda()
                    .equalsIgnoreCase(editConsultaFazenda.getText().toString()))) {
                ApontamentoPlantio rf = listaApontamentoPlantioSalvos
                        .stream()
                        .filter(ApontamentoFazenda -> ApontamentoFazenda.getNomeFazenda()
                                .equalsIgnoreCase(editConsultaFazenda.getText().
                                        toString())).findFirst().orElse(null);
                setRecyclerFiltroConsulta(false);

                listaApontamentoPlantioFiltrada = listaApontamentoPlantioSalvos.stream()
                        .filter(te -> te.getNomeFazenda().equalsIgnoreCase(editConsultaFazenda.getText().toString())).collect(Collectors.toList());

                if (rf != null) {
                    editConsultaFazenda.setText(String.valueOf(rf.getNomeFazenda()));
                    setRecyclerFiltroConsulta(false);
                } else {
                    editConsultaFazenda.setText("");
                    setRecyclerFiltroConsulta(false);
                }
            } else {
                editConsultaFazenda.setText("");
                listaApontamentoPlantioSalvos.clear();
                setRecyclerFiltroConsulta(false);
            }
        } else {
            setRecyclerFiltroConsulta(false);
        }
        listaApontamentoPlantioSalvos = listaApontamentoPlantioFiltrada;
    }

    private void filtrarPorEnviado() {
        if (cbNaoEnviado.isChecked()) {
            if (listaApontamentoPlantioSalvos.stream().anyMatch(u -> u.getEnviado().
                    equalsIgnoreCase("N"))) {
                listaApontamentoPlantioFiltrada = listaApontamentoPlantioSalvos.stream()
                        .filter(te -> te.getEnviado().equalsIgnoreCase("N")).
                        collect(Collectors.toList());
            } else {
                listaApontamentoPlantioFiltrada.clear();
            }
        }
        setRecyclerFiltroConsulta(false);
    }

    private void onCliqueFiltraData() {
        cliqueFiltrarDataInicial();
        cliqueFiltrarDataFinal();
        cliqueBuscaFazenda();
    }

    @SuppressLint("MissingInflatedId")
    private void cliqueFiltrarDataInicial() {
        btnDataInicial.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                chamaCalendario();
                dataInicial = true;
            }
        });

    }

    private void cliqueFiltrarDataFinal() {
        btnDataFinal.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                chamaCalendario();
                datafinal = true;
            }
        });

    }

    private void cliqueBuscaFazenda() {
        btnConsultaFazenda.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (listaFazenda.size() != 0) {
                    btnFazendaPressionado = true;
                    Intent i = new Intent(ConsultaActivity.this, ListaConsultaFazenda.class);
                    aguardaResultado.launch(i);
                } else {
                    alertAviso("Fazenda(s) Não Encontrada(s). Tente Sincronizar.", false);
                }
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        apontamentoPlantio = listaApontamentoPlantioFiltrada.get(position);
        if (apontamentoPlantio.getEnviado().equalsIgnoreCase("N")) {
            alertAviso("Deseja editar o apontamento do dia: " + U_Data_Hora.formatarData(
                    apontamentoPlantio.getDataPlantio(), U_Data_Hora.YYYY_MM_DD, U_Data_Hora.DDMMYYYY) +
                    "?", true);
        } else {
            alertAviso("Apontamento já enviado. Não é possível editar.", false);

        }
    }

    private void setEditConsultaFazenda() {

        editConsultaFazenda.addTextChangedListener(new TextWatcher() {
            final Handler handler = new Handler();
            Runnable runnable;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                handler.removeCallbacks(runnable);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                handler.postDelayed(() -> {
                    if (!editConsultaFazenda.getText().toString().equalsIgnoreCase("")) {
                        codFazenda = listaFazenda.stream().filter(u -> String.valueOf(u.getCodFazenda()).equalsIgnoreCase(editConsultaFazenda.getText().toString())).findFirst().orElse(null);
                    }
                    if (codFazenda != null) {
                        editConsultaFazenda.setText(codFazenda.getNomeFazenda());
                    }
                }, 2500);
            }
        });
    }

    private void setBuscaFazenda(Fazendas fazenda) {
        if (fazenda == null) return;
        editConsultaFazenda.setText(fazenda.getNomeFazenda());
    }

    private void setRecyclerFiltroConsulta(boolean vazio) {
        if (vazio)
            consultaAdapter = new ConsultaAdapter(listaApontamentoPlantioVazia, this, res);
        if (!vazio)
            consultaAdapter = new ConsultaAdapter(listaApontamentoPlantioFiltrada, this, res);
        consultaAdapter.setCliqueRecycler((CliqueRecycler) this);
        rvConsulta.setAdapter(consultaAdapter);
        if (acrescenta == 0) {
            rvConsulta.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
            acrescenta++;
        }
        rvConsulta.setLayoutManager(new LinearLayoutManager(this));
        if (vazio) txtListaConsulta.setText(res.getString(R.string.lista_apontamentos, 0));
        if (!vazio) setarTotalRequisicoes();
    }

    private void setarTotalRequisicoes() {
        if (listaApontamentoPlantioFiltrada != null) {
            txtListaConsulta.setText(res.getString(R.string.lista_apontamentos, listaApontamentoPlantioFiltrada.size()));
        } else {
            listaApontamentoPlantioFiltrada = new ArrayList<>();
            txtListaConsulta.setText(res.getString(R.string.lista_apontamentos, 0));
        }
    }

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    private void alertAviso(String mensagem, boolean trocarMsg) {
        //CRIA ALERT
        AlertDialog.Builder builder = new AlertDialog.Builder(ConsultaActivity.this);
        View view1 = getLayoutInflater().inflate(R.layout.alert_aviso, null);

        //DECLARA VARIAVEIS
        Button btnOk, btnCancela, btnExcluir;
        TextView msg;
        btnOk = view1.findViewById(R.id.btn_OkAlert2);
        btnCancela = view1.findViewById(R.id.btn_CancelaAlert);
        btnExcluir = view1.findViewById(R.id.btn_excluir);
        msg = view1.findViewById(R.id.txtmsg);
        if (trocarMsg) {
            btnCancela.setVisibility(View.VISIBLE);
            btnExcluir.setVisibility(View.VISIBLE);
            btnOk.setText("Editar");
            editar = true;
            msg.setText(mensagem);
        } else {
            btnOk.setText("Ok");
            msg.setText(mensagem);
        }

        builder.setView(view1);
        trocarMsg = false;
        AlertDialog alertDialog = builder.create();
        btnOk.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (editar) {
                    editar = false;
                    Intent i = new Intent(ConsultaActivity.this, ApontamentoPlantioActivity.class);
                    i.putExtra("Editar", apontamentoPlantio);
                    startActivity(i);
                }
                alertDialog.dismiss();
                btnCancela.setVisibility(View.GONE);
//                btnExcluir.setVisibility(View.GONE);
            }
        });
        btnExcluir.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                chamaAlertPergunta();
                alertDialog.dismiss();
            }
        });
        btnCancela.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                editar = false;
                alertDialog.dismiss();
                btnCancela.setVisibility(View.GONE);
//                btnExcluir.setVisibility(View.GONE);
            }
        });
        alertDialog.show();
    }

    @SuppressLint({"SetTextI18n", "MissingInflatedId"})
    private void chamaAlertPergunta() {
        APDatabase db = APDatabase.getInstance(this.getApplicationContext());
        try {
            listaExcluirCarga = APDatabase.getInstance(this).getCargaDeMudaDAO().buscarPorApontamento(apontamentoPlantio.getId()).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        //CRIA ALERT
        AlertDialog.Builder builder = new AlertDialog.Builder(ConsultaActivity.this);
        View view1 = getLayoutInflater().inflate(R.layout.alert_pergunta, null);

        //DECLARA VARIAVEIS
        Button btnOk, btnCancela, btnExcluir;
        TextView msg;
        btnOk = view1.findViewById(R.id.btn_OkAlert2);
        btnCancela = view1.findViewById(R.id.btn_CancelaAlert);
        msg = view1.findViewById(R.id.txtmsg);
        msg.setText("O Apontamento será excluído. Deseja continuar?");
        btnOk.setText("Sim");
        builder.setView(view1);
        AlertDialog alertDialog = builder.create();
        btnOk.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                db.getApontamentoPlantioDAO().excluir(apontamentoPlantio);
                db.getCargaDeMudaDAO().excluir(listaExcluirCarga);
                editar = false;
                alertAviso("Apontamento excluído com sucesso!", false);
                chamarCondicoesFiltrar();
                alertDialog.dismiss();
            }
        });

        btnCancela.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!btnFazendaPressionado) chamarCondicoesFiltrar();
        setarTotalRequisicoes();
        btnFazendaPressionado = false;
    }

    private void carregarTela() {
        editDataInicial.setText(U_Data_Hora.retornaData(0, U_Data_Hora.DDMMYYYY));
        editDataFInal.setText(U_Data_Hora.retornaData(0, U_Data_Hora.DDMMYYYY));
        setRecyclerFiltroConsulta(false);

        try {
            //CONFIGURAR PARA BUSCAR APONTAMENTOS SOMENTE DO USUARIO LOGADO E DATA
            listaApontamentoPlantioFiltrada = APDatabase
                    .getInstance(this)
                    .getApontamentoPlantioDAO()
                    .buscarApontamentosPorData(ConfigGerais.UsuarioLogado.getNome(), U_Data_Hora
                                    .formatarData(U_Data_Hora
                                            .formatarData(String.valueOf(editDataInicial.getText()), U_Data_Hora.DDMMYYYY), U_Data_Hora.YYYY_MM_DD)
                            , U_Data_Hora
                                    .formatarData(U_Data_Hora
                                                    .formatarData(String.valueOf(editDataFInal.getText()), U_Data_Hora.DDMMYYYY),
                                            U_Data_Hora.YYYY_MM_DD)).get();
            setRecyclerFiltroConsulta(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void carregaListas() {
        try {
            listaFazenda = APDatabase.getInstance(this).getFazendasDAO().buscarTodas().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void carregaBotoes() {
        txtListaConsulta = findViewById(R.id.txtListaApontamentos);
        btnDataInicial = findViewById(R.id.btn_consulta_data_inicial);
        btnDataFinal = findViewById(R.id.btn_consulta_data_final);
        btnFiltrar = findViewById(R.id.btn_consulta_filtrar);
        editDataInicial = findViewById(R.id.edit_consulta_data_inicial);
        editDataFInal = findViewById(R.id.edit_consulta_data_final);
        rvConsulta = findViewById(R.id.rv_apontamentos_consulta);
        btnConsultaFazenda = findViewById(R.id.btnPesquisaFazenda);
        editConsultaFazenda = findViewById(R.id.editConsultaFazenda);
        cbNaoEnviado = findViewById(R.id.cbNaoEnviado);
        res = getResources();
    }

}