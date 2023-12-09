package week2;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DayNine {
    private static final String FILE_NAME = "inputfiles/daynine.txt";

    private Scanner sc;

    public DayNine() throws IOException {
        initScanner();
        solvePartOne();
        initScanner();
        solvePartTwo();
    }

    private void solvePartOne() {
        // each line of input represents a sequence.
        // find the difference in the numbers of the sequence.
        // if the differences are ALL 0, then we stop making sequences.
        // otherwise, the the sequences becomes input the next time.
        ArrayList<ArrayList<Long>> originalSequences = new ArrayList<>();
        while (sc.hasNextLine()) {
            String curLine = sc.nextLine();
            ArrayList<Long> newestSeq = new ArrayList<>();
            for (String num : curLine.split(" "))
                newestSeq.add(Long.parseLong(num));
            originalSequences.add(newestSeq);
        }

        // now that we have all of the original sequences, we need to look at their expansion...
        // note that this expansion is also sequence... so we'll probably want another stacked AL
        long extrapolatedValuesSum = 0;
        for (ArrayList<Long> seqToExpand : originalSequences) {
            ArrayList<ArrayList<Long>> expandedHistory = new ArrayList<>();
            expandedHistory.add(seqToExpand);
            int levelsDeep = 0;
            int numZeros = 0;
            int originalLength = expandedHistory.get(0).size();
            while (numZeros != originalLength - levelsDeep) {
                // pull the most recent history...
                // also, we're making a new sequence, so we don't know how many
                // zeros we have yet.
                numZeros = 0;
                ArrayList<Long> mostRecent = expandedHistory.get(levelsDeep);
                ArrayList<Long> newSeq = new ArrayList<>();
                // we are always looking at two numbers at a time...
                for (int i = 0; i < mostRecent.size() - 1; i++) {
                    // long diff = Math.abs(mostRecent.get(i + 1) - mostRecent.get(i));
                    long diff = mostRecent.get(i + 1) - mostRecent.get(i);
                    if (diff == 0) {
                        numZeros++;
                    }
                    newSeq.add(diff);
                }
                // now that we've created our new sequence, say we're another level deep
                // and put the newly generated one on our history
                expandedHistory.add(newSeq);
                levelsDeep++;
            }
            // now, traverse our expanded history in REVERSE, in order to
            // extrapolate the newest value. So, start at the all 0 sequence,
            // tack on another zero, then fetch the end of the layer below us, and
            // the last number in our current layer. repeat until we reach the first
            // sequence.

            // screw you java, and stupid 0L rules.
            expandedHistory.get(expandedHistory.size() - 1).add(0L);
            // ok, now begin our pattern. Start at the 2nd to last layer,
            // and go until the topmost.
            for (int i = expandedHistory.size() - 2; i >= 0; i--) {
                Long prevEndValue = expandedHistory.get(i + 1).get(expandedHistory.get(i + 1).size() - 1);
                Long curEndValue = expandedHistory.get(i).get(expandedHistory.get(i).size() - 1);
                expandedHistory.get(i).add(prevEndValue + curEndValue);
            }
            // get the VERY last entry of the first sequence, since we just added on the newest value.
            extrapolatedValuesSum += expandedHistory.get(0).get(expandedHistory.get(0).size() - 1);
        }
        System.out.println("part 1 extrapolated values sum: " + extrapolatedValuesSum);


    }

    private void solvePartTwo() {
        // only difference from P1 is we extrapolate on the LHS instead
        ArrayList<ArrayList<Long>> originalSequences = new ArrayList<>();
        while (sc.hasNextLine()) {
            String curLine = sc.nextLine();
            ArrayList<Long> newestSeq = new ArrayList<>();
            for (String num : curLine.split(" "))
                newestSeq.add(Long.parseLong(num));
            originalSequences.add(newestSeq);
        }

        // now that we have all of the original sequences, we need to look at their expansion...
        // note that this expansion is also sequence... so we'll probably want another stacked AL
        long extrapolatedValuesSum = 0;
        for (ArrayList<Long> seqToExpand : originalSequences) {
            ArrayList<ArrayList<Long>> expandedHistory = new ArrayList<>();
            expandedHistory.add(seqToExpand);
            int levelsDeep = 0;
            int numZeros = 0;
            int originalLength = expandedHistory.get(0).size();
            while (numZeros != originalLength - levelsDeep) {
                // pull the most recent history...
                // also, we're making a new sequence, so we don't know how many
                // zeros we have yet.
                numZeros = 0;
                ArrayList<Long> mostRecent = expandedHistory.get(levelsDeep);
                ArrayList<Long> newSeq = new ArrayList<>();
                // we are always looking at two numbers at a time...
                for (int i = 0; i < mostRecent.size() - 1; i++) {
                    // long diff = Math.abs(mostRecent.get(i + 1) - mostRecent.get(i));
                    long diff = mostRecent.get(i + 1) - mostRecent.get(i);
                    if (diff == 0) {
                        numZeros++;
                    }
                    newSeq.add(diff);
                }
                // now that we've created our new sequence, say we're another level deep
                // and put the newly generated one on our history
                expandedHistory.add(newSeq);
                levelsDeep++;
            }
            // now, traverse our expanded history in REVERSE, in order to
            // extrapolate the newest value. So, start at the all 0 sequence,
            // tack on another zero, then fetch the end of the layer below us, and
            // the last number in our current layer. repeat until we reach the first
            // sequence.

            // screw you java, and stupid 0L rules.
            expandedHistory.get(expandedHistory.size() - 1).add(0L);
            // ok, now begin our pattern. Start at the 2nd to last layer,
            // and go until the topmost.
            for (int i = expandedHistory.size() - 2; i >= 0; i--) {
                Long prevEndValue = expandedHistory.get(i + 1).get(0);
                Long curEndValue = expandedHistory.get(i).get(0);
                expandedHistory.get(i).add(0, curEndValue - prevEndValue);
            }
            // get the VERY last entry of the first sequence, since we just added on the newest value.
            extrapolatedValuesSum += expandedHistory.get(0).get(0);
        }
        System.out.println("part 1 extrapolated values sum: " + extrapolatedValuesSum);
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
        new DayNine();
    }
}
