package br.com.database.dao;

import br.com.database.model.Processo;

import java.util.List;
import java.util.Optional;

public interface IProcessosDAO {
    Processo save(Processo processo);
    Processo update(Processo processo);
    void delete(long id) throws ProcessosDAO.ProcessoComPublicacoesOuIntimacoesVinculadasException;
    List<Processo> findAll();
    Optional<Processo> findById(long id);
}