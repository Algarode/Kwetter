package batch;

import java.util.Properties;
import javax.batch.operations.JobOperator;
import javax.batch.operations.JobSecurityException;
import javax.batch.operations.JobStartException;
import javax.batch.runtime.BatchRuntime;

/**
 *
 * @author edwinlambregts
 */
public class BatchProcessor {

    private final String url = "/META-INF/04c-JEA-kwetter-input.json";

    public void processBatch() throws JobStartException, JobSecurityException {
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        Properties props = new Properties();
        props.setProperty("jsonUrl", url);
        jobOperator.start("KweetBatch", props);
    }

}
