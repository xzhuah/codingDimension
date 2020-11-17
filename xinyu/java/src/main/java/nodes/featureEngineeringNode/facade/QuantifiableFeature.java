package nodes.featureEngineeringNode.facade;

import java.util.List;

/**
 * Created by Xinyu Zhu on 2020/11/16, 22:50
 * nodes.featureEngineeringNode.facade in codingDimensionTemplate
 *
 * QuantifiableFeature is at higher level of ComparableFeature, it just provides a easy way to help you manage your
 * model class, most of the time you can and should use ComparableFeature instead, using this QuantifiableFeature
 * simple means you want to use it with some Complex but Quantifiable Models. If you model is just Number, you can
 * Just use ComparableFeature
 */
public interface QuantifiableFeature<T, E extends Quantifiable> extends ComparableFeature<T, Quantifiable> {

    default List<T> sortInstanceWithFeature(List<T> targets, boolean reverse) {
        List<T> sortedResult = new java.util.ArrayList<>(targets);
        sortedResult.sort((target1, target2) -> {
            if (!reverse) {
                return Double.compare(extractForInstance(target1).doubleValue(), extractForInstance(target2).doubleValue());
            } else {
                return Double.compare(extractForInstance(target2).doubleValue(), extractForInstance(target1).doubleValue());
            }
        });
        return sortedResult;
    }

    default boolean isComparable() {
        return true;
    }
}
