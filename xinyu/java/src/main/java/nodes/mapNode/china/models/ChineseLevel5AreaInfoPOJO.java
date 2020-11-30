package nodes.mapNode.china.models;

import common.io.database.mongodb.BaseMongoPOJO;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Xinyu Zhu on 2020/11/29, 19:50
 * nodes.mapNode.china.models in codingDimensionTemplate
 * https://github.com/kakuilan/china_area_mysql
 * 国家统计局2019年的行政区域数据(截止2019年10月31日),共78万多记录.
 */
@Data
@NoArgsConstructor
public class ChineseLevel5AreaInfoPOJO extends BaseMongoPOJO {

    // 层级 0-4, 0:省级行政区 34 个,1:地级行政区 371个, 2:县级行政区 3745个, 3: 乡级行政区 122631个, 4: 乡级以下基层群众自治组织 656781
    private int level;

    // 父级行政代码
    private String parentCode;

    // 行政代码: primary key
    private String areaCode;

    // 邮政编码
    private String zipCode;

    // 区号
    private String cityCode;

    // 名称
    private String name;

    // 简称
    private String shortName;

    // 组合名
    private String mergerName;

    // 拼音
    private String pinyin;

    // 经度
    private double lng;

    // 维度
    private double lat;

}
