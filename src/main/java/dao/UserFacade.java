/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import domain.Kweet;
import domain.User;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author edwinlambregts
 */
@Stateless
public class UserFacade extends AbstractUserFacade {
    @PersistenceContext(unitName = "KwetterPU")
    private EntityManager em;
    
    @EJB
    private UserDao udi;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserFacade() {
        super(User.class);
    }
    
    public void createUser(User u) {
        udi.create(u);
    }

    @Override
    public User find(Object id) {
        List<User> users = em.createNamedQuery("User.findAll").getResultList();
        if (users.size() > 0) {
            return users.get(0);
        }
        return null;
    }

    @Override
    public User findByUserName(String username) {
        List<User> users = em.createNamedQuery("User.findByName").setParameter("name", username).getResultList();
        if (users.size() > 0) {
            return users.get(0);
        }
        return null;
    }
    
    public List<Kweet> findKweets(String username) {
        return em.createNamedQuery("Kweet.getByUsername").setParameter("username", username).getResultList();
    }
    
    public void postKweet(Kweet k) {
        em.persist(k);
    }
    
}
