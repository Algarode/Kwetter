package interceptor;

import domain.Kweet;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 *
 * @author edwinlambregts
 */
@Interceptor
public class TweetInterceptor {
    
    public TweetInterceptor() { }
    
    @AroundInvoke
    public Object replace(InvocationContext ic) throws Exception {
        Object[] parameters = ic.getParameters();
        if (parameters.length > 0 && parameters[0] instanceof Kweet) {
            Kweet kweet = (Kweet) parameters[0];
            String content = kweet.getContent();
            content = content.replace("vet", "hard").replace("cool", "dik");
            kweet.setContent(content);
        }
        return ic.proceed();
    }
    
}
