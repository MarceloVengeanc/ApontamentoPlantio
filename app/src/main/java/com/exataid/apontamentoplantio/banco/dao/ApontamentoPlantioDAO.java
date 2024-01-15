package com.exataid.apontamentoplantio.banco.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.exataid.apontamentoplantio.banco.modelos.ApontamentoPlantio;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

@Dao
public interface ApontamentoPlantioDAO extends ModeloDAO<ApontamentoPlantio> {

    @Query("SELECT * FROM AP_APONTAMENTOPLANTIO")
    ListenableFuture<List<ApontamentoPlantio>> buscarTodos();

    @Query("SELECT * FROM AP_APONTAMENTOPLANTIO WHERE nomeFuncionario=:funcionario")
    ListenableFuture<List<ApontamentoPlantio>> buscarPorFUncionario(String funcionario);

    @Query("SELECT * FROM AP_APONTAMENTOPLANTIO ORDER BY id desc limit 1")
    ListenableFuture<ApontamentoPlantio> buscarUltimoApontamento();

    @Query("SELECT * FROM AP_APONTAMENTOPLANTIO r WHERE r.nomeFuncionario=:funcionario and r.dataPlantio between :dataInicial and :dataFinal ORDER BY r.dataPlantio DESC, r.id desc ")
    ListenableFuture<List<ApontamentoPlantio>> buscarApontamentosPorData(String funcionario, String dataInicial, String dataFinal);
    @Query("SELECT * FROM AP_APONTAMENTOPLANTIO r where r.enviado=:enviado and r.nomeFuncionario=:funcionario")
    ListenableFuture<List<ApontamentoPlantio>> buscarPorNaoEnviado(String enviado, String funcionario);
}
