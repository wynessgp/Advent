package week1;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Arrays;
import java.util.HashSet;
import java.util.HashMap;

public class DayFour {
   private static final String FILE_NAME = "inputfiles/dayfour.txt";
   private static final int MAX_CARD_NUM = 213;

    private Scanner sc;

    public DayFour() throws IOException {
        handleFileInput(FILE_NAME);
        solvePuzzle();
    }

    public void solvePuzzle() {
        // get all of the winning numbers for a given line; put them in
        // a set. Then, check to see if the numbers after are
        // a member of the set. Keep track of the number of matches;
        // then multiply 2 by that power for the total later.
        int totalPoints = 0;
        // part 2
        int totalNumberOfCards = 0;
        HashMap<Integer, Integer> cardNumToNumOfCard = new HashMap<>();
        // initialize the map, cause I'm too lazy to deal with those other issues later
        for (int i = 1; i <= MAX_CARD_NUM; i++) {
            cardNumToNumOfCard.put(i, 1);
        }

        while (sc.hasNextLine()) {
            // start at the first number
            String curLine = sc.nextLine();
            // determine the card number (part 2)
            String temp = "";
            for (int i = 0; i < curLine.substring(5, 8).length(); i++) {
                if (Character.isDigit(curLine.charAt(5 + i)))
                    temp += curLine.charAt(5 + i);
            }
            int cardNum = Integer.parseInt(temp);

            // get the winning/actual nums (both parts)
            curLine = curLine.substring(curLine.indexOf(":") + 2);
            HashSet<String> winningNums = new HashSet<String>();
            // add all of the winning numbers. This is a bit complex
            winningNums.addAll(Arrays.asList(curLine.substring(0, curLine.indexOf("|")).split(" ")));
            // remove the empty string. This can be caused by single-digit nums
            winningNums.remove("");

            // now get the actual numbers.
            int matches = 0;
            HashSet<String> actualNums = new HashSet<String>(); 
            actualNums.addAll(Arrays.asList(curLine.substring(curLine.indexOf("|") + 1).split(" ")));
            actualNums.remove("");

            for (String s : actualNums) {
                if (winningNums.contains(s)) {
                    matches++;
                }
            }
            if (matches != 0) {
                totalPoints += Math.pow(2, matches - 1);
                // this handles the number of cards (part 2)
                for (int i = 1; i <= matches; i++) {
                    // fetch the number of THIS card we have.
                    int numOfCurrentCard = cardNumToNumOfCard.get(cardNum);
                    // this amount gets added to each of the next cards (within matches)
                    if (cardNum + i <= MAX_CARD_NUM)
                        cardNumToNumOfCard.put(cardNum + i, numOfCurrentCard + cardNumToNumOfCard.get(cardNum + i));
                }
            } 
        }

        System.out.println("points: " + totalPoints);
        // part 2
        for (Integer key : cardNumToNumOfCard.keySet()) {
            totalNumberOfCards += cardNumToNumOfCard.get(key);
        }
        System.out.println("total num cards: " + totalNumberOfCards);
    }

    private void handleFileInput(String fileName) throws IOException{
        // creates a scanner for the provided input
        // and puts it into this.sc
        try {
            this.sc = new Scanner(new File(FILE_NAME));
        } catch (IOException e) {
            throw e;
        }
    }

    public static void main(String[] args) throws IOException {
        new DayFour();
    } 
}
