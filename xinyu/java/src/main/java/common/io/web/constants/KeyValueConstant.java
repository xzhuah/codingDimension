package common.io.web.constants;


/**
 * Created by Xinyu Zhu on 6/29/2020, 7:55 PM
 * common.io.web.constants in AllInOne
 */
public interface KeyValueConstant<T, E> {
    public T getKey();

    public E getValue();

    enum StringAttribute implements KeyValueConstant<String, String> {
        DEFAULT_USER_AGENT("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36");

        private final String key;
        private final String value;

        StringAttribute(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return key + ": " + value;
        }

    }
}
