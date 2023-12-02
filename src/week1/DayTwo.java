package week1;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;

public class DayTwo {
    private static final String FILE_NAME = "inputfiles/daytwo.txt";
    private static int MAX_RED_CUBES = 12;
    private static int MAX_GREEN_CUBES = 13;
    private static int MAX_BLUE_CUBES = 14;

    private Scanner sc;

    public DayTwo() throws IOException {
        handleFileInput(FILE_NAME);
        solvePuzzle();
    }

    public void solvePuzzle() {
        // first things first: parse input lines into something usable.
        HashMap<Integer, ArrayList<Integer>> highestCubes = new HashMap<>();
        while (sc.hasNextLine()) {
            // start at 0.
            int highestRedCubes = 0;
            int highestBlueCubes = 0;
            int highestGreenCubes = 0;
            String curLine = sc.nextLine();
            // parse out the game's ID.
            String[] colonSplit = curLine.split(":");
            int gameId = Integer.parseInt(colonSplit[0].substring(5));
            // now create the list.
            System.out.println(curLine);
            for (String longString : colonSplit[1].split(";")) {
                for (String s : longString.split(",")) {
                    // this is now each individual entry. Remove the leading whitespace.
                    String cur = s.strip();
                    if (cur.contains("red")) {
                        int numReds = Integer.parseInt(cur.substring(0, cur.indexOf(' ')));
                        if (numReds > highestRedCubes)
                            highestRedCubes = numReds;
                    } else if (cur.contains("green")) {
                        int numGreens = Integer.parseInt(cur.substring(0, cur.indexOf(' ')));
                        if (numGreens > highestGreenCubes)
                            highestGreenCubes = numGreens;
                    } else if (cur.contains("blue")) {
                        int numBlues = Integer.parseInt(cur.substring(0, cur.indexOf(' ')));
                        if (numBlues > highestBlueCubes)
                            highestBlueCubes = numBlues;
                    }
                }
            }
            ArrayList<Integer> highest = new ArrayList<>();
            highest.add(highestRedCubes);
            highest.add(highestGreenCubes);
            highest.add(highestBlueCubes);
            highestCubes.put(gameId, highest);
        }

        int idSum = 0;
        for (Integer i : highestCubes.keySet()) {
            if (highestCubes.get(i).get(0) <= MAX_RED_CUBES &&
                highestCubes.get(i).get(1) <= MAX_GREEN_CUBES && 
                highestCubes.get(i).get(2) <= MAX_BLUE_CUBES) {
                idSum += i;
            }
        }
        int idPowerSum = 0;
        for (Integer i : highestCubes.keySet()) {
            ArrayList<Integer> cur = highestCubes.get(i);
            idPowerSum += (cur.get(0) * cur.get(1) * cur.get(2));
        }
        System.out.println("valid games sum: " + idSum);
        System.out.println("minimal cubes power sum: " + idPowerSum);
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
        new DayTwo();
    }
}
