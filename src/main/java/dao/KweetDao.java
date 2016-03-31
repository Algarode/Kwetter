package dao;

import domain.Kweet;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author edwinlambregts
 */
//@Remote
public interface KweetDao {
    
    Kweet find(int id);
    
    List<Kweet> findAllKweets();
    
    List<Kweet> findByContent(String content);
    
    List<Kweet> getByUserName(String username);
    
    List<String> getTrending();
    
}
