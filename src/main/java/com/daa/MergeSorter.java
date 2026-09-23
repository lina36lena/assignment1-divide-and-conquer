package com.daa;

public class MergeSorter {
    public static int maxDepth = 0;

    public static void sort(int[] arr) {
        maxDepth = 0;
        if (arr == null || arr.length <= 1) return;
        int[] aux = new int[arr.length];
        sort(arr, aux, 0, arr.length - 1, 1);
    }

    private static void sort(int[] arr, int[] aux, int low, int high, int depth) {
        if (depth > maxDepth) maxDepth = depth;
        if (low >= high) return;

        int mid = low + (high - low) / 2;
        sort(arr, aux, low, mid, depth + 1);
        sort(arr, aux, mid + 1, high, depth + 1);
        merge(arr, aux, low, mid, high);
    }

    private static void merge(int[] arr, int[] aux, int low, int mid, int high) {
        System.arraycopy(arr, low, aux, low, high - low + 1);
        int i = low, j = mid + 1;
        for (int k = low; k <= high; k++) {
            if (i > mid) arr[k] = aux[j++];
            else if (j > high) arr[k] = aux[i++];
            else if (aux[j] < aux[i]) arr[k] = aux[j++];
            else arr[k] = aux[i++];
        }
    }
}