package com.exataid.apontamentoplantio.banco.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.exataid.apontamentoplantio.banco.modelos.Procedencia;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

@Dao
public interface ProcedenciaDAO extends ModeloDAO<Procedencia> {
    @Query("SELECT * FROM AP_PROCEDENCIA ORDER BY codProcedencia asc")
    ListenableFuture<List<Procedencia>> buscarTodas();
}
