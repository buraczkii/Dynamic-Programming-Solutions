import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * Created by susannaedens on 6/27/16.
 */
public class CleanSolutions {

    /**
     * Solution for Problem4.
     * <p>
     * Calculate the maximum sum possible of a contiguous sequence in the passed in array. Print the max
     * sum and the numbers in that sequence.
     *
     * @param orig the original array of numbers to pass in
     */
    public static void maxContiguousSubsequence(int[] orig) {
        int start = 0;
        int end = 0;
        int total = orig[0];
        int tempSum = total;
        int tempBeg = 0;
        for (int i = 1; i < orig.length; i++) {
            if (tempSum > 0) {              // only if the sum until i-1 i positive, add this to that number
                tempSum = tempSum + orig[i];
            }
            if (orig[i] > tempSum) {        // if this number is greater than that, set tempSum to this number
                tempSum = orig[i];
                tempBeg = i;                // store temp beginning (this index)
            }
            if (tempSum > total) {
                start = tempBeg;            // only gets changed if entered prev for loop
                total = tempSum;
                end = i;                    // store end as this index
            }
        }
        System.out.println("The length of the max subsequence is " + (end - start + 1));
        System.out.println("The sum of the max subarray is: " + total);
        System.out.println("The numbers in the max subarray are: ");
        for (int k = start; k <= end; k++) {
            System.out.print(" " + orig[k]);
        }
    }

    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>//

    /**
     * Solution for Problem5.
     *
     * Given an array of hotel distances (from the starting point), aim to travel a given amount of miles (goal)
     * each day. You must end at the last hotel in the list of hotels. This algorithm tries to minimize the sum
     * penalty over all travel days. The penalty is calculated by the square of the distance traveled - goal amount.
     *
     * @param hotels the distances of the hotels from starting point
     * @param goal   the amount of miles you wish to travel every day
     */
    public static void hotelStops(int[] hotels, int goal) {
        int[] C = new int[hotels.length];               // array to keep track of penalty at i if i is destination
        if (hotels.length == 0) {
            System.out.println("You are at your destination, don't leave");
            return;
        }
        C[0] = penalty(goal, hotels[0]); //if the first stop is the destination, it costs the penalty of that distance
        int lastStop = 0;                // keep track of the previous stop (start at 0)
        boolean[] stops = new boolean[hotels.length];   // boolean array to keep track of which hotels you stay at
        stops[lastStop] = true;
        for (int i = 1; i < hotels.length; i++) {
            C[i] = penalty(goal, hotels[i] - hotels[lastStop]) + C[lastStop];   // cost at last plus penalty to this
            for (int k = i - 1; k > lastStop; k--) {
                if (penalty(goal, hotels[i] - hotels[k]) + C[k] < C[i]) {       //traverse backwards to see if you can
                    C[i] = penalty(goal, hotels[i] - hotels[k]) + C[k];         // get a smaller penalty if stop at k
                    lastStop = k;                                               // if so, store k as last Stop
                }
            }
            stops[lastStop] = true;                                             // mark lastStop as true in stops array
        }
        stops[hotels.length - 1] = true;                                        // true because you must stop at end
        System.out.println("The hotel stops you will be staying at are: ");     // print out all trues
        for (int j = 0; j < stops.length; j++) {
            if (stops[j]) {
                System.out.print(" " + j);
            }
        }
    }

    /**
     * Helper for Problem 5.
     * Calculates and returns the penalty for traveling a certain distance (actual) compared to the goal.
     *
     * @param goal   the desired number of miles to travel
     * @param actual the actual number of miles traveled
     * @return the penalty
     */
    public static int penalty(int goal, int actual) {
        return (int) Math.abs(Math.pow(goal - actual, 2));
    }

    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>//

    /**
     * Solution for Problem6.
     * Yuckdonald's is opening restaurants along a strip of highway. You are given the distances from the starting
     * point of the possible locations of the highways and the profits at each of these locations. You can only
     * have one restaurant at each location and each location must be at least (min) miles apart.
     *
     * @param distance the distances from the starting point of each possible restaurant location
     * @param profit   the profit at a given location
     * @param min      the minimum distance between restaurants
     */
    public static void yuckDonalds(int[] distance, int[] profit, int min) {
        if (distance.length == 0) {
            System.out.println("Your profit is $0");
            return;
        }
        int[] C = new int[distance.length]; // initializing counter array for profit at restaurant i
        C[0] = profit[0];   // if you only had one restaurant, the profit is that restaurant's profit
        for (int i = 1; i < C.length; i++) {
            C[i] = Math.max(profit[i], C[i - 1]);   // first, set C[i] to either it's profit or previous profit
            int j = 0;
            while (distance[i] - distance[j] >= min) {  // while this restauraunt is far enough away from prev
                if (profit[i] + C[j] > C[i]) {          // if profit of this plus an earlier one is greater than C[i]
                    C[i] = profit[i] + C[j];            // store new value
                }
                j++;
            }
        }
        System.out.println("Your total profit is $" + C[C.length - 1]);     // print max expected profit
    }


    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>//

