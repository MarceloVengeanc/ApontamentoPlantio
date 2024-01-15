package com.exataid.apontamentoplantio.banco.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.exataid.apontamentoplantio.banco.modelos.AutenticaLogin;
import com.google.common.util.concurrent.ListenableFuture;

@Dao
public interface AutenticaLoginDAO extends ModeloDAO<AutenticaLogin> {
    @Query("SELECT * FROM AP_AUTENTICALOGIN ORDER BY ID DESC LIMIT 1")
    ListenableFuture<com.exataid.apontamentoplantio.banco.modelos.AutenticaLogin> buscarAtivo();

    @Query("SELECT * FROM AP_AUTENTICALOGIN WHERE id = :id")
    ListenableFuture<AutenticaLogin> buscarPorId(Long id);
}
