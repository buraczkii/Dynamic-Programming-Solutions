import java.util.Arrays;
import java.util.HashMap;

public class AllSolutions {
    /**
     * Solution for Problem1.
     *
     * Given a string, determine the length of the longest palindrome contained within the string.
     * @param str the string to analyze
     */
    public static void longestPalindrome(char[] str) {
        int n = str.length;
        // Base case 0: if length is 0 or 1, that is also the length of the longest palindrome
        if (n == 0 || n == 1) {
            System.out.println("Your longest palindrome has a length of: " + n);
            System.out.println();
            return;
        }
        // Result matrix of length * length
        boolean[][] R = new boolean[n][n];
        // variable to keep track of max length, start index, end index
        int max = 0;
        int start = 0;
        int end = 0;
        // Base case 1: for indices i to i, answer is true (single character is palindrome)
        for (int i = 0; i < n; i++) {
            R[i][i] = true;
        }
        // Base case 2: for indices i to i+1, answer is true if characters are same
        for (int i = 0; i < n - 1; i++) {
            R[i][i + 1] = (str[i] == str[i + 1]);
            if (R[i][i + 1]) {
                max = 2;
                start = i;
                end = i + 1;
            }
        }
        // diff represents starting index of string - ending index. starts at 3 and ends at n-1
        for (int diff = 2; diff < n; diff++) {
            for (int row = 0; row < n - diff; row++) {
                // column or ending index is row or starting index + diff
                int col = row + diff;
                // true if first and last are equal and inner string is a palindrome
                R[row][col] = (str[row] == str[col]) && R[row + 1][col - 1];
                // if true, then update max
                if (R[row][col]) {
                    max = diff + 1;
                    start = row;
                    end = col;
                }
            }
        }
        System.out.println("The length of your longest palindrome is: " + max);
        String orig = new String(str);
        System.out.println("Your longest palindrome is: " + orig.substring(start, end + 1));
    }

////>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>/////

    /**
     * Solution for Problem2.
     *
     * Given two strings, determine the length of the longest common substring. Longest common substring is
     * defined as characters in a consecutive sequence appearing in both strings.
     * @param m the first string
     * @param n the second string
     */
    public static void longestConsecutiveSubstring(char[] m, char[] n) {
        // for optimization of space, find shorter string
        char[] shorter = m;
        char[] longer = n;
        if (m.length > n.length) {
            shorter = n;
            longer = m;
        }
        int lcs_max = 0;
        int[] prev = new int[shorter.length + 1];
        int[] current = new int[shorter.length + 1];
        // for all the rows which correspond to characters of the longer string
        for (int i = 1; i <= longer.length; i++) {
            // for each slot in the existing array corresponding to the characters of the shorter string
            for (int j = 1; j <= shorter.length; j++) {
                // if the last characters in the string match, 1 + whatever the value is for the prev chars in both str
                if (longer[i - 1] == shorter[j - 1]) {
                    current[j] = 1 + prev[j - 1];
                    // update lcs as needed
                    if (current[j] > lcs_max) {
                        lcs_max = current[j];
                    }
                }
            }
            // fill prev with current values and reset current to all 0s for the next iteration
            System.arraycopy(current, 0, prev, 0, prev.length);
            Arrays.fill(current, 0);
        }
        System.out.println("the longest common subsequence has a length of: " + lcs_max);
    }

////>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>/////

    // Global map for problem 3
    public static HashMap<Indices, Integer> solutions = new HashMap();

    /**
     * Solution for Problem3.
     *
     * String cutting operation. Given the start and end indices of a string (first input should be 0 and n) and an
     * array representing the locations of cuts to make on the string, return the minimumCost of making these cuts
     * on the string. Cost is generally the length of the string you are making the cut on. Minimum Cost is calculated
     * by an optimal ordering of cuts to minimize costs.
     * @param start the starting index of the string
     * @param end   the ending index of the string
     * @param m     the array of cuts to make on the string
     * @return the minimum cost of making all these cuts on the string
     */
    public static int stringCut(int start, int end, int[] m) {
        if (solutions.containsKey(new Indices(start, end, m))) {
            return solutions.get(new Indices(start, end, m));
        }
        int minCost = Integer.MAX_VALUE;
        // length of the substring we're dealing with
        int strLen = end - start;
        if (m.length == 0) {
            // if no cuts, no cost
            return 0;
        } else if (m.length == 1) {
            //if only one cut, then the cost is the length of the string
            return strLen;
        }
        int tempCost;
        // for each of the cuts, if it was the first cut for this string range, find the minimum resulting cost
        for (int k = 0; k < m.length; k++) {
            tempCost = strLen +
                    stringCut(start, m[k], Arrays.copyOfRange(m, 0, k)) +
                    stringCut(m[k], end, Arrays.copyOfRange(m, k + 1, m.length));
            //if the tempCost is less than current minCost, update minCost
            if (tempCost < minCost) {
                minCost = tempCost;
            }
        }
        solutions.put(new Indices(start, end, m), minCost);
        return minCost;
    }

////>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>/////

    /**
     * Solution for Problem4.
     *
     * Given the number of coins, the number of heads, and the probability of getting heads for each coin,
     * determine the probability of getting exactly k heads as a result of flipping n coins.
     * @param coins number of coins to flip
     * @param heads number of heads you want
     * @param prob  array of probability of head for ith coin
     */
    public static void countingHeads(int coins, int heads, double[] prob) {
        // Base case 0: if num coins < num heads, return 0
        if (coins < heads) {
            System.out.printf("Probability of %s heads occurring given %s coins is 0", heads, coins);
            System.out.println();
            return;
        }
        double[][] C = new double[coins + 1][heads + 1];
        // Base case 1: if no coins and no heads, prob = 1
        C[0][0] = 1;
        // Base case 2: if # heads = 0, prob = 1-prob[i] * C[i-1][0]
        for (int l = 1; l <= coins; l++) {
            C[l][0] = (1 - prob[l - 1]) * C[l - 1][0];
        }
        // All other cases
        for (int n = 0; n <= coins; n++) {
            for (int k = 1; k <= heads; k++) {
                // Base case 3: if # coins < # heads, prob = 0
                if (k > n) {
                    C[n][k] = 0;
                } else {
                    //either n is heads times prob of n-1,k-1 OR n is not heads * n-1, k
                    C[n][k] = (prob[n - 1] * C[n - 1][k - 1]) + (1 - prob[n - 1]) * C[n - 1][k];
                }
            }
        }
        System.out.printf("Probability of %s heads occurring given %s coins is %s", heads, coins, C[coins][heads]);
    }

////>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>/////

    /**
     * Solution for Problem5.
     *
     * Given a set of cards in a certain order, precompute and return a matrix where a user can lookup in O(1) time
     * if they should choose either the last or the first card of the sequence in order to maximize profit.
     * @param cards the sequence of card values
     * @return a precomputed choice matrix
     */
    public static char[][] precompute(int[] cards) {
        int n = cards.length;
        int[][] maxVal = new int[n][n];
        char[][] choice = new char[n][n];
        for (int i = 0; i < n; i++) {
            // value for a single card
            maxVal[i][i] = cards[i];
        }
        for (int i = 0; i < n - 1; i++) {
            //max value for 2 given cards
            if (cards[i] > cards[i + 1]) {
                maxVal[i][i + 1] = cards[i];
                choice[i][i + 1] = 'f';
            } else {
                maxVal[i][i + 1] = cards[i + 1];
                choice[i][i + 1] = 'l';
            }
        }
        int first, last;
        for (int diff = 2; diff < n; diff++) {
            for (int start = 0; start < n - diff; start++) {
                //ending index is starting + difference
                int end = start + diff;
                //calculate the max values if you choose first and if you choose last
                //if you choose first, you are giving up the maxVal that the 2nd player wculd choose (i+1...end)
                first = cards[start] - maxVal[start + 1][end];
                //if you choose last, you are giving up the maxVal that the 2nd player could choose (start... end-1)
                last = cards[end] - maxVal[start][end - 1];
                if (first > last) {
                    maxVal[start][end] = first;
                    choice[start][end] = 'f';
                } else {
                    maxVal[start][end] = last;
                    choice[start][end] = 'l';
                }
            }
        }
        return choice;
    }

////>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>/////

    /**
     * Solution for Problem6.
     *
     * Teams A and B are competing to see who can win n games first. Given a (how many A has won), b (how many B has
     * won), and n (the goal), determine the probability of A winning the competition. They both have equal chances
     * of winning any given name.
     * @param a the number of games A has won so far
     * @param b the number of games B has won so far
     * @param n the number of games won needed to win the competition
     */
    public static void probAWin(int a, int b, int n) {
        // if a and b alternated between winning until they were 1 away and someone wins the last one
        int games_left = (2 * n) - a - b - 1;
        // goal - how many already won
        int games_to_win = n - a;
        double[][] C = new double[games_left + 1][games_to_win + 1];
        // Base case 1 & 2:
        for (int i = 0; i <= games_left; i++) {
            // 1: if games to win is 0, probability is 100%
            C[i][0] = (double) 1;
            // 2: if games left = games to win, probability is 50% * C[i-1][i-1]
            // only if i is greater than 0 (base case 1) and only up to the num of games to win
            if (i > 0 && i <= games_to_win) {
                C[i][i] = .5 * C[i - 1][i - 1];
            }
        }
        for (int diff = 1; diff <= games_left - 1; diff++) {
            for (int i = 1; i <= games_to_win; i++) {
                // column or games_left is games to win + diff
                int col = i + diff;
                // probability is A or B where
                // A = win this game * C[games_left-1][games_to_win-1]
                // B = don't win this game * C[games_left-1][games_to_win]
                if (col <= games_left) {
                    C[col][i] = (.5 * C[col - 1][i - 1]) + (.5 * C[col - 1][i]);
                }
            }
        }
        System.out.println("The probability of A winning the competition is: " + C[games_left][games_to_win]);
    }

    ////>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>/////

    /**
     * Solution for Problem7.
     *
     * Given an unlimited supply of coins with certain values, determine if it is possible to make exact change
     * for the given value.
     * @param values the values of the coins
     * @param V      the value you wish to make exact change for
     */
    public static void makeChange(int[] values, int V) {
        boolean[] C = new boolean[V + 1];
        // base case: if value is 0, you can make change
        C[0] = true;
        for (int i = 1; i <= V; i++) {
            for (int j = 0; j < values.length; j++) {
                if (values[j] <= i && C[i - values[j]]) {
                    C[i] = true;
                }
            }
        }
        System.out.printf("Is it possible to make change for %s?   %b\n", V, C[V]);
    }

    ////>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>/////

    /**
     * Solution for Problem8.
     *
     * Given a limited supply of coins with certain values, determine if it is possible to make exact change
     * for the given value.
     * @param values the values of the coins
     * @param V      the value you wish to make exact change for
     */
    public static void makeChange2(int[] values, int V) {
        boolean[][] C = new boolean[values.length + 1][V + 1];
        // Base case: if value is 0, you can make change.
        for (int i = 0; i <= values.length; i++) {
            C[i][0] = true;
        }
        for (int i = 1; i <= values.length; i++) {
            for (int j = 1; j <= V; j++) {
                C[i][j] = C[i - 1][j];
                if (values[i - 1] <= j && C[i - 1][j - values[i - 1]]) {
                    C[i][j] = true;
                }
            }
        }
        // traversing last column to find a true
        boolean result = false;
        for (int l = 0; l <= values.length; l++) {
            result = result || C[l][V];
        }
        System.out.printf("Is it possible to make change for %s?   %b\n", V, result);
    }

    ////>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>/////

    /**
     * Solution for Problem9.
     *
     * Given an unlimited supply of coins with certain values, determine if it is possible to make exact change
     * for the given value using up to k coins.
     * @param values the values of the coins
     * @param V      the value you wish to make exact change for
     * @param max    the maximum number of coins you are allowed to use
     */
    public static void makeChange3(int[] values, int V, int max) {
        boolean[][] C = new boolean[max + 1][V + 1];
        // base case: if value is 0, you can make change
        for (int k = 0; k <= max; k++) {
            C[k][0] = true;
        }
        for (int k = 1; k <= max; k++) {
            for (int j = 1; j <= V; j++) {
                C[k][j] = C[k - 1][j];
                for (int m = 0; m < values.length; m++) {
                    if (values[m] <= j && C[k - 1][j - values[m]]) {
                        C[k][j] = true;
                    }
                }
            }
        }
        System.out.printf("Is it possible to make change for %s?   %b\n", V, C[max][V]);
    }

    ////>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>/////

    /**
     * Solution for Problem10.
     *
     * Given a set of positive integers (s) and another integer (t), determine if there exists a subset of
     * s that sums to t using any value at most once.
     * @param values the set of positive integers
     * @param t      the value you wish to sum to
     */
    public static void subsetSum(int[] values, int t) {
        boolean[][] C = new boolean[values.length + 1][t + 1];
        // Base case: if t is 0, you can sum to it.
        for (int i = 0; i <= values.length; i++) {
            C[i][0] = true;
        }
        for (int i = 1; i <= values.length; i++) {
            for (int j = 1; j <= t; j++) {
                C[i][j] = C[i - 1][j];
                if (values[i - 1] <= j && C[i - 1][j - values[i - 1]]) {
                    C[i][j] = true;
                }
            }
        }
        // traversing last column to find a true
        boolean result = false;
        for (int l = 0; l <= values.length; l++) {
            result = result || C[l][t];
        }
        System.out.printf("Is it possible to make change for %s?   %b\n", t, result);
    }

////>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>/////
}