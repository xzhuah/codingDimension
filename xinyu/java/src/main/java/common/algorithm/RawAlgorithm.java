package common.algorithm;

import java.util.*;

/**
 * Created by Xinyu Zhu on 2020/11/18, 0:12
 * common.algorithm in codingDimensionTemplate
 * <p>
 * There are so many algorithm, not all of the have use cases in this project, but I would like to collect
 * some algorithm here so that when I encounter some use cases, I can realize that there is an available algorithm here
 * With those algorithm, I can also think of use cases.
 * <p>
 * Algorithm with use case will be move to another classes
 */
public class RawAlgorithm {

    // find the index of two element in nums such that their sum equal to target
    public int[] twoSum(int[] nums, int target) {
        HashMap<Integer, Integer> map = new HashMap<>(nums.length);
        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(target - nums[i])) {
                return new int[]{i, map.get(target - nums[i])};
            }
            map.put(nums[i], i);
        }
        return new int[]{-1, -1};
    }

    // 判断回文数
    public boolean isPalindrome(int x) {
        char str[] = String.valueOf(x).toCharArray();
        for (int i = 0; i < str.length / 2; i++) {
            if (str[i] != str[str.length - i - 1]) {
                return false;
            }
        }
        return true;
    }

    private static Map<Character, Integer> romanToIntMap = Map.of(
            'I', 1,
            'V', 5,
            'X', 10,
            'L', 50,
            'C', 100,
            'D', 500,
            'M', 1000);


    // Roman to Integer
    // "MCMXCIV"
    public static int romanToInt(String s) {
        char[] intString = s.toCharArray();
        int lastValue = 0;
        int resultValue = 0;
        for (char romanChar : intString) {
            int romanInt = romanToIntMap.get(romanChar);
            if (romanInt <= lastValue) {
                resultValue += romanInt;
            } else {
                resultValue += romanInt - lastValue - lastValue;
            }
            println(romanInt, resultValue, lastValue);
            lastValue = romanInt;
        }
        return resultValue;
    }

    // 最长公共前缀
    public static String longestCommonPrefix(String[] strs) {
        int minLength = Integer.MAX_VALUE;
        for (String str : strs) {
            if (str.length() < minLength) {
                minLength = str.length();
            }
        }
        StringBuilder prefix = new StringBuilder();
        for (int i = 0; i < minLength; i++) {
            for (int j = 1; j < strs.length; j++) {
                if (strs[j].length() < i + 1 || strs[j].charAt(i) != strs[0].charAt(i)) {
                    return prefix.toString();
                }
            }
            prefix.append(strs[0].charAt(i));
        }
        return prefix.toString();
    }

    // 括号字符串是否合法
    public static boolean isValid(String s) {
        LinkedList<Character> stack = new LinkedList<>();
        for (char c : s.toCharArray()) {
            if (c == '(' || c == '[' || c == '{') {
                stack.push(c);
            } else {
                if (stack.isEmpty()) {
                    return false;
                }
                char previous = stack.poll();
                if (c == ')') {
                    if (previous != '(') {
                        return false;
                    }
                } else if (c == ']') {
                    if (previous != '[') {
                        return false;
                    }
                } else if (c == '}') {
                    if (previous != '{') {
                        return false;
                    }
                }
            }
        }
        return stack.isEmpty();
    }

    public static class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }

        public static void print(ListNode head) {
            if (head == null) {
                System.out.println();
            }
            StringBuilder result = new StringBuilder();
            while (head != null) {
                result.append(head.val).append(",");
                head = head.next;
            }
            System.out.println(result.substring(0, result.length() - 1));

        }

        public static ListNode fromString(String string) {
            String[] split = string.split(",");
            if (split.length == 0) {
                return null;
            }
            ListNode head = new ListNode(Integer.valueOf(split[0]));
            ListNode result = head;
            for (int i = 1; i < split.length; i++) {
                head.next = new ListNode(Integer.valueOf(split[i]));
                head = head.next;
            }
            return result;
        }
    }

    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        ListNode head;
        if (list1 == null) {
            return list2;
        } else if (list2 == null) {
            return list1;
        }

        if (list1.val > list2.val) {
            head = new ListNode(list2.val);
            list2 = list2.next;
        } else {
            head = new ListNode(list1.val);
            list1 = list1.next;
        }
        ListNode result = head;


        while (list1 != null && list2 != null) {
            if (list1.val > list2.val) {
                head.next = new ListNode(list2.val);
                list2 = list2.next;
            } else {
                head.next = new ListNode(list1.val);
                list1 = list1.next;
            }
            head = head.next;
        }
        if (list1 == null) {
            head.next = list2;
        } else {
            head.next = list1;
        }
        return result;
    }

    // 找出现n/2次以上的整数
    public static int findMajorElement(List<Integer> arr) {
        if (arr.size() == 0) {
            return -1;
        }
        int curr = arr.get(0);
        int currCount = 1;
        for (int i : arr) {
            if (currCount == 0) {
                curr = i;
                currCount = 1;
                continue;
            } else {
                if (i == curr) {
                    currCount += 1;

                    // optional branch
                    if (currCount > arr.size() / 2) {
                        return curr;
                    }
                } else {
                    currCount -= 1;
                }
            }
        }
        if (currCount == 0) {
            return -1;
        } else {
            int maxCount = 0;
            for (int i : arr) {
                if (i == curr) {
                    maxCount += 1;
                }
            }
            if (maxCount > arr.size() / 2) {
                return curr;
            } else {
                return -1;
            }
        }
    }

    // Use KMP algorithm to find index of (first appearance of toFind in findFrom)
    public static int kmpIndexOf(String findFrom, String toFind) {
        // return findFrom.indexOf(toFind);
        return kmpIndexOf(findFrom.toCharArray(), toFind.toCharArray());

    }

    private static int kmpIndexOf(char[] findFrom, char[] toFind) {
        if (toFind.length > findFrom.length) {
            return -1;
        }
        if (toFind.length == 0) {
            return 0;
        }
        int[] jumpTable = kmpJumpTable(toFind);
        int curr = 0;
        int offset = 0;
        while (true) {
            if (curr >= toFind.length) {
                return offset;
            }
            if (offset >= findFrom.length) {
                return -1;
            }
            if (findFrom[curr + offset] == toFind[curr]) {
                curr += 1;
            } else {
                offset += curr - jumpTable[curr] + 1;
                curr = jumpTable[curr];
            }
        }
    }

    public int[] buildArrayFromPermutation(int[] nums) {
        int[] newArray = new int[nums.length];
        for (int i = 0; i < newArray.length; i++) {
            newArray[i] = nums[nums[i]];
        }
        return newArray;
    }

    public int[] getConcatenation(int[] nums) {
        int[] result = Arrays.copyOf(nums, nums.length + nums.length);
        System.arraycopy(nums, 0, result, nums.length, nums.length);
        return result;
    }

    // https://leetcode.com/problems/count-number-of-pairs-with-absolute-difference-k/
    public int countKDifference(int[] nums, int k) {
        int result = 0;
        Map<Integer, Integer> numCount = new HashMap<>();
        numCount.clear();
        for (int num : nums) {
            int needToFind1 = num - k;
            int needToFind2 = num + k;
            result += numCount.getOrDefault(needToFind1, 0) + numCount.getOrDefault(needToFind2, 0);
            numCount.put(num, numCount.getOrDefault(num, 0) + 1);
        }
        return result;
    }

    // 80%
    public int countGoodTriplets(int[] arr, int a, int b, int c) {
        int result = 0;
        for (int i = 0; i < arr.length - 2; i++) {
            for (int j = i + 1; j < arr.length - 1; j++) {
                if (Math.abs(arr[i] - arr[j]) > a) {
                    continue;
                }
                for (int z = j + 1; z < arr.length; z++) {
                    if (Math.abs(arr[i] - arr[z]) > c || Math.abs(arr[j] - arr[z]) > b) {
                        continue;
                    }
                    result += 1;
                }
            }
        }
        return result;
    }

    public int wateringPlants(int[] plants, int capacity) {
        int waterRemaining = capacity;
        int result = 0;
        for (int i = 0; i < plants.length; i++) {
            if (waterRemaining < plants[i]) {
                waterRemaining = capacity - plants[i];
                result += (i + 1) * 2 - 1;
            } else {
                result += 1;
                waterRemaining -= plants[i];
            }
        }
        return result;
    }

    public int minPairSum(int[] nums) {
        Arrays.sort(nums);
        int result = 0;
        for (int i = 0; i < nums.length / 2; i++) {
            int pairSum = nums[i] + nums[nums.length - i - 1];
            if (pairSum > result) {
                result = pairSum;
            }
        }
        return result;
    }

    // Array 80%
    public int maxProductDifference(int[] nums) {
        int max1 = Integer.MIN_VALUE + 1;
        int max2 = Integer.MIN_VALUE;
        int min1 = Integer.MAX_VALUE - 1;
        int min2 = Integer.MAX_VALUE;

        for (int i = 0; i < nums.length; i++) {
            if (nums[i] > max1) {
                max2 = max1;
                max1 = nums[i];

            } else if (nums[i] > max2) {
                max2 = nums[i];
            }

            if (nums[i] < min1) {
                min2 = min1;
                min1 = nums[i];
            } else if (nums[i] < min2) {
                min2 = nums[i];
            }
        }
        return max1 * max2 - min1 * min2;
    }

    public String truncateSentence(String s, int k) {
        int currentSpace = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ' ') {
                currentSpace += 1;
                if (currentSpace == k) {
                    return s.substring(0, i);
                }
            }
        }
        return s;
    }

    private static String[] morseMap = new String[]{".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..", ".---", "-.-", ".-..", "--", "-.", "---", ".--.", "--.-", ".-.", "...", "-", "..-", "...-", ".--", "-..-", "-.--", "--.."};

    public int uniqueMorseRepresentations(String[] words) {
        Set<String> uniqueString = new HashSet<>();
        for (String word : words) {
            uniqueString.add(toMorse(word));
        }
        return uniqueString.size();
    }

    public String toMorse(String word) {
        StringBuilder builder = new StringBuilder();
        for (char c : word.toCharArray()) {
            builder.append(morseMap[c - 'a']);
        }
        return builder.toString();
    }

    // Array 70%
    public int maxDistance(int[] colors) {
        int start = 0;
        int end = colors.length - 1;
        if (colors[start] != colors[end]) {
            return end - start;
        } else {
            while (colors[0] == colors[start] && colors[0] == colors[end]) {
                start += 1;
                end -= 1;
            }
            if (colors[0] != colors[start]) {
                return colors.length - start - 1;
            } else {
                return end;
            }
        }
    }

    public int[] nextGreaterElement(int[] nums1, int[] nums2) {
        Map<Integer, Integer> nextGreateMap = new HashMap<>();
        LinkedList<Integer> stack = new LinkedList<>();
        for (int num : nums2) {
            if (stack.isEmpty()) {
                stack.push(num);
            } else {
                while (!stack.isEmpty() && stack.peek() < num) {
                    nextGreateMap.put(stack.poll(), num);
                }
                stack.push(num);
            }
        }
        int[] result = new int[nums1.length];
        for (int i = 0; i < nums1.length; i++) {
            result[i] = nextGreateMap.getOrDefault(nums1[i], -1);

        }
        return result;
    }

    private static class Coordinate {
        int x;
        int y;

        Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Coordinate that = (Coordinate) o;
            return x == that.x &&
                    y == that.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        public String toString() {
            return x + "," + y;
        }
    }

    public int[][] allCellsDistOrder(int rows, int cols, int rCenter, int cCenter) {
        int[][] result = new int[rows * cols][2];
        Set<Coordinate> visited = new HashSet<>();
        LinkedList<Coordinate> queue = new LinkedList<>();
        Coordinate first = new Coordinate(rCenter, cCenter);
        queue.push(first);
        visited.add(first);
        int i = 0;

        while (!queue.isEmpty()) {
            Coordinate next = queue.pollLast();
            System.out.println(i + ": " +  next);
            result[i] = new int[]{next.x, next.y};
            i += 1;
            Coordinate up = new Coordinate(next.x, next.y - 1);
            Coordinate down = new Coordinate(next.x, next.y + 1);
            Coordinate left = new Coordinate(next.x - 1, next.y);
            Coordinate right = new Coordinate(next.x + 1, next.y);
            if (isValidCoordinate(up, visited, rows, cols)) {
                queue.push(up);
                visited.add(up);
            }
            if (isValidCoordinate(down, visited, rows, cols)) {
                queue.push(down);
                visited.add(down);
            }
            if (isValidCoordinate(left, visited, rows, cols)) {
                queue.push(left);
                visited.add(left);
            }
            if (isValidCoordinate(right, visited, rows, cols)) {
                queue.push(right);
                visited.add(right);
            }
        }
        return result;
    }

    private boolean isValidCoordinate(Coordinate coordinate, Set<Coordinate> visited, int rows, int cols) {
        if (visited.contains(coordinate)) {
            return false;
        }
        return coordinate.x < rows && coordinate.y < cols && coordinate.x >= 0 && coordinate.y >= 0;
    }

    // Array 60%

    private static int[] kmpJumpTable(char[] toFind) {
        int[] jumpTable = new int[toFind.length];
        jumpTable[0] = 0;
        if (toFind.length < 2) {
            return jumpTable;
        }
        jumpTable[1] = 0;
        return jumpTable;
    }

    private static void println(int... xs) {
        StringBuilder builder = new StringBuilder();
        for (int x : xs) {
            builder.append(x).append(" ");
        }
        System.out.println(builder.substring(0, builder.length() - 1));
    }

    private static void println(boolean... xs) {
        StringBuilder builder = new StringBuilder();
        for (boolean x : xs) {
            builder.append(x).append(" ");
        }
        System.out.println(builder.substring(0, builder.length() - 1));
    }

    private static void println(double... xs) {
        StringBuilder builder = new StringBuilder();
        for (double x : xs) {
            builder.append(x).append(" ");
        }
        System.out.println(builder.substring(0, builder.length() - 1));
    }


    private static void printBoard(char[][] input) {
        for (char[] line : input) {
            for (char c : line) {
                System.out.print(c);
            }
            System.out.println();
        }
        System.out.println("===============================");
    }

    private static void printBoard(int[][] input) {
        for (int[] line : input) {
            for (int c : line) {
                System.out.print(c);
            }
            System.out.println();
        }
        System.out.println("===============================");
    }

    private static void print(Object obj) {
        System.out.println(obj);
    }

    private static void print(int obj) {
        System.out.println(obj);
    }

    private static void print(double obj) {
        System.out.println(obj);
    }

    private static void print(long obj) {
        System.out.println(obj);
    }

    private static void print(short obj) {
        System.out.println(obj);
    }

    private static void print(byte obj) {
        System.out.println(obj);
    }

    private static void print(boolean obj) {
        System.out.println(obj);
    }

    private static void print(float obj) {
        System.out.println(obj);
    }


    public static void main(String[] args) {
        RawAlgorithm rawAlgorithm = new RawAlgorithm();

        printBoard(rawAlgorithm.allCellsDistOrder(2, 3, 1, 2));


    }
}
