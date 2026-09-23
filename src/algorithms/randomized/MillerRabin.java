package algorithms.randomized;

/**
 * Miller-Rabin Randomized Primality Test.
 * Evaluates whether a number n is prime or composite using randomized witnesses.
 * Course Outcome 6: Randomized Algorithms.
 * Handcrafted pseudo-random generator without java.util.Random or java.util.*
 * Time Complexity: O(k log^3 n) where k is the number of randomized iterations.
 * Error probability of claiming a composite is prime is strictly bounded by (1/4)^k.
 */
public class MillerRabin {

    // Simple Linear Congruential Generator (LCG) state for deterministic/random sequence without java.util.*
    private static long lcgState = 0x5DEECE66DL;

    private static synchronized long nextRandomLong(long min, long max) {
        lcgState = (lcgState * 0x5DEECE66DL + 0xBL) & ((1L << 48) - 1);
        long range = max - min + 1;
        long val = Math.abs(lcgState) % range;
        return min + val;
    }

    public static class PrimalityResult {
        public long number;
        public boolean isPrime;
        public int iterationsUsed;
        public double errorProbability;
        public String witnessWitnessed;
        public String classification; // "Definitely Prime", "Probably Prime", or "Definitely Composite"

        public PrimalityResult(long number, boolean isPrime, int iterationsUsed,
                               double errorProbability, String witnessWitnessed, String classification) {
            this.number = number;
            this.isPrime = isPrime;
            this.iterationsUsed = iterationsUsed;
            this.errorProbability = errorProbability;
            this.witnessWitnessed = witnessWitnessed;
            this.classification = classification;
        }
    }

    /**
     * Modular exponentiation: (base^exp) % mod
     * Handles large 64-bit integers safely using modular multiplication to avoid overflow.
     */
    public static long powerMod(long base, long exp, long mod) {
        long res = 1;
        base = base % mod;
        while (exp > 0) {
            if ((exp & 1) == 1) {
                res = multiplyMod(res, base, mod);
            }
            base = multiplyMod(base, base, mod);
            exp >>= 1;
        }
        return res;
    }

    /**
     * Modular multiplication (a * b) % mod to prevent 64-bit overflow.
     */
    private static long multiplyMod(long a, long b, long mod) {
        long res = 0;
        a = a % mod;
        while (b > 0) {
            if ((b & 1) == 1) {
                res = (res + a) % mod;
            }
            a = (2 * a) % mod;
            b >>= 1;
        }
        return res;
    }

    /**
     * Performs Miller-Rabin test on n with k random iterations.
     */
    public static PrimalityResult test(long n, int k) {
        if (n <= 1) {
            return new PrimalityResult(n, false, 0, 0.0, "None (n <= 1)", "Definitely Composite");
        }
        if (n <= 3) {
            return new PrimalityResult(n, true, 0, 0.0, "Base case", "Definitely Prime");
        }
        if (n % 2 == 0) {
            return new PrimalityResult(n, false, 1, 0.0, "Divisible by 2", "Definitely Composite");
        }

        // Decompose n - 1 = d * 2^s where d is odd
        long d = n - 1;
        int s = 0;
        while ((d & 1) == 0) {
            d >>= 1;
            s++;
        }

        for (int i = 0; i < k; i++) {
            long a = nextRandomLong(2, n - 2);
            long x = powerMod(a, d, n);

            if (x == 1 || x == n - 1) {
                continue;
            }

            boolean composite = true;
            for (int r = 1; r < s; r++) {
                x = multiplyMod(x, x, n);
                if (x == n - 1) {
                    composite = false;
                    break;
                }
            }

            if (composite) {
                return new PrimalityResult(n, false, i + 1, 0.0, "Witness base a=" + a, "Definitely Composite");
            }
        }

        double errorProb = Math.pow(0.25, k);
        return new PrimalityResult(n, true, k, errorProb, "Passed all " + k + " witness rounds", "Probably Prime");
    }
}
