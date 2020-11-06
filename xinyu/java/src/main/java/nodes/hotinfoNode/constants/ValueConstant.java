package nodes.hotinfoNode.constants;

public interface ValueConstant<T> {
    T getValue();

    enum Path implements ValueConstant<String> {
        DATABASE_NAME("Bilibili_data"), COLLECTION_NAME("Hot_video");

        private final String filepath;

        Path(String filepath) {
            this.filepath = filepath;
        }

        public String getValue() {
            return filepath;
        }

        @Override
        public String toString() {
            return filepath;
        }


    }

    enum CrawlerParameter implements ValueConstant<Integer> {
        // Use relative slow parameter since the website may have anti-crawler mechanism
        MINIMUM_CONCURRENT_REQUEST(1),
        MAXIMUM_CONCURRENT_REQUEST(4),
        MINMUM_WAIT_TIME(3000),
        MAXIMUM_WAIT_TIME(15000)
        ;
        private final Integer value;

        CrawlerParameter(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }

        @Override
        public String toString() {
            return value.toString();
        }
    }
}