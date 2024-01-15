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
import com.exataid.apontamentoplantio.banco.modelos.Variedade;
import com.exataid.apontamentoplantio.telas.adapters.SistemaPlantioAdapter;
import com.exataid.apontamentoplantio.telas.adapters.VariedadeAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ListaConsultaVariedade extends AppCompatActivity {

    private EditText consultaVariedade;
    private TextView txtListaVariedade;
    private RecyclerView rvVariedade;
    Resources res;
    private int acrescenta;
    private VariedadeAdapter listaVariedadeAdapter;
    private List<Variedade> listaVariedade, listaVariedadeFiltrada;
    private List<Variedade> listaVariedadeVazia = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_consulta_variedade);
        try {
            listaVariedade = APDatabase.getInstance(this).getVariedadeDAO().buscarTodos().get();

            carregaBotoes();
            setarCampos();
            setarRecyclerListaVariedade(false);
            setVariedadeEscolhida();
        } catch (Exception e) {
            e.printStackTrace();
        }

        consultaVariedade.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setVariedadeEscolhida();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setVariedadeEscolhida() {
        try {
            if (!consultaVariedade.getText().toString().equalsIgnoreCase("")) {
                //BUSCA POR NOME
                if (!Character.isDigit(consultaVariedade.getText().toString().charAt(0))) {
                    if (listaVariedade.stream().anyMatch(item -> item.getNomeVariedade().toLowerCase(Locale.ROOT).contains(String.valueOf(consultaVariedade.getText())))) {
                        listaVariedadeFiltrada = listaVariedade.stream().filter(itens -> itens.getNomeVariedade().toLowerCase(Locale.ROOT).contains(String.valueOf(consultaVariedade.getText()))).collect(Collectors.toList());
                        setarRecyclerListaVariedade(false);
                    } else {
                        setarRecyclerListaVariedade(true);
                    }
                    //BUSCA POR CODIGO
                } else if (Character.isDigit(consultaVariedade.getText().toString().charAt(0))) {
                    if (listaVariedade.stream().anyMatch(cod -> String.valueOf(cod.getCodVariedade()).toLowerCase(Locale.ROOT).contains(consultaVariedade.getText()))) {
                        listaVariedadeFiltrada = listaVariedade.stream().filter(u -> String.valueOf(u.getCodVariedade()).toLowerCase(Locale.ROOT).contains(consultaVariedade.getText())).collect(Collectors.toList());
                        setarRecyclerListaVariedade(false);
                    } else {
                        setarRecyclerListaVariedade(true);
                    }
                }
            } else {
                listaVariedadeFiltrada = listaVariedade;
                setarRecyclerListaVariedade(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setResultVariedadeEscolhido(Variedade variedade) {
        setResult(Activity.RESULT_OK, this.getIntent().putExtra("resultadoBuscaVariedade", variedade));
        this.finish();
    }

    private void setarCampos() {
        res = getResources();
        setarTotalItens();
    }

    private void setarTotalItens() {
        if (listaVariedadeFiltrada != null) {
            txtListaVariedade.setText(res.getString(R.string.lista_variedade, listaVariedadeFiltrada.size()));
        } else {
            listaVariedadeFiltrada = new ArrayList<>();
            txtListaVariedade.setText(res.getString(R.string.lista_variedade, 0));
        }
    }

    private void setarRecyclerListaVariedade(boolean vazio) {
        if (vazio)
            listaVariedadeAdapter = new VariedadeAdapter(listaVariedadeVazia, this::setResultVariedadeEscolhido);
        if (!vazio)
            listaVariedadeAdapter = new VariedadeAdapter(listaVariedadeFiltrada, this::setResultVariedadeEscolhido);
        rvVariedade.setAdapter(listaVariedadeAdapter);
        if (acrescenta == 0) {
            rvVariedade.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
            acrescenta++;
        }
        rvVariedade.setLayoutManager(new LinearLayoutManager(this));
        if (vazio) txtListaVariedade.setText(res.getString(R.string.lista_variedade, 0));
        if (!vazio) setarTotalItens();
    }

    public void carregaBotoes() {
        consultaVariedade = findViewById(R.id.edit_variedade);
        txtListaVariedade = findViewById(R.id.txt_listaVariedade);
        rvVariedade = findViewById(R.id.rv_Variedade);
    }
}