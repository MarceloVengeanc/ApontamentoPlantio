package com.exataid.apontamentoplantio.banco.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.exataid.apontamentoplantio.banco.modelos.Variedade;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

@Dao
public interface VariedadeDAO extends ModeloDAO<Variedade> {
    @Query("SELECT * FROM AP_VARIEDADE order by codVariedade asc")
    ListenableFuture<List<Variedade>> buscarTodos();
}
