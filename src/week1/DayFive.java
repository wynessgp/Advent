package week1;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.HashSet;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;

public class DayFive {
    private static final String FILE_NAME = "inputfiles/dayfive.txt";

    private Scanner sc;

    public DayFive() throws IOException {
        handleFileInput(FILE_NAME);
        solvePartOne();
        // solvePartTwo();
    }

    private void solvePartOne() {
        // first... parse the input. as usual.
        HashSet<Long> seedNums = new HashSet<>();
        ArrayList<ArrayList<Long>> numsForMaps = new ArrayList<>();
        for (int i = 0; i < 7; i++) 
            numsForMaps.add(new ArrayList<Long>());

        int activeMap = -1;

        while (sc.hasNextLine()) {
            String curLine = sc.nextLine();
            int whichMap = determineWhichMapIsActive(curLine);
            if (whichMap != -1) {
                activeMap = whichMap;
            }

            if (curLine.contains("seeds:")) { 
                // Parse the first line. Cut it off, and get the actual nums
                curLine = curLine.substring(curLine.indexOf(":") + 2);
                for (String s : Arrays.asList(curLine.split(" "))) 
                    seedNums.add(Long.parseLong(s));
            } else if (!curLine.isEmpty() && !Character.isAlphabetic(curLine.charAt(0))) {
                String[] splitOnEmpty = curLine.split(" ");
                // adds the destination range, source range, then range len.
                // so everything implicitly comes in groups of 3...
                for (int i = 0; i < splitOnEmpty.length; i++) {
                    long toAdd = Long.parseLong(splitOnEmpty[i]);
                    // let's think about this: I am given three values.
                    // The first of which is a destination range start.
                    // The second of which is a source range start.
                    // The third of which is the range length.
                    // So, for some i in [src-start : src-start + range]
                    // I should return the equivalent in [dest-start : dest-start + range]
                    // It is in theory easier to just store these three values and do more
                    // careful looping later on.
                    numsForMaps.get(activeMap).add(toAdd);
                }
            }
        }

        long lowestLocation = Long.MAX_VALUE;
        for (Long seedNum : seedNums) {
            long prevValue = seedNum;
            for (int i = 0; i < numsForMaps.size(); i++) {
                // pull the arrayList, to make things easier.
                ArrayList<Long> curMap = numsForMaps.get(i);
                // now: loop through the different ranges.
                for (int j = 0; j < curMap.size() - 2; j += 3) {
                    long destStart = curMap.get(j);
                    long srcStart = curMap.get(j + 1);
                    long range = curMap.get(j + 2);
                    // if it falls somewhere in the range, then we have a re-assigned mapping.
                    if (prevValue >= srcStart && prevValue < srcStart + range) {
                        // find out "how much" of an offset for dest.
                        long offset = prevValue - srcStart;
                        prevValue = destStart + offset;
                        // it's not necessary to keep looking.
                        break;
                    } else {
                        // force the next iteration.
                        continue;
                    }
                }
            }
            if (prevValue < lowestLocation) {
                lowestLocation = prevValue;
            }
        }
        

        System.out.println("Part 1 lowest location: " + lowestLocation);

       
    }

