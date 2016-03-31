package dao;

import domain.Kweet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author edwinlambregts
 */
@Stateless
public class KweetDaoImp implements KweetDao {
    
    @PersistenceContext(unitName = "KwetterPU")
    private EntityManager em;
    
    @Override
    public Kweet find(int id) {
        return (Kweet) em.createNamedQuery("Kweet.findById")
                .setParameter("id", id)
                .getSingleResult();
    }
    
    @Override
    public List<Kweet> findAllKweets() {
        return (List<Kweet>) em.createNamedQuery("Kweet.findAll")
                .getResultList();
    }
    
    @Override
    public List<Kweet> findByContent(String content) {
        content = content.toLowerCase();
        return (List<Kweet>) em.createNamedQuery("Kweet.findByContent")
                .setParameter("content", "%" + content + "%")
                .getResultList();
    }

    @Override
    public List<Kweet> getByUserName(String username) {
        return (List<Kweet>) em.createNamedQuery("Kweet.getByUsername")
                .setParameter("username", username)
                .getResultList();
    }

    @Override
    public List<String> getTrending() {
        List<String> trending = new ArrayList<>();
        List<String> hashtags;
        hashtags = new ArrayList<>();
        
        List<Kweet> kweets = findAllKweets();
        
        kweets.stream().forEach((kweet) -> {
            for (String word : kweet.getContent().split(" ")) {
                if (word.startsWith("#")) {
                    hashtags.add(word);
                }
            }
        });
        
        Map<String, Long> occurrences;
        occurrences = hashtags.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));
        
        occurrences.entrySet().stream().filter((item) -> (item.getValue() >= 2)).forEach((item) -> {
            trending.add(item.getKey());
        });
        
        return trending;
    }
    
    public void setEm(EntityManager em) {
        this.em = em;
    }
    
}
