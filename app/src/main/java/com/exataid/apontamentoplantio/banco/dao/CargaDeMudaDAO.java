package com.exataid.apontamentoplantio.banco.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.exataid.apontamentoplantio.banco.modelos.CargaDeMuda;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

@Dao
public interface CargaDeMudaDAO extends ModeloDAO<CargaDeMuda> {
    @Query("SELECT*FROM AP_CARGADEMUDA")
    ListenableFuture<List<CargaDeMuda>> buscarTodas();

    @Query("SELECT * FROM AP_CARGADEMUDA cm Where cm.idApontamento=:idapontamento")
    ListenableFuture<List<CargaDeMuda>> buscarPorApontamento(long idapontamento);

    @Query("SELECT * FROM AP_CARGADEMUDA cm Where cm.enviado=:enviado and cm.nomeFuncionario=:nomeFuncionario")
    ListenableFuture<List<CargaDeMuda>> buscarPorApontamento(String enviado, String nomeFuncionario);

}
