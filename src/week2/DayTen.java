package week2;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DayTen {
    private static final String FILE_NAME = "inputfiles/dayten.txt";

    private Scanner sc;

    public DayTen() throws IOException {
        initScanner();
        solvePartOne();
        initScanner();
        solvePartTwo();
    }


    private void solvePartOne() {
        // | vertical N/S
        // - horizontal E/W
        // L 90 degree N/E
        // J 90 degree N/W
        // 7 90 degree S/W
        // F 90 degree S/E
        // . ground, no pipe
        // S starting position of animal

        // plan: construct a 2d boolean array that determines if a position
        // is connected or not...
        // traverse that main loop, remembering just the previous position we were at,
        // so we do not backtrack. Keep track of how many steps we've taken, and if
        // it's currently maximal...

        // first things first: parse the input into usable java data
        ArrayList<String> data = new ArrayList<>();
        int startRow = -1;
        int startCol = -1;
        int curRow = 0;
        while (sc.hasNext()) {
            // maintain the structure of the data...
            String curLine = sc.nextLine();
            if (curLine.contains("S")) {
                startRow = curRow;
                startCol = curLine.indexOf('S');
            }
            data.add(curLine);
            curRow++;
        }

        // find out where we can traverse from the starting point, then we'll do it programmatically using the current character.
        int loopLength = 1;
        // 1 means we previously went north, 2 means previously went south, 3 means west, 4 means east.
        int prevDir = -1;
        int curCol = startCol;
        curRow = startRow;
        if (startRow > 0) {
            Character charAbove = data.get(startRow - 1).charAt(startCol);
            if (charAbove == '|' || charAbove == '7' || charAbove == 'F') {
                prevDir = 1;
                curRow--;
            }
        }
        if (startRow < data.size() && prevDir == -1) {
            Character charBelow = data.get(startRow + 1).charAt(startCol);
            if (charBelow == '|' || charBelow == 'L' || charBelow == 'J') {
                prevDir = 2;
                curRow++;
            }
        }
        if (startCol > 0 && prevDir == -1) {
            Character charLeft = data.get(startRow).charAt(startCol - 1);
            if (charLeft == '-' || charLeft == 'F' || charLeft == 'L') {
                prevDir = 3;
                curCol--;
            }
        }
        if (startCol < data.get(0).length() && prevDir == -1) {
            Character charRight = data.get(startRow).charAt(startCol + 1);
            if (charRight == '-' || charRight == 'J' || charRight == '7') {
                prevDir = 4;
                curCol++;
            }
        }
        if (prevDir == -1) {
            // uhh...
            System.out.println("wtf?");
        }

        System.out.println("start row: " + startRow + " start col: " + startCol);
        while (curRow != startRow || curCol != startCol) {
            // keep going until we reach S again
            Character cur = data.get(curRow).charAt(curCol);
            System.out.println("\ncur row: " + curRow);
            System.out.println("cur col: " + curCol);
            System.out.println("character: " + cur);
            if (cur == '-') {
                if (prevDir == 3) { // "enter left"
                    curCol++;
                    prevDir = 4;
                } else { // "enter right"
                    curCol--;
                    prevDir = 3;
                }
            } else if (cur == '|') {
                if (prevDir == 2) { // "enter above"
                    curRow++;
                    prevDir = 1;
                } else { // "enter below"
                    curRow--;
                    prevDir = 2;
                }
            } else if (cur == 'L') {
                if (prevDir == 2) { // "enter above"
                    curCol++;
                    prevDir = 4;
                } else { // "enter left"
                    curRow--;
                    prevDir = 4;
                }
            } else if (cur == '7') {
                if (prevDir == 1) { // "enter below"
                    curCol--;
                    prevDir = 2;
                } else { // "enter right"
                    curRow++;
                    prevDir = 3;
                }
            } else if (cur == 'J') {
                if (prevDir == 2) {// from above
                    curCol--;
                    prevDir = 3;
                } else { // from the left
                    curRow--;
                    prevDir = 2;
                }
            } else if (cur == 'F') {
                if (prevDir == 1) {// from below
                    curCol++;
                    prevDir = 4;
                } else { // from the right
                    curRow++;
                    prevDir = 1;
                }
            }
            loopLength++;
        }
        System.out.println("total loop length: " + loopLength);
        
    }

    private void solvePartTwo() {

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
        new DayTen();
    }
}
