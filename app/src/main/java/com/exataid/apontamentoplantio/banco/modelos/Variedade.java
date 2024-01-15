package com.exataid.apontamentoplantio.banco.modelos;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
@Entity(tableName = "AP_VARIEDADE")
public class Variedade implements Serializable {

    @PrimaryKey
    private long id;
    private String nomeVariedade;
    private int codVariedade;

    public Variedade(long id, String nomeVariedade, int codVariedade) {
        this.id = id;
        this.nomeVariedade = nomeVariedade;
        this.codVariedade = codVariedade;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNomeVariedade() {
        return nomeVariedade;
    }

    public void setNomeVariedade(String nomeVariedade) {
        this.nomeVariedade = nomeVariedade;
    }

    public int getCodVariedade() {
        return codVariedade;
    }

    public void setCodVariedade(int codVariedade) {
        this.codVariedade = codVariedade;
    }
}
