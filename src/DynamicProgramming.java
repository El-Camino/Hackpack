import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

class DynamicProgramming
{

    public static void main( String[] args )
    {
    }

    /*
     * 
     * This must be done beforehand:
     * 
     * float[][] dp = new float[n][(1<<(n+1))]; for (int i = 0; i < dp.length; i++) Arrays.fill(dp[i], -1);
     * 
     * Should also have adjacency matrix with costs.
     * 
     * Source: https://algorithmcafe.wordpress.com/2012/02/07/10496-collecting-beepers
     */
    public static int SolveTSPRec( int start, int bitmask, int[][] dp, int[][] matrix )
    {
        if ( bitmask == ( 1 << dp.length ) - 1 && start == 0 )
            return 0;
        if ( bitmask == ( 1 << dp.length ) - 1 )
            return 100000;
        else if ( dp[start][bitmask] != -1 )
            return dp[start][bitmask];
        else
        {
            int min = Integer.MAX_VALUE;
            for ( int i = 0; i < dp.length; i++ )
            {
                if ( ( bitmask & ( 1 << i ) ) == 0 )
                {
                    min = Math.min(min, matrix[start][i] + SolveTSPRec(i, bitmask | ( 1 << i ), dp, matrix));

                }
            }
            return dp[start][bitmask] = min;
        }
    }

    // Solve the traveling salesman problem.
    // Determine the minimum cost to travel from a start point to an end point,
    // hitting every other point in the graph exactly once.
    public static int SolveTSDP( int[][] graph, int start, int end )
    {
        int n = graph.length;
        int[][] dp = new int[1 << n][n];

        for ( int mask = 1; mask < ( 1 << n ); mask++ )
        {
            for ( int last = 0; last < n; last++ )
            {
                // If the end point is not in the subset, ignore
                // the iteration (this situation is impossible).
                if ( 0 == ( mask & ( 1 << last ) ) )
                {
                    continue;
                }

                // The 'prev' subset represents all vertices in the subset
                // that are visiting before the endpoint.
                int prev = mask - ( 1 << last );

                dp[mask][last] = Integer.MAX_VALUE;

                // Iterate through possible endpoints in the prev subset.
                for ( int possEnd = 0; possEnd < n; possEnd++ )
                {
                    // It's only a possible endpoint if it is in the subset
                    // and it is not the start point.
                    if ( ( possEnd == start ) || ( 0 == ( prev & ( 1 << possEnd ) ) ) )
                    {
                        continue;
                    }

                    int score = dp[prev][possEnd] + TPCost(graph, possEnd, last);
                    dp[mask][last] = Integer.min(dp[mask][last], score);
                }
            }
        }

        return dp[dp.length - 1][end];
    }

    public static int TPCost( int[][] graph, int start, int end )
    {
        return graph[start][end];
    }

    // Determine the fewest number of operations needed to perform a given MCM
    // (Matrix chain multiplication).
    // The begin and end indices are inclusive.
    public static int SolveMCM( int[][] matrices, int[][] dp, int beg, int end )
    {
        // If the result has already been calculated, use the memoized result.
        if ( 0 != dp[beg][end] )
        {
            return dp[beg][end];
        }

        // Base case. There is only one matrix in the range of matrices, so no operations
        // need to be performed.
        if ( beg >= end )
        {
            return 0;
        }

        // Determine cost of first possible split.
        int minCost = SolveMCM(matrices, dp, beg + 1, end) + ( matrices[beg][0] * matrices[beg][1] * matrices[end][1] );

        // Now, iterate through each possible split and find minimum cost.
        for ( int i = beg + 1; i < end; i++ )
        {
            int currentCost = SolveMCM(matrices, dp, beg, i) + SolveMCM(matrices, dp, i + 1, end) + ( matrices[beg][0] * matrices[i][1] * matrices[end][1] );

            minCost = Integer.min(minCost, currentCost);
        }

        dp[beg][end] = minCost;

        return minCost;
    }

    // Determine if there is a subset of the given array that sums to the target.
    public static boolean SolveSubsetSum( int[] set, int t )
    {
        int n = set.length;
        boolean[][] table = new boolean[n + 1][t + 1];

        // When t == 0, a subset of all subsets S_1 to S_i can be found to
        // sum to t (i.e. the empty set).
        for ( int i = 0; i < n + 1; i++ )
        {
            table[i][0] = true;
        }

        // If the subset is empty, then no subset of the subset can be
        // found to sum to a target value greater than 0.
        for ( int i = 1; i < t + 1; i++ )
        {
            table[0][i] = false;
        }

        // We know that if table[i][j] is true, that means that there exists
        // a subset of the set S_1 to S_i that sums to j. Such a subset exists
        // if and only if 1) table[i-1][j] is true or 2) table[i-1][j-S_i] is true.
        // Therefore, populate the table accordingly.
        for ( int i = 1; i < n + 1; i++ )
        {
            for ( int j = 1; j < t + 1; j++ )
            {
                table[i][j] = table[i - 1][j] || ( ( j >= set[i - 1] ) ? ( table[i - 1][j - set[i - 1]] ) : ( false ) );
            }
        }

        return table[n][t];
    }

