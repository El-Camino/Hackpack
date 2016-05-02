import java.util.Arrays;

/**
 * Created by Team El Camino
 */
public class BruteForce {

    public static void main(String[] argv) {
        combinationTest();
        permutationTest();
    }

    // Used to test combination
    public static void combinationTest() {

        // Dummy data for testing
        int N = 10;
        int R = 2;
        boolean[] used = new boolean[N];

        // Run
        System.out.println("=====Run Combination=====");
        RunCombination(N - 1, R, used);
    }

    // Run Combination algorithm to generate the combinations of R values in N items.
    // Inputs: N - Numbers to choose from, R - Number of numbers to choose, used - a boolean array to represent combinations
    // Nothing is returned.
    public static void RunCombination(int N, int R, boolean[] used) {
        if (R == 0) {
            // Do something
            System.out.println(Arrays.toString(used));
        } else if (N < 0) {
            return;
        } else {
            used[N] = true;
            RunCombination(N - 1, R - 1, used);
            used[N] = false;
            RunCombination(N - 1, R, used);
        }
    }

    // Used to test permutation
    public static void permutationTest() {

        // Dummy data for testing
        int[] values = {1, 2, 3, 4};
        int[] perm = new int[values.length];
        boolean[] used = new boolean[values.length];

        // Run
        System.out.println("=====Run Permutation=====");
        RunPermutation(values.length - 1, values, perm, used);
    }

    // Run Permutation algorithm to generate the permutations of R values in N items.
    // Inputs: N - Numbers to choose from, R - Number of numbers to choose, used - a boolean array to represent permutations
    // Nothing is returned.
    public static void RunPermutation(int index, int[] values, int[] perm, boolean[] used) {
        if (index < 0) {
            // Do something
            System.out.println(Arrays.toString(perm));
        } else {
            for (int i = 0; i < values.length; i++) {
                if (!used[i]) {
                    used[i] = true;
                    perm[index] = i;
                    RunPermutation(index - 1, values, perm, used);
                    used[i] = false;
                }
            }
        }
    }
}
