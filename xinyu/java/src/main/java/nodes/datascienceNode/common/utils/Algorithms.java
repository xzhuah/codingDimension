package nodes.datascienceNode.common.utils;

import com.google.common.math.Stats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static common.utils.ConditionChecker.checkStatus;

/**
 * Created by Xinyu Zhu on 2020/11/20, 2:47
 * nodes.datascienceNode.common.utils in codingDimensionTemplate
 */
public class Algorithms {

    // sliding window avg, [2, 3, 4], windowSize = 2 --> [2.5, 3.5]
    public static List<Double> slidingWindowAvg(List<? extends Number> targetList, int windowSize) {
        checkStatus(windowSize > 0, String.format("Only support positive int as window size, your window size is %s", windowSize));
        checkStatus(windowSize <= targetList.size(), String.format("Your window size %s is larger than array size %s", windowSize, targetList.size()));
        List<Double> result = new ArrayList<>(targetList.size() - windowSize + 1);

        double currentWindowSum = 0;
        for (int i = 0; i < windowSize; i++) {
            currentWindowSum += targetList.get(i).doubleValue();
        }
        result.add(currentWindowSum / windowSize);
        for (int i = windowSize; i < targetList.size(); i++) {
            currentWindowSum -= targetList.get(i - windowSize).doubleValue();
            currentWindowSum += targetList.get(i).doubleValue();
            result.add(currentWindowSum / windowSize);
        }

        return result;
    }

    public static List<Double> listPadding(List<? extends Number> targetList, int sizeToPad, PaddingPolicy paddingPolicy, double paddingValue) {
        Double[] padder = new Double[sizeToPad];
        Arrays.fill(padder, paddingValue);
        List<Double> padderAsDouble = Arrays.asList(padder);
        switch (paddingPolicy) {
            case LEFT:
                for (Number number : targetList) {
                    padderAsDouble.add(number.doubleValue());
                }
                return padderAsDouble;
            case RIGHT:
                List<Double> result = new ArrayList<>(targetList.size() + sizeToPad);
                for (Number number : targetList) {
                    result.add(number.doubleValue());
                }
                result.addAll(padderAsDouble);
                return result;
            case BOTH:
                List<Double> resultForBoth = new ArrayList<>(targetList.size() + sizeToPad * 2);
                resultForBoth.addAll(padderAsDouble);
                for (Number number : targetList) {
                    resultForBoth.add(number.doubleValue());
                }
                resultForBoth.addAll(padderAsDouble);
                return resultForBoth;
            default:
                throw new RuntimeException("Not supported PaddingPolicy");
        }

    }

    public static List<Double> listPadding(List<? extends Number> targetList, int sizeToPad, PaddingPolicy paddingPolicy) {
        checkStatus(targetList.size() > 0, "This method does not support empty list, please provide a value to pad");
        List<Double> result;
        switch (paddingPolicy) {
            case LEFT:
            case RIGHT:
                result = new ArrayList<>(targetList.size() + sizeToPad);
                break;
            case BOTH:
                result = new ArrayList<>(targetList.size() + sizeToPad * 2);
                break;
            default:
                throw new RuntimeException("Not supported PaddingPolicy");
        }

        switch (paddingPolicy) {
            case LEFT:
                Double padderOnLeft = targetList.get(0).doubleValue();
                for (int i = 0; i < sizeToPad; i++) {
                    result.add(padderOnLeft);
                }
                for (Number number : targetList) {
                    result.add(number.doubleValue());
                }
                break;
            case RIGHT:
                Double padderOnRight = targetList.get(targetList.size() - 1).doubleValue();
                for (Number number : targetList) {
                    result.add(number.doubleValue());
                }
                for (int i = 0; i < sizeToPad; i++) {
                    result.add(padderOnRight);
                }
                break;
            case BOTH:
                Double leftPadder = targetList.get(0).doubleValue();
                for (int i = 0; i < sizeToPad; i++) {
                    result.add(leftPadder);
                }
                for (Number number : targetList) {
                    result.add(number.doubleValue());
                }
                Double rightPadder = targetList.get(targetList.size() - 1).doubleValue();
                for (int i = 0; i < sizeToPad; i++) {
                    result.add(rightPadder);
                }
                break;
            default:
                throw new RuntimeException("Not supported PaddingPolicy");
        }
        return result;
    }

    public enum PaddingPolicy {
        LEFT, RIGHT, BOTH
    }

    // normalize the list with this formula: f[n] = (f[n] - min) / (max - min)
    public static List<Double> normalizeList(List<? extends Number> targetList) {
        if (targetList.size() == 0) {
            return new ArrayList<>();
        }
        Stats statistics = Stats.of(targetList);
        double max = statistics.max();
        double min = statistics.min();
        double base = max - min;

        List<Double> result = new ArrayList<>(targetList.size());
        if (min == max) {
            for (int i = 0; i < targetList.size(); i++) {
                result.add(1.0);
            }
            return result;
        } else {
            for (Number number : targetList) {
                result.add((number.doubleValue() - min) / base);
            }
            return result;
        }
    }

    // normalize the list with this formula: f[n] = f[n] / sum
    public static List<Double> normalizeListWithSum(List<? extends Number> targetList) {
        if (targetList.size() == 0) {
            return new ArrayList<>();
        }
        Stats statistics = Stats.of(targetList);
        double sum = statistics.sum();

        List<Double> result = new ArrayList<>(targetList.size());
        for (Number number : targetList) {
            result.add(number.doubleValue() / sum);
        }
        return result;

    }

    public static List<Double> standardizeList(List<? extends Number> targetList) {
        if (targetList.size() == 0) {
            return new ArrayList<>();
        }

        if (targetList.size() == 1) {
            return List.of(0D);
        }
        Stats statistics = Stats.of(targetList);
        double mean = statistics.mean();
        double mu = statistics.sampleStandardDeviation();

        List<Double> result = new ArrayList<>(targetList.size());
        if (mu == 0) {
            result.add(0D);
        } else {
            for (Number number : targetList) {
                result.add((number.doubleValue() - mean) / mu);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(slidingWindowAvg(listPadding(List.of(2, 3, 4), 1, PaddingPolicy.LEFT), 2));
        System.out.println(standardizeList(List.of(2, 3, 6)));
    }
}
