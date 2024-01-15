package com.exataid.apontamentoplantio.banco.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.exataid.apontamentoplantio.banco.modelos.ConfigWS;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;


@Dao
public interface ConfigWSDAO extends ModeloDAO<ConfigWS> {
    @Query("SELECT * FROM AP_CONFIGWS order by prioridade asc")
    ListenableFuture<List<ConfigWS>> buscarTodas();

    @Query("SELECT * FROM AP_CONFIGWS")
    ListenableFuture<ConfigWS>buscarUm();

    @Query("SELECT * FROM AP_CONFIGWS WHERE prioridade=:pri")
    ListenableFuture<ConfigWS> buscarPorPrioridade(Integer pri);

    @Query("DELETE FROM AP_CONFIGWS")
    ListenableFuture<Void> limpar();
}