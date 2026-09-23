package com.daa;

import java.util.Arrays;

public class DeterministicSelector {
    public static int maxDepth = 0;

    public static int select(int[] arr, int k) {
        maxDepth = 0;
        if (arr == null || arr.length == 0 || k < 0 || k >= arr.length) return -1;
        return select(arr, 0, arr.length - 1, k, 1);
    }

    private static int select(int[] arr, int low, int high, int k, int depth) {
        if (depth > maxDepth) maxDepth = depth;
        if (low == high) return arr[low];

        int pivotIndex = getPivotIndex(arr, low, high, depth);
        pivotIndex = partition(arr, low, high, pivotIndex);

        if (k == pivotIndex) {
            return arr[k];
        } else if (k < pivotIndex) {
            return select(arr, low, pivotIndex - 1, k, depth + 1);
        } else {
            return select(arr, pivotIndex + 1, high, k, depth + 1);
        }
    }

    private static int getPivotIndex(int[] arr, int low, int high, int depth) {
        if (high - low < 5) {
            return medianOfGroup(arr, low, high);
        }

        int numGroups = (int) Math.ceil((double) (high - low + 1) / 5);
        for (int i = 0; i < numGroups; i++) {
            int subLow = low + i * 5;
            int subHigh = Math.min(subLow + 4, high);
            int medianIdx = medianOfGroup(arr, subLow, subHigh);
            swap(arr, medianIdx, low + i);
        }

        int mid = low + (numGroups - 1) / 2;
        select(arr, low, low + numGroups - 1, mid, depth + 1);
        return mid;
    }

    private static int medianOfGroup(int[] arr, int low, int high) {
        int len = high - low + 1;
        int[] sub = new int[len];
        System.arraycopy(arr, low, sub, 0, len);
        Arrays.sort(sub);
        int medVal = sub[len / 2];
        for (int i = low; i <= high; i++) {
            if (arr[i] == medVal) return i;
        }
        return low;
    }

    private static int partition(int[] arr, int low, int high, int pivotIndex) {
        int pivotValue = arr[pivotIndex];
        swap(arr, pivotIndex, high);
        int storeIndex = low;
        for (int i = low; i < high; i++) {
            if (arr[i] < pivotValue) {
                swap(arr, storeIndex, i);
                storeIndex++;
            }
        }
        swap(arr, storeIndex, high);
        return storeIndex;
    }

    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
}