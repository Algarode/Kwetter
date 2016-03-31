package dao;

import domain.Kweet;
import domain.User;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import util.DatabaseCleaner;

/**
 *
 * @author edwinlambregts
 */
public class KweetDaoImpTest {
    
    private KweetDaoImp kdi;
    private UserDaoImp udi;
    private final String PU = "KwetterPUTest";
    private EntityManager em;
    
    public KweetDaoImpTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        kdi = new KweetDaoImp();
        udi = new UserDaoImp();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PU);
        em = emf.createEntityManager();
        kdi.setEm(em);
        udi.setEm(em);
        DatabaseCleaner dc = new DatabaseCleaner(em);
        dc.clean();
        em.getTransaction().begin();
    }
    
    @After
    public void tearDown() {
        em.getTransaction().commit();
    }
    
    @Test
    public void testfindByContent() {
        System.out.println("find by content");
        String content = "dit";
        User user = new User("Edwin", "Eindhoven", "Software engineering student", "http://www.google.nl");
        udi.create(user);
        Kweet k = new Kweet(user, "dit is een test kweet", "computer", new Date());
        udi.postKweet(k);
        assertEquals(1, kdi.findByContent(content).size());
    }

    @Test
    public void testGetByUserName() throws Exception {
        System.out.println("getByUserName");
        String username = "Edwin";
        User user = new User("Edwin", "Eindhoven", "Software engineering student", "http://www.google.nl");
        udi.create(user);
        user = udi.find("Edwin");
        Kweet k = new Kweet(user, "dit is een test #kweet", "computer", new Date());
        udi.postKweet(k);
        assertEquals(1, kdi.getByUserName(username).size());
    }

    @Test
    public void testGetTrending() throws Exception {
        System.out.println("getTrending");
        User user = new User("Edwin", "Eindhoven", "Software engineering student", "http://www.google.nl");
        udi.create(user);
        user = udi.find("Edwin");
        Kweet k = new Kweet(user, "dit is een test #kweet", "computer", new Date());
        Kweet k2 = new Kweet(user, "#kweet 2", "computer", new Date());
        udi.postKweet(k);
        udi.postKweet(k2);
        kdi.getTrending();
        assertEquals(1, kdi.getTrending().size());
    }
    
}
