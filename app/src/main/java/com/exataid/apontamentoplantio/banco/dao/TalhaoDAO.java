package com.exataid.apontamentoplantio.banco.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.exataid.apontamentoplantio.banco.modelos.Talhao;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

@Dao
public interface TalhaoDAO extends ModeloDAO<Talhao>{
    @Query("SELECT * FROM AP_TALHAO order by codTalhao asc")
    ListenableFuture<List<Talhao>> buscarTodos();

    @Query("SELECT * FROM AP_TALHAO t WHERE t.codFazenda=:codFazenda")
    ListenableFuture<List<Talhao>> buscaTalhaoFazenda(int codFazenda);
}