    /**
     * Solution for Problem7.
     * <p>
     * Given an array of characters, determine if it is possible to break up the characters such that you form
     * a valid sentence. If you receive a valid sentence, print the words out. If not, print out a statement
     * saying the sentence was not valid.
     *
     * @param letters the array of characters to parse.
     */
    public static void isSentence(char[] letters) {
        if (letters.length == 0) {
            System.out.println("Your sentence is blank");
        }
        int[] result = new int[letters.length + 1];     // int array to hold starting indices of possible words
        result[0] = 0;
        for (int s = 1; s < result.length; s++) {
            result[s] = -1;                   // initialize all values to -1 (indicating not starting index of a word)
        }
        for (int i = 1; i < result.length; i++) {   // for each character
            for (int k = i - 1; k >= 0; k--) {      // traverse through previous characters
                if (result[k] > -1) {               // if greater than -1 (indicates ending index of word)
                    if (dict(new String(Arrays.copyOfRange(letters, k, i)))) {  //if the substring is a word
                        result[i] = k;    // store end index of last word as start index for word ending at this index
                        break;
                    }
                }
            }
        }
        if (result[letters.length] > -1) {      //print out the words only if the last character is greater than -1
            int l = letters.length;
            String sentence = "";
            while (l > 0) {
                sentence = " " + new String(Arrays.copyOfRange(letters, result[l], l)) + sentence;
                l = result[l];
            }
            System.out.println("Your sentence is: " + sentence);
        }
    }


    /**
     * Represents a small dictionary used for testing isSentence.
     *
     * @param letters the given letters to check
     * @return true if the given letters form a word in the dictionary
     */
    public static boolean dict(String letters) {
        return (letters.equals("word")) || (letters.equals("is")) || (letters.equals("a")) ||
                (letters.equals("sentence")) || (letters.equals("i")) || (letters.equals("my")) ||
                (letters.equals("bye")) || (letters.equals("hello")) || (letters.equals("hell"));
    }

    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>//

    /**
     * Solution for problem8.
     *
     * We have a n x 4 checkerboard which is filled with various integers representing the value at that square.
     * With 2n pebbles, we want to place the pebbles on the board and maximize the value of the board. The constraint
     * is that no pebbles can be horizontally or vertically adjacent. This algorithm will return the maximum value
     * you can achieve of the board in O(n) time.
     *
     * @param board the checkerboard with integer values
     * @param map   a map holding types as keys and possible adjacent types as values
     */
    public static void pebbling(int[][] board, LinkedHashMap<Integer, LinkedList<Integer>> map) {
        int n = board.length + 1;
        int[][] results = new int[n][8];    // create matrix of sums of the column based on the 8 types
        for (int j = 0; j < 8; j++) {
            results[0][j] = 0;              // if column 0, the sum is 0
        }
        for (int i = 1; i < n; i++) {       // for all the columns
            for (int t = 0; t < 8; t++) {       // for all the types
                results[i][t] = sum(t, board[i - 1]);   // sum of only this column for this type
                LinkedList<Integer> poss = map.get(t);  // get the possible adjacent types
                int tempSum = results[i][t];            // temp sum is whatever is currently stored
                for (int p = 0; p < poss.size(); p++)   // for all possibilities
                    if (results[i - 1][poss.get(p)] + results[i][t] > tempSum) {    // if a possible prev. column + this
                        tempSum = results[i - 1][poss.get(p)] + results[i][t];      // is greater, reset tempSum
                    }
                results[i][t] = tempSum;    // set to tempSum which is max of all possible adjacents + this sum
            }
        }
        int max = 0;
        int type = 0;
        for (int j = 0; j < 8; j++) {
            System.out.printf("The value at results[n-1][%s] is %s\n", j, results[n-1][j]);
            if (results[n - 1][j] > max) {
                max = results[n - 1][j];
                type = j;
            }
        }
        System.out.println("The maximum value available for your board is: " + max);
        System.out.println("The last column of your board is type: " + type);
        //> in order to find placement of all pebbles, we can keep track of the type possibility of prev column that
        //> led to the max value of column i. (inside third for loop).
    }


    /**
     * Given a type and a column, calculate the sum of that column given the type's pattern.
     *
     * @param type an enum representing a column's pattern
     * @param col  an array representing the integers held within the column
     * @return the sum of the column based on the type
     */
    public static int sum(int type, int[] col) {
        if (type == 0) {
            return col[0] + col[2];
        }
        if (type == 1) {
            return col[0] + col[3];
        }
        if (type == 2) {
            return col[1] + col[2];
        }
        if (type == 3) {
            return col[0];
        }
        if (type == 4) {
            return col[1];
        }
        if (type == 5) {
            return col[2];
        }
        if (type == 6) {
            return col[3];
        }
        return 0;
    }

    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>//

