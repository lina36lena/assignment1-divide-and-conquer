# Assignment 1: Divide-and-Conquer Algorithm Analysis

This project implements and compares four divide-and-conquer algorithms in Java: Merge Sort, randomized QuickSort, deterministic selection using Median-of-Medians, and the closest-pair-of-points algorithm.

## Algorithms and analysis

### Merge Sort

The array is recursively split in half, then the sorted halves are merged with a linear merge using an auxiliary buffer. For subarrays of at most 15 elements, the implementation uses insertion sort.

- Recurrence: `T(n) = 2T(n/2) + Θ(n)`
- Time: `Θ(n log n)` in all cases
- Extra space: `O(n)` for the reusable merge buffer, plus `O(log n)` recursion stack

By the Master Theorem, `a = 2`, `b = 2`, and `f(n) = Θ(n)`, so `T(n) = Θ(n log n)`.

### Randomized QuickSort

QuickSort selects a randomized pivot and partitions the array in place. It recursively processes the smaller partition and iterates over the larger one. This limits call-stack growth, including on highly unbalanced partitions.

- Expected time: `Θ(n log n)`
- Worst-case time: `Θ(n²)`
- Auxiliary space: `O(log n)` expected stack use; the smaller-partition recursion strategy bounds stack depth by `O(log n)`
- Worst-case recurrence: `T(n) = T(n − 1) + Θ(n)`

Random pivot selection makes consistently unbalanced partitions unlikely, but does not remove the theoretical quadratic worst case.

### Deterministic Select (Median-of-Medians)

The algorithm divides elements into groups of five, finds each group’s median, and selects the median of those medians as a pivot. It partitions in place and continues only in the partition containing the requested order statistic.

- Recurrence: `T(n) ≤ T(⌈n/5⌉) + T(7n/10 + O(1)) + Θ(n)`
- Worst-case time: `Θ(n)`
- Auxiliary space: `O(log n)` recursion stack

The pivot discards a constant fraction of the elements on each side. Since the recursive subproblem fractions sum to less than one (`1/5 + 7/10 < 1`), the total work is linear.

### Closest Pair of Points

The algorithm sorts points by x-coordinate, splits them into two halves, recursively finds the closest pair in each half, and checks the narrow strip around the dividing line in y-order. Each point needs to be compared with only a constant number of following points in that order.

- Recurrence with linear strip processing: `T(n) = 2T(n/2) + Θ(n)`
- Time: `Θ(n log n)`
- Space: depends on the implementation’s sorting and temporary buffers; see `ClosestPairSolver.java`

The linear strip scan at each recursion level gives `Θ(n log n)` overall. Sorting the strip again at every recursive call would add avoidable work, so the y-order must be maintained or constructed efficiently.

## Project layout

```text
.
├── docs/
│   ├── plots/
│   │   ├── time_vs_n.png
│   │   └── depth_vs_n.png
│   └── screenshots/
├── results/
├── src/
│   ├── main/java/com/daa/
│   │   ├── ClosestPairSolver.java
│   │   ├── DeterministicSelector.java
│   │   ├── Experiment.java
│   │   ├── Main.java
│   │   ├── MergeSorter.java
│   │   ├── Point.java
│   │   └── QuickSorter.java
│   └── test/java/com/daa/
├── build.gradle
├── gradlew
└── gradlew.bat
```

The repository uses Gradle. Run the test suite from the repository root with:

```bash
./gradlew test
```

On Windows:

```powershell
./gradlew.bat test
```

Run the program using the Gradle task or IDE configuration available in the project. The exact main-class invocation depends on the Gradle configuration in `build.gradle`.

## Experimental results

The tables below reproduce the measurements currently reported in this repository. Times are in nanoseconds. Input sizes are `n`; the README currently does not specify the input distribution, number of repetitions, JVM warm-up procedure, or hardware. Treat these as the recorded sample measurements, not as a controlled benchmark or a guarantee of performance.

### Execution time (ns)

| n | Closest Pair | Deterministic Select | Merge Sort | QuickSort |
|---:|---:|---:|---:|---:|
| 10 | 3,136,700 | 1,166,900 | 246,000 | 268,900 |
| 50 | 164,900 | 32,700 | 17,500 | 14,800 |
| 100 | 263,200 | 114,000 | 38,300 | 80,700 |
| 500 | 1,522,300 | 246,600 | 173,800 | 172,600 |
| 1,000 | 1,503,100 | 264,300 | 141,900 | 87,300 |
| 5,000 | 7,495,900 | 913,900 | 817,900 | 664,300 |

