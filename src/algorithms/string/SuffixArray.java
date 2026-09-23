package algorithms.string;

/**
 * Suffix Array construction and search algorithm.
 * Strict DSA-3: Pure array operations, handcrafted sorting, NO java.util.* used.
 * Time Complexity: O(N log^2 N) construction, O(M log N) pattern search.
 * Space Complexity: O(N) auxiliary.
 */
public class SuffixArray {

    /**
     * Helper structure to store suffix ranking during prefix doubling.
     */
    public static class SuffixRank {
        public int index;
        public int rank1;
        public int rank2;
    }

    /**
     * Builds the suffix array for string s using prefix doubling.
     * @param s Input text
     * @return int array containing suffix indices in lexicographical order
     */
    public static int[] buildSuffixArray(String s) {
        if (s == null || s.length() == 0) return new int[0];
        int n = s.length();

        SuffixRank[] suffixes = new SuffixRank[n];
        for (int i = 0; i < n; i++) {
            suffixes[i] = new SuffixRank();
            suffixes[i].index = i;
            suffixes[i].rank1 = Character.toLowerCase(s.charAt(i));
            suffixes[i].rank2 = (i + 1 < n) ? Character.toLowerCase(s.charAt(i + 1)) : -1;
        }

        // Custom handcrafted mergesort on suffixes by rank1, then rank2
        mergeSort(suffixes, 0, n - 1);

        int[] ind = new int[n];
        for (int k = 4; k < 2 * n; k *= 2) {
            int rank = 0;
            int prevRank = suffixes[0].rank1;
            suffixes[0].rank1 = rank;
            ind[suffixes[0].index] = 0;

            for (int i = 1; i < n; i++) {
                if (suffixes[i].rank1 == prevRank && suffixes[i].rank2 == suffixes[i - 1].rank2) {
                    suffixes[i].rank1 = rank;
                } else {
                    prevRank = suffixes[i].rank1;
                    suffixes[i].rank1 = ++rank;
                }
                ind[suffixes[i].index] = i;
            }

            for (int i = 0; i < n; i++) {
                int nextIndex = suffixes[i].index + k / 2;
                suffixes[i].rank2 = (nextIndex < n) ? suffixes[ind[nextIndex]].rank1 : -1;
            }

            mergeSort(suffixes, 0, n - 1);
        }

        int[] sa = new int[n];
        for (int i = 0; i < n; i++) {
            sa[i] = suffixes[i].index;
        }
        return sa;
    }

    /**
     * Handcrafted recursive merge sort for SuffixRank objects without java.util.*
     */
    private static void mergeSort(SuffixRank[] arr, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSort(arr, left, mid);
            mergeSort(arr, mid + 1, right);
            merge(arr, left, mid, right);
        }
    }

    private static void merge(SuffixRank[] arr, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        SuffixRank[] L = new SuffixRank[n1];
        SuffixRank[] R = new SuffixRank[n2];

        for (int i = 0; i < n1; i++) L[i] = arr[left + i];
        for (int j = 0; j < n2; j++) R[j] = arr[mid + 1 + j];

        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (compare(L[i], R[j]) <= 0) {
                arr[k++] = L[i++];
            } else {
                arr[k++] = R[j++];
            }
        }
        while (i < n1) arr[k++] = L[i++];
        while (j < n2) arr[k++] = R[j++];
    }

    private static int compare(SuffixRank a, SuffixRank b) {
        if (a.rank1 != b.rank1) return a.rank1 - b.rank1;
        return a.rank2 - b.rank2;
    }

    /**
     * Binary searches for pattern occurrences using the Suffix Array.
     * @return int array containing indices where pattern occurs
     */
    public static int[] search(String text, String pattern, int[] sa) {
        if (text == null || pattern == null || pattern.length() == 0 || sa == null || sa.length == 0) {
            return new int[0];
        }

        int n = text.length();
        int m = pattern.length();

        // Find lower bound
        int low = 0, high = n - 1;
        int first = -1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            int suffixStart = sa[mid];
            int cmp = comparePrefix(text, suffixStart, pattern);
            if (cmp >= 0) {
                if (cmp == 0) first = mid;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }

        if (first == -1) return new int[0];

        // Find upper bound
        low = 0;
        high = n - 1;
        int last = -1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            int suffixStart = sa[mid];
            int cmp = comparePrefix(text, suffixStart, pattern);
            if (cmp <= 0) {
                if (cmp == 0) last = mid;
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        int count = last - first + 1;
        int[] matches = new int[count];
        for (int i = 0; i < count; i++) {
            matches[i] = sa[first + i];
        }
        return matches;
    }

    private static int comparePrefix(String text, int start, String pattern) {
        int n = text.length();
        int m = pattern.length();
        for (int i = 0; i < m; i++) {
            if (start + i >= n) return -1;
            char tc = Character.toLowerCase(text.charAt(start + i));
            char pc = Character.toLowerCase(pattern.charAt(i));
            if (tc != pc) return tc - pc;
        }
        return 0;
    }
}
