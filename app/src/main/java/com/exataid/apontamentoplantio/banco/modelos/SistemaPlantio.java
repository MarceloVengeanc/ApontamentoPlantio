package com.exataid.apontamentoplantio.banco.modelos;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "AP_SISTEMAPLANTIO")
public class SistemaPlantio implements Serializable {

    @PrimaryKey
    private long id;
    private String nomeSistemaPlantio;
    private int codSistemaPlantio;

    public SistemaPlantio(long id, String nomeSistemaPlantio, int codSistemaPlantio) {
        this.id = id;
        this.nomeSistemaPlantio = nomeSistemaPlantio;
        this.codSistemaPlantio = codSistemaPlantio;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNomeSistemaPlantio() {
        return nomeSistemaPlantio;
    }

    public void setNomeSistemaPlantio(String nomeSistemaPlantio) {
        this.nomeSistemaPlantio = nomeSistemaPlantio;
    }

    public int getCodSistemaPlantio() {
        return codSistemaPlantio;
    }

    public void setCodSistemaPlantio(int codSistemaPlantio) {
        this.codSistemaPlantio = codSistemaPlantio;
    }
}
