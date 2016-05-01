public class catalan_numbers {
	// First 10 catalan numbers(0-based): 1, 1, 2, 5, 14, 42, 132, 429, 1430, 4862
	// Catalan numbers overflow longs after n = 34
	/* Combinatorics problems solved using catalan numbers:
	 * Strings with n 0's & n 1's such that no prefix has more 1's than 0's
	 * Expressions containing n pairs of correctly matched parentheses
	 * Ways n + 1 factors can be parenthesized -> n = 2 gives (ab)c & a(bc)
	 * Full binary trees with n + 1 leaves
	 * Lattice paths from (0, 0) to (n, n) that don't pass the diagonal
	 * Ways a convex polygon with n + 2 sides can be cut into triangles by connecting vertices
	 */
	 
	public static void main(String[] args) {
		long[] cat = new long[35];
		cat[0] = cat[1] = 1;
		for (int i = 2; i < cat.length; i++)
			for (int j = 0; j < i; j++)
				cat[i] += cat[j] * cat[i - 1 - j];
	}

}
