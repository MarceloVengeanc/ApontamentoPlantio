package com.exataid.apontamentoplantio.utils.interfaces.endpoints.services;

import com.exataid.apontamentoplantio.banco.modelos.Fazendas;
import com.exataid.apontamentoplantio.banco.modelos.Funcionario;
import com.exataid.apontamentoplantio.banco.modelos.ApontamentoPlantio;
import com.exataid.apontamentoplantio.banco.modelos.Procedencia;
import com.exataid.apontamentoplantio.banco.modelos.SistemaPlantio;
import com.exataid.apontamentoplantio.banco.modelos.Talhao;
import com.exataid.apontamentoplantio.banco.modelos.TipoPlantio;
import com.exataid.apontamentoplantio.banco.modelos.Variedade;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface CadastrosService {

    //Busca Fazendas
    @GET("d6608f55116cdad8dbaa")
    Call<List<Fazendas>> baixarTodasFazendas();
    //Busca Talhões
    @GET("45de8105ab10c26f5f3a")
    Call<List<Talhao>> baixarTodosTalhoes();
    //Busca Funcionarios
    @GET("655afe3ab06b338099da")
    Call<List<Funcionario>> baixarTodosFuncionarios();
    @GET("090fae57e464a93a8728")
    Call<List<SistemaPlantio>> baixarTodosSistemaPlantio();
    @GET("3a336ecde4afc75c06b3")
    Call<List<TipoPlantio>> baixarTodosTiposPlantio();
    @GET("7ee0787cef80976cc87f")
    Call<List<Variedade>> baixarTodasVariedades();
    @GET("562fd66944b4fdd40837")
    Call<List<Procedencia>> baixarTodasProcedencias();

    @POST("post")
    Call<ApontamentoPlantio> enviarRequisicaoFinalizada(@Body ApontamentoPlantio apontamentoPlantio);

}