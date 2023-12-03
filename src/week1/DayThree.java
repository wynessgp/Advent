package week1;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

public class DayThree {
    private static final String FILE_NAME = "inputfiles/daythree.txt";

    private Scanner sc;
    private ArrayList<String> allLines;

    public DayThree() throws IOException {
        this.allLines = new ArrayList<>();
        handleFileInput(FILE_NAME);
        solvePuzzle();
    }

    public void solvePuzzle() {
        // this is going to be a fun one :) /s
        // Plan is to first pre-load this into an arraylist of strings, since you can't
        // go back with scanners.
        while (sc.hasNextLine()) {
            allLines.add(sc.nextLine());
        }

        // now, we want to look through the lines for SYMBOLS. We'll go through
        // and check adjacency once we've found a symbol.
        int sum = 0;
        int gearRatioSum = 0;
        for (int i = 0; i < allLines.size(); i++) {
            String s = allLines.get(i);
            for (int j = 0; j < s.length(); j++) {
                // if it's not a period, and it's not a digit, it must be a symbol.
                if (s.charAt(j) == '*') {
                    // note that this messes up the answer to part 1
                    gearRatioSum += getGearRatio(i, j);
                } else if (s.charAt(j) != '.' && !Character.isDigit(s.charAt(j))) {
                    sum += getSumOfAdjacent(i, j);
                }
            }
        }

        System.out.println("Sum: " + sum);
        System.out.println("Gear ratio: " + gearRatioSum);
    }

    private int getSumOfAdjacent(int row, int col) {
        int retVal = 0;
        // first: check the current line.
        String curLine = allLines.get(row);
        if (col < curLine.length() - 1) {
            // there can be something to the right.
            retVal += fetchRightNumber(curLine, col + 1);
        }
        if (col > 0) {
            // there can be something to the left.
            // note that we feed col - 1 since (row, col) is a symbol.
            retVal += fetchLeftNumber(curLine, col - 1);
        }
        // second: check above/below.
        boolean numberAbove = false;
        if (row > 0) {
            String prevLine = allLines.get(row - 1);
            if (Character.isDigit(prevLine.charAt(col))) {
                // something is directly above me, so I don't
                // need to check my upper-diagonals.
                // (because this number would extend otherwise).
                numberAbove = true;
                retVal += fetchAbove(prevLine, col);
            }
        }
        boolean numberBelow = false;
        if (row < allLines.size() - 1) {
            String nextLine = allLines.get(row + 1);
            if (Character.isDigit(nextLine.charAt(col))) {
                // something is directly above me, so I don't
                // need to check my lower-diagonals.
                // (because this number would extend otherwise).
                numberBelow = true;
                retVal += fetchBelow(nextLine, col);
            }
        }
        // third: check diagonals.
        if (!numberAbove) {
            // upper-left and upper-right diagonal must be checked.
            if (row > 0) {
                String prevLine = allLines.get(row - 1);
                // upper-left.
                if (col < curLine.length() - 1) {
                    retVal += fetchLeftNumber(prevLine, col - 1);
                }
                // upper-right.
                if (col > 0) {
                    retVal += fetchRightNumber(prevLine, col + 1);
                }
            }
        }
        if (!numberBelow) {
            // lower-left and lower-right diagonal must be checked.
            if (row < allLines.size() - 1) {
                String nextLine = allLines.get(row + 1);
                // lower left.
                if (col < curLine.length() - 1) {
                    retVal += fetchLeftNumber(nextLine, col - 1);
                }
                // lower-right.
                if (col > 0) {
                    retVal += fetchRightNumber(nextLine, col + 1);
                }
            }
        }
        return retVal;
    }

    private int getGearRatio(int row, int col) {
        // keep track of the numbers we find, and how many (use an arraylist)
        ArrayList<Integer> numsFound = new ArrayList<>();
        // the rest of this will look (almost) identical to getSumOfAdjacent.
        // namely, we still need to fetch all possible numbers, just
        // do something different with the results.
        String curLine = allLines.get(row);
        if (col < curLine.length() - 1) {
            // there can be something to the right.
            int right = fetchRightNumber(curLine, col + 1);
            if (right != 0) 
                numsFound.add(right);
        }
        if (col > 0) {
            // there can be something to the left.
            // note that we feed col - 1 since (row, col) is a symbol.
            int left = fetchLeftNumber(curLine, col - 1);
            if (left != 0)
                numsFound.add(left);
        }
        // second: check above/below.
        boolean numberAbove = false;
        if (row > 0) {
            String prevLine = allLines.get(row - 1);
            if (Character.isDigit(prevLine.charAt(col))) {
                // something is directly above me, so I don't
                // need to check my upper-diagonals.
                // (because this number would extend otherwise).
                numberAbove = true;
                numsFound.add(fetchAbove(prevLine, col));
            }
        }
        boolean numberBelow = false;
        if (row < allLines.size() - 1) {
            String nextLine = allLines.get(row + 1);
            if (Character.isDigit(nextLine.charAt(col))) {
                // something is directly above me, so I don't
                // need to check my lower-diagonals.
                // (because this number would extend otherwise).
                numberBelow = true;
                numsFound.add(fetchBelow(nextLine, col));
            }
        }
        // third: check diagonals.
        if (!numberAbove) {
            // upper-left and upper-right diagonal must be checked.
            if (row > 0) {
                String prevLine = allLines.get(row - 1);
                // upper-left.
                if (col < curLine.length() - 1) {
                    int left = fetchLeftNumber(prevLine, col - 1);
                    if (left != 0)
                        numsFound.add(left);
                }
                // upper-right.
                if (col > 0) {
                    int right = fetchRightNumber(prevLine, col + 1);
                    if (right != 0)
                        numsFound.add(right);
                }
            }
        }
        if (!numberBelow) {
            // lower-left and lower-right diagonal must be checked.
            if (row < allLines.size() - 1) {
                String nextLine = allLines.get(row + 1);
                // lower left.
                if (col < curLine.length() - 1) {
                    int left = fetchLeftNumber(nextLine, col - 1);
                    if (left != 0)
                        numsFound.add(left);
                }
                // lower-right.
                if (col > 0) {
                    int right = fetchRightNumber(nextLine, col + 1);
                    if (right != 0)
                        numsFound.add(right);
                }
            }
        }
        return numsFound.size() == 2 ? numsFound.get(0) * numsFound.get(1) : 0;
    }
    
    private int fetchLeftNumber(String cur, int startingCol) {
        String toLeft = "";
        int leftIndexMod = 0;
        while (startingCol - leftIndexMod >= 0) {
            // make sure we don't walk out of bounds.
            if (!Character.isDigit(cur.charAt(startingCol - leftIndexMod)))
                break;
            toLeft += cur.charAt(startingCol - leftIndexMod);
            leftIndexMod++;
        }
        // this needs to be reversed.
        String ret = "";
        for (int i = toLeft.length() - 1; i > -1; i--) {
            ret += toLeft.charAt(i);
        }
        return ret.isEmpty() ? 0 : Integer.parseInt(ret);
    }

    private int fetchRightNumber(String cur, int startingCol) {
        String toRight = "";
        int rightIndexMod = 0;
        while (startingCol + rightIndexMod <= cur.length() - 1) {
            // make sure we don't walk out of bounds.
            if (!Character.isDigit(cur.charAt(startingCol + rightIndexMod)))
                break;
            toRight += cur.charAt(startingCol + rightIndexMod);
            rightIndexMod++;
        }
        return toRight.isEmpty() ? 0 : Integer.parseInt(toRight);
    }

    private int fetchAbove(String cur, int startingCol) {
        String above = "";
        // walk left first.
        int leftIndexMod = 0;
        while (startingCol - leftIndexMod >= 0 && 
               Character.isDigit(cur.charAt(startingCol - leftIndexMod))) {
            // push as far left as we can.
            leftIndexMod++;
        }
        // ok, we've found the starting digit of the number. NOW start building.
        int rightIndexMod = 0;
        while (startingCol + rightIndexMod <= cur.length() - 1 &&
               Character.isDigit(cur.charAt(startingCol - leftIndexMod + rightIndexMod + 1))) {
            above += cur.charAt(startingCol - leftIndexMod + rightIndexMod + 1);
            rightIndexMod++;
        }
        return Integer.parseInt(above);
    }

    private int fetchBelow(String cur, int startingCol) {
        String below = "";
        // walk left first.
        int leftIndexMod = 0;
        while (startingCol - leftIndexMod >= 0 && 
               Character.isDigit(cur.charAt(startingCol - leftIndexMod))) {
            // push as far left as we can.
            leftIndexMod++;
        }
        // ok, we've found the starting digit of the number. NOW start building.
        int rightIndexMod = 0;
        while (startingCol + rightIndexMod <= cur.length() - 1 &&
               Character.isDigit(cur.charAt(startingCol - leftIndexMod + rightIndexMod + 1))) {
            below += cur.charAt(startingCol - leftIndexMod + rightIndexMod + 1);
            rightIndexMod++;
        }
        return Integer.parseInt(below);
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
        new DayThree();
    }
}
