package common.algorithm;

import java.util.HashMap;

/**
 * Created by Xinyu Zhu on 2020/11/18, 0:12
 * common.algorithm in codingDimensionTemplate
 *
 * There are so many algorithm, not all of the have use cases in this project, but I would like to collect
 * some algorithm here so that when I encounter some use cases, I can realize that there is an available algorithm here
 *  With those algorithm, I can also think of use cases.
 *
 * Algorithm with use case will be move to another classes
 */
public class RawAlgorithm {

    // find the index of two element in nums such that their sum equal to target
    public int[] twoSum(int[] nums, int target) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0;i<nums.length;i++){
            if (map.containsKey(target - nums[i])){
                return new int[]{i, map.get(target- nums[i])};
            }
            map.put(nums[i],i);
        }
        return new int[]{-1,-1};
    }

    public static void main(String[] args) {

    }
}
