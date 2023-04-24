package br.com.database.dao;

import br.com.database.infra.ConnectionFactory;
import br.com.database.model.Clientes;
import br.com.database.model.Processo;
import br.com.database.model.PublicacoesEIntimacoes;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PublicacoesAndIntimacoesDAO implements IPublicacoesAndIntimacoesDAO{
    private final Connection connection;
    public PublicacoesAndIntimacoesDAO(Connection connection){
        this.connection = connection;
    }
    @Override
    public PublicacoesEIntimacoes save(PublicacoesEIntimacoes publicacoesAndIntimacoes) {
        try (Connection connection = ConnectionFactory.getConnection()) {
            // Verifica se já existe uma publicação ou intimação com o ID informado
            if (existePublicacaoOuIntimacaoComId(publicacoesAndIntimacoes.getId())) {
                throw new RuntimeException("Já existe uma publicação ou intimação com o ID informado.");
            }

            String sql = "INSERT INTO publicacoes_e_intimacoes (tipo, data, processo, texto) VALUES (?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, publicacoesAndIntimacoes.getTipo());
            preparedStatement.setDate(2, Date.valueOf(publicacoesAndIntimacoes.getData()));
            preparedStatement.setLong(3, publicacoesAndIntimacoes.getProcesso().getNumeroDoProcesso());
            preparedStatement.setString(4, publicacoesAndIntimacoes.getTexto());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return publicacoesAndIntimacoes;
    }

    public boolean existePublicacaoOuIntimacaoComId(Long id) {
        try (Connection connection = ConnectionFactory.getConnection()) {
            String sql = "SELECT COUNT(*) FROM publicacoes_e_intimacoes WHERE id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            int count = resultSet.getInt(1);
            return count > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PublicacoesEIntimacoes update(PublicacoesEIntimacoes publicacoesAndIntimacoes) {
        try (Connection connection = ConnectionFactory.getConnection()) {
            String sql = "UPDATE publicacoes_e_intimacoes SET tipo = ?, data = ?, processo = ?, texto = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, publicacoesAndIntimacoes.getTipo());
            preparedStatement.setDate(2, Date.valueOf(publicacoesAndIntimacoes.getData()));
            preparedStatement.setLong(3, publicacoesAndIntimacoes.getProcesso().getNumeroDoProcesso());
            preparedStatement.setString(4, publicacoesAndIntimacoes.getTexto());
            preparedStatement.setLong(5, publicacoesAndIntimacoes.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return publicacoesAndIntimacoes;
    }

    @Override
    public void delete(long id) {
        try (Connection connection = ConnectionFactory.getConnection()) {
            String sql = "DELETE FROM publicacoes_e_intimacoes WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<PublicacoesEIntimacoes> findAll() {
        String sql = "SELECT id, tipo, data, processo, texto FROM publicacoes_e_intimacoes";

        List<PublicacoesEIntimacoes> publicacoes = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String descricao = resultSet.getString("tipo");
                LocalDate data = resultSet.getDate("data").toLocalDate();
                long numero_do_processo = resultSet.getLong("processo");
                String texto = resultSet.getString("texto");

                ProcessosDAO daoProcessos = new ProcessosDAO(connection);
                Optional<Processo> processoOptional = daoProcessos.findByNumeroDoProcesso(numero_do_processo);
                Processo processo = processoOptional.get();
                PublicacoesEIntimacoes publicacao = new PublicacoesEIntimacoes(id, descricao, data, processo,texto);
                publicacoes.add(publicacao);

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return publicacoes;
    }

    @Override
    public Optional<PublicacoesEIntimacoes> findById(long id) {
        String sql = "SELECT id, tipo, data, processo, texto " +
                "FROM publicacoes_e_intimacoes " +
                "WHERE id = ?";

        PublicacoesEIntimacoes publicacao = null;

        try (Connection connection = ConnectionFactory.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();


            if (resultSet.next()) {
                long publicacaoId = resultSet.getLong("id");
                String tipo = resultSet.getString("tipo");
                LocalDate data = resultSet.getDate("data").toLocalDate();
                long numeroDoProcesso = resultSet.getLong("processo");
                String texto = resultSet.getString("texto");

                ProcessosDAO daoProcesso = new ProcessosDAO(connection);
                Optional<Processo> processoOptional = daoProcesso.findByNumeroDoProcesso(numeroDoProcesso);
                Processo processo = processoOptional.get();
                publicacao = new PublicacoesEIntimacoes(publicacaoId, tipo, data, processo, texto);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(publicacao);
    }
}
