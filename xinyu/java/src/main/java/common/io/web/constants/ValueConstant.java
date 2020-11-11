package common.io.web.constants;

import java.nio.charset.StandardCharsets;

/**
 * Created by Xinyu Zhu on 6/29/2020, 7:30 PM
 * common.io.web.constants in AllInOne
 */
public interface ValueConstant<T> {
    public T getValue();

    enum Timeout implements ValueConstant<Integer> {
        SHORT_CONNECTION_REQUEST_TIMEOUT(3000), DEFAULT_CONNECTION_REQUEST_TIMEOUT(5000), LONG_CONNECTION_REQUEST_TIMEOUT(10000),
        SHORT_CONNECTION_TIMEOUT(3000), DEFAULT_CONNECTION_TIMEOUT(5000), LONG_CONNECTION_TIMEOUT(10000),
        SHORT_SOCKET_TIMEOUT(3000), DEFAULT_SOCKET_TIMEOUT(5000), LONG_SOCKET_TIMEOUT(10000);

        private final Integer time;

        Timeout(Integer milliSeconds) {
            time = milliSeconds;
        }

        public Integer getValue() {
            return time;
        }

        @Override
        public String toString() {
            return time.toString();
        }


    }


    enum ConnectionNumber implements ValueConstant<Integer> {
        MAX_TOTAL_CONNECTION(200), MAX_CONNECTION_PER_ROUTE(20),
        MAX_RETRY(3);
        private final Integer maxAllow;

        ConnectionNumber(Integer maxAllowNumber) {
            maxAllow = maxAllowNumber;
        }

        public Integer getValue() {
            return maxAllow;
        }

        @Override
        public String toString() {
            return maxAllow.toString();
        }
    }

    enum BufferSize implements ValueConstant<Integer> {
        DEFAULT_RECEIVE_BUFFER_SIZE(8192), DEFAULT_SENT_BUFFER_SIZE(8192);
        private final Integer size;

        BufferSize(Integer sizeInByte) {
            this.size = sizeInByte;
        }

        public Integer getValue() {
            return size;
        }

        @Override
        public String toString() {
            return size.toString();
        }
    }

    enum ThreadPoolParam implements ValueConstant<Integer> {
        CORE_POOL_SIZE(16), MAXIMUM_POOL_SIZE(128), KEEP_ALIVE_TIME_SECOND(300), BLOCK_QUEUE_CAPACITY(1024);
        private final Integer parameter;

        ThreadPoolParam(Integer parameter) {
            this.parameter = parameter;
        }

        public Integer getValue() {
            return parameter;
        }

        @Override
        public String toString() {
            return parameter.toString();
        }
    }

    enum Encoding implements ValueConstant<String> {
        UTF_8(StandardCharsets.UTF_8.name());
        private final String encoding;

        Encoding(String encoding) {
            this.encoding = encoding;
        }

        public String getValue() {
            return encoding;
        }

        @Override
        public String toString() {
            return encoding;
        }
    }

    enum FormatTemplate implements ValueConstant<String> {
        HTTPCLIENT_POOL("PoolingAsyncHttpClient-Pool-%d");
        private final String template;

        FormatTemplate(String template) {
            this.template = template;
        }

        public String getValue() {
            return template;
        }

        @Override
        public String toString() {
            return template;
        }
    }

}
