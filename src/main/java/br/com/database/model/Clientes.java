package br.com.database.model;



public class Clientes {

    private long id;
    private String nome;

    public Clientes(long id, String nome) {
        this.id = id;
        this.nome = nome;
    }


    public Clientes (String nome){
        this.nome = nome;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
