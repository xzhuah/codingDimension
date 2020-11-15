package nodes.featureEngineeringNode.facade;

import java.util.Comparator;
import java.util.List;

/**
 * Created by Xinyu Zhu on 2020/11/14, 20:36
 * nodes.datascienceNode.facade in codingDimensionTemplate
 */
public interface ComparableFeature<T, E extends Comparable<E>> extends Feature<T, E>{
    default List<T> sortInstanceWithFeature(List<T> targets) {
        List<T> sortedResult = new java.util.ArrayList<>(targets);
        sortedResult.sort(Comparator.comparing(this::extractForInstance));
        return sortedResult;
    }

    default boolean isComparable() {
        return true;
    }
}
