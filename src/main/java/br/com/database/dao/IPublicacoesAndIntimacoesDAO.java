package br.com.database.dao;

import br.com.database.model.PublicacoesEIntimacoes;

import java.util.List;
import java.util.Optional;

public interface IPublicacoesAndIntimacoesDAO {
    PublicacoesEIntimacoes save(PublicacoesEIntimacoes publicacoesAndIntimacoes);
    PublicacoesEIntimacoes update(PublicacoesEIntimacoes publicacoesAndIntimacoes);
    void delete(long id);
    List<PublicacoesEIntimacoes> findAll();
    Optional<PublicacoesEIntimacoes> findById(long id);
}