    public static int FindEditDistance( String x, String y )
    {
        int[][] table = new int[x.length() + 1][y.length() + 1];

        // Fill row 0 and column 0. We know that the edit distance between a string
        // and the empty tring is just the length of the string.
        for ( int i = 1; i <= x.length(); i++ )
        {
            table[i][0] = i;
        }
        for ( int i = 1; i <= y.length(); i++ )
        {
            table[0][i] = i;
        }

        // Iterate through the table and fill it in with the edit distance between larger
        // and larger substrings of the first and second strings. table[i][j] represents
        // the edit distance between the first i characters of x and the first j characters of y.
        for ( int i = 1; i < table.length; i++ )
        {
            for ( int j = 1; j < table[0].length; j++ )
            {
                // Either an insertion, deletion or substitution is performed from a previous (and
                // smaller) set of substrings, which is why answers can be reused.
                int costOfSubstitution = ( x.charAt(i - 1) == y.charAt(j - 1) ) ? 0 : 1;

                table[i][j] = Integer.min(table[i - 1][j] + 1, table[i][j - 1] + 1);
                table[i][j] = Integer.min(table[i][j], ( table[i - 1][j - 1] + costOfSubstitution ));
            }
        }

        return table[x.length()][y.length()];
    }

    // Given a list of item weight, a list of correponsding values for each item,
    // and the maximum weight that can be carried in the knapsack, determine the
    // maximum value of items that can be in the knapsack.
    public static int RunKnapsack( int[] weights, int[] values, int maxWeight, boolean returnSelection )
    {
        // The first row and first column will always be filled with zeroes.
        // Each column represents a possible max weight.
        // Each row n represents a subset of the items (i.e., items 1..n)
        // that can be placed in the knapsack.
        int[][] table = new int[weights.length + 1][maxWeight + 1];
        int[][] selections = new int[weights.length + 1][maxWeight + 1];

        // Iterate through item subsets.
        for ( int i = 1; i < table.length; i++ )
        {
            // Iterate through possible max weights.
            for ( int j = 1; j < table[0].length; j++ )
            {
                // If it is possible for us to include the current item for the
                // current max weight
                selections[i][j] = selections[i - 1][j];
                if ( weights[i - 1] <= j )
                {
                    // Compare the total value including the item with the total value
                    // not including the item. If including the item produces a higher
                    // total value, then do so.
                    int valueWithItem = table[i - 1][j - weights[i - 1]] + values[i - 1];

                    if ( valueWithItem > table[i - 1][j] )
                    {
                        table[i][j] = valueWithItem;
                        selections[i][j] |= ( 1 << ( i - 1 ) );
                    }
                    else
                    {
                        table[i][j] = table[i - 1][j];
                    }
                }
                // Otherwise, do not include the item.
                else
                {
                    table[i][j] = table[i - 1][j];
                }
            }
        }

        if ( returnSelection )
        {
            return selections[weights.length][maxWeight];
        }
        else
        {
            return table[weights.length][maxWeight];
        }
    }

    public static int FindLCSLength( String x, String y )
    {
        int[][] table = new int[x.length() + 1][y.length() + 1];

        // Iterate through the table in the bottom up fashion solving LCS
        // for small substrings of x and y in order to find the length
        // of the LCS for the entire strings.
        for ( int i = 1; i < table.length; i++ )
        {
            for ( int j = 1; j < table[0].length; j++ )
            {
                // If the last character of the prefixes of x and y match,
                // include the character in the LCS length calculation.
                if ( x.charAt(i - 1) == y.charAt(j - 1) )
                {
                    table[i][j] = table[i - 1][j - 1] + 1;
                }
                // Otherwise, take the max of the two LCS's(one that excludes the
                // last character of the x prefix and one that exludes the last
                // character of the y prefix.
                else
                {
                    table[i][j] = Math.max(table[i][j - 1], table[i - 1][j]);
                }
            }
        }

        // Return LCS length for the entire strings.
        return table[x.length()][y.length()];
    }

}
