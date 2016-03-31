package dao;

import domain.Kweet;
import domain.User;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author edwinlambregts
 */
@Stateless
public class UserDaoImp implements UserDao {

    @PersistenceContext(unitName = "KwetterPU")
    private EntityManager em;
    
    @Override
    @Deprecated
    public void login(String userName, String password) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    @Deprecated
    public void logout() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateUser(User user) {
        em.merge(user);
    }

    @Override
    public void postKweet(Kweet k) {
        em.persist(k);
    }

    @Override
    public User find(String username) {
        try {
            List<User> users = em.createNamedQuery("User.findByName").setParameter("name", username).getResultList();
            if (users.size() > 0) {
                return users.get(0);
            }
            return null;
        } catch (NoResultException ex) {
            return null;
        }
    }
    
    @Override
    public User find(int userId) {
        return (User) em.createNamedQuery("User.findById").setParameter("id", userId).getSingleResult();
    }
    
    @Override
    public void follow(User u, User follower) {
        follower.follow(u);
        updateUser(follower);
        updateUser(u);
    }

    @Override
    public void create(User u) {
        System.out.println("Inserting user: " + u.getName());
        em.persist(u);
    }

    @Override
    public void remove(User u) {
        em.remove(u);
    }
    
    public void setEm(EntityManager em) {
        this.em = em;
    }
    
}
