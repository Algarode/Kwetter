package dao;

import domain.Kweet;
import domain.User;
import javax.ejb.Remote;

/**
 *
 * @author edwinlambregts
 */
//@Remote
public interface UserDao {
    
    void login(String userName, String password);
    
    void logout();
    
    User find(String username);
    
    User find(int userId);
    
    void follow(User u, User follower);
    
    void create(User u);
    
    void remove(User u);
    
    void postKweet(Kweet k);
    
    void updateUser(User u);
    
}
