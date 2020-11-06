package nodes.hotinfoNode.constants;


/**
 * Created by Xinyu Zhu on 6/29/2020, 7:55 PM
 */
public interface KeyValueConstant<T, E> {
    public T getKey();

    public E getValue();

    enum TypeCode implements KeyValueConstant<String, String> {
        QUAN_ZHAN("全站", "all"),
        FANG_JU("番剧", "bangumi"),
        GUO_CHAN_DONG_HUA("国产动画", "guochan"),
        GUO_CHUANG_XIANG_GUAN("国创相关", "guochuang"),
        JI_LU_PIAN("纪录片", "documentary"),
        DONG_HUA("动画", "douga"),
        YIN_YUE("音乐", "music"),

        WU_DAO("舞蹈", "dance"),

        YOU_XI("游戏", "game"),
        ZHI_SHI("知识", "technology"),
        SHU_MA("数码", "digital"),
        SHENG_HUO("生活", "life"),

        MEI_SHI("美食", "food"),

        GUI_CHU("鬼畜", "kichiku"),

        SHI_SHANG("时尚", "fashion"),
        YU_LE("娱乐", "ent"),
        YING_SHI("影视", "cinephile"),


        DIAN_YING("电影", "movie"),
        DIAN_SHI_JU("电视剧", "tv"),

        YUAN_CHUANG("原创", "origin"),
        XIN_REN("新人", "rookie");

        private final String type;
        private final String code;


        TypeCode(String type, String code) {
            this.type = type;
            this.code = code;

        }

        public String getType() {
            return type;
        }

        public String getCode() {
            return code;
        }

        @Override
        public String toString() {
            return type + ": " + code;
        }

        @Override
        public String getKey() {
            return type;
        }

        @Override
        public String getValue() {
            return code;
        }
    }
}
