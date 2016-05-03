# Hackpack
Problem Solving and Team Dynamics Hackpack

## Table of Contents

### Mathematics class
#### `static java.util.ArrayList<java.lang.Integer> CreateNumListFromSieve(boolean[] sieve)`
#### `static boolean[] CreateSieve(int limit)`
#### `static int FindGCD(int a, int b)`
#### `static int FindLCM(int a, int b)`
#### `static int FindMaxContSumsequenceSum(int[] sequence)`
#### `static int FindNumDivisors(int num)`
#### `static java.util.ArrayList<Term> FindPrimeFactorization(long num)`
#### `static long FindSumOfDivisors(long num)`

### GraphTheory class
#### `static void RunBellmanFord(int[][] matrix, int start)`
#### `static void RunBFSAdjacencyList(Vertex rootVertex)`
#### `static void RunDFSAdjacencyList(Vertex rootVertex)`
#### `static int[] RunDijkstrasWithoutQueue(int[][] graph, int source)`
#### `static int[] RunDijkstrasWithQueue(int[][] graph, int source)`
#### `static int[][] RunFloyds(int[][] graph)`
#### `static int RunPrims(java.util.ArrayList[] graph, int v)`
#### `static java.util.ArrayList<Vertex> RunTopoSort(Vertex[] graph)`

### DynamicProgramming class
#### `public static int SolveTSPRec( int start, int bitmask, int[][] dp, int[][] matrix )`
#### `public static int SolveTSDP( int[][] graph, int start, int end )`
#### `public static int SolveMCM( int[][] matrices, int[][] dp, int beg, int end )`
#### `public static boolean SolveSubsetSum( int[] set, int t )`
#### `public static int FindEditDistance( String x, String y )`
#### `public static int RunKnapsack( int[] weights, int[] values, int maxWeight, boolean returnSelection )`
#### `public static int FindLCSLength( String x, String y )`

### Geometry class
#### Classes
##### Vector class
##### Line class
##### Plane class
#### Functions
##### `public static boolean IsPointInPoly( Vec point, ArrayList<Vec> poly )`
##### `public static ArrayList<Vec> DetermineConvexHull( ArrayList<Vec> points, Vec start, boolean skipCollinear )`
##### `public static int FindDeterminant(int a[][], int n)`


### BruteForce class
#### `static void RunCombination(int N, int R, boolean[] used)`
#### `static void RunPermutation(int index, int[] values, int[] perm, boolean[] used)`

### Misc additional classes
#### Binary Search
#### Binomial Coefficients
#### Catalan Numbers
#### Edmonds Karp Max Flow
#### Kruskals MST
#### Network Flow
