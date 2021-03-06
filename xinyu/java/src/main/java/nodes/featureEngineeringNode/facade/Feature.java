package nodes.featureEngineeringNode.facade;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Xinyu Zhu on 2020/11/14, 20:31
 * nodes.datascienceNode.facade in codingDimensionTemplate
 * <p>
 * Feature is not a kind of data type, it is a kind of methodology defined by extractForInstance()
 * When you define a feature, simply saying the feature is a vector does not make any sense, you should give the
 * algorithm to calculate it. Vector is not a feature, is a model, feature is a Mapping from one data model to another
 * data model, in this case, is from T to E
 */
public interface Feature<T, E> {
    E extractForInstance(T target);

    default List<E> extractForInstances(List<T> targets) {
        List<E> result = new ArrayList<>(targets.size());
        targets.forEach(target -> result.add(extractForInstance(target)));
        return result;
    }

    default String getFeatureName() {
        String subClassName = getClass().getSimpleName();
        if (subClassName.length() == 0) {
            return "Feature" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE);
        }
        return subClassName;
    }

    // Only comparable feature can compare with each other and sort
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