    /**
     * Solution for problem 9.
     *
     * We are given a definition for multiplication operations on three symbols: a, b, c (these are held within the
     * given math matrix). Given a string of characters, we examine these characters and determine if it is possible
     * to parenthesize the string in such a way that the resulting value of the expression is a.
     * Assume that you are given a string solely made up of a, b, or c characters.
     * @param orig the string of characters to examine
     * @param math the matrix of product results from multiplying two characters together
     */
    public static void funkyMath(char[] orig, int[][] math){
        // if given only one character, answer true only if that character is a
        if(orig.length == 1){
            boolean ans =  (orig[0] == 'a');
            System.out.println("single character result: " + ans);
            return;
        }

        // hashmap used to convert string of characters into integers
        HashMap<Character, Integer> map = new HashMap<>();
        map.put('a', 0);
        map.put('b', 1);
        map.put('c', 2);
        int[] string = new int[orig.length];
        for(int i = 0; i< orig.length; i++) {
            string[i] = map.get(orig[i]);
        }

        boolean[][] result = new boolean[string.length][3];
        //> base case 1. if only one character, row is true if character == row
        //> base case 2. if only two character, row is true if 1st * 2nd == row
        for(int i = 0; i< 3; i++){
            result[0][i] = (string[0] == i);        // only if that character is equal to the row char
            result[1][i] = (math[string[0]][string[1]] == i);   //if the two characters multiplied are equal to the row
        }

        //> for all other characters, you check 2 cases: 1. ([you-1] * you) || 2. ([you-2] * (you x you-1))
        for(int j = 2; j<string.length;j++) {   //if the string ended in the ith character
            for (int k = 0; k < 3; k++) {       // could it end in a a(0), b(1), or c(2) ?
                for (int l = 0; l < 3; l++) {   // for each type (a, b, c) check if the previous column is true
                    if (result[j - 1][l]) {         // indicating that the prev subsequence can result in that letter
                        result[j][k] = ((math[l][string[j]]) == k) || result[j][k]; // first case
                    }
                    if (result[j - 2][l]) {                                         // second case
                        result[j][k] = ((math[l][math[string[j - 1]][string[j]]]) == k) || result[j][k];
                    }
                }
            }
        }
        // check the last column, row a if it is true.
        boolean answer = result[string.length-1][0];
        if(answer){
            System.out.println("You entered a string that can result in an a!");
        }else{
            System.out.println("Sorry, the string you entered can not result in an a.");
        }
    }

    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>//

    public static void main(String[] args) {
        //> For problem 8
        //> Map holding possible adjacent types given a type
        LinkedHashMap<Integer, LinkedList<Integer>> adjacents = new LinkedHashMap<>();
        LinkedList<Integer> a, b, c, d, e, f, g, h;
        a = new LinkedList<Integer>();
        b = new LinkedList<Integer>();
        c = new LinkedList<Integer>();
        d = new LinkedList<>();
        e = new LinkedList<>();
        f = new LinkedList<>();
        g = new LinkedList<>();
        h = new LinkedList<>();
        a.add(2);
        a.add(4);
        a.add(6);
        a.add(7);
        b.add(4);
        b.add(5);
        b.add(7);
        c.add(0);
        c.add(3);
        c.add(5);
        c.add(7);
        d.add(2);
        d.add(4);
        d.add(5);
        d.add(6);
        d.add(7);
        e.add(0);
        e.add(1);
        e.add(3);
        e.add(5);
        e.add(6);
        e.add(7);
        f.add(1);
        f.add(2);
        f.add(3);
        f.add(4);
        f.add(6);
        f.add(7);
        g.add(0);
        g.add(3);
        g.add(4);
        g.add(5);
        g.add(7);
        h.add(0);
        h.add(1);
        h.add(2);
        h.add(3);
        h.add(4);
        h.add(5);
        h.add(6);
        adjacents.put(0, a);
        adjacents.put(1, b);
        adjacents.put(2, c);
        adjacents.put(3, d);
        adjacents.put(4, e);
        adjacents.put(5, f);
        adjacents.put(6, g);
        adjacents.put(7, h);

        //> Math matrix for problem9
        int[][] math = new int[3][3];
        math[0][0] = 1;
        math[0][1] = 1;
        math[0][2] = 0;
        math[1][0] = 2;
        math[1][1] = 1;
        math[1][2] = 0;
        math[2][0] = 0;
        math[2][1] = 2;
        math[2][2] = 2;

    }


}
