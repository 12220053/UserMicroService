package bt.edu.gcit.usermicroservice.dao;

import bt.edu.gcit.usermicroservice.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import bt.edu.gcit.usermicroservice.exception.UserNotFoundException;

import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

    private final EntityManager entityManager;

    @Autowired
    public UserDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public User save(User user) {
        return entityManager.merge(user);
    }

    @Override
    public User findByEmail(String email) {
        TypedQuery<User> query = entityManager.createQuery("FROM User WHERE email = :email", User.class);
        query.setParameter("email", email);
        List<User> users = query.getResultList();
        if (users.isEmpty()) {
            return null;
        } else {
            return users.get(0);
        }
    }

    @Override
    public User findById(int theid) {
        return entityManager.find(User.class, theid);
    }

    @Override
    public void deleteById(int theid) {
        User user = findById(theid);
        if (user != null) {
            entityManager.remove(user);
        }
    }

    @Override
    public void updateUserEnabledStatus(int id, boolean enabled) {
        User user = entityManager.find(User.class, id);
        System.out.println(user);
        if (user == null) {
            throw new UserNotFoundException("User not found with id " + id);
        }
            user.setEnabled(enabled);
            entityManager.persist(user);
 }
}
