package com.exataid.apontamentoplantio.telas.listas;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.exataid.apontamentoplantio.APDatabase;
import com.exataid.apontamentoplantio.R;
import com.exataid.apontamentoplantio.banco.modelos.Procedencia;
import com.exataid.apontamentoplantio.banco.modelos.Variedade;
import com.exataid.apontamentoplantio.telas.adapters.ProcedenciaAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ListaConsultaProcedencia extends AppCompatActivity {

    private EditText consultaProcedencia;
    private TextView txtListaProcedencia;
    private RecyclerView rvProcedencia;
    Resources res;
    private int acrescenta;
    private ProcedenciaAdapter listaProcedenciaAdapter;
    private List<Procedencia> listaProcedencia, listaProcedenciaFiltrada;
    private List<Procedencia> listaProcedenciaVazia = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_consulta_procedencia);
        try {
            listaProcedencia = APDatabase.getInstance(this).getProcedenciaDAO().buscarTodas().get();

            carregaBotoes();
            setarCampos();
            setarRecyclerListaProcedencia(false);
            setProcedenciaEscolhida();
        } catch (Exception e) {
            e.printStackTrace();
        }

        consultaProcedencia.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setProcedenciaEscolhida();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setProcedenciaEscolhida() {
        try {
            if (!consultaProcedencia.getText().toString().equalsIgnoreCase("")) {
                //BUSCA POR NOME
                if (!Character.isDigit(consultaProcedencia.getText().toString().charAt(0))) {
                    if (listaProcedencia.stream().anyMatch(item -> item.getNomeProcedencia().toLowerCase(Locale.ROOT).contains(String.valueOf(consultaProcedencia.getText())))) {
                        listaProcedenciaFiltrada = listaProcedencia.stream().filter(itens -> itens.getNomeProcedencia().toLowerCase(Locale.ROOT).contains(String.valueOf(consultaProcedencia.getText()))).collect(Collectors.toList());
                        setarRecyclerListaProcedencia(false);
                    } else {
                        setarRecyclerListaProcedencia(true);
                    }
                    //BUSCA POR CODIGO
                } else if (Character.isDigit(consultaProcedencia.getText().toString().charAt(0))) {
                    if (listaProcedencia.stream().anyMatch(cod -> String.valueOf(cod.getCodProcedencia()).toLowerCase(Locale.ROOT).contains(consultaProcedencia.getText()))) {
                        listaProcedenciaFiltrada = listaProcedencia.stream().filter(u -> String.valueOf(u.getCodProcedencia()).toLowerCase(Locale.ROOT).contains(consultaProcedencia.getText())).collect(Collectors.toList());
                        setarRecyclerListaProcedencia(false);
                    } else {
                        setarRecyclerListaProcedencia(true);
                    }
                }
            } else {
                listaProcedenciaFiltrada = listaProcedencia;
                setarRecyclerListaProcedencia(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setResultProcedenciaEscolhida(Procedencia procedencia) {
        setResult(Activity.RESULT_OK, this.getIntent().putExtra("resultadoBuscaProcedencia", procedencia));
        this.finish();
    }

    private void setarCampos() {
        res = getResources();
        setarTotalItens();
    }

    private void setarTotalItens() {
        if (listaProcedenciaFiltrada != null) {
            txtListaProcedencia.setText(res.getString(R.string.lista_procedencia, listaProcedenciaFiltrada.size()));
        } else {
            listaProcedenciaFiltrada = new ArrayList<>();
            txtListaProcedencia.setText(res.getString(R.string.lista_procedencia, 0));
        }
    }

    private void setarRecyclerListaProcedencia(boolean vazio) {
        if (vazio)
            listaProcedenciaAdapter = new ProcedenciaAdapter(listaProcedenciaVazia, this::setResultProcedenciaEscolhida);
        if (!vazio)
            listaProcedenciaAdapter = new ProcedenciaAdapter(listaProcedenciaFiltrada, this::setResultProcedenciaEscolhida);
        rvProcedencia.setAdapter(listaProcedenciaAdapter);
        if (acrescenta == 0) {
            rvProcedencia.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
            acrescenta++;
        }
        rvProcedencia.setLayoutManager(new LinearLayoutManager(this));
        if (vazio) txtListaProcedencia.setText(res.getString(R.string.lista_procedencia, 0));
        if (!vazio) setarTotalItens();
    }

    public void carregaBotoes() {
        consultaProcedencia = findViewById(R.id.edit_procedencia);
        txtListaProcedencia = findViewById(R.id.txt_listaProcedencia);
        rvProcedencia = findViewById(R.id.rv_procedencia);
    }
}