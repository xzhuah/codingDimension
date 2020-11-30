package nodes.featureEngineeringNode;

import com.google.common.collect.Sets;
import nodes.featureEngineeringNode.facade.Feature;

import java.util.*;

import static com.google.common.collect.Maps.newHashMapWithExpectedSize;

/**
 * Created by Xinyu Zhu on 2020/11/14, 20:48
 * nodes.datascienceNode in codingDimensionTemplate
 * <p>
 * This class is just for fun, provides some crazy but workable examples
 * A better usage of features would be to use them seprately.
 */
@Deprecated
public class BaseFeatureExtractor<T> {
    private final Map<String, Feature<T, ?>> allFeatures;

    public BaseFeatureExtractor() {
        allFeatures = new HashMap<>();
    }

    public BaseFeatureExtractor(List<Feature<T, ?>> features) {
        allFeatures = newHashMapWithExpectedSize(features.size());
        features.forEach(feature -> allFeatures.put(feature.getFeatureName(), feature));
    }

    public void addFeature(Feature<T, ?> newFeature) {
        allFeatures.put(newFeature.getFeatureName(), newFeature);
    }

    public Feature<T, ?> getFeature(String featureName) {
        return allFeatures.get(featureName);
    }

    public Map<String, ?> extractFeatureForInstance(T target) {
        return extractFeatureForInstance(target, null);
    }

    public Map<String, ?> extractFeatureForInstance(T target, Set<String> featureNames) {
        featureNames = filterFeatureNames(featureNames);

        Map<String, Object> result = new HashMap<>();
        for (String featureName : featureNames) {
            result.put(featureName, allFeatures.get(featureName).extractForInstance(target));
        }
        return result;
    }

    public List<Map<String, ?>> extractListOfFeatureMapForInstances(List<T> targets) {
        return extractListOfFeatureMapForInstances(targets, null);
    }

    public Map<String, List<?>> extractMapToFeatureListForInstances(List<T> targets) {
        return extractMapToFeatureListForInstances(targets, null);
    }

    public List<Map<String, ?>> extractListOfFeatureMapForInstances(List<T> targets, Set<String> featureNames) {
        featureNames = filterFeatureNames(featureNames);

        List<Map<String, ?>> result = new ArrayList<>();
        for (T target : targets) {
            Map<String, Object> resultForTarget = new HashMap<>();
            for (String featureName : featureNames) {
                resultForTarget.put(featureName, allFeatures.get(featureName).extractForInstance(target));
            }
            result.add(resultForTarget);
        }
        return result;
    }

    public Map<String, List<?>> extractMapToFeatureListForInstances(List<T> targets, Set<String> featureNames) {
        featureNames = filterFeatureNames(featureNames);
        Map<String, List<?>> result = new HashMap<>();
        for (String featureName : featureNames) {
            result.put(featureName, allFeatures.get(featureName).extractForInstances(targets));
        }
        return result;
    }

    private Set<String> filterFeatureNames(Set<String> featureNames) {
        if (null == featureNames) {
            return allFeatures.keySet();
        } else {
            return Sets.intersection(featureNames, allFeatures.keySet());
        }
    }

}
