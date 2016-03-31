package mockitoTests;

import dao.KweetDaoImp;
import dao.UserDaoImp;
import domain.Kweet;
import domain.User;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import service.KwetterService;

/**
 *
 * @author edwinlambregts
 */
@RunWith(MockitoJUnitRunner.class)
public class ServiceMockitoTest {

    @Mock
    private UserDaoImp udi;
    
    @Mock
    private KweetDaoImp kdi;
    
    private KwetterService ks;

    @Before
    public void setUp() {
        System.out.println("Setup");
        
        ks = new KwetterService();
        ks.setMockDao(udi, kdi);
    }

    @After
    public void validate() {
        //validateMockitoUsage();
    }
    
    @Test
    public void testAddUser() {
        System.out.println("Add user");
        
        User user = new User();
        user.setName("Edwin");
        
        when(udi.find("Edwin")).thenReturn(user);
        
        ks.addUser(user);
        
        verify(udi, times(0)).find("Edwin");
        verify(udi, times(1)).create(user);
    }

    @Test
    public void testFindUser() {
        System.out.println("Find user");
        
        User user = new User();
        user.setName("Edwin");
        
        when(udi.find("Edwin")).thenReturn(user);
        // Call the method to find a user twice
        ks.findUser("Edwin");
        ks.findUser("Edwin");
        // We expect the method to be called twice
        verify(udi, times(2)).find("Edwin");
    }

    @Test
    public void testEditUser() {
        System.out.println("Edit user");
        
        User user = new User();
        user.setName("Edwin");
        
        when(udi.find("Edwin")).thenReturn(user);
        
        user.setBio("hepk ni");
        user.setLocation("thuis");
        user.setWeb("www.google.nl");
        
        ks.editUser(user);
        
        verify(udi, times(0)).find("Edwin");
        verify(udi, times(0)).create(user);
        verify(udi, times(1)).updateUser(user);
    }
    
    @Test
    public void testAddFollower() {
        System.out.println("Follow user");
        
        User user = new User();
        user.setName("Edwin");
        
        User user2 = new User();
        user2.setName("Kees");
        
        when(udi.find("Edwin")).thenReturn(user);
        when(udi.find("Kees")).thenReturn(user2);
        
        ks.followUser(user, user2);
        
        verify(udi, times(1)).follow(user, user2);
    }
    
    @Test
    public void testPostKweet() {
        System.out.println("Post a kweet");
        
        User user = new User();
        user.setName("Edwin");
        
        when(udi.find("Edwin")).thenReturn(user);
        
        Kweet kweet = new Kweet(user, "dit is een test #kweet", "computer", new Date());
        
        ks.postKweet(kweet);
        
        verify(udi, atLeastOnce()).postKweet(kweet);
    }
    
    @Test
    public void testFindKweet() {
        System.out.println("Find a kweet by id");
        
        User user = new User();
        user.setName("Edwin");
        
        Kweet kweet = new Kweet(user, "dit is een test #kweet", "computer", new Date());
        
        when(udi.find("Edwin")).thenReturn(user);
        when(kdi.find(2)).thenReturn(kweet);
        
        ks.postKweet(kweet);
        ks.findKweet(2);
        
        verify(udi, times(0)).find("Edwin");
        verify(kdi, times(1)).find(2);
    }

    @Test
    public void testFindKweetsByUser() {
        System.out.println("Find kweets by username");
        
        when(kdi.getByUserName("Edwin")).thenReturn(null);
        
        ks.findKweetsByUser("Edwin");
        
        verify(kdi, atLeastOnce()).getByUserName("Edwin");
    }
    
    @Test
    @SuppressWarnings("UnusedAssignment")
    public void testGetTrending() {
        System.out.println("Trending topics");
        
        User user = new User();
        user.setName("Edwin");
        
        User user2 = new User();
        user2.setName("Kees");
        
        Kweet k = new Kweet(user, "dit is een test #kweet", "computer", new Date());
        Kweet k2 = new Kweet(user, "#kweet 2", "computer", new Date());
        Kweet k3 = new Kweet(user2, "ik wil een nieuw #trending topic", "android", new Date());
        
        ks.postKweet(k);
        ks.postKweet(k2);
        ks.postKweet(k3);
        
        List<String> trendingTopics = new ArrayList<>();
        
        when(kdi.getTrending()).thenReturn(trendingTopics);
        
        ks.getTrending();
        trendingTopics = ks.getTrending();
        
        verify(kdi, times(2)).getTrending();
    }
    
    @Test
    public void testMention() {
        System.out.println("Mentions in kweets");
        
        User user = new User();
        user.setName("Edwin");
        
        User user2 = new User();
        user2.setName("Kees");
        
        List<Kweet> kweets = new ArrayList<>();
        List<Kweet> mentions;
        
        Kweet kweet = new Kweet(user, "@Kees ontvang jij dit?", "ios", new Date());
        kweets.add(kweet);
        
        when(kdi.findAllKweets()).thenReturn(kweets);
        
        ks.postKweet(kweet);
        mentions = ks.findMentionedUser("Kees");
        
        verify(udi, times(1)).postKweet(kweet);
        Assert.assertEquals(1, mentions.size());
    }

}
