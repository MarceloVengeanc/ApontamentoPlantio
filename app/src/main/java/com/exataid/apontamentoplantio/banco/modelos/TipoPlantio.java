package com.exataid.apontamentoplantio.banco.modelos;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "AP_TIPOPLANTIO")
public class TipoPlantio implements Serializable {

    @PrimaryKey
    private long id;
    private String nomeTipo;
    private int codTipo;

    public TipoPlantio(long id, String nomeTipo, int codTipo) {
        this.id = id;
        this.nomeTipo = nomeTipo;
        this.codTipo = codTipo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNomeTipo() {
        return nomeTipo;
    }

    public void setNomeTipo(String nomeTipo) {
        this.nomeTipo = nomeTipo;
    }

    public int getCodTipo() {
        return codTipo;
    }

    public void setCodTipo(int codTipo) {
        this.codTipo = codTipo;
    }
}
