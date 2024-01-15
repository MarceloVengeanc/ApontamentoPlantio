package com.exataid.apontamentoplantio.banco.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.exataid.apontamentoplantio.banco.modelos.TipoPlantio;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

@Dao
public interface TipoPlantioDAO extends ModeloDAO<TipoPlantio> {
    @Query("SELECT * FROM AP_TIPOPLANTIO order by codTipo asc")
    ListenableFuture<List<TipoPlantio>> buscarTodos();
}
