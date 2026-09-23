package com.daa;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting experiments...");
        Experiment.runExperiments();
        System.out.println("All experiments finished! CSV written to results/results.csv");
    }
}