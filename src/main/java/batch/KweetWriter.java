package batch;

import dao.KweetDaoImp;
import dao.UserDaoImp;
import domain.Kweet;
import domain.User;
import java.util.List;
import javax.batch.api.chunk.AbstractItemWriter;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import service.KwetterService;

/**
 *
 * @author edwinlambregts
 */
@Named
public class KweetWriter extends AbstractItemWriter {

    private KwetterService ks;
    private EntityManager em;
    
    @Override
    public void writeItems(List<Object> items) throws Exception {
        List<Kweet> kweets = (List<Kweet>) (List<?>) items;
        setKweetService();
        
        for (Kweet kweet : kweets) {
            if (kweet.getSender() != null) {
                em.getTransaction().begin();
                User user = getUser(kweet.getSender().getName());
                Kweet k = new Kweet();
                k.setSender(user);
                k.setContent(kweet.getContent());
                k.setPostedFrom(kweet.getPostedFrom());
                k.setDate(kweet.getDate());
                ks.postKweet(kweet);
                em.getTransaction().commit();
            }
        }
    }
    
    private User getUser(String username) {
        User user = ks.findUser(username);
        if (user == null) {
            user = new User(username, "", "", "");
            ks.addUser(user);
        }
        return user;
    }
    
    public void setKweetService() {
        ks = new KwetterService();
        EntityManagerFactory emf;
        emf = Persistence.createEntityManagerFactory("KwetterPUBatch");
        em = emf.createEntityManager();
        UserDaoImp udi = new UserDaoImp();
        udi.setEm(em);
        KweetDaoImp kdi = new KweetDaoImp();
        kdi.setEm(em);
        ks.setMockDao(udi, kdi);
    }
    
}
