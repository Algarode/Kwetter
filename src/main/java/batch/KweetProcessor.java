package batch;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import domain.Kweet;
import domain.User;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.batch.api.chunk.ItemProcessor;
import javax.inject.Named;

/**
 *
 * @author edwinlambregts
 */
@Named
public class KweetProcessor implements ItemProcessor {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    @Override
    public Object processItem(Object item) throws Exception {
        String input = (String) item;
        
        if (input.contains("screenName") == false) {
            return new Kweet();
        }
        
        if (input.substring(0, 1).equals(",")) {
            input = input.substring(1);
        }
        
        JsonParser parser = new JsonParser();
        JsonObject obj = (JsonObject) parser.parse(input);
        
        Kweet kweet = new Kweet();
        User user = new User();
        user.setName(obj.get("screenName").getAsString());
        kweet.setSender(user);
        kweet.setContent(obj.get("tweet").getAsString());
        kweet.setPostedFrom(obj.get("postedFrom").getAsString());
        Date postDate = DATE_FORMAT.parse(obj.get("postDate").getAsString());
        kweet.setDate(postDate);
        
        return kweet;
    }
    
}
