package util;

import domain.Kweet;
import domain.User;
import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;

/**
 *
 * @author edwinlambregts
 */
public class DatabaseCleaner {
    
    private static final Class<?>[] ENTITY_TYPES = {
       Kweet.class,
       User.class
   };
   private final EntityManager em;

   public DatabaseCleaner(EntityManager entityManager) {
       em = entityManager;
   }

   public void clean() {
       em.getTransaction().begin();

       for (Class<?> entityType : ENTITY_TYPES) {
           deleteEntities(entityType);
       }
       em.getTransaction().commit();
       //em.close();
   }

   private void deleteEntities(Class<?> entityType) {
       em.createQuery("delete from " + getEntityName(entityType)).executeUpdate();
   }

   protected String getEntityName(Class<?> clazz) {
       EntityType et = em.getMetamodel().entity(clazz);
       return et.getName();
   }
    
}
