package week1;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DaySeven {
    private static final String FILE_NAME = "inputfiles/dayseven.txt";

    private Scanner sc;
    private HashMap<Character, Integer> rankMapping;

    public DaySeven() throws IOException {
        initScanner();
        solvePartOne();
        initScanner();
        solvePartTwo();
    }

    private void setupRankings(int part1) {
        rankMapping = new HashMap<>();
        if (part1 == 1) {
            rankMapping.put('J', 4);
        } else {
            rankMapping.put('J', 13);
        }
        rankMapping.put('A', 1);
        rankMapping.put('K', 2);
        rankMapping.put('Q', 3);
        rankMapping.put('T', 4 + part1);
        rankMapping.put('9', 5 + part1);
        rankMapping.put('8', 6 + part1);
        rankMapping.put('7', 7 + part1);
        rankMapping.put('6', 8 + part1);
        rankMapping.put('5', 9 + part1);
        rankMapping.put('4', 10 + part1);
        rankMapping.put('3', 11 + part1);
        rankMapping.put('2', 12 + part1);
    }

    private void solvePartOne() {
        // this will get sorted later on
        setupRankings(1);
        ArrayList<ArrayList<String>> hands = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            hands.add(new ArrayList<String>());
        }
        while (sc.hasNextLine()) {
            String curLine = sc.nextLine();
            String[] splitLine = curLine.split(" ");
            // we only care about splitLine[0] here for the hand ranking determ.
            int res = determineHandType(splitLine[0], false);
            hands.get(res).add(curLine);
        }

        for (int i = 6; i >= 0; i--) {
            // let java do the work for us
            Collections.sort(hands.get(i), new HandComparator());
        }

        long totalWinnings = 0;
        int curRank = 1;
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < hands.get(i).size(); j++) {
                totalWinnings += curRank * Integer.parseInt(hands.get(i).get(j).substring(6));
                curRank++;
            }
        }

        System.out.println("Part 1 total winnings: " + totalWinnings);
        
    }

    private void solvePartTwo() {
        setupRankings(0);
        ArrayList<ArrayList<String>> hands = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            hands.add(new ArrayList<String>());
        }
        while (sc.hasNextLine()) {
            String curLine = sc.nextLine();
            String[] splitLine = curLine.split(" ");
            // we only care about splitLine[0] here for the hand ranking determ.
            int res = determineHandType(splitLine[0], true);
            hands.get(res).add(curLine);
        }

        for (int i = 6; i >= 0; i--) {
            // let java do the work for us
            Collections.sort(hands.get(i), new HandComparator());
        }

        long totalWinnings = 0;
        int curRank = 1;
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < hands.get(i).size(); j++) {
                totalWinnings += curRank * Integer.parseInt(hands.get(i).get(j).substring(6));
                curRank++;
            }
        }

        System.out.println("Part 2 total winnings: " + totalWinnings);

    }

    protected class HandComparator implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {
            // assume the strings are the same length.
            // only compare the first 5 characters so we can bring bids with.
            // note that J (jokers in P2) don't change anything with comparisons,
            // since they are done within same-rank hands.
            // AKA, they are still the weakest card.
            for (int i = 0; i < 5; i++) {
                if (rankMapping.get(o1.charAt(i)) < rankMapping.get(o2.charAt(i)))
                    return 1;
                else if (rankMapping.get(o1.charAt(i)) > rankMapping.get(o2.charAt(i)))
                    return -1;
            }
            return 0;
        }
    }


    private int determineHandType(String hand, boolean part2) {
        HashMap<Character, Integer> numOfRank = new HashMap<>();
        for (int i = 0; i < hand.length(); i++) {
            Character cur = hand.charAt(i);
            if (!numOfRank.containsKey(cur))
                numOfRank.put(cur, 1);
            else 
                numOfRank.put(cur, numOfRank.get(cur) + 1);
        }
        if (part2) {
            // for part 2: J (jokers) now count as the "best" card for the hand.
            // so if we find a joker, we'll add to whatever has the "best" 
            // ranking at the end.
            // first: determine if there ARE any jokers
            int jokers = 0;
            if (numOfRank.containsKey('J'))
                jokers = numOfRank.get('J');
            // if there's less than 5 (but more than 0)
            // then we can improve something...
            if (jokers > 0 && jokers < 5) {
                numOfRank.remove('J');
                // find the best ranking item out of the remaining characters.
                Character bestRanking = '2'; // set it to the lowest to start...
                for (Character key : numOfRank.keySet()) {
                    if (rankMapping.get(key) < rankMapping.get(bestRanking))
                        bestRanking = key;
                }
                // remove the "best" from the map.
                int bestValue = numOfRank.get(bestRanking);
                // if this had a higher (or equal) amount of occur. then the new max,
                // then we add the jokers here, because this was the best ranking.
                if (numOfRank.isEmpty()) { 
                    // edge case: only one item BESIDES jokers...
                    numOfRank.put(bestRanking, bestValue + jokers);
                } else if (bestValue >= Collections.max(numOfRank.values()))
                    numOfRank.put(bestRanking, bestValue + jokers);
                else { // less occur, so add the jokers to the new max item...
                    int max = Collections.max(numOfRank.values());
                    for (Character key : numOfRank.keySet()) {
                        if (max == numOfRank.get(key))
                            numOfRank.put(key, numOfRank.get(key) + jokers);
                    }
                }
            }
        }

        if (numOfRank.containsValue(5))
            return 6;
        if (numOfRank.containsValue(4))
            return 5;
        if (numOfRank.containsValue(3) && numOfRank.containsValue(2))
            return 4;
        if (numOfRank.containsValue(3))
            return 3;
        // end of the easy checks.
        int numTwos = 0;
        for (Character key : numOfRank.keySet()) {
            if (numOfRank.get(key) == 2) {
                numTwos++;
            } 
        }

        if (numTwos == 2) {
            return 2;
        } else if (numTwos == 1) {
            return 1;
        }
        return 0;

        
        
    }

    private void initScanner() throws IOException{
        // creates a scanner for the provided input
        // and puts it into this.sc
        try {
            this.sc = new Scanner(new File(FILE_NAME));
        } catch (IOException e) {
            throw e;
        }
    }

    public static void main(String[] args) throws IOException {
        new DaySeven();
    }

}
