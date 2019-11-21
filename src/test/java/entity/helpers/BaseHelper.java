package entity.helpers;

import com.google.common.hash.Hashing;
import entity.builders.RequestBuilder;
import entity.builders.ResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class BaseHelper {
    static final Properties properties = ApplicationManager.properties();
    static final Logger logger = LoggerFactory.getLogger(BaseHelper.class);
    protected ResponseHandler response;
    RequestBuilder request = new RequestBuilder();

    public ResponseHandler response() {
        return response;
    }
}
