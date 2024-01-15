package com.exataid.apontamentoplantio.utils.interfaces.endpoints.services;

import com.exataid.apontamentoplantio.banco.modelos.ApontamentoPlantio;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EnvioService {
    //ENVIO
    @POST("post")
    Call<ApontamentoPlantio> enviarRegistros(@Body ApontamentoPlantio apontamentoPlantio);

}