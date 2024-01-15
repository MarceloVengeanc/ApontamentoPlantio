package com.exataid.apontamentoplantio.banco.modelos;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "AP_CARGADEMUDA")
public class CargaDeMuda implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private int codProcedencia;
    private String nomeProcedencia;
    private int codFazenda;
    private String nomeFazenda;
    private int codTalhao;
    private float areaTalhao;
    private int carga;
    private double peso;

    private String nomeFuncionario;
    private long matriculaFuncionario;
    private long coletor;
    private long idApontamento;
    private String dataApontamento;
    private String dataEnviado;
    @ColumnInfo(name = "enviado", defaultValue = "N")
    private String enviado;

    public CargaDeMuda() {
    }

    public CargaDeMuda(long id, int codProcedencia, String nomeProcedencia, int codFazenda, String nomeFazenda, int codTalhao, float areaTalhao, int carga, double peso, String nomeFuncionario, long matriculaFuncionario, long coletor, long idApontamento, String dataApontamento, String dataEnviado, String enviado) {
        this.id = id;
        this.codProcedencia = codProcedencia;
        this.nomeProcedencia = nomeProcedencia;
        this.codFazenda = codFazenda;
        this.nomeFazenda = nomeFazenda;
        this.codTalhao = codTalhao;
        this.areaTalhao = areaTalhao;
        this.carga = carga;
        this.peso = peso;
        this.nomeFuncionario = nomeFuncionario;
        this.matriculaFuncionario = matriculaFuncionario;
        this.coletor = coletor;
        this.idApontamento = idApontamento;
        this.dataApontamento = dataApontamento;
        this.dataEnviado = dataEnviado;
        this.enviado = enviado;
    }

    public String getDataEnviado() {
        return dataEnviado;
    }

    public void setDataEnviado(String dataEnviado) {
        this.dataEnviado = dataEnviado;
    }

    public String getNomeFuncionario() {
        return nomeFuncionario;
    }

    public void setNomeFuncionario(String nomeFuncionario) {
        this.nomeFuncionario = nomeFuncionario;
    }

    public long getMatriculaFuncionario() {
        return matriculaFuncionario;
    }

    public void setMatriculaFuncionario(long matriculaFuncionario) {
        this.matriculaFuncionario = matriculaFuncionario;
    }

    public long getColetor() {
        return coletor;
    }

    public void setColetor(long coletor) {
        this.coletor = coletor;
    }

    public String getEnviado() {
        return enviado;
    }

    public void setEnviado(String enviado) {
        this.enviado = enviado;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCodProcedencia() {
        return codProcedencia;
    }

    public void setCodProcedencia(int codProcedencia) {
        this.codProcedencia = codProcedencia;
    }

    public String getNomeProcedencia() {
        return nomeProcedencia;
    }

    public void setNomeProcedencia(String nomeProcedencia) {
        this.nomeProcedencia = nomeProcedencia;
    }

    public int getCodFazenda() {
        return codFazenda;
    }

    public void setCodFazenda(int codFazenda) {
        this.codFazenda = codFazenda;
    }

    public String getNomeFazenda() {
        return nomeFazenda;
    }

    public void setNomeFazenda(String nomeFazenda) {
        this.nomeFazenda = nomeFazenda;
    }

    public int getCodTalhao() {
        return codTalhao;
    }

    public void setCodTalhao(int codTalhao) {
        this.codTalhao = codTalhao;
    }

    public int getCarga() {
        return carga;
    }

    public void setCarga(int carga) {
        this.carga = carga;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public long getIdApontamento() {
        return idApontamento;
    }

    public void setIdApontamento(long idApontamento) {
        this.idApontamento = idApontamento;
    }

    public String getDataApontamento() {
        return dataApontamento;
    }

    public void setDataApontamento(String dataApontamento) {
        this.dataApontamento = dataApontamento;
    }

    public float getAreaTalhao() {
        return areaTalhao;
    }

    public void setAreaTalhao(float areaTalhao) {
        this.areaTalhao = areaTalhao;
    }
}
