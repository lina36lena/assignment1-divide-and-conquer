package com.daa;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class AlgorithmTests {

    @Test
    public void testMergeSort() {
        int[] arr = {5, 2, 8, 1, 9, 3};
        int[] expected = {1, 2, 3, 5, 8, 9};
        MergeSorter.sort(arr);
        assertArrayEquals(expected, arr);
    }

    @Test
    public void testQuickSort() {
        int[] arr = {5, 2, 8, 1, 9, 3};
        int[] expected = {1, 2, 3, 5, 8, 9};
        QuickSorter.sort(arr);
        assertArrayEquals(expected, arr);
    }

    @Test
    public void testDeterministicSelect() {
        Random r = new Random();
        for (int i = 0; i < 100; i++) {
            int n = 50;
            int[] arr = r.ints(n, 0, 1000).toArray();
            int k = r.nextInt(n);

            int[] sorted = arr.clone();
            Arrays.sort(sorted);
            int expected = sorted[k];

            int actual = DeterministicSelector.select(arr.clone(), k);
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testClosestPairVsBruteForce() {
        Random r = new Random();
        Point[] points = new Point[100];
        for (int i = 0; i < 100; i++) {
            points[i] = new Point(r.nextDouble() * 100, r.nextDouble() * 100);
        }

        double expected = ClosestPairSolver.bruteForce(points);
        double actual = ClosestPairSolver.findClosest(points);
        assertEquals(expected, actual, 1e-5);
    }
}