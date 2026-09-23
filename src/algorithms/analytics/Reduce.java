package algorithms.analytics;

/**
 * Reduce / Fold Algorithm for aggregate analytics.
 * Evaluates summary statistics (Total Attractions, Average Daily Budget, Total Distance, Count by Category).
 * Strict DSA-3: Pure array operations, NO java.util.* used.
 * Time Complexity: O(N), Space Complexity: O(1) auxiliary.
 */
public class Reduce {

    public interface IntReducer {
        int reduce(int accumulator, int current);
    }

    public interface DoubleReducer {
        double reduce(double accumulator, double current);
    }

    public static int sum(int[] values) {
        if (values == null || values.length == 0) return 0;
        int total = 0;
        for (int i = 0; i < values.length; i++) {
            total += values[i];
        }
        return total;
    }

    public static double sum(double[] values) {
        if (values == null || values.length == 0) return 0.0;
        double total = 0.0;
        for (int i = 0; i < values.length; i++) {
            total += values[i];
        }
        return total;
    }

    public static int min(int[] values) {
        if (values == null || values.length == 0) return 0;
        int minVal = values[0];
        for (int i = 1; i < values.length; i++) {
            if (values[i] < minVal) minVal = values[i];
        }
        return minVal;
    }

    public static int max(int[] values) {
        if (values == null || values.length == 0) return 0;
        int maxVal = values[0];
        for (int i = 1; i < values.length; i++) {
            if (values[i] > maxVal) maxVal = values[i];
        }
        return maxVal;
    }

    public static double average(int[] values) {
        if (values == null || values.length == 0) return 0.0;
        return (double) sum(values) / values.length;
    }

    public static int fold(int[] values, int initialValue, IntReducer reducer) {
        if (values == null) return initialValue;
        int acc = initialValue;
        for (int i = 0; i < values.length; i++) {
            acc = reducer.reduce(acc, values[i]);
        }
        return acc;
    }
}
