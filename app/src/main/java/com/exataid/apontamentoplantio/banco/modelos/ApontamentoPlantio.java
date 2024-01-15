package com.exataid.apontamentoplantio.banco.modelos;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = "AP_APONTAMENTOPLANTIO")
public class ApontamentoPlantio implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private long coletor;
    private String nomeFuncionario;
    private long matriculaFuncionario;
    private String dataPlantio;
    private String dataPlantioSalvo;
    private String concluido;
    private int codFazenda;
    private String nomeFazenda;
    private int codTalhao;
    private float areaTalhao;
    private int codSisPlantio;
    private String nomeSisPlantio;
    private int codVariedade;
    private String nomeVariedade;
    private int codTipoPlantio;
    private String nomeTipoPlantio;
    private double areaPlantada;
    private String lote;
    private String enviado;
    private String dataEnviado;
    @Ignore
    private List<CargaDeMuda> cargaDeMuda;

    public ApontamentoPlantio() {
    }

    public ApontamentoPlantio(long id, long coletor, String nomeFuncionario, long matriculaFuncionario, String dataPlantio, String dataPlantioSalvo, String concluido, int codFazenda, String nomeFazenda, int codTalhao, float areaTalhao, int codSisPlantio, String nomeSisPlantio, int codVariedade, String nomeVariedade, int codTipoPlantio, String nomeTipoPlantio, double areaPlantada, String lote, String enviado, String dataEnviado) {
        this.id = id;
        this.coletor = coletor;
        this.nomeFuncionario = nomeFuncionario;
        this.matriculaFuncionario = matriculaFuncionario;
        this.dataPlantio = dataPlantio;
        this.dataPlantioSalvo = dataPlantioSalvo;
        this.concluido = concluido;
        this.codFazenda = codFazenda;
        this.nomeFazenda = nomeFazenda;
        this.codTalhao = codTalhao;
        this.areaTalhao = areaTalhao;
        this.codSisPlantio = codSisPlantio;
        this.nomeSisPlantio = nomeSisPlantio;
        this.codVariedade = codVariedade;
        this.nomeVariedade = nomeVariedade;
        this.codTipoPlantio = codTipoPlantio;
        this.nomeTipoPlantio = nomeTipoPlantio;
        this.areaPlantada = areaPlantada;
        this.lote = lote;
        this.enviado = enviado;
        this.dataEnviado = dataEnviado;
    }

    public List<CargaDeMuda> getCargaDeMuda() {
        return cargaDeMuda;
    }

    public void setCargaDeMuda(List<CargaDeMuda> cargaDeMuda) {
        this.cargaDeMuda = cargaDeMuda;
    }

    public String getConcluido() {
        return concluido;
    }

    public void setConcluido(String concluido) {
        this.concluido = concluido;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getColetor() {
        return coletor;
    }

    public void setColetor(long coletor) {
        this.coletor = coletor;
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

    public String getDataPlantio() {
        return dataPlantio;
    }

    public void setDataPlantio(String dataPlantio) {
        this.dataPlantio = dataPlantio;
    }

    public String getDataPlantioSalvo() {
        return dataPlantioSalvo;
    }

    public void setDataPlantioSalvo(String dataPlantioSalvo) {
        this.dataPlantioSalvo = dataPlantioSalvo;
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

    public float getAreaTalhao() {
        return areaTalhao;
    }

    public void setAreaTalhao(float areaTalhao) {
        this.areaTalhao = areaTalhao;
    }

    public int getCodSisPlantio() {
        return codSisPlantio;
    }

    public void setCodSisPlantio(int codSisPlantio) {
        this.codSisPlantio = codSisPlantio;
    }

    public String getNomeSisPlantio() {
        return nomeSisPlantio;
    }

    public void setNomeSisPlantio(String nomeSisPlantio) {
        this.nomeSisPlantio = nomeSisPlantio;
    }

    public int getCodVariedade() {
        return codVariedade;
    }

    public void setCodVariedade(int codVariedade) {
        this.codVariedade = codVariedade;
    }

    public String getNomeVariedade() {
        return nomeVariedade;
    }

    public void setNomeVariedade(String nomeVariedade) {
        this.nomeVariedade = nomeVariedade;
    }

    public int getCodTipoPlantio() {
        return codTipoPlantio;
    }

    public void setCodTipoPlantio(int codTipoPlantio) {
        this.codTipoPlantio = codTipoPlantio;
    }

    public String getNomeTipoPlantio() {
        return nomeTipoPlantio;
    }

    public void setNomeTipoPlantio(String nomeTipoPlantio) {
        this.nomeTipoPlantio = nomeTipoPlantio;
    }

    public double getAreaPlantada() {
        return areaPlantada;
    }

    public void setAreaPlantada(double areaPlantada) {
        this.areaPlantada = areaPlantada;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getEnviado() {
        return enviado;
    }

    public void setEnviado(String enviado) {
        this.enviado = enviado;
    }

    public String getDataEnviado() {
        return dataEnviado;
    }

    public void setDataEnviado(String dataEnviado) {
        this.dataEnviado = dataEnviado;
    }
}
