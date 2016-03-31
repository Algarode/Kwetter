package batch;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import javax.batch.api.chunk.AbstractItemReader;
import javax.inject.Named;

/**
 *
 * @author edwinlambregts
 */
@Named
public class KweetReader extends AbstractItemReader {

    private BufferedReader reader;

    @Override
    public Object readItem() throws Exception {
        return reader.readLine();
    }

    @Override
    public void close() throws Exception {
        reader.close();
    }

    @Override
    public void open(Serializable checkpoint) throws Exception {
        reader = new BufferedReader(new InputStreamReader(this.getClass()
                .getClassLoader()
                .getResourceAsStream("/META-INF/04c-JEA-kwetter-input.json")));
    }

}
