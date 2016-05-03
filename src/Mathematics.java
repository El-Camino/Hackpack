import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Team El Camino
 */

class Term
{
    public long base;
    public int exponent;
    
    public Term ( long b, int e )
    {
        base = b;
        exponent = e;
    }
}

public class Mathematics
{

    public static void main( String[] args )
    {
    }
    
    // Determine the GCD (greatest common denominator) between two numbers.
    public static int FindGCD ( int a, int b )
    {
        return (0 == b) ? (a) : (FindGCD(b, a % b));
    }
    
    // Determine LCM (least common multiple) of two numbers.
    public static int FindLCM( int a, int b )
    {
        return (a * b) / FindGCD(a, b);
    }
    
    public static ArrayList<Integer> CreateNumListFromSieve( boolean[] sieve )
    {
        // Iterate through sieve and add all primes to list.
        ArrayList<Integer> primes = new ArrayList<Integer>();        
        for ( int i = 0; i < sieve.length; i++ )
        {
            if ( sieve[i] )
            {
                primes.add(i);
            }
        }
        
        return primes;
    }
    
    // Create a Sieve of Eratosthenes (limit defines last number to be analyzed)
    public static boolean[] CreateSieve ( int limit )
    {
        boolean[] sieve = new boolean[limit + 1];
        
        // At the beginning, assume every number is a prime.
        Arrays.fill(sieve, true);
        sieve[0] = false;
        sieve[1] = false;
        
        for ( int i = 2; (i * i) <= sieve.length; i++ )
        {
            if ( sieve[i] )
            {
                int currIndex = 2 * i;
                while ( currIndex < sieve.length )
                {
                    sieve[currIndex] = false;
                    currIndex += i;
                }
            }
        }
        
        return sieve;
    }
    
    
    // Find the prime factorization of a number.
    public static ArrayList<Term> FindPrimeFactorization( long num )
    {
        ArrayList<Term> terms = new ArrayList<Term>();        

        long currNum = num;
        for ( long i = 2; i * i <= num; i++ )
        {          
            int exponent = 0;
            while ( 0 == currNum % i )
            {
                currNum /= i;
                exponent++;
            }
            
            if ( exponent > 0 )
            {
                terms.add(new Term(i, exponent));
            }
        }
        
        if ( currNum > 1 )
        {
            terms.add(new Term(currNum, 1));
        }        
        
        return terms;
    }

    
    public static int FindNumDivisors( int num )
    {
        ArrayList<Term> terms = FindPrimeFactorization(num);
        
        int numDivisors = 1;
        for ( Term term : terms )
        {
            numDivisors *= term.exponent + 1;
        }
        
        return numDivisors;
    }
    
    public static long FindSumOfDivisors( long num )
    {
        ArrayList<Term> terms = FindPrimeFactorization(num);
        
        BigInteger sum = BigInteger.ONE;
        for ( Term term : terms )
        {
            BigInteger expression = BigInteger.valueOf(term.base).pow(term.exponent + 1).subtract(BigInteger.ONE).divide(BigInteger.valueOf(term.base - 1));
            sum = sum.multiply(expression);
        }
        
        return sum.longValue();
    }
    
    public static int FindMaxContSumsequenceSum( int[] sequence )
    {
        int maxSum = 0;        
        int currSum = 0;
        for ( int i = 0; i < sequence.length; i++ )
        {
            currSum += sequence[i];
            if ( currSum < 0 )
            {
                currSum = 0;
            }
            else if ( currSum > maxSum )
            {
                maxSum = currSum;
            }
        }
        
        return maxSum;
    }
}
