package nodes.featureEngineeringNode.facade;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xinyu Zhu on 2020/11/14, 22:51
 * nodes.featureEngineeringNode.facade in codingDimensionTemplate
 */
public interface FeatureGroup<T> {
    void addFeature(List<Feature<T, ?>> features);

    void addFeature(Feature<T, ?> feature);

    List<String> getFeatureNameAsList();
    List<String> getFeatureValueAsString(T target);

    // result feature1, feature2, feature3
    // o1       v11        v12       v13
    // o2       v21        v22       v23
    // o3       v31        v32       v33
    default List<List<String>> getFeatureValueAsString(List<T> targets) {
        List<List<String>> result = new ArrayList<>();
        targets.forEach(target -> result.add(getFeatureValueAsString(target)));
        return result;
    }
}
