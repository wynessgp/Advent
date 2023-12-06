package week1;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.*;

public class DaySix {
    private static final String FILE_NAME = "inputfiles/daysix.txt";

    private Scanner sc;

    public DaySix() throws IOException {
        initScanner();
        solvePartOne();
        initScanner();
        solvePartTwo();
    }

    private void solvePartOne() {
        // only two lines of input to parse, interesting.
        ArrayList<Integer> times = new ArrayList<>();
        ArrayList<Integer> distances = new ArrayList<>();

        while (sc.hasNextLine()) {
            String curLine = sc.nextLine();
            if (curLine.contains("Time:")) {
                curLine = curLine.substring(11);
                String curNum = "";
                for (int i = 0; i < curLine.length(); i++) {
                    if (curLine.charAt(i) == ' ' && !curNum.isEmpty()) {
                        times.add(Integer.parseInt(curNum));
                        curNum = "";
                    } else if (Character.isDigit(curLine.charAt(i))) {
                        curNum += curLine.charAt(i);
                    }
                }
                if (!curNum.isEmpty())
                    times.add(Integer.parseInt(curNum));
            }
            if (curLine.contains("Distance:")) {
                curLine = curLine.substring(11);
                String curNum = "";
                for (int i = 0; i < curLine.length(); i++) {
                    if (curLine.charAt(i) == ' ' && !curNum.isEmpty()) {
                        distances.add(Integer.parseInt(curNum));
                        curNum = "";
                    } else if (Character.isDigit(curLine.charAt(i))) {
                        curNum += curLine.charAt(i);
                    }
                }
                if (!curNum.isEmpty())
                    distances.add(Integer.parseInt(curNum));
            }
        }

        long product = 1;
        for (int i = 0; i < times.size(); i++) {
            int count = 0;
            int maxTimeForRace = times.get(i);
            int reqDistanceForRace = distances.get(i);
            for (int j = 1; j < maxTimeForRace; j++) {
                if (j * (maxTimeForRace - j) > reqDistanceForRace) {
                    count++;
                }
            }
            if (count != 0)
                product *= count;
        }

        System.out.println("Part 1: Product of ways: " + product);

    }

    private void solvePartTwo() {
        // only two lines of input to parse, interesting.
        ArrayList<Long> times = new ArrayList<>();
        ArrayList<Long> distances = new ArrayList<>();

        while (sc.hasNextLine()) {
            String curLine = sc.nextLine();
            if (curLine.contains("Time:")) {
                curLine = curLine.substring(11);
                String curNum = "";
                for (int i = 0; i < curLine.length(); i++) {
                    if (Character.isDigit(curLine.charAt(i))) {
                        curNum += curLine.charAt(i);
                    }
                }
                if (!curNum.isEmpty())
                    times.add(Long.parseLong(curNum));
            }
            if (curLine.contains("Distance:")) {
                curLine = curLine.substring(11);
                String curNum = "";
                for (int i = 0; i < curLine.length(); i++) {
                    if (Character.isDigit(curLine.charAt(i))) 
                        curNum += curLine.charAt(i);
                }
                if (!curNum.isEmpty())
                    distances.add(Long.parseLong(curNum));
            }
        }

        long product = 1;
        for (int i = 0; i < times.size(); i++) {
            long count = 0;
            long maxTimeForRace = times.get(i);
            long reqDistanceForRace = distances.get(i);
            for (long j = 1; j < maxTimeForRace; j++) {
                if (j * (maxTimeForRace - j) > reqDistanceForRace) {
                    count++;
                }
            }
            if (count != 0)
                product *= count;
        }

        System.out.println("Part 2: Product of ways: " + product);
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
        new DaySix();
    }
}
