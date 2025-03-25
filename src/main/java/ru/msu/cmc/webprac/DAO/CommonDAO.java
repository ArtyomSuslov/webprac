package ru.msu.cmc.webprac.DAO;

import ru.msu.cmc.webprac.entities.CommonEntity;

import java.util.Collection;

public interface CommonDAO<T extends CommonEntity<ID>, ID> {

    // Getting an entity by its id
    T getById(ID id);

    // Getting all the entities
    Collection<T> getAll();

    // Saving an entity
    void save(T entity);

    // Saving a collection of entities at once
    void saveCollection(Collection<T> entities);

    // Deleting an entity
    void delete(T entity);

    // Deleting an entity by its id
    void deleteById(ID id);

    // Updating an entity
    void update(T entity);
}