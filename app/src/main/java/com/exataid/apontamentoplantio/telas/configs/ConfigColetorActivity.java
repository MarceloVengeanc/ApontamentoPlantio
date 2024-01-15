package com.exataid.apontamentoplantio.telas.configs;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.exataid.apontamentoplantio.APDatabase;
import com.exataid.apontamentoplantio.R;
import com.exataid.apontamentoplantio.banco.modelos.Coletor;

import java.util.concurrent.ExecutionException;

public class ConfigColetorActivity extends AppCompatActivity {
    private EditText numeroColetor;
    private EditText descricaoColetor;
    private Button btnSalvar;
    private Coletor coletor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_coletor);
        inicializarCampos();
        carregaColetor();
    }

    void inicializarCampos() {
        numeroColetor = findViewById(R.id.etxtConfigNumeroColetor);
        descricaoColetor = findViewById(R.id.etxColDesc);
        btnSalvar = findViewById(R.id.btnSalvarConfigColetor);
        btnSalvar.setOnClickListener(v -> salvarColetor());
    }

    void salvarColetor() {
        if (numeroColetor.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Campo Coletor não informado.", Toast.LENGTH_LONG).show();
            numeroColetor.requestFocus();
            return;
        } else if (descricaoColetor.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Campo Descrição não informado.", Toast.LENGTH_LONG).show();
            descricaoColetor.requestFocus();
            return;
        }

        Long coletorTemp = 0L;
        boolean novo = false;
        if (coletor == null) {
            coletor = new Coletor();
            novo = true;
        } else {
            coletorTemp = coletor.getNumero();
        }
        coletor.setNumero(Long.parseLong(numeroColetor.getText().toString()));
        coletor.setDescricao(descricaoColetor.getText().toString());

        if (novo) {
            APDatabase.getInstance(this).getColetorDAO().inserir(coletor);
        } else {
            // Atualizar não esta funcionando pq código é chave
            if (coletorTemp > 0) {
                Coletor c = new Coletor();
                c.setNumero(coletorTemp);

                APDatabase.getInstance(this).getColetorDAO().excluir(c);
                APDatabase.getInstance(this).getColetorDAO().inserir(coletor);
            }
        }

        Toast.makeText(this, "Coletor salvo.", Toast.LENGTH_LONG).show();
        finish();
    }

    void carregaColetor() {
        try {
            coletor = APDatabase.getInstance(this).getColetorDAO().buscarAtivo().get();
            if (coletor != null) {
                numeroColetor.setText(coletor.getNumero().toString());
                descricaoColetor.setText(coletor.getDescricao());
            } else {
                numeroColetor.setText("");
                descricaoColetor.setText("");
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
