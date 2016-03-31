package dao;

import domain.User;
import dao.util.JsfUtil;
import dao.util.PaginationHelper;
import domain.Kweet;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import service.KwetterService;

@Named("userController")
@SessionScoped
public class UserController implements Serializable {

    private User current;
    private DataModel items = null;
    @EJB
    private UserFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private ContentType contentType;
    @Inject
    private KwetterService ks;
    
    private String searchFor;

    public UserController() {
    }

    public User getSelected() {
        if (current == null) {
            User user = new User("Edwin", "Software engineering", "hepk ni", "www.google.nl");
            ks.addUser(user);
            current = ks.findUser("Edwin");
            selectedItemIndex = -1;
        }
        return current;
    }

    private UserFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public void prepareView() {
        current = ks.findUser("Edwin");
        if (current == null) {
            current = getSelected();
        }
        //return "home";
    }

    public String prepareCreate() {
        current = new User();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UserCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (User) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UserUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (User) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UserDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public void createUser() {
        ks.addUser(current);
    }
    
    public User getUser(int id) {
        return ks.findUser(id);
    }
    
    public List<Kweet> getMyKweets() {
        return ks.findKweetsByUser(getSelected().getName());
    }
    
    public int getKweetCount() {
        return getMyKweets().size();
    }
    
    public int getFollowerCount() {
        return current.getFollowers().size();
    }
    
    public int getFollowingCount() {
        return current.getFollowing().size();
    }
    
    public int getMentionCount() {
        return ks.findMentionedUser(getSelected().getName()).size();
    }
    
    public void postKweet(String content) {
        Kweet kweet = new Kweet(current, content, "Computer", new Date());
        ks.postKweet(kweet);
    }
    
    public void setSearchFor() {
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        this.searchFor = request.getParameter("searchForm:searchContent");
        this.setContentType(ContentType.Search);
    }
    
    public void setContentType(ContentType value) {
        contentType = value;
    }

    public enum ContentType {
        Kweet,
        Followers,
        Following,
        Mentions,
        Timeline,
        Search
    }
    
    public String getContent() {
        String result = "<table class='table'>";
        
        if (contentType == null) {
            contentType = ContentType.Kweet;
        }
        
        switch (contentType) {
            case Kweet:
                List<Kweet> kweets = getMyKweets();
                
                if (kweets.isEmpty()) {
                    result += "Geen Kweets gevonden.";
                }
                
                for (Kweet k : kweets) {
                    result += "<tr><td>" + k.getContent() + "</td></tr>";
                }
                
                break;
            case Followers:
                List<User> followers = current.getFollowers();
                
                if (followers.isEmpty()) {
                    result += "Geen Followers gevonden.";
                }
                
                for (User user : followers) {
                    result += "<tr><td>" + user.getName() + "</td></tr>";
                }
                
                break;
            case Following:
                List<User> following = current.getFollowing();
                
                if (following.isEmpty()) {
                    result += "Geen Following gevonden.";
                }
                
                for (User user : following) {
                    result += "<tr><td>" + user.getName() + "</td></tr>";
                }
                
                break;
            case Mentions:
                List<Kweet> mentions = ks.findMentionedUser(getSelected().getName());
                
                if (mentions.isEmpty()) {
                    result += "Geen mentions gevonden.";
                }
                
                for (Kweet k : mentions) {
                    result += "<tr><td>" + k.getContent() + "</td></tr>";
                }
                
                break;
            case Timeline:
                List<Kweet> timeline = ks.getTimeline(getSelected().getName());
                
                if (timeline.isEmpty()) {
                    result += "Timeline bevat geen Kweets.";
                }
                
                for (Kweet k : timeline) {
                    result += "<tr><td>" + k.getContent() + "</td></tr>";
                }
                
                break;
            case Search:
                List<Kweet> kweet = ks.findKweetByContent(searchFor);
                
                if (kweet.isEmpty()) {
                    result += "Geen zoekresultaten gevonden voor de term '"+ searchFor +"'.";
                }
                
                for (Kweet k : kweet) {
                    result += "<tr><td>" + k.getContent() + "</td></tr>";
                }
                
                break;
        }
        
        result += "</table>";
        
        return result;
    }

}
