package br.com.database.dao;

import br.com.database.model.Clientes;

import java.util.List;
import java.util.Optional;

public interface IClientesDAO {
        Clientes save(Clientes cliente);
        Clientes update(Clientes cliente);
        void delete(Long id);
        List<Clientes> findAll();
        Optional<Clientes> findById(Long id);
}
