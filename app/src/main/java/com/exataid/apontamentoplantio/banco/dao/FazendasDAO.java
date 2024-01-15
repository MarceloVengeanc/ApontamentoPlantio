package com.exataid.apontamentoplantio.banco.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.exataid.apontamentoplantio.banco.modelos.Fazendas;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

@Dao
public interface FazendasDAO extends ModeloDAO<Fazendas> {
    @Query("SELECT * FROM AP_FAZENDAS ORDER BY codFazenda asc")
    ListenableFuture<List<Fazendas>> buscarTodas();
}
