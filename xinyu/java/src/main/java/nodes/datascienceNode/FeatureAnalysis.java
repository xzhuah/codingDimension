package nodes.datascienceNode;

import nodes.featureEngineeringNode.facade.ComparableFeature;
import nodes.featureEngineeringNode.facade.Feature;

import java.util.List;

/**
 * Created by Xinyu Zhu on 2020/11/15, 4:44
 * nodes.datascienceNode in codingDimensionTemplate
 */
public interface FeatureAnalysis<T, E> {
    default void setFeature(ComparableFeature<T, ? extends Comparable> primaryCompanyFeature, Feature<T, ?>... otherCompanyFeature) {
        setFeature(primaryCompanyFeature, List.of(otherCompanyFeature));
    }

    void setFeature(ComparableFeature<T, ? extends Comparable> primaryCompanyFeature, List<Feature<T, ?>> otherCompanyFeature);

    void printReportForTarget(List<E> targets, boolean sortByPrimaryFeature);

    default void printReportForTarget(List<E> targets) {
        printReportForTarget(targets, true);
    }

    default void printReportForTarget() {
        printReportForTarget(true);
    }

    void printReportForTarget(boolean sortByPrimaryFeature);
}
