package br.com.database.dao;

import br.com.database.infra.ConnectionFactory;
import br.com.database.model.Clientes;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientesDAO implements IClientesDAO{

    @Override
    public Clientes save(Clientes cliente) {
        try (Connection connection = ConnectionFactory.getConnection()) {
            // Verifica se já existe um registro com o mesmo nome
            String checkSql = "SELECT COUNT(*) FROM Clientes WHERE nome = ?";
            PreparedStatement checkStatement = connection.prepareStatement(checkSql);
            checkStatement.setString(1, cliente.getNome());
            ResultSet checkResult = checkStatement.executeQuery();
            checkResult.next();
            int count = checkResult.getInt(1);
            if (count > 0) {
                throw new RuntimeException("Já existe um cliente com este nome.");
            }

            // Insere o registro
            String insertSql = "INSERT INTO Clientes (nome) VALUES (?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
            insertStatement.setString(1, cliente.getNome());
            insertStatement.executeUpdate();

            // Obtém o ID gerado
            ResultSet resultSet = insertStatement.getGeneratedKeys();
            resultSet.next();
            Long id = resultSet.getLong("id");
            cliente.setId(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return cliente;
    }

    @Override
    public Clientes update(Clientes cliente) {
        try (Connection connection = ConnectionFactory.getConnection()) {
            String sql = "UPDATE Clientes SET nome = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, cliente.getNome());
            preparedStatement.setLong(2, cliente.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } ;
        return cliente;
    }

    @Override
    public void delete(Long id) {
        try (Connection connection = ConnectionFactory.getConnection()) {
            // Verifica se o Cliente possui Processos vinculados
            String sqlCount = "SELECT COUNT(*) AS count FROM processos WHERE idcliente = ?";
            PreparedStatement countStatement = connection.prepareStatement(sqlCount);
            countStatement.setLong(1, id);
            ResultSet countResult = countStatement.executeQuery();

            if (countResult.next() && countResult.getInt("count") > 0) {
                // Exibe um alerta ao usuário e não executa a exclusão
                System.out.println("Não é possível excluir o Cliente pois há Processos vinculados a ele.");
                return;
            }

            // Executa a exclusão caso não haja Processos vinculados
            String sql = "DELETE FROM Clientes WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Clientes> findAll() {
        String sql = "SELECT id, nome from Clientes";

        List<Clientes> clientes = new ArrayList<>();

        try(Connection connection = ConnectionFactory.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                long id = resultSet.getLong("id");
                String nome = resultSet.getString("nome");

                Clientes cliente = new Clientes(id, nome);
                clientes.add(cliente);
            }

        }catch (SQLException e){
            throw new RuntimeException(e);
        }

        return clientes;
    }

    @Override
    public Optional<Clientes> findById(Long id) {
        String sql = "SELECT id, nome from Clientes WHERE id = ?";

        Clientes cliente = null;

        try(Connection connection = ConnectionFactory.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                long pKey = resultSet.getLong("id");
                String nome = resultSet.getString("nome");

                cliente = new Clientes(pKey, nome);
            }

        }catch (SQLException e){
            throw new RuntimeException(e);
        }

        return Optional.ofNullable(cliente);
    }
}
