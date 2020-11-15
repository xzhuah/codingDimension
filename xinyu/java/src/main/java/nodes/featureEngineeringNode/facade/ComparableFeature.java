package nodes.featureEngineeringNode.facade;

import java.util.List;

/**
 * Created by Xinyu Zhu on 2020/11/14, 20:36
 * nodes.datascienceNode.facade in codingDimensionTemplate
 */
public interface ComparableFeature<T, E extends Comparable<E>> extends Feature<T, E>{
    default List<T> sortInstanceWithFeature(List<T> targets, boolean reverse) {
        List<T> sortedResult = new java.util.ArrayList<>(targets);
        sortedResult.sort((target1, target2) -> {
            if (!reverse) {
                return extractForInstance(target1).compareTo(extractForInstance(target2));
            } else {
                return extractForInstance(target2).compareTo(extractForInstance(target1));
            }
        });
        return sortedResult;
    }
    default boolean isComparable() {
        return true;
    }
}
