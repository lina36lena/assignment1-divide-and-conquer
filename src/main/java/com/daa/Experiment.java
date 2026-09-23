package com.daa;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class Experiment {

    public static void runExperiments() {
        File resultsDir = new File("results");
        if (!resultsDir.exists()) resultsDir.mkdirs();

        try (PrintWriter writer = new PrintWriter(new FileWriter("results/results.csv"))) {
            writer.println("Algorithm,InputType,Size,ExecutionTimeNs,MaxRecursionDepth");

            int[] sizes = {10, 50, 100, 500, 1000, 5000};
            Random rand = new Random(42);

            for (int n : sizes) {
                int[] randomArr = rand.ints(n, -10000, 10000).toArray();

                // MergeSort
                int[] arrCopy = randomArr.clone();
                long start = System.nanoTime();
                MergeSorter.sort(arrCopy);
                long elapsed = System.nanoTime() - start;
                writer.printf("MergeSort,Random,%d,%d,%d\n", n, elapsed, MergeSorter.maxDepth);

                // QuickSort
                arrCopy = randomArr.clone();
                start = System.nanoTime();
                QuickSorter.sort(arrCopy);
                elapsed = System.nanoTime() - start;
                writer.printf("QuickSort,Random,%d,%d,%d\n", n, elapsed, QuickSorter.maxDepth);

                // DeterministicSelect
                arrCopy = randomArr.clone();
                start = System.nanoTime();
                DeterministicSelector.select(arrCopy, n / 2);
                elapsed = System.nanoTime() - start;
                writer.printf("DeterministicSelect,Random,%d,%d,%d\n", n, elapsed, DeterministicSelector.maxDepth);

                // ClosestPair
                Point[] points = new Point[n];
                for (int i = 0; i < n; i++) {
                    points[i] = new Point(rand.nextDouble() * 1000, rand.nextDouble() * 1000);
                }
                start = System.nanoTime();
                ClosestPairSolver.findClosest(points);
                elapsed = System.nanoTime() - start;
                writer.printf("ClosestPair,RandomPoints,%d,%d,%d\n", n, elapsed, ClosestPairSolver.maxDepth);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}