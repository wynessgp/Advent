package week2;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DayEight {
    private static final String FILE_NAME = "inputfiles/dayeight.txt";

    private Scanner sc;

    public DayEight() throws IOException {
        initScanner();
        solvePartOne();
        initScanner();
        solvePartTwo();
    }

    private void solvePartOne() {
        // set up the mappings from an input to their L/R counterparts
        // get the full directions list, as well.
        ArrayList<Character> navDirections = new ArrayList<>();
        HashMap<String, ArrayList<String>> nodeMap = new HashMap<>();
        boolean fetchDirections = true;
        while (sc.hasNextLine()) {
            String curLine = sc.nextLine();
            if (fetchDirections) {
                for (int i = 0; i < curLine.length(); i++) {
                    navDirections.add(curLine.charAt(i));
                }
                // scooch the line pointer down one to avoid the empty line
                sc.nextLine();
                fetchDirections = false;
            } else {
                // parse the directions list...
                // some constants: node name is from indexes 0 to 3
                // right entry is at 12-15
                // left entry is at 7-10
                ArrayList<String> nodeList = new ArrayList<>();
                nodeList.add(curLine.substring(7, 10));
                nodeList.add(curLine.substring(12, 15));
                nodeMap.put(curLine.substring(0, 3), nodeList);
            }

        }
        // ok, start traversing the map...
        // we start at AAA, the target is ZZZ.
        String curLocation = "AAA";
        // keep going through the directions until we reach ZZZ!
        int indexInNavDirections = 0;
        int numSteps = 0;
        while (!curLocation.equals("ZZZ")) {
            // if we're at the end of nav directions, reset our index.
            // we keep going through UNTIL we reach ZZZ.
            if (indexInNavDirections == navDirections.size()) 
                indexInNavDirections = 0;
            else {
                if (navDirections.get(indexInNavDirections) == 'L') {
                    // select the first entry in our current node's locations...
                    curLocation = nodeMap.get(curLocation).get(0);
                } else {
                    curLocation = nodeMap.get(curLocation).get(1);
                }
                indexInNavDirections++;
                numSteps++;
            }
        }
        System.out.println("num steps for part 1: " + numSteps);
    }

    private void solvePartTwo() {
        // now we start traversing from ALL nodes that end in 'A'
        // and we wait until ALL of them end in 'Z' at the SAME TIME.
        ArrayList<Character> navDirections = new ArrayList<>();
        HashMap<String, ArrayList<String>> nodeMap = new HashMap<>();
        ArrayList<String> curLocs = new ArrayList<>();
        boolean fetchDirections = true;
        while (sc.hasNextLine()) {
            String curLine = sc.nextLine();
            if (fetchDirections) {
                for (int i = 0; i < curLine.length(); i++) {
                    navDirections.add(curLine.charAt(i));
                }
                // scooch the line pointer down one to avoid the empty line
                sc.nextLine();
                fetchDirections = false;
            } else {
                // parse the directions list...
                // some constants: node name is from indexes 0 to 3
                // right entry is at 12-15
                // left entry is at 7-10
                ArrayList<String> nodeList = new ArrayList<>();
                String nodeName = curLine.substring(0, 3);
                if (nodeName.charAt(2) == 'A') 
                    curLocs.add(nodeName);
                nodeList.add(curLine.substring(7, 10));
                nodeList.add(curLine.substring(12, 15));
                nodeMap.put(nodeName, nodeList);
            }

        }
        // better idea instead of brute forcing this:
        // we can find how many steps it takes for each one to reach 'Z' at the end
        // and find the LCM of all of the numbers.
        ArrayList<Integer> stepsForEachItem = new ArrayList<>();
        for (String curLocation : curLocs) {
            int indexInNavDirections = 0;
            int numSteps = 0;
            while (curLocation.charAt(2) != 'Z') {
                // keep going until we have something that ENDS in Z.
                if (indexInNavDirections == navDirections.size()) 
                    indexInNavDirections = 0;
                else {
                    if (navDirections.get(indexInNavDirections) == 'L') {
                        // select the first entry in our current node's locations...
                        curLocation = nodeMap.get(curLocation).get(0);
                    } else {
                        curLocation = nodeMap.get(curLocation).get(1);
                    }
                    indexInNavDirections++;
                    numSteps++;
                }
            }
            stepsForEachItem.add(numSteps);
        }
        // now we need to compute the LCM of this list...
        // make a few helper functions first...
        long totalNumSteps = 1;
        for (Integer steps : stepsForEachItem) 
            totalNumSteps = LCM(totalNumSteps, (long) steps);
        System.out.println("num steps for part 2: " + totalNumSteps);
    }

    // general algo found on stackoverflow at: https://stackoverflow.com/questions/17689529/lcm-of-all-the-numbers-in-an-array-in-java
    // cause I'm lazy :shrug:
    private static long gcd(long a, long b) {
        if (b == 0)
            return a;
        return gcd(b, a % b);
    }

    private static long LCM(long a, long b) {
        return a * b / gcd(a, b);
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
        new DayEight();
    }
}
