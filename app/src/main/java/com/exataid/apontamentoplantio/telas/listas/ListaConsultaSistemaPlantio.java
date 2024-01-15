package com.exataid.apontamentoplantio.telas.listas;

import android.app.Activity;
import android.content.Intent;
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
import com.exataid.apontamentoplantio.banco.modelos.SistemaPlantio;
import com.exataid.apontamentoplantio.telas.adapters.SistemaPlantioAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ListaConsultaSistemaPlantio extends AppCompatActivity {

    private EditText consultaSistemaPlantio;
    private TextView txtListaSistemaPlantio;
    private RecyclerView rvSistemaPlantio;
    Resources res;
    private int acrescenta, codSistemaPlantio;
    private SistemaPlantioAdapter listaSistemaPlantioAdapter;
    private List<SistemaPlantio> listaSistemaPlantio, listaSistemaPlantioFiltrada;
    private List<SistemaPlantio> listaSistemaPlantioVazia = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_consulta_sistema_plantio);
        try {
            listaSistemaPlantio = APDatabase.getInstance(this).getSistemaPlantioDAO().buscarTodos().get();

            carregaBotoes();
            setarCampos();
            setarRecyclerListaSisPlantio(false);
            setSistemaPlantioEscolhido();
        } catch (Exception e) {
            e.printStackTrace();
        }

        consultaSistemaPlantio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setSistemaPlantioEscolhido();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setSistemaPlantioEscolhido() {
        try {
            if (!consultaSistemaPlantio.getText().toString().equalsIgnoreCase("")) {
                //BUSCA POR NOME
                if (!Character.isDigit(consultaSistemaPlantio.getText().toString().charAt(0))) {
                    if (listaSistemaPlantio.stream().anyMatch(item -> item.getNomeSistemaPlantio().toLowerCase(Locale.ROOT).contains(String.valueOf(consultaSistemaPlantio.getText())))) {
                        listaSistemaPlantioFiltrada = listaSistemaPlantio.stream().filter(itens -> itens.getNomeSistemaPlantio().toLowerCase(Locale.ROOT).contains(String.valueOf(consultaSistemaPlantio.getText()))).collect(Collectors.toList());
                        setarRecyclerListaSisPlantio(false);
                    } else {
                        setarRecyclerListaSisPlantio(true);
                    }
                    //BUSCA POR CODIGO
                } else if (Character.isDigit(consultaSistemaPlantio.getText().toString().charAt(0))) {
                    if (listaSistemaPlantio.stream().anyMatch(cod -> String.valueOf(cod.getCodSistemaPlantio()).toLowerCase(Locale.ROOT).contains(consultaSistemaPlantio.getText()))) {
                        listaSistemaPlantioFiltrada = listaSistemaPlantio.stream().filter(u -> String.valueOf(u.getCodSistemaPlantio()).toLowerCase(Locale.ROOT).contains(consultaSistemaPlantio.getText())).collect(Collectors.toList());
                        setarRecyclerListaSisPlantio(false);
                    } else {
                        setarRecyclerListaSisPlantio(true);
                    }
                }
            } else {
                listaSistemaPlantioFiltrada = listaSistemaPlantio;
                setarRecyclerListaSisPlantio(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setResultSistemaPlantioEscolhido(SistemaPlantio sistemaPlantio) {
        setResult(Activity.RESULT_OK, this.getIntent().putExtra("resultadoBuscaSisPlantio", sistemaPlantio));
        this.finish();
    }

    private void setarCampos() {
        res = getResources();
        setarTotalItens();
    }

    private void setarTotalItens() {
        if (listaSistemaPlantioFiltrada != null) {
            txtListaSistemaPlantio.setText(res.getString(R.string.lista_sis_plantio, listaSistemaPlantioFiltrada.size()));
        } else {
            listaSistemaPlantioFiltrada = new ArrayList<>();
            txtListaSistemaPlantio.setText(res.getString(R.string.lista_sis_plantio, 0));
        }
    }

    private void setarRecyclerListaSisPlantio(boolean vazio) {
        if (vazio)
            listaSistemaPlantioAdapter = new SistemaPlantioAdapter(listaSistemaPlantioVazia, this::setResultSistemaPlantioEscolhido);
        if (!vazio)
            listaSistemaPlantioAdapter = new SistemaPlantioAdapter(listaSistemaPlantioFiltrada, this::setResultSistemaPlantioEscolhido);
        rvSistemaPlantio.setAdapter(listaSistemaPlantioAdapter);
        if (acrescenta == 0) {
            rvSistemaPlantio.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
            acrescenta++;
        }
        rvSistemaPlantio.setLayoutManager(new LinearLayoutManager(this));
        if (vazio) txtListaSistemaPlantio.setText(res.getString(R.string.lista_sis_plantio, 0));
        if (!vazio) setarTotalItens();
    }

    public void carregaBotoes() {
        consultaSistemaPlantio = findViewById(R.id.edit_sisPlan);
        txtListaSistemaPlantio = findViewById(R.id.txt_sisPlan);
        rvSistemaPlantio = findViewById(R.id.rv_sisPlantio);
    }
}