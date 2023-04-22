package br.com.database.model;

public class Processo {

    private long id;

    private long numero_do_processo;

    private Clientes idCliente;

    public Processo(long id, long numero_do_processo, Clientes idCliente) {
        this.id = id;
        this.numero_do_processo = numero_do_processo;
        this.idCliente = idCliente;
    }

    public Processo(long numero_do_processo, Clientes idCliente){
        this.numero_do_processo = numero_do_processo;
        this.idCliente = idCliente;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getNumeroDoProcesso() {
        return numero_do_processo;
    }

    public void setNumeroDoProcesso(long numero_do_processo) {
        this.numero_do_processo = numero_do_processo;
    }

    public Clientes getCliente() {
        return idCliente;
    }

    public void setCliente(Clientes idCliente) {
        this.idCliente = idCliente;
    }
}
