package bt.edu.gcit.usermicroservice.dao;

import bt.edu.gcit.usermicroservice.entity.Country;
import bt.edu.gcit.usermicroservice.entity.State;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StateDAOImpl implements StateDAO {

    private final EntityManager entityManager;

    @Autowired
    public StateDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<State> findByCountryOrderByNameAsc(Country country) {
        return entityManager.createQuery(
                "SELECT s FROM State s WHERE s.country = :country ORDER BY s.name ASC", State.class)
                .setParameter("country", country)
                .getResultList();
    }

    @Override
    public List<State> findAll() {
        return entityManager.createQuery("SELECT s FROM State s", State.class)
                .getResultList();
    }

    @Override
    public State findById(int theId) {
        return entityManager.find(State.class, theId);
    }

    @Override
    @Transactional
    public void save(State theState, Long countryId) {
        // Assuming theState already has a country set before this method is called.
        entityManager.persist(theState);
    }

    @Override
    @Transactional
    public void deleteById(int theId) {
        State state = entityManager.find(State.class, theId);
        if (state != null) {
            entityManager.remove(state);
        }
    }

    @Override
    public List<State> listStatesByCountry(Long countryId) {
        TypedQuery<State> query = entityManager.createQuery(
                "SELECT s FROM State s WHERE s.country.id = :countryId", State.class);
        query.setParameter("countryId", countryId);
        return query.getResultList();
    }
}
