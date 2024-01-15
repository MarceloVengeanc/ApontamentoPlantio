package com.exataid.apontamentoplantio.banco.modelos;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
@Entity(tableName = "AP_PROCEDENCIA")
public class Procedencia implements Serializable {

    @PrimaryKey
    private long id;
    private String nomeProcedencia;
    private int codProcedencia;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNomeProcedencia() {
        return nomeProcedencia;
    }

    public void setNomeProcedencia(String nomeProcedencia) {
        this.nomeProcedencia = nomeProcedencia;
    }

    public int getCodProcedencia() {
        return codProcedencia;
    }

    public void setCodProcedencia(int codProcedencia) {
        this.codProcedencia = codProcedencia;
    }

    public Procedencia(long id, String nomeProcedencia, int codProcedencia) {
        this.id = id;
        this.nomeProcedencia = nomeProcedencia;
        this.codProcedencia = codProcedencia;
    }
}
