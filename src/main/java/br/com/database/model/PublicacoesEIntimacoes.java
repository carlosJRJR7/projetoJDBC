package br.com.database.model;

import java.time.LocalDate;

public class PublicacoesEIntimacoes {
    private long id;
    private String tipo;
    private LocalDate data;
    private Processo processo;

    private String texto;

    public PublicacoesEIntimacoes(long id, String tipo, LocalDate data, Processo processo, String texto) {
        this.id = id;
        this.tipo = tipo;
        this.data = data;
        this.processo = processo;
        this.texto = texto;
    }
    public PublicacoesEIntimacoes(String tipo, LocalDate data, Processo processo, String texto) {
        this.tipo = tipo;
        this.data = data;
        this.processo = processo;
        this.texto = texto;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Processo getProcesso() {
        return processo;
    }

    public void setProcesso(Processo processo) {
        this.processo = processo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
