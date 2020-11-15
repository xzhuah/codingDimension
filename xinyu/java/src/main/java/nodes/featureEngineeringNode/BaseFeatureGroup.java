package nodes.featureEngineeringNode;

import nodes.featureEngineeringNode.facade.Feature;
import nodes.featureEngineeringNode.facade.FeatureGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Xinyu Zhu on 2020/11/14, 22:53
 * nodes.featureEngineeringNode in codingDimensionTemplate
 */
public class BaseFeatureGroup<T> implements FeatureGroup<T> {
    private List<Feature<T, ?>> allFeatures;

    public BaseFeatureGroup() {
        allFeatures = new ArrayList<>();
    }

    public BaseFeatureGroup(List<Feature<T, ?>> features) {
        allFeatures = new ArrayList<>(features);
    }

    @Override
    public void addFeature(List<Feature<T, ?>> features) {
        allFeatures.addAll(features);
    }

    @Override
    public void addFeature(Feature<T, ?> feature) {
        allFeatures.add(feature);
    }

    @Override
    public List<String> getFeatureNameAsList() {
        return allFeatures.stream().map(Feature::getFeatureName).collect(Collectors.toList());
    }

    @Override
    public List<String> getFeatureValueAsString(T target) {
        List<String> result = new ArrayList<>();
        allFeatures.forEach(feature -> result.add(feature.extractForInstance(target).toString()));
        return result;
    }

}
