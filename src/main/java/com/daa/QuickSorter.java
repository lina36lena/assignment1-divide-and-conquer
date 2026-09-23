package com.daa;

public class QuickSorter {
    public static int maxDepth = 0;

    public static void sort(int[] arr) {
        maxDepth = 0;
        if (arr == null || arr.length <= 1) return;
        sort(arr, 0, arr.length - 1, 1);
    }

    private static void sort(int[] arr, int low, int high, int depth) {
        if (depth > maxDepth) maxDepth = depth;
        if (low >= high) return;

        int p = partition(arr, low, high);
        sort(arr, low, p - 1, depth + 1);
        sort(arr, p + 1, high, depth + 1);
    }

    private static int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (arr[j] <= pivot) {
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, high);
        return i + 1;
    }

    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
}
