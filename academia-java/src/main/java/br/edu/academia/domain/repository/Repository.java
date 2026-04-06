package br.edu.academia.domain.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T> {
    void save(T entity);

    void update(T entity);

    List<T> findAll();

    Optional<T> findById(long id);

    void delete(long id);
}
