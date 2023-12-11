package week2;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.awt.Point;

public class DayEleven {
    private static final String FILE_NAME = "inputfiles/dayeleven.txt";

    private Scanner sc;

    public DayEleven() throws IOException {
        initScanner();
        solvePartOne();
        initScanner();
        solvePartTwo();
    }

    private void solvePartOne() {
        // manhattan distance anyone?
        // anyways, we need to do expansion FIRST
        // THEN handle mapping galaxies out.
        // so... get all of the strings out first.
        ArrayList<ArrayList<Character>> allLines = new ArrayList<>();

        while (sc.hasNextLine()) {
            ArrayList<Character> toAdd = new ArrayList<>();
            // we can handle row-expansion during parsing.
            String curLine = sc.nextLine();
            for (Character c : curLine.toCharArray()) {
                toAdd.add(c);
            }
            if (!curLine.contains("#")) {
                allLines.add(toAdd);
            } 
            allLines.add(toAdd);
        }

        // now we need to do column expansion. There's a reason why it's a character arraylist...
        // we'll use a finalized list for galaxy parsing, so we need another new intermediary for output for now...
        // at least, to avoid concurrency issues
        ArrayList<ArrayList<Character>> expandedGalaxy = new ArrayList<>();
        for (int i = 0; i < allLines.size(); i++)
            expandedGalaxy.add(new ArrayList<Character>());
        for (int i = 0; i < allLines.get(0).size(); i++) {
            int numGalaxies = 0;
            for (int j = 0; j < allLines.size(); j++) {
                if (allLines.get(j).get(i) == '#') 
                    numGalaxies++;
            }
            if (numGalaxies == 0) {
                // then we add a new column.
                for (int j = 0; j < allLines.size(); j++) {
                    expandedGalaxy.get(j).add('.');
                }
            }
            for (int j = 0; j < allLines.size(); j++) {
                expandedGalaxy.get(j).add(allLines.get(j).get(i));
            }
        }

        // ok, now parse for galaxies
        HashMap<Integer, Point> galaxies = new HashMap<>();
        int curRow = 0;
        int galaxyNum = 1;
        for (ArrayList<Character> bruh : expandedGalaxy) {
            if (bruh.contains('#')) {
                for (int i = 0; i < bruh.size(); i++) {
                    if (bruh.get(i) == '#') {
                        Point toAdd = new Point(curRow, i);
                        galaxies.put(galaxyNum, toAdd);
                        galaxyNum++;
                    }
                }
            }
            curRow++;
        }

        // there are <num galaxies> * <num galaxies - 1> pairs to generate...
        // they should all be distinct, too. So, that number becomes <num galaxies> * <num galaxies - 1> / 2.
        long totalDistances = 0;
        for (int i = 1; i <= galaxies.keySet().size(); i++) {
            for (int j = i + 1; j <= galaxies.keySet().size(); j++) {
                Point galaxyOne = galaxies.get(i);
                Point galaxyTwo = galaxies.get(j);
                totalDistances += (Math.abs(galaxyTwo.x - galaxyOne.x) + Math.abs(galaxyTwo.y - galaxyOne.y));
            }
        }
        System.out.println("Part 1 total distance between all galaxies: " + totalDistances);
    }

    private void solvePartTwo() {
        // manhattan distance anyone?
        // anyways, we need to do expansion FIRST
        // THEN handle mapping galaxies out.
        // so... get all of the strings out first.

        // now we do 1 million expansions...
        // idea has to change here, as brute force is not efficient enough.
        // what if we parse all of the galaxy positions first, and then search for rows/columns?
        // we can modify x and y accordingly.
        long numExpansions = 1000000;

        // beef the point to be an arraylist of longs since coordinates are going to get huge
        ArrayList<ArrayList<Character>> input = new ArrayList<>();
        HashMap<Integer, ArrayList<Long>> galaxies = new HashMap<>();
        long curRow = 0;
        int galaxyNum = 1;
        while (sc.hasNextLine()) {
            String curLine = sc.nextLine();
            if (curLine.contains("#")) {
                for (int i = 0; i < curLine.length(); i++) {
                    if (curLine.charAt(i) == '#') {
                        ArrayList<Long> coords = new ArrayList<>();
                        coords.add(curRow);
                        coords.add((long) i);
                        galaxies.put(galaxyNum, coords);
                        galaxyNum++;
                    }
                }
            }
            ArrayList<Character> bruh = new ArrayList<>();
            for (Character c : curLine.toCharArray()) 
                bruh.add(c);
            input.add(bruh);
            curRow++;
        }

        

        HashMap<Integer, ArrayList<Long>> updatedGalaxyMap = new HashMap<>();
        for (Integer key : galaxies.keySet())
            updatedGalaxyMap.put(key, new ArrayList<Long>(galaxies.get(key))); 
        // iterate over the rows of our input.
        for (int row = 0; row < input.size(); row++) {
            if (!input.get(row).contains('#')) {
                // then add to the coordinates of ALL galaxies below this.
                for (Integer galaxyKey : galaxies.keySet()) {
                    if (galaxies.get(galaxyKey).get(0) > row) {
                        updatedGalaxyMap.get(galaxyKey).add(0, updatedGalaxyMap.get(galaxyKey).get(0) + (numExpansions - 1));
                        updatedGalaxyMap.get(galaxyKey).remove(1);
                    }
                }
            }
        }

        // iterate over the columns of our input.
        for (int col = 0; col < input.get(0).size(); col++) {
            int galaxiesInCol = 0;
            for (int row = 0; row < input.size(); row++) {
                if (input.get(row).get(col) == '#') 
                    galaxiesInCol++;
            }
            if (galaxiesInCol == 0) {
                // expand for all of those to the right
                for (Integer galaxyKey : galaxies.keySet()) {
                    if (galaxies.get(galaxyKey).get(1) > col) {
                        updatedGalaxyMap.get(galaxyKey).add(1, updatedGalaxyMap.get(galaxyKey).get(1) + (numExpansions - 1));
                        updatedGalaxyMap.get(galaxyKey).remove(2);
                    }
                }
            }
        }
        
        // there are <num galaxies> * <num galaxies - 1> pairs to generate...
        // they should all be distinct, too. So, that number becomes <num galaxies> * <num galaxies - 1> / 2.
        long totalDistances = 0;
        for (int i = 1; i <= updatedGalaxyMap.keySet().size(); i++) {
            for (int j = i + 1; j <= updatedGalaxyMap.keySet().size(); j++) {
                ArrayList<Long> galaxyOne = updatedGalaxyMap.get(i);
                ArrayList<Long> galaxyTwo = updatedGalaxyMap.get(j);
                totalDistances += (Math.abs(galaxyTwo.get(0) - galaxyOne.get(0)) + Math.abs(galaxyTwo.get(1) - galaxyOne.get(1)));
            }
        }
        System.out.println("Part 2 total distance between all galaxies: " + totalDistances);
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
        new DayEleven();
    }
}
