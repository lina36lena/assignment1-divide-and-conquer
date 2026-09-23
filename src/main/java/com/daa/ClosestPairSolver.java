package com.daa;

import java.util.Arrays;
import java.util.Comparator;

public class ClosestPairSolver {
    public static int maxDepth = 0;

    public static double bruteForce(Point[] points) {
        double minDist = Double.POSITIVE_INFINITY;
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                double d = points[i].distanceTo(points[j]);
                if (d < minDist) minDist = d;
            }
        }
        return minDist;
    }

    public static double findClosest(Point[] points) {
        maxDepth = 0;
        Point[] px = points.clone();
        Point[] py = points.clone();
        Arrays.sort(px, Comparator.comparingDouble(p -> p.x));
        Arrays.sort(py, Comparator.comparingDouble(p -> p.y));
        return closestUtil(px, py, 0, px.length - 1, 1);
    }

    private static double closestUtil(Point[] px, Point[] py, int low, int high, int depth) {
        if (depth > maxDepth) maxDepth = depth;
        int n = high - low + 1;
        if (n <= 3) {
            Point[] sub = Arrays.copyOfRange(px, low, high + 1);
            return bruteForce(sub);
        }

        int mid = low + (high - low) / 2;
        Point midPoint = px[mid];

        Point[] pyl = new Point[mid - low + 1];
        Point[] pyr = new Point[high - mid];
        int li = 0, ri = 0;

        for (Point p : py) {
            if (p.x <= midPoint.x && li < pyl.length) pyl[li++] = p;
            else if (ri < pyr.length) pyr[ri++] = p;
        }

        double dl = closestUtil(px, pyl, low, mid, depth + 1);
        double dr = closestUtil(px, pyr, mid + 1, high, depth + 1);
        double d = Math.min(dl, dr);

        Point[] strip = new Point[n];
        int j = 0;
        for (Point p : py) {
            if (Math.abs(p.x - midPoint.x) < d) strip[j++] = p;
        }

        for (int i = 0; i < j; i++) {
            for (int k = i + 1; k < j && (strip[k].y - strip[i].y) < d; k++) {
                double dist = strip[i].distanceTo(strip[k]);
                if (dist < d) d = dist;
            }
        }

        return d;
    }
}