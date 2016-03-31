package domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;

/**
 *
 * @author edwinlambregts
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Kweet.findById", query = "SELECT k FROM Kweet k WHERE k.id = :id"),
    @NamedQuery(name = "Kweet.findAll", query = "SELECT k FROM Kweet k"),
    @NamedQuery(name = "Kweet.findByContent", query = "SELECT k FROM Kweet k WHERE k.content LIKE :content"),
    @NamedQuery(name = "Kweet.getByUsername", query = "SELECT k FROM Kweet k WHERE k.sender.name = :username")
})
public class Kweet implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne()
    private User sender;
    private String content;
    private String postedFrom;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date date;

    public Kweet() { }

    public Kweet(User user, String content, String postedFrom, Date date) {
        this.sender = user;
        this.content = content;
        this.postedFrom = postedFrom;
        this.date = date;
    }
    
    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }
    
    public int getUserId() {
        return sender.getId().intValue();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPostedFrom() {
        return postedFrom;
    }

    public void setPostedFrom(String postedFrom) {
        this.postedFrom = postedFrom;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Kweet)) {
            return false;
        }
        Kweet other = (Kweet) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "domain.Kweet[ id=" + id + " ]";
    }
    
}
