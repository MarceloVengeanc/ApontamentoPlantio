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
import com.exataid.apontamentoplantio.banco.modelos.TipoPlantio;
import com.exataid.apontamentoplantio.telas.adapters.TipoAdapter;
import com.exataid.apontamentoplantio.telas.adapters.VariedadeAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ListaConsultaTipo extends AppCompatActivity {

    private EditText consultaTipo;
    private TextView txtListaTipo;
    private RecyclerView rvTipo;
    Resources res;
    private int acrescenta;
    private TipoAdapter listaTipoAdapter;
    private List<TipoPlantio> listaTipo, listaTipoFiltrada;
    private List<TipoPlantio> listaTipoVazia = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_consulta_tipo);
        try {
            listaTipo = APDatabase.getInstance(this).getTipoPlantioDAO().buscarTodos().get();

            carregaBotoes();
            setarCampos();
            setarRecyclerListaTipo(false);
            setTipoEscolhido();
        } catch (Exception e) {
            e.printStackTrace();
        }

        consultaTipo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setTipoEscolhido();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setTipoEscolhido() {
        try {
            if (!consultaTipo.getText().toString().equalsIgnoreCase("")) {
                //BUSCA POR NOME
                if (!Character.isDigit(consultaTipo.getText().toString().charAt(0))) {
                    if (listaTipo.stream().anyMatch(item -> item.getNomeTipo().toLowerCase(Locale.ROOT).contains(String.valueOf(consultaTipo.getText())))) {
                        listaTipoFiltrada = listaTipo.stream().filter(itens -> itens.getNomeTipo().toLowerCase(Locale.ROOT).contains(String.valueOf(consultaTipo.getText()))).collect(Collectors.toList());
                        setarRecyclerListaTipo(false);
                    } else {
                        setarRecyclerListaTipo(true);
                    }
                    //BUSCA POR CODIGO
                } else if (Character.isDigit(consultaTipo.getText().toString().charAt(0))) {
                    if (listaTipo.stream().anyMatch(cod -> String.valueOf(cod.getCodTipo()).toLowerCase(Locale.ROOT).contains(consultaTipo.getText()))) {
                        listaTipoFiltrada = listaTipo.stream().filter(u -> String.valueOf(u.getCodTipo()).toLowerCase(Locale.ROOT).contains(consultaTipo.getText())).collect(Collectors.toList());
                        setarRecyclerListaTipo(false);
                    } else {
                        setarRecyclerListaTipo(true);
                    }
                }
            } else {
                listaTipoFiltrada = listaTipo;
                setarRecyclerListaTipo(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setResultTipoEscolhido(TipoPlantio tipo) {
        setResult(Activity.RESULT_OK, this.getIntent().putExtra("resultadoBuscaTipo", tipo));
        this.finish();
    }

    private void setarCampos() {
        res = getResources();
        setarTotalItens();
    }

    private void setarTotalItens() {
        if (listaTipoFiltrada != null) {
            txtListaTipo.setText(res.getString(R.string.lista_tipo, listaTipoFiltrada.size()));
        } else {
            listaTipoFiltrada = new ArrayList<>();
            txtListaTipo.setText(res.getString(R.string.lista_tipo, 0));
        }
    }

    private void setarRecyclerListaTipo(boolean vazio) {
        if (vazio)
            listaTipoAdapter = new TipoAdapter(listaTipoVazia, this::setResultTipoEscolhido);
        if (!vazio)
            listaTipoAdapter = new TipoAdapter(listaTipoFiltrada, this::setResultTipoEscolhido);
        rvTipo.setAdapter(listaTipoAdapter);
        if (acrescenta == 0) {
            rvTipo.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
            acrescenta++;
        }
        rvTipo.setLayoutManager(new LinearLayoutManager(this));
        if (vazio) txtListaTipo.setText(res.getString(R.string.lista_tipo, 0));
        if (!vazio) setarTotalItens();
    }

    public void carregaBotoes() {
        consultaTipo = findViewById(R.id.edit_tipo);
        txtListaTipo = findViewById(R.id.txt_listaTipo);
        rvTipo = findViewById(R.id.rv_tipo);
    }
}