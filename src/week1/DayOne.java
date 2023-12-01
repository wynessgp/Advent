package week1;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.HashMap;

public class DayOne {

    private static final String FILE_NAME = "inputfiles/dayone.txt";

    private Scanner sc;
    private HashMap<String, Integer> numNamesToDigits;

    public DayOne() throws IOException {
        handleFileInput(FILE_NAME);
        this.numNamesToDigits = new HashMap<>();
        numNamesToDigits.put("one", 1);
        numNamesToDigits.put("two", 2);
        numNamesToDigits.put("three", 3);
        numNamesToDigits.put("four", 4);
        numNamesToDigits.put("five", 5);
        numNamesToDigits.put("six", 6);
        numNamesToDigits.put("seven", 7);
        numNamesToDigits.put("eight", 8);
        numNamesToDigits.put("nine", 9);
        solvePuzzle();
    }

    public void solvePuzzle() {
        int sum = 0;
        while (this.sc.hasNextLine()) {
            String nextLine = this.sc.nextLine();
            String digitsInLine = "";
            for (int i = 0; i < nextLine.length(); i++) {
                if (Character.isDigit(nextLine.charAt(i))) {
                    digitsInLine += nextLine.charAt(i);
                }
                // returns the number in question
                int res = checkForLetterNumber(i, nextLine);
                if (res != -1) 
                    digitsInLine += res;
            }
            sum += Integer.parseInt(digitsInLine.substring(0, 1) +
                    digitsInLine.substring(digitsInLine.length() - 1));
        }
        System.out.println("sum: " + sum);
    }

    private int checkForLetterNumber(int index, String cur) {
        if (index + 3 > cur.length()) 
            return -1;

        String potNumber = cur.substring(index, index + 3);
        if (numNamesToDigits.containsKey(potNumber)) {
            return numNamesToDigits.get(potNumber);
        }

        if (index + 4 > cur.length()) 
            return -1;

        potNumber = cur.substring(index, index + 4);
        if (numNamesToDigits.containsKey(potNumber)) 
            return numNamesToDigits.get(potNumber);

        if (index + 5 > cur.length()) 
            return -1;

        potNumber = cur.substring(index, index + 5);
        if (numNamesToDigits.containsKey(potNumber)) 
            return numNamesToDigits.get(potNumber);

        return -1;
    }

    private void handleFileInput(String fileName) throws IOException {
        // creates a scanner for the provided input
        // and puts it into this.sc
        try {
            File f = new File(FILE_NAME);
            System.out.println(f.getAbsolutePath());
            this.sc = new Scanner(new File(FILE_NAME));
        } catch (IOException e) {
            throw e;
        }
    }

    public static void main(String[] args) throws IOException {
        new DayOne();
    }
}
