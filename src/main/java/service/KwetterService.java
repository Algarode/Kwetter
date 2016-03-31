package service;

import dao.KweetDao;
import dao.UserDao;
import domain.Kweet;
import domain.User;
import interceptor.TweetInterceptor;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

/**
 *
 * @author edwinlambregts
 */
@Stateless
public class KwetterService {
    
    @Inject
    private UserDao udi;
    @Inject
    private KweetDao kdi;
    
    public void setMockDao(UserDao ud, KweetDao kd) {
        udi = ud;
        kdi = kd;
    }
    
    /**
     * Used to find a specific User
     * @param username
     * @return 
     */
    public User findUser(String username) {
        return udi.find(username);
    }
    
    /**
     * Used to find a specific user
     * @param userId
     * @return 
     */
    public User findUser(int userId) {
        return udi.find(userId);
    }
    
    /**
     * Used to add a new user
     * @param u 
     */
    public void addUser(User u) {
        udi.create(u);
    }
    
    /**
     * Used to edit a User
     * @param u 
     */
    public void editUser(User u) {
        udi.updateUser(u);
    }
    
    /**
     * Used to follow a user
     * @param me
     * @param follower
     */
    public void followUser(User me, User follower) {
        udi.follow(me, follower);
    }
    
    /**
     * Used to get a list of users that are following a specific user
     * @param user
     * @return 
     */
    public List<User> getFollowers(User user) {
        return user.getFollowers();
    }
    
    /**
     * Used to get a list of users that a specific user is following
     * @param user
     * @return 
     */
    public List<User> getFollowing(User user) {
        return user.getFollowing();
    }
    
    /**
     * Used to post a new kweet
     * @param kweet 
     */
    @Interceptors(TweetInterceptor.class)
    public void postKweet(Kweet kweet) {
        udi.postKweet(kweet);
    }
    
    /**
     * Used to find a single kweet
     * @param id
     * @return 
     */
    public Kweet findKweet(int id) {
        return kdi.find(id);
    }
    
    /**
     * Get a list of kweets based on a string of content
     * @param content
     * @return 
     */
    public List<Kweet> findKweetByContent(String content) {
        return kdi.findByContent(content);
    }
    
    /**
     * Used to get all Kweets sent by a user
     * @param username
     * @return 
     */
    public List<Kweet> findKweetsByUser(String username) {
        return kdi.getByUserName(username);
    }
    
    /**
     * Used to get trending topics
     * Scheduled to run each hour because it's quite resource intensive
     * @return 
     */
    //@Schedule(hour = "*")
    public List<String> getTrending() {
        return kdi.getTrending();
    }
    
    /**
     * Used to get the timeline of a specific user
     * @param username
     * @return List of all Kweets of the user and the people he is following
     */
    public List<Kweet> getTimeline(String username) {
        List<Kweet> myKweets = this.findKweetsByUser(username);
        List<Kweet> otherKweets = new ArrayList<>();
        List<Kweet> timeline = new ArrayList<>();
        User user = this.findUser(username);
        List<User> following = this.getFollowing(user);
        
        following.stream().forEach((u) -> {
            otherKweets.addAll(this.findKweetsByUser(u.getName()));
        });
        
        timeline.addAll(myKweets);
        timeline.addAll(otherKweets);
        
        return timeline;
    }
    
    /**
     * Used to find Kweets in which a specific user is mentioned
     * @param username
     * @return 
     */
    public List<Kweet> findMentionedUser(String username) {
        username = "@" + username.toLowerCase();
        List<Kweet> mentions = new ArrayList<>();
        for (Kweet kweet : kdi.findAllKweets()) {
            if (kweet.getContent().toLowerCase().contains(username)) {
                mentions.add(kweet);
            }
        }
        return mentions;
    }
    
}