### Maximum recursion depth

| n | Closest Pair | Deterministic Select | Merge Sort | QuickSort |
|---:|---:|---:|---:|---:|
| 10 | 3 | 4 | 5 | 7 |
| 50 | 6 | 6 | 7 | 11 |
| 100 | 7 | 7 | 8 | 12 |
| 500 | 9 | 8 | 10 | 19 |
| 1,000 | 10 | 9 | 11 | 22 |
| 5,000 | 12 | 12 | 14 | 33 |

### Plots

- [Execution time vs. input size](docs/plots/time_vs_n.png)
- [Maximum recursion depth vs. input size](docs/plots/depth_vs_n.png)

## Discussion

**Do the results match theoretical complexity?** The larger input sizes broadly show the expected increase for the sorting and closest-pair algorithms, while the selection measurements grow more slowly in this sample. The table is small and the benchmark protocol is undocumented, so it cannot establish asymptotic complexity by itself. The unusually high measurements at `n = 10` are consistent with fixed startup or measurement overhead dominating small workloads.

**How does input structure affect performance?** Merge Sort’s asymptotic running time is insensitive to whether values are sorted, reversed, random, or duplicated. QuickSort can be affected by pivot choices and duplicate handling; randomized pivots reduce the chance of repeated bad splits but do not eliminate the worst case. Add separate measurements for each input type if the experiment generated them.

**Why recurse on the smaller QuickSort partition?** The smaller side has at most half of the current elements, so recursive calls shrink geometrically. Iterating over the larger side bounds the call-stack depth by `O(log n)`, even if the running time for an unlucky pivot sequence is quadratic.

**Why does Median-of-Medians guarantee linear time?** A pivot chosen from medians of groups of five leaves at most about `7n/10` elements in the larger remaining side, apart from a constant number of small-input exceptions. The pivot-selection work and partitioning are linear, yielding a recurrence whose total work is `Θ(n)`.

**Why is divide-and-conquer Closest Pair faster than brute force?** Brute force checks every pair and takes `Θ(n²)` time. Divide-and-conquer solves two half-size problems and checks a linearly processed strip, giving `Θ(n log n)` time.

**What practical factors affect timing?** JVM JIT warm-up, garbage collection, operating-system scheduling, CPU caching, allocation behavior, and timer granularity can all affect short runs. A stronger benchmark should warm up the JVM, repeat each case, report a median or mean, and record the Java version and machine details.

## Correctness tests

Tests are located under `src/test/java/com/daa/`. For the assignment’s full test requirement, the suite should compare both sorting algorithms with `Arrays.sort()` over random, sorted, reverse-sorted, duplicate-heavy, empty, and single-element inputs; compare at least 100 random selection cases with the sorted-array result at index `k`; and compare Closest Pair with a brute-force reference for small point sets (`n ≤ 2,000`).

Add the actual test count or test-run output here after running `./gradlew test`. Do not report passing tests unless the current run confirms them.

## Screenshots and results

The repository includes `docs/screenshots/` for readable screenshots of program output and test results. Keep the submitted screenshots in that directory and link them here once the actual filenames are confirmed. Experimental data should be saved under `results/` in CSV format; include the CSV filename and the input distribution/measurement protocol when those details are available.

## Reflection

Implementing these algorithms demonstrates that asymptotic guarantees and practical speed are different: a deterministic worst-case guarantee can involve more constant overhead than a randomized algorithm on small inputs. Instrumenting recursion depth and runtime also requires care, because measurement and bookkeeping can influence short runs.

The main implementation challenges were managing in-place partitions and edge cases, preserving the closest-pair strip’s ordering, and tracking metrics without changing algorithm behavior. Replace or expand this reflection with your own account of the decisions and difficulties you encountered while implementing the project.

## GitHub workflow

The commit history should represent the work that actually happened. A clear history may include separate commits for project setup, each algorithm, experiment metrics, tests, documentation, and bug fixes. Do not create backdated or misleading commits to imitate a sample storyline.

