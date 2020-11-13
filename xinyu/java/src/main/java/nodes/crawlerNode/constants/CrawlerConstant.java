package nodes.crawlerNode.constants;

import common.io.web.constants.KeyValueConstant;

import java.util.HashMap;
import java.util.Map;

public class CrawlerConstant {

    public static final Map<String, String> DEFAULT_HEADER = new HashMap<>() {
        {
            put(KeyValueConstant.StringAttribute.DEFAULT_USER_AGENT.getKey(), KeyValueConstant.StringAttribute.DEFAULT_USER_AGENT.getValue());
        }
    };

    public static final long DEFAULT_COOL_DOWN_MIN_TIME = 1000;
    public static final long DEFAULT_COOL_DOWN_MAX_TIME = 5000;
    public static final int DEFAULT_COOL_DOWN_INTERVAL = 0;
}