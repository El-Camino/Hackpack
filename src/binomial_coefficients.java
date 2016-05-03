/**
 * Everything here was created by Team El Camino
 */

public class binomial_coefficients {

	public static void main(String[] args) {
		int n = 15, k = 7;
		System.out.println("Combinations w/o replacement: " + choose(n, k));
		System.out.println("Combinations w/ replacement: " + choose(n + k - 1, k));
		System.out.println("Strings w/ K 1's & N 0's: " + choose(n + k, k));
		System.out.println("Strings w/ K 1's & N 0's(no adjacent 1's): " + choose(n + 1, k));
		System.out.println("Lattice paths from (0, 0) to (n, k): " + choose(n + k, n));
		
	}

	public static long choose(int n, int k) {
		if (k > n - k)
			k = n - k;
		long b = 1;
		for (int i = 1, m = n; i <= k; i++, m--)
			b = b * m / i;
		return b;
	}
}