    public void solvePartTwo() {
        // first... parse the input. as usual.
        ArrayList<Long> seedNumsAndRanges = new ArrayList<>();
        ArrayList<ArrayList<Long>> numsForMaps = new ArrayList<>();
        for (int i = 0; i < 7; i++) 
            numsForMaps.add(new ArrayList<Long>());

        int activeMap = -1;

        while (sc.hasNextLine()) {
            String curLine = sc.nextLine();
            int whichMap = determineWhichMapIsActive(curLine);
            if (whichMap != -1) {
                activeMap = whichMap;
            }

            if (curLine.contains("seeds:")) { 
                // Parse the first line. Cut it off, and get the actual nums
                curLine = curLine.substring(curLine.indexOf(":") + 2);
                String[] splitOnSpaces = curLine.split(" ");
                for (int i = 0; i < splitOnSpaces.length - 1; i += 2) {
                    // seed num
                    seedNumsAndRanges.add(Long.parseLong(splitOnSpaces[i]));
                    // range
                    seedNumsAndRanges.add(Long.parseLong(splitOnSpaces[i + 1]));
                }
                    
            } else if (!curLine.isEmpty() && !Character.isAlphabetic(curLine.charAt(0))) {
                String[] splitOnEmpty = curLine.split(" ");
                // adds the destination range, source range, then range len.
                // so everything implicitly comes in groups of 3...
                for (int i = 0; i < splitOnEmpty.length; i++) {
                    long toAdd = Long.parseLong(splitOnEmpty[i]);
                    // let's think about this: I am given three values.
                    // The first of which is a destination range start.
                    // The second of which is a source range start.
                    // The third of which is the range length.
                    // So, for some i in [src-start : src-start + range]
                    // I should return the equivalent in [dest-start : dest-start + range]
                    // It is in theory easier to just store these three values and do more
                    // careful looping later on.
                    numsForMaps.get(activeMap).add(toAdd);
                }
            }
        }

        long lowestLocation = Long.MAX_VALUE;
        // this changes to reflect that it's intervals now.
        // if we don't want Java to crash while running, we need
        // to be smarter about this, because we can't run through 
        // literally EVERY single number in every interval...

        // so, we probably want to do an interval overlap idea...
        HashSet<Long> suitableSeedNums = new HashSet<>();
        ArrayList<Long> soilMap = numsForMaps.get(0);
        for (int i = 0; i < seedNumsAndRanges.size() - 1; i += 2) {
            long seedStart = seedNumsAndRanges.get(i);
            long seedRange = seedNumsAndRanges.get(i + 1);
            for (int j = 0; j < soilMap.size() - 2; j += 3) {
                long soilStart = soilMap.get(j);
                long soilSeedStart = soilMap.get(j + 1);
                long soilRange = soilMap.get(j + 2);

                // check for an overlap between these two ranges.
                if (Math.max(seedStart, soilSeedStart) <= 
                    Math.min(seedStart + seedRange, soilSeedStart + soilRange)) {
                    if (seedStart <= soilSeedStart) {
                        for (long z = seedStart; z <= soilSeedStart; z++)
                            suitableSeedNums.add(seedStart + z);
                    } else {
                        for (long z = soilSeedStart; z <= seedStart; z++)
                            suitableSeedNums.add(soilSeedStart + z);
                    }
                }
            }
        }
        System.out.println("suitable nums: " + suitableSeedNums);


            // long prevValue = seedNum;
            // for (int i = 0; i < numsForMaps.size(); i++) {
            //     // pull the arrayList, to make things easier.
            //     ArrayList<Long> curMap = numsForMaps.get(i);
            //     // now: loop through the different ranges.
            //     for (int j = 0; j < curMap.size() - 2; j += 3) {
            //         long destStart = curMap.get(j);
            //         long srcStart = curMap.get(j + 1);
            //         long range = curMap.get(j + 2);
            //         // if it falls somewhere in the range, then we have a re-assigned mapping.
            //         if (prevValue >= srcStart && prevValue < srcStart + range) {
            //             // find out "how much" of an offset for dest.
            //             long offset = prevValue - srcStart;
            //             prevValue = destStart + offset;
            //             // it's not necessary to keep looking.
            //             break;
            //         } else {
            //             // force the next iteration.
            //             continue;
            //         }
            //     }
            // }
            // if (prevValue < lowestLocation) {
            //     lowestLocation = prevValue;
            // }
        

        System.out.println("Part 2 lowest location: " + lowestLocation);
    }

    public int determineWhichMapIsActive(String curLine) {
        if (curLine.contains("seed-to-soil"))
            return 0;
        if (curLine.contains("soil-to-fertilizer"))
            return 1;
        if (curLine.contains("fertilizer-to-water"))
            return 2;
        if (curLine.contains("water-to-light"))
            return 3;
        if (curLine.contains("light-to-temperature"))
            return 4;
        if (curLine.contains("temperature-to-humidity"))
            return 5;
        if (curLine.contains("humidity-to-location"))
            return 6;
        
        return -1;
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
        new DayFive();
    }
}
