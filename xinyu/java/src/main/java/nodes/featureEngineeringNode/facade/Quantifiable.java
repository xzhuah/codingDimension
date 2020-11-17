package nodes.featureEngineeringNode.facade;

/**
 * Created by Xinyu Zhu on 2020/11/16, 22:49
 * nodes.featureEngineeringNode.facade in codingDimensionTemplate
 */
public interface Quantifiable extends Comparable<Quantifiable> {
    double doubleValue();

    @Override
    default int compareTo(Quantifiable other) {
        return Double.compare(doubleValue(), other.doubleValue());
    }
}
