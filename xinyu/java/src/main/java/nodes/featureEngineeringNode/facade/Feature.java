package nodes.featureEngineeringNode.facade;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Xinyu Zhu on 2020/11/14, 20:31
 * nodes.datascienceNode.facade in codingDimensionTemplate
 */
public interface Feature<T, E> {
    E extractForInstance(T target);

    default List<E> extractForInstances(List<T> targets) {
        List<E> result = new ArrayList<>();
        targets.forEach(target -> result.add(extractForInstance(target)));
        return result;
    }

    default String getFeatureName() {
        String subClassName = getClass().getSimpleName();
        if (subClassName.length() == 0) {
            return "Feature" +  ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE);
        }
        return subClassName;
    }

    default boolean isComparable() {
        return false;
    }

    default List<T> sortInstanceWithFeature(List<T> targets) {
        return sortInstanceWithFeature(targets, false);
    }

    default List<T> sortInstanceWithFeature(List<T> targets, boolean reverse) {
        return new ArrayList<>(targets);
    }
}
