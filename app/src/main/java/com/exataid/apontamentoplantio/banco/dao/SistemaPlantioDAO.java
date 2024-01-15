package com.exataid.apontamentoplantio.banco.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.exataid.apontamentoplantio.banco.modelos.SistemaPlantio;
import com.exataid.apontamentoplantio.banco.modelos.Talhao;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

@Dao
public interface SistemaPlantioDAO extends ModeloDAO<SistemaPlantio> {
    @Query("SELECT* FROM AP_SISTEMAPLANTIO order by codSistemaPlantio asc")
    ListenableFuture<List<SistemaPlantio>> buscarTodos();

    @Query("SELECT * FROM AP_SISTEMAPLANTIO t WHERE t.codSistemaPlantio=:codSistemaPlantio")
    ListenableFuture<List<SistemaPlantio>> buscaSistemaPlantio(int codSistemaPlantio);
}
