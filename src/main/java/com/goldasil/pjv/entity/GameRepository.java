package com.goldasil.pjv.entity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface GameRepository extends CrudRepository<GameEntity, Long> {

    GameEntity save(GameEntity game);

}



/*
public class GameRepository {

    private EntityManager entityManager;
    public GameRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    public Optional<GameEntity> findById(Integer id) {
        GameEntity game = entityManager.find(GameEntity.class, id);
        return game != null ? Optional.of(game) : Optional.empty();
    }

    public List<GameEntity> findAll() {
        return entityManager.createQuery("from GameEntity").getResultList();
    }

    public Optional<GameEntity> findByName(String mainPlayerName) {
        GameEntity game = entityManager.createQuery("SELECT game FROM GameEntity game WHERE game.mainPlayerName = :mainPlayerName", GameEntity.class)
                .setParameter("mainPlayerName", mainPlayerName)
                .getSingleResult();
        return game != null ? Optional.of(game) : Optional.empty();
    }*/
    /*public Optional<GameEntity> findByNameNamedQuery(String mainPlayerName) {
        GameEntity game = entityManager.createNamedQuery("GameEntity.findByName", GameEntity.class)
                .setParameter("mainPlayerName", mainPlayerName)
                .getSingleResult();
        return game != null ? Optional.of(game) : Optional.empty();
    }*/
    /*public Optional<GameEntity> save(GameEntity game) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(game);
            entityManager.getTransaction().commit();
            return Optional.of(game);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}*/
