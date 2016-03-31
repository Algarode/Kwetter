/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import domain.User;
import javax.persistence.EntityManager;

/**
 *
 * @author edwinlambregts
 */
public abstract class AbstractUserFacade extends AbstractFacade<User> {

    public AbstractUserFacade(Class<User> entityClass) {
        super(entityClass);
    }

    @Override
    protected abstract EntityManager getEntityManager();

    protected abstract User findByUserName(String username);

}
