package dao;

import domain.Kweet;
import domain.User;
import java.util.Date;
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
public class UserDaoImpTest {
    
    private UserDaoImp udi;
    private KweetDaoImp kdi;
    private final String PU = "KwetterPUTest";
    private EntityManager em;
    
    public UserDaoImpTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        udi = new UserDaoImp();
        kdi = new KweetDaoImp();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PU);
        em = emf.createEntityManager();
        udi.setEm(em);
        kdi.setEm(em);
        DatabaseCleaner dc = new DatabaseCleaner(em);
        dc.clean();
        em.getTransaction().begin();
    }
    
    @After
    public void tearDown() {
        em.getTransaction().commit();
    }

    @Test
    public void testCreate() throws Exception {
        System.out.println("create");
        User u = new User("Edwin", "Eindhoven", "Software engineering student", "http://www.google.nl");
        udi.create(u);
        assertEquals("Eindhoven", udi.find("Edwin").getLocation());
    }
    
    @Test
    public void testFindByName() throws Exception {
        System.out.println("find by name");
        User u = new User("Edwin", "Eindhoven", "Software engineering student", "http://www.google.nl");
        udi.create(u);
        String username = "Edwin";
        User user = udi.find(username);
        assertNotNull(user);
        assertEquals(username, udi.find("Edwin").getName());
    }

    @Test
    public void testUpdateUser() throws Exception {
        System.out.println("updateUser");
        User u = new User("Edwin", "Eindhoven", "Software engineering student", "http://www.google.nl");
        udi.create(u);
        User user = udi.find("Edwin");
        user.setLocation("Utrecht");
        udi.updateUser(user);
        assertEquals("Utrecht", udi.find("Edwin").getLocation());
    }

    @Test
    public void testPostKweet() throws Exception {
        System.out.println("postKweet");
        User u = new User("Edwin", "Eindhoven", "Software engineering student", "http://www.google.nl");
        udi.create(u);
        User user = udi.find("Edwin");
        Kweet k = new Kweet(user, "dit is een test #kweet", "computer", new Date());
        Kweet k2 = new Kweet(user, "#kweet 2", "computer", new Date());
        udi.postKweet(k);
        udi.postKweet(k2);
        int amount = kdi.findAllKweets().size();
        assertEquals(2, amount);
    }
    
    @Test
    public void testAddFollower() {
        System.out.println("add follower");
        User u = new User("Edwin", "Eindhoven", "Software engineering student", "http://www.google.nl");
        udi.create(u);
        User user1 = udi.find("Edwin");
        User user = new User("Kees", "Amsterdam", "CEO Apple Nederland", "http://www.apple.com");
        udi.create(user);
        User user2 = udi.find("Kees");
        udi.follow(user1, user2);
        assertEquals(1, user1.getFollowers().size());
        assertEquals(1, user2.getFollowing().size());
    }
    
}
