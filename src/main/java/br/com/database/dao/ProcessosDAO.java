package br.com.database.dao;

import br.com.database.infra.ConnectionFactory;
import br.com.database.model.Clientes;
import br.com.database.model.Processo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProcessosDAO implements IProcessosDAO{
    private final Connection connection;
    public ProcessosDAO(Connection connection){
        this.connection = connection;
    }


    @Override
    public Processo save(Processo processo) {
        try (Connection connection = ConnectionFactory.getConnection()) {
            String sql = "INSERT INTO Processos (numero_do_processo, idcliente) VALUES (?, ?)";

            // Verifica se o processo já existe
            if (existsProcesso(processo.getNumeroDoProcesso())) {
                throw new IllegalArgumentException("O processo já está cadastrado");
            }

            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, processo.getNumeroDoProcesso());
            preparedStatement.setLong(2, processo.getCliente().getId());

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();

            Long id = resultSet.getLong("id");
            processo.setId(id.intValue());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return processo;
    }

    private boolean existsProcesso(long numeroDoProcesso) {
        String sql = "SELECT COUNT(*) AS count FROM Processos WHERE numero_do_processo = ?";

        try (Connection connection = ConnectionFactory.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, numeroDoProcesso);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            return resultSet.getInt("count") > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Processo update(Processo processo) {
        try (Connection connection = ConnectionFactory.getConnection()) {
            String sql = "UPDATE processos SET numero_do_processo = ?, idcliente = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, processo.getNumeroDoProcesso());
            preparedStatement.setLong(2, processo.getCliente().getId());
            preparedStatement.setLong(3, processo.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return processo;
    }

    @Override
    public void delete(long id) throws SQLException{
        try (Connection connection = ConnectionFactory.getConnection()) {
            String sql = "SELECT COUNT(*) FROM publicacoes_e_intimacoes WHERE processo = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            int count = resultSet.getInt(1);

            if (count > 0) {
                throw new SQLException("Não é possível excluir o processo pois existem " + count + " intimações ou publicações vinculadas.");
            }

            sql = "DELETE FROM processos WHERE id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Processo> findAll() {
        String sql = "SELECT p.id, p.numero_do_processo, c.id as cliente_id, c.nome as cliente_nome " +
                "FROM processos p " +
                "INNER JOIN Clientes c ON p.idcliente = c.id";

        List<Processo> processos = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                long numeroDoProcesso = resultSet.getLong("numero_do_processo");
                long clienteId = resultSet.getLong("cliente_id");

                ClientesDAO daoCliente = new ClientesDAO(connection);
                Optional<Clientes> clientesOptional = daoCliente.findById(clienteId);
                Clientes cliente = clientesOptional.get();
                Processo processo = new Processo(id, numeroDoProcesso, cliente);
                processos.add(processo);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return processos;
    }

    @Override
    public Optional<Processo> findById(long id) {
        String sql = "SELECT p.id, p.numero_do_processo, c.id as cliente_id, c.nome as cliente_nome " +
                "FROM processos p " +
                "INNER JOIN Clientes c ON p.idcliente = c.id " +
                "WHERE p.id = ?";

        Processo processo = null;

        try (Connection connection = ConnectionFactory.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                long processoId = resultSet.getLong("id");
                long numeroDoProcesso = resultSet.getLong("numero_do_processo");
                long clienteId = resultSet.getLong("cliente_id");

                ClientesDAO daoCliente = new ClientesDAO(connection);
                Optional<Clientes> clientesOptional = daoCliente.findById(clienteId);
                Clientes cliente = clientesOptional.get();
                processo = new Processo(processoId, numeroDoProcesso, cliente);

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(processo);
    }

    public Optional<Processo> findByNumeroDoProcesso(long numeroDoProcesso) {
        String sql = "SELECT p.id, p.numero_do_processo, c.id as cliente_id, c.nome as cliente_nome " +
                "FROM processos p " +
                "INNER JOIN Clientes c ON p.idcliente = c.id " +
                "WHERE p.numero_do_processo = ?";

        Processo processo = null;

        try (Connection connection = ConnectionFactory.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, numeroDoProcesso);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                long processoId = resultSet.getLong("id");
                long numeroProcesso = resultSet.getLong("numero_do_processo");
                long clienteId = resultSet.getLong("cliente_id");

                ClientesDAO daoCliente = new ClientesDAO(connection);
                Optional<Clientes> clientesOptional = daoCliente.findById(clienteId);
                Clientes cliente = clientesOptional.get();
                processo = new Processo(processoId, numeroDoProcesso, cliente);

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(processo);
    }
}
