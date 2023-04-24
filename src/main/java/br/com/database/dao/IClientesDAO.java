package br.com.database.dao;

import br.com.database.model.Clientes;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface IClientesDAO {
        Clientes save(Clientes cliente);
        Clientes update(Clientes cliente);
        public void delete(Long id) throws SQLException, ClientesDAO.ClienteComProcessosVinculadosException;
        List<Clientes> findAll();
        Optional<Clientes> findById(Long id);
}
