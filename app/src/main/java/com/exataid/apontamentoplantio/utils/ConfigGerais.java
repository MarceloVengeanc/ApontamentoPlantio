package com.exataid.apontamentoplantio.utils;

import android.content.Context;

import com.exataid.apontamentoplantio.APDatabase;
import com.exataid.apontamentoplantio.banco.dao.AutenticaLoginDAO;
import com.exataid.apontamentoplantio.banco.dao.ConfigWSDAO;
import com.exataid.apontamentoplantio.banco.modelos.AutenticaLogin;
import com.exataid.apontamentoplantio.banco.modelos.ConfigWS;
import com.exataid.apontamentoplantio.banco.modelos.Funcionario;
import com.exataid.apontamentoplantio.telas.LoginActivity;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ConfigGerais {


    public List<ConfigWS> WS;
    public AutenticaLogin Autenticacao;
    public static Funcionario UsuarioLogado;
    public static ConfigGerais instance;

    public ConfigGerais(List<ConfigWS> ws, AutenticaLogin aut) {

        WS = ws;
        Autenticacao = aut;
    }

    public static ConfigGerais getInstance(Context ctx) throws ExecutionException, InterruptedException {
        if (instance != null) {
            return instance;
        }

        APDatabase instance = APDatabase.getInstance((LoginActivity) ctx);
        ConfigWSDAO wsDAO = instance.getConfigWSDAO();
        AutenticaLoginDAO autDao = instance.getAutenticaLoginDAO();
        List<ConfigWS> ws = wsDAO.buscarTodas().get();
        AutenticaLogin aut = autDao.buscarAtivo().get();

        if (ws != null && ws.size() > 0 && aut != null) {
            return new ConfigGerais(ws, aut);
        }
        return null;
    }
}