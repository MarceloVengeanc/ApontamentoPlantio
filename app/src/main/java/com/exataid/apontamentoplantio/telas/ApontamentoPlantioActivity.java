package com.exataid.apontamentoplantio.telas;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

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
import com.exataid.apontamentoplantio.banco.modelos.Coletor;
import com.exataid.apontamentoplantio.banco.modelos.Fazendas;
import com.exataid.apontamentoplantio.banco.modelos.Procedencia;
import com.exataid.apontamentoplantio.banco.modelos.SistemaPlantio;
import com.exataid.apontamentoplantio.banco.modelos.Talhao;
import com.exataid.apontamentoplantio.banco.modelos.TipoPlantio;
import com.exataid.apontamentoplantio.banco.modelos.Variedade;
import com.exataid.apontamentoplantio.telas.adapters.CargaDeMudaAdapter;
import com.exataid.apontamentoplantio.telas.listas.ListaConsultaFazenda;
import com.exataid.apontamentoplantio.telas.listas.ListaConsultaProcedencia;
import com.exataid.apontamentoplantio.telas.listas.ListaConsultaSistemaPlantio;
import com.exataid.apontamentoplantio.telas.listas.ListaConsultaTalhoes;
import com.exataid.apontamentoplantio.telas.listas.ListaConsultaTipo;
import com.exataid.apontamentoplantio.telas.listas.ListaConsultaVariedade;
import com.exataid.apontamentoplantio.utils.CliqueRecycler;
import com.exataid.apontamentoplantio.utils.ConfigGerais;
import com.exataid.apontamentoplantio.utils.OnOneOffClickListener;
import com.exataid.apontamentoplantio.utils.U_Data_Hora;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class ApontamentoPlantioActivity extends AppCompatActivity implements CliqueRecycler {
    private Button btnSalvar, btnAdicionarCarga;
    private CheckBox cbConcluido;
    private ImageButton btnData, btnFazenda, btnTalhao, btnSisPlan, btnVariedade, btnTipo, btnAddCarga, btnFazendaD, btnTalhaoD, btnProcedenciaD;
    private TextView txtPlantio;
    private EditText editData, editFazenda, editTalhao, editNomeFazenda, editSisPlan, editVariedade, editTipo, editAreaPlantada;
    private RecyclerView rvPlantio;
    private RadioButton rbLiquidado, rbPorConta;
    private Calendar dataCompleta, dataAtual, dataEscolhida;
    private List<Fazendas> listaTodasFazendas;
    private List<Talhao> listaTalhaoFazendas;
    private List<SistemaPlantio> listaSistemaPlantio;
    private List<Variedade> listaVariedade;
    private List<Procedencia> listaProcedencia;
    private List<TipoPlantio> listaTipo;
    private List<CargaDeMuda> listaCargaDeMuda = new ArrayList<>(), listaTodasCargasDeMuda;
    private Fazendas fazendaEscolhida, fazendaEscolhidaD;
    private Talhao talhaoEscolhido, talhaoEscolhidoD;
    private SistemaPlantio sistemaPlantioEscolhido;
    private Variedade variedadeEscolhida;
    private Procedencia procedenciaEscolhida;
    private TipoPlantio tipoEscolhido;
    private Coletor coletor;
    private CargaDeMuda cargaDeMuda;
    private CargaDeMudaAdapter cargaDeMudaAdapter;
    private ApontamentoPlantio apontamentoPlantioEditar, ultimoApontamentoPlantio;
    private Context ctx;
    private Resources res;
    private String dataSelecionada = "", servicoescolhido = "";
    private int acrescenta = 0, area = 0, contSalvar = 0;
    private boolean trocarMsg = false, editar = false, finalizar = false, possuiPonto = false, editarCargaD = false;
    private Dialog dialog;
    private EditText editFazendaD, editConsultaFazendaD, editProcedenciaD, editConsultaProcedenciaD, editTalhaoD, editCargaD, editPesoD;

    ActivityResultLauncher<Intent> aguardaResultado = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        if (data.hasExtra("resultadoBuscaFazendas")) {
                            setBuscaFazenda((Fazendas) data.getSerializableExtra("resultadoBuscaFazendas"));
                        } else if (data.hasExtra("resultadoBuscaTalhoes")) {
                            setBuscaTalhao((Talhao) data.getSerializableExtra("resultadoBuscaTalhoes"));
                        } else if (data.hasExtra("resultadoBuscaSisPlantio")) {
                            setBuscaSistemaPlantio((SistemaPlantio) data.getSerializableExtra("resultadoBuscaSisPlantio"));
                        } else if (data.hasExtra("resultadoBuscaVariedade")) {
                            setBuscaVariedade((Variedade) data.getSerializableExtra("resultadoBuscaVariedade"));
                        } else if (data.hasExtra("resultadoBuscaProcedencia")) {
                            setBuscaProcedencia((Procedencia) data.getSerializableExtra("resultadoBuscaProcedencia"));
                        } else if (data.hasExtra("resultadoBuscaTipo")) {
                            setBuscaTipo((TipoPlantio) data.getSerializableExtra("resultadoBuscaTipo"));
                        }
                    }
                }
            }
    );

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apontamento_plantio);
        Intent i = getIntent();
        apontamentoPlantioEditar = (ApontamentoPlantio) i.getSerializableExtra("Editar");

        ctx = this;
        carregaBotoes();
        setarData();
        setarRecyclerListaCargaDeMuda();
        setarBotoes();
        carregaBanco();
        carregaEditarPlantio();
        setarListenerEditArea();


        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (listaCargaDeMuda.size() != 0) {
                    finalizar = true;
                    String msg = "";
                    if (editar) {
                        alertAviso("Os registros editados não serão alterados. Deseja continuar?", true, false, true);
                    } else {
                        alertAviso("Os registros inseridos não serão salvos. Deseja continuar?", true, false, true);
                    }
                } else {
                    finish();
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);


    }


    private void salvaApontamento() {
        APDatabase db = APDatabase.getInstance(this.getApplicationContext());
        if (!editar) {
            ApontamentoPlantio apontamentoPlantio = criarApontamentoPlantio();
            if (apontamentoPlantio != null) {
                db.getApontamentoPlantioDAO().inserir(apontamentoPlantio);
            }
            try {
                ultimoApontamentoPlantio = APDatabase.getInstance(this).getApontamentoPlantioDAO().buscarUltimoApontamento().get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            salvarCargaDeMudas(ultimoApontamentoPlantio);
            alertAviso("Apontamento salvo com sucesso!", true, false, false);
            limparCampos();

        } else {
            //ALTERAR MODO EDITAR
            apontamentoPlantioEditar.setDataPlantio(!dataSelecionada.equalsIgnoreCase("") ? dataSelecionada : apontamentoPlantioEditar.getDataPlantio());
            apontamentoPlantioEditar.setConcluido(cbConcluido.isChecked() ? "Sim" : "Não");
            apontamentoPlantioEditar.setCodFazenda(fazendaEscolhida.getCodFazenda());
            apontamentoPlantioEditar.setNomeFazenda(fazendaEscolhida.getNomeFazenda());
            apontamentoPlantioEditar.setCodTalhao(talhaoEscolhido.getCodTalhao());
            apontamentoPlantioEditar.setAreaTalhao(talhaoEscolhido.getArea());
            apontamentoPlantioEditar.setCodSisPlantio(sistemaPlantioEscolhido.getCodSistemaPlantio());
            apontamentoPlantioEditar.setNomeSisPlantio(sistemaPlantioEscolhido.getNomeSistemaPlantio());
            apontamentoPlantioEditar.setCodVariedade(variedadeEscolhida.getCodVariedade());
            apontamentoPlantioEditar.setNomeVariedade(variedadeEscolhida.getNomeVariedade());
            apontamentoPlantioEditar.setCodTipoPlantio(tipoEscolhido.getCodTipo());
            apontamentoPlantioEditar.setNomeTipoPlantio(tipoEscolhido.getNomeTipo());
            apontamentoPlantioEditar.setAreaPlantada(Double.parseDouble(editAreaPlantada.getText().toString()));
            apontamentoPlantioEditar.setLote(rbLiquidado.isChecked() ? "Por Liquidado" : "Por Conta");
            db.getApontamentoPlantioDAO().atualizar(apontamentoPlantioEditar);
            salvarCargaDeMudas(apontamentoPlantioEditar);
        }
    }

    private ApontamentoPlantio criarApontamentoPlantio() {
        ApontamentoPlantio apontamentoPlantio = new ApontamentoPlantio();
        apontamentoPlantio.setEnviado("N");
        apontamentoPlantio.setColetor(coletor.getNumero());
        apontamentoPlantio.setNomeFuncionario(ConfigGerais.UsuarioLogado.getNome());
        apontamentoPlantio.setMatriculaFuncionario(ConfigGerais.UsuarioLogado.getMatricula());
        apontamentoPlantio.setDataPlantio(!dataSelecionada.equalsIgnoreCase("") ? dataSelecionada : U_Data_Hora.retornaData(0, U_Data_Hora.YYYY_MM_DD));
        apontamentoPlantio.setDataPlantioSalvo(U_Data_Hora.retornaData(0, U_Data_Hora.YYYY_MM_DD));
        String resultadoConcluido = (cbConcluido.isChecked()) ? "Sim" : "Não";
        apontamentoPlantio.setConcluido(resultadoConcluido);
        apontamentoPlantio.setCodFazenda(fazendaEscolhida.getCodFazenda());
        apontamentoPlantio.setNomeFazenda(fazendaEscolhida.getNomeFazenda());
        apontamentoPlantio.setCodTalhao(talhaoEscolhido.getCodTalhao());
        apontamentoPlantio.setAreaTalhao(talhaoEscolhido.getArea());
        apontamentoPlantio.setCodSisPlantio(sistemaPlantioEscolhido.getCodSistemaPlantio());
        apontamentoPlantio.setNomeSisPlantio(sistemaPlantioEscolhido.getNomeSistemaPlantio());
        apontamentoPlantio.setCodVariedade(variedadeEscolhida.getCodVariedade());
        apontamentoPlantio.setNomeVariedade(variedadeEscolhida.getNomeVariedade());
        apontamentoPlantio.setCodTipoPlantio(tipoEscolhido.getCodTipo());
        apontamentoPlantio.setNomeTipoPlantio(tipoEscolhido.getNomeTipo());
        apontamentoPlantio.setAreaPlantada(Double.parseDouble(editAreaPlantada.getText().toString()));
        String resultadoLote = (rbLiquidado.isChecked()) ? "Por Liquidado" : "Por Conta";
        apontamentoPlantio.setLote(resultadoLote);
        return apontamentoPlantio;
    }

    private void salvarCargaDeMudas(ApontamentoPlantio ultimoApontamentoPlantio) {
        APDatabase db = APDatabase.getInstance(this.getApplicationContext());
        if (!editar) {
            for (CargaDeMuda cargaDeMuda : listaCargaDeMuda) {
                CargaDeMuda cargaDeMuda1 = criarCargaMuda(ultimoApontamentoPlantio, cargaDeMuda);
                db.getCargaDeMudaDAO().inserir(cargaDeMuda1);
            }
        } else {
            for (CargaDeMuda cargaDeMuda1 : listaCargaDeMuda) {
                if (cargaDeMuda1.getIdApontamento() != ultimoApontamentoPlantio.getId()) {
                    CargaDeMuda novaCargaDeMuda = criarCargaMuda(ultimoApontamentoPlantio, cargaDeMuda1);
                    db.getCargaDeMudaDAO().inserir(novaCargaDeMuda);
                } else {
                    db.getCargaDeMudaDAO().atualizar(cargaDeMuda1);
                }
            }
            finalizar = true;
            alertAviso("Apontamento alterado com sucesso!", true, false, false);
        }
    }

    private CargaDeMuda criarCargaMuda(ApontamentoPlantio ultimoApontamentoPlantio, CargaDeMuda cargaDeMuda) {
        CargaDeMuda cargaDeMuda1 = new CargaDeMuda();
        cargaDeMuda1.setEnviado("N");
        cargaDeMuda1.setMatriculaFuncionario(ConfigGerais.UsuarioLogado.getMatricula());
        cargaDeMuda1.setNomeFuncionario(ConfigGerais.UsuarioLogado.getNome());
        cargaDeMuda1.setColetor(coletor.getNumero());
        cargaDeMuda1.setDataApontamento(ultimoApontamentoPlantio.getDataPlantio());
        cargaDeMuda1.setCodFazenda(cargaDeMuda.getCodFazenda());
        cargaDeMuda1.setNomeFazenda(cargaDeMuda.getNomeFazenda());
        cargaDeMuda1.setCodTalhao(cargaDeMuda.getCodTalhao());
        cargaDeMuda1.setAreaTalhao(cargaDeMuda.getAreaTalhao());
        cargaDeMuda1.setCodProcedencia(cargaDeMuda.getCodProcedencia());
        cargaDeMuda1.setNomeProcedencia(cargaDeMuda.getNomeProcedencia());
        cargaDeMuda1.setCarga(cargaDeMuda.getCarga());
        cargaDeMuda1.setPeso(cargaDeMuda.getPeso());
        cargaDeMuda1.setIdApontamento(ultimoApontamentoPlantio.getId());
        return cargaDeMuda1;
    }

    private void adicionaCargaNaLista() {
        if (!editarCargaD) {
            CargaDeMuda cargaDeMuda = new CargaDeMuda();
            cargaDeMuda.setCodProcedencia(procedenciaEscolhida.getCodProcedencia());
            cargaDeMuda.setNomeProcedencia(procedenciaEscolhida.getNomeProcedencia());
            cargaDeMuda.setCodFazenda(fazendaEscolhidaD.getCodFazenda());
            cargaDeMuda.setNomeFazenda(fazendaEscolhidaD.getNomeFazenda());
            cargaDeMuda.setCodTalhao(talhaoEscolhidoD.getCodTalhao());
            cargaDeMuda.setAreaTalhao(talhaoEscolhidoD.getArea());
            cargaDeMuda.setCarga(Integer.parseInt(editCargaD.getText().toString()));
            cargaDeMuda.setPeso(Double.parseDouble(editPesoD.getText().toString()));
            listaCargaDeMuda.add(cargaDeMuda);
        } else {
            cargaDeMuda.setCodProcedencia(procedenciaEscolhida.getCodProcedencia());
            cargaDeMuda.setNomeProcedencia(procedenciaEscolhida.getNomeProcedencia());
            cargaDeMuda.setCodFazenda(fazendaEscolhidaD.getCodFazenda());
            cargaDeMuda.setNomeFazenda(fazendaEscolhidaD.getNomeFazenda());
            cargaDeMuda.setCodTalhao(talhaoEscolhidoD.getCodTalhao());
            cargaDeMuda.setAreaTalhao(talhaoEscolhidoD.getArea());
            cargaDeMuda.setCarga(Integer.parseInt(editCargaD.getText().toString()));
            cargaDeMuda.setPeso(Double.parseDouble(editPesoD.getText().toString()));
            editarCargaD = false;
        }
        setarRecyclerListaCargaDeMuda();
        dialog.dismiss();
    }

    private void chamaVerificaCampos(String servico) {
        if (servico.equalsIgnoreCase("apontamento")) {
            if (editVazio(editData, "Data")) return;
            if (editVazio(editFazenda, "Fazenda")) return;
            if (editVazio(editTalhao, "Talhão")) return;
            if (editVazio(editAreaPlantada, "Área Plantada")) return;
        } else if (servico.equalsIgnoreCase("carga")) {
            if (editVazio(editProcedenciaD, "Procedência")) return;
            if (editVazio(editFazendaD, "Fazenda")) return;
            if (editVazio(editTalhaoD, "Talhão")) return;
            if (editVazio(editCargaD, "Carga")) return;
            if (editVazio(editPesoD, "Peso")) return;
        }

        String estimativaText = editAreaPlantada.getText().toString();
        if (estimativaText.matches("^0+$")) {
            alertAviso("Campo Área Plantada não pode ser 0.", true, false, false);
            return;
        }
        String cargaText = editCargaD.getText().toString();
        if (cargaText.matches("^0+$")) {
            alertAviso("Campo Carga não pode ser 0.", true, false, false);
            return;
        }
        String pesoText = editPesoD.getText().toString();
        if (pesoText.matches("^0+$")) {
            alertAviso("Campo Peso não pode ser 0.", true, false, false);
            return;
        }
        if (servico.equalsIgnoreCase("apontamento")) {
//            SALVA APONTAMENTO
            salvaApontamento();
        } else if (servico.equalsIgnoreCase("carga")) {
            // ADICIONA NA LISTA DE CARGAS DE MUDA
            adicionaCargaNaLista();
        }

    }

    private boolean editVazio(EditText editText, String fieldName) {
        if (editText.getText().toString().trim().isEmpty()) {
            alertAviso(fieldName, false, false, false);
            editText.requestFocus();
            return true;
        }
        return false;
    }

    private void chamarListaFazenda(String servico) {
        if (listaTodasFazendas.size() != 0) {
            servicoescolhido = servico;
            Intent i = new Intent(ApontamentoPlantioActivity.this, ListaConsultaFazenda.class);
            aguardaResultado.launch(i);
        } else {
            alertAviso("Fazenda(s) não encontrada(s). Tente sincronizar", true, false, false);
        }
    }

    private void chamaListaTalhao(String servico) {
        if (!editFazenda.getText().toString().equalsIgnoreCase("")) {
            chamaTalhaoFazenda();
        } else if (!editFazendaD.getText().toString().equalsIgnoreCase("")) {
            chamaTalhaoFazendaD();
        } else {
            alertAviso("Fazenda", false, false, false);
        }
        servicoescolhido = servico;
    }

    private void chamaTalhaoFazenda() {
        if (listaTalhaoFazendas.stream().anyMatch(u -> u.getCodFazenda() == fazendaEscolhida.getCodFazenda())) {
            Intent i = new Intent(ApontamentoPlantioActivity.this, ListaConsultaTalhoes.class);
            i.putExtra("CodFazenda", fazendaEscolhida.getCodFazenda());
            aguardaResultado.launch(i);
        } else {
            alertAviso("Nenhum talhão encontrado para fazenda selecionada!", true, false, false);
        }
    }

    private void chamaTalhaoFazendaD() {
        if (listaTalhaoFazendas.stream().anyMatch(u -> u.getCodFazenda() == fazendaEscolhidaD.getCodFazenda())) {
            Intent i = new Intent(ApontamentoPlantioActivity.this, ListaConsultaTalhoes.class);
            i.putExtra("CodFazenda", fazendaEscolhidaD.getCodFazenda());
            aguardaResultado.launch(i);
        } else {
            alertAviso("Nenhum talhão encontrado para fazenda selecionada!", true, false, false);
        }
    }

    private void chamarListaProcedencia() {
        if (listaProcedencia.size() != 0) {
            Intent i = new Intent(ApontamentoPlantioActivity.this, ListaConsultaProcedencia.class);
            aguardaResultado.launch(i);
        } else {
            alertAviso("Procedencia(s) não encontrada(s). Tente sincronizar", true, false, false);
        }
    }

    @SuppressLint("SetTextI18n")
    private void chamaDialogPersonalizado(boolean editarCarga) {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_de_baixo_para_cima);

        btnAdicionarCarga = dialog.findViewById(R.id.btnAdicionar);
        btnProcedenciaD = dialog.findViewById(R.id.btnBuscaProcedencia);
        btnFazendaD = dialog.findViewById(R.id.btnBuscaFazenda3);
        btnTalhaoD = dialog.findViewById(R.id.btnBuscaTalhao3);
        editProcedenciaD = dialog.findViewById(R.id.editProcedencia);
        editConsultaProcedenciaD = dialog.findViewById(R.id.editConsultaProcedencia);
        editFazendaD = dialog.findViewById(R.id.editFazenda3);
        editConsultaFazendaD = dialog.findViewById(R.id.editConsultaFazenda3);
        editTalhaoD = dialog.findViewById(R.id.editTalhao3);
        editCargaD = dialog.findViewById(R.id.editCarga);
        editPesoD = dialog.findViewById(R.id.editPeso);

        if (editarCarga) {
            editarCargaD = true;
            procedenciaEscolhida = listaProcedencia.stream().filter(u -> u.getCodProcedencia() == cargaDeMuda.getCodProcedencia()).findFirst().orElse(null);
            fazendaEscolhidaD = listaTodasFazendas.stream().filter(f -> f.getCodFazenda() == cargaDeMuda.getCodFazenda()).findFirst().orElse(null);
            talhaoEscolhidoD = listaTalhaoFazendas.stream().filter(t -> t.getCodTalhao() == cargaDeMuda.getCodTalhao()).findFirst().orElse(null);
            if (procedenciaEscolhida != null) {
                editProcedenciaD.setText("Procedência: " + procedenciaEscolhida.getCodProcedencia());
                editConsultaProcedenciaD.setText(procedenciaEscolhida.getNomeProcedencia());
            }
            if (fazendaEscolhidaD != null) {
                editConsultaFazendaD.setText("Faz: " + fazendaEscolhidaD.getCodFazenda());
                editFazendaD.setText(String.valueOf(cargaDeMuda.getCodFazenda()));
                editConsultaFazendaD.setText(cargaDeMuda.getNomeFazenda());
            }
            if (talhaoEscolhidoD != null) {
                editTalhaoD.setText("Talhão: " + talhaoEscolhidoD.getCodTalhao());
            }

            editCargaD.setText(String.valueOf(cargaDeMuda.getCarga()));
            editPesoD.setText(String.valueOf(cargaDeMuda.getPeso()));
        }
        btnProcedenciaD.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                chamarListaProcedencia();
            }
        });
        btnFazendaD.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                chamarListaFazenda("carga");
            }
        });
        btnTalhaoD.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                chamaListaTalhao("carga");
            }
        });
        editPesoD.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                if (text.contains(".")) {
                    possuiPonto = true;
                } else {
                    possuiPonto = false;
                }

                if (possuiPonto) {

                    int dotIndex = text.indexOf(".");
                    int maxLength = dotIndex + 3;
                    editPesoD.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
                } else {
                    editPesoD.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
                }
            }
        });
        btnAdicionarCarga.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                chamaVerificaCampos("carga");
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void chamaAlertCalendario() {
        //CRIA ALERT
        AlertDialog.Builder builder = new AlertDialog.Builder(ApontamentoPlantioActivity.this);
        View view1 = getLayoutInflater().inflate(R.layout.alert_calendario_dialog, null);

        //DECLARA VARIAVEIS
        Button btnOk;
        DatePicker datePicker;
        btnOk = view1.findViewById(R.id.btn_Okdata);
        datePicker = view1.findViewById(R.id.dtpicker);
        datePicker.setMinDate(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(6));
        builder.setView(view1);
        AlertDialog alertDialog = builder.create();
        btnOk.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                int ano = datePicker.getYear();
                int mes = datePicker.getMonth() + 1;
                int dia = datePicker.getDayOfMonth();

                // Criar um objeto Calendar para a data selecionada
                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.set(ano, (mes - 1), dia);

                // Obter a data atual do sistema
                Calendar currentCalendar = Calendar.getInstance();


                if (selectedCalendar.before(currentCalendar) || selectedCalendar.equals(currentCalendar)) {
                    if (mes < 10 && dia < 10) {
                        editData.setText("0" + dia + "/0" + mes + "/" + ano);
                        dataSelecionada = ano + "-0" + mes + "-0" + dia;
                        alertDialog.dismiss();
                    } else if (mes < 10) {
                        editData.setText(dia + "/0" + mes + "/" + ano);
                        dataSelecionada = ano + "-0" + mes + "-" + dia;
                        alertDialog.dismiss();
                    } else if (dia < 10) {
                        editData.setText("0" + dia + "/" + mes + "/" + ano);
                        dataSelecionada = ano + "-" + mes + "-0" + dia;
                        alertDialog.dismiss();
                    } else {
                        editData.setText(dia + "/" + mes + "/" + ano);
                        dataSelecionada = ano + "-" + mes + "-" + dia;
                        alertDialog.dismiss();
                    }

                } else if (selectedCalendar.after(currentCalendar)) {
                    Toast.makeText(ApontamentoPlantioActivity.this, "A data é posterior à data atual", Toast.LENGTH_SHORT).show();
                }
            }

        });
        alertDialog.show();
    }

    private void setarListenerEditArea() {
        editAreaPlantada.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                if (text.contains(".")) {
                    possuiPonto = true;
                } else {
                    possuiPonto = false;
                }

                if (possuiPonto) {

                    int dotIndex = text.indexOf(".");
                    int maxLength = dotIndex + 3;
                    editAreaPlantada.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
                } else {
                    editAreaPlantada.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
                }
            }
        });
    }

    private void setarBotoes() {
        btnFazenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chamarListaFazenda("apontamento");
            }
        });

        btnTalhao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editFazenda.getText().toString().equalsIgnoreCase("")) {
                    chamaListaTalhao("apontamento");
                } else {
                    alertAviso("Fazenda", false, false, false);
                }
            }
        });
        btnSisPlan.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (listaSistemaPlantio.size() != 0) {
                    Intent i = new Intent(ApontamentoPlantioActivity.this, ListaConsultaSistemaPlantio.class);
                    aguardaResultado.launch(i);
                } else {
                    alertAviso("Sistema(s) de Plantio não encontrado(s). Tente sincronizar", true, false, false);
                }
            }
        });
        btnVariedade.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (listaVariedade.size() != 0) {
                    Intent i = new Intent(ApontamentoPlantioActivity.this, ListaConsultaVariedade.class);
                    aguardaResultado.launch(i);
                } else {
                    alertAviso("Variedade(s) não encontrada(s). Tente sincronizar", true, false, false);
                }
            }
        });
        btnTipo.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (listaTipo.size() != 0) {
                    Intent i = new Intent(ApontamentoPlantioActivity.this, ListaConsultaTipo.class);
                    aguardaResultado.launch(i);
                } else {
                    alertAviso("Tipo(s) de Planto(s) não encontrado(s). Tente Sincronizar", false, false, false);
                }
            }
        });
        btnAddCarga.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                chamaDialogPersonalizado(false);
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (listaCargaDeMuda.size() != 0) {
                    chamaVerificaCampos("apontamento");
                } else {
                    alertAviso("Nenhuma carga de muda adicionada!", true, false, false);
                }
            }
        });
    }

    private void setarRecyclerListaCargaDeMuda() {
        //configurar Adapter
        cargaDeMudaAdapter = new CargaDeMudaAdapter(listaCargaDeMuda, ctx, res);
        cargaDeMudaAdapter.setCliqueRecycler((CliqueRecycler) this);
        //configurar Recyclerview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rvPlantio.setLayoutManager(layoutManager);
        rvPlantio.setHasFixedSize(true);
        rvPlantio.setAdapter(cargaDeMudaAdapter);
        if (acrescenta == 0) {
            rvPlantio.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
            acrescenta++;
        }
    }

    private void setarData() {
        editData.setText(U_Data_Hora.retornaData(0, U_Data_Hora.DDMMYYYY));
        btnData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ApontamentoPlantioActivity.this);
                builder.setTitle("Senha");
                final EditText txtSenha = new EditText(ApontamentoPlantioActivity.this);
                txtSenha.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                txtSenha.requestFocus();
                builder.setView(txtSenha);
                builder.setPositiveButton(("Ok"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (txtSenha.getText().toString().equalsIgnoreCase("17395")) {
                            chamaAlertCalendario();
                        } else if (txtSenha.getText().toString().equalsIgnoreCase("")) {
                            alertAviso("Informe a Senha!", true, false, false);
                        } else {
                            alertAviso("Senha incorreta!", true, false, false);
                        }
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void setBuscaFazenda(Fazendas fazenda) {
        if (fazenda == null) return;
        if (servicoescolhido.equalsIgnoreCase("apontamento")) {
            editNomeFazenda.setText(fazenda.getNomeFazenda());
            editFazenda.setText("Faz: " + fazenda.getCodFazenda());
            editTalhao.setText("");
            fazendaEscolhida = fazenda;
        } else if (servicoescolhido.equalsIgnoreCase("carga")) {
            editFazendaD.setText("Faz: " + fazenda.getCodFazenda());
            editConsultaFazendaD.setText(fazenda.getNomeFazenda());
            editTalhaoD.setText("");
            fazendaEscolhidaD = fazenda;
        }
        servicoescolhido = "";
    }

    @SuppressLint("SetTextI18n")
    private void setBuscaTalhao(Talhao talhao) {
        if (talhao == null) return;
        if (servicoescolhido.equalsIgnoreCase("apontamento")) {
            editTalhao.setText("Talhão: " + talhao.getCodTalhao());
            talhaoEscolhido = talhao;
        } else if (servicoescolhido.equalsIgnoreCase("carga")) {
            editTalhaoD.setText("Talhão: " + talhao.getCodTalhao());
            talhaoEscolhidoD = talhao;
        }
    }

    @SuppressLint("SetTextI18n")
    private void setBuscaSistemaPlantio(SistemaPlantio sistemaPlantio) {
        if (sistemaPlantio == null) return;
        editSisPlan.setText("Sist. Pl: " + sistemaPlantio.getNomeSistemaPlantio());
        sistemaPlantioEscolhido = sistemaPlantio;
    }

    @SuppressLint("SetTextI18n")
    private void setBuscaVariedade(Variedade variedade) {
        if (variedade == null) return;
        editVariedade.setText("Var: " + variedade.getNomeVariedade());
        variedadeEscolhida = variedade;
    }

    @SuppressLint("SetTextI18n")
    private void setBuscaTipo(TipoPlantio tipoPlantio) {
        if (tipoPlantio == null) return;
        editTipo.setText("Tipo: " + tipoPlantio.getNomeTipo());
        tipoEscolhido = tipoPlantio;
    }

    @SuppressLint("SetTextI18n")
    private void setBuscaProcedencia(Procedencia procedencia) {
        if (procedencia == null) return;
        editProcedenciaD.setText("Procedencia: " + procedencia.getCodProcedencia());
        editConsultaProcedenciaD.setText(procedencia.getNomeProcedencia());
        procedenciaEscolhida = procedencia;
    }

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    private void alertAviso(String mensagem, boolean trocarMsg, boolean editarCarga, boolean sair) {
        //CRIA ALERT
        AlertDialog.Builder builder = new AlertDialog.Builder(ApontamentoPlantioActivity.this);
        View view1 = getLayoutInflater().inflate(R.layout.alert_aviso, null);

        //DECLARA VARIAVEIS
        Button btnOk, btnCancela;
        TextView msg;
        btnOk = view1.findViewById(R.id.btn_OkAlert2);
        btnCancela = view1.findViewById(R.id.btn_CancelaAlert);
        msg = view1.findViewById(R.id.txtmsg);
        if (sair) {
            btnCancela.setVisibility(View.VISIBLE);
            btnOk.setText("Sim");
        }
        if (editarCarga) {
            btnCancela.setVisibility(View.VISIBLE);
            btnOk.setText("Editar");
        }
        if (trocarMsg) {
            msg.setText(mensagem);
        } else {
            btnOk.setText("Ok");
            msg.setText("Campo " + mensagem + " Vazio!");
        }

        builder.setView(view1);
        trocarMsg = false;
        AlertDialog alertDialog = builder.create();
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editarCarga) {
                    chamaDialogPersonalizado(true);
                }
                if (finalizar) {
                    finish();
                }
                alertDialog.dismiss();
                btnCancela.setVisibility(View.GONE);
            }
        });
        btnCancela.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                editar = false;
                finalizar = false;
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void limparCampos() {
        editFazenda.setText("");
        editNomeFazenda.setText("");
        editTalhao.setText("");
        editSisPlan.setText("");
        editVariedade.setText("");
        editTipo.setText("");
        editAreaPlantada.setText("");
        cbConcluido.setChecked(false);
        listaCargaDeMuda.clear();
        setarData();
        setarRecyclerListaCargaDeMuda();
    }

    @SuppressLint("SetTextI18n")
    private void carregaEditarPlantio() {
        if (apontamentoPlantioEditar != null) {
            editData.setText(U_Data_Hora.formatarData(apontamentoPlantioEditar.getDataPlantio(), U_Data_Hora.YYYY_MM_DD, U_Data_Hora.DDMMYYYY));
            if (apontamentoPlantioEditar.getConcluido().equalsIgnoreCase("Sim"))
                cbConcluido.setChecked(true);
            editFazenda.setText("Faz: " + apontamentoPlantioEditar.getCodFazenda());
            editNomeFazenda.setText(apontamentoPlantioEditar.getNomeFazenda());
            editTalhao.setText("Talhão: " + apontamentoPlantioEditar.getCodTalhao());
            editSisPlan.setText("Sis. Pl: " + apontamentoPlantioEditar.getNomeSisPlantio());
            editVariedade.setText("Var: " + apontamentoPlantioEditar.getNomeVariedade());
            editTipo.setText("Tipo: " + apontamentoPlantioEditar.getNomeTipoPlantio());
            editAreaPlantada.setText(String.valueOf(apontamentoPlantioEditar.getAreaPlantada()));
            if (apontamentoPlantioEditar.getLote().equalsIgnoreCase("Por Liquidado")) {
                rbLiquidado.setChecked(true);
            } else {
                rbPorConta.setChecked(true);
            }
            try {
                listaTodasCargasDeMuda = APDatabase.getInstance(this).getCargaDeMudaDAO().buscarPorApontamento(apontamentoPlantioEditar.getId()).get();
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            listaCargaDeMuda = listaTodasCargasDeMuda;
            setarRecyclerListaCargaDeMuda();
            editar = true;
            fazendaEscolhida = listaTodasFazendas.stream().filter(u -> u.getCodFazenda() == apontamentoPlantioEditar.getCodFazenda()).findFirst().orElse(null);
            talhaoEscolhido = listaTalhaoFazendas.stream().filter(u -> u.getCodTalhao() == apontamentoPlantioEditar.getCodTalhao()).findFirst().orElse(null);
            sistemaPlantioEscolhido = listaSistemaPlantio.stream().filter(u -> u.getCodSistemaPlantio() == apontamentoPlantioEditar.getCodSisPlantio()).findFirst().orElse(null);
            variedadeEscolhida = listaVariedade.stream().filter(u -> u.getCodVariedade() == apontamentoPlantioEditar.getCodVariedade()).findFirst().orElse(null);
            tipoEscolhido = listaTipo.stream().filter(u -> u.getCodTipo() == apontamentoPlantioEditar.getCodTipoPlantio()).findFirst().orElse(null);

        }
    }

    private void carregaBanco() {
        try {
            listaTodasFazendas = APDatabase.getInstance(this).getFazendasDAO().buscarTodas().get();
            listaTalhaoFazendas = APDatabase.getInstance(this).getTalhaoDAO().buscarTodos().get();
            listaSistemaPlantio = APDatabase.getInstance(this).getSistemaPlantioDAO().buscarTodos().get();
            listaVariedade = APDatabase.getInstance(this).getVariedadeDAO().buscarTodos().get();
            listaTipo = APDatabase.getInstance(this).getTipoPlantioDAO().buscarTodos().get();
            listaProcedencia = APDatabase.getInstance(this).getProcedenciaDAO().buscarTodas().get();
            coletor = APDatabase.getInstance(this).getColetorDAO().buscarAtivo().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("WrongViewCast")
    private void carregaBotoes() {
        cbConcluido = findViewById(R.id.cbConcluido);
        btnSalvar = findViewById(R.id.btn_Salvar);
        btnData = findViewById(R.id.btnBuscaData);
        btnFazenda = findViewById(R.id.btnBuscaFazenda);
        btnTalhao = findViewById(R.id.btnBuscaTalhao);
        btnSisPlan = findViewById(R.id.btnBuscaSisPlan);
        btnVariedade = findViewById(R.id.btnBuscaVariedade);
        btnTipo = findViewById(R.id.btnBuscaTipo);
        btnAddCarga = findViewById(R.id.btnAddCarga);
        editData = findViewById(R.id.editData);
        editFazenda = findViewById(R.id.editFazenda);
        editTalhao = findViewById(R.id.editTalhao);
        editSisPlan = findViewById(R.id.editSisPlan);
        editVariedade = findViewById(R.id.editVariedade);
        editTipo = findViewById(R.id.editTipo);
        editAreaPlantada = findViewById(R.id.editAreaPlantada);
        editNomeFazenda = findViewById(R.id.editConsultaFazenda);
        rvPlantio = findViewById(R.id.rvPlantio);
        txtPlantio = findViewById(R.id.txtCargaMudas);
        rbLiquidado = findViewById(R.id.rbLiquidado);
        rbPorConta = findViewById(R.id.rbPorConta);
        res = getResources();
        rbLiquidado.setChecked(true);
    }

    @Override
    public void onItemClick(int position) {
        cargaDeMuda = listaCargaDeMuda.get(position);
        alertAviso("Deseja editar a carga de muda selecionada?", true, true, false);
    }
}