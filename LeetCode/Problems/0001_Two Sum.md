# 0001. Two Sum 문제풀이

<https://leetcode.com/problems/two-sum/description/>

다음의 내용은 `LeetCode 0001. Two Sum` 문제를 풀고 개인적으로 정리한 내용입니다. 잘못된 부분들이 있을 수 도 있으니 제대로된 학습을 위해서는 LeetCode 사이트에서 제공하는 솔루션을 참고하시길 권장해 드립니다.

---

## Description

Given an array of integers, return **indices** of the two numbers such that they add up to a specific target.

You may assume that each input would have **exactly** one solution, and you may not use the same element twice.

**Example:**

```txt
Given nums = [2, 7, 11, 15], target = 9,

Because nums[0] + nums[1] = 2 + 7 = 9,
return [0, 1].
```

## Solution

```java
# Approach 1 - 시간복잡도 O(N²)

class Solution {

    public int[] twoSum(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) {
                    return new int[] { i, j };
                }
            }
        }
        throw new IllegalArgumentException("No two sum solution");
    }
}
```

```java
# Approach 2 - 시간복잡도 O(N)

class Solution {
    
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        
        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(target - nums[i])) {
                return new int[] { map.get(target - nums[i]), i };
            }
            map.put(nums[i], i);
        }
        
        throw new IllegalArgumentException("No two sum solution");
    }
}
```