package week3;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DayFifteen {
    private static final String FILE_NAME = "inputfiles/dayfifteen.txt";

    private Scanner sc;

    public DayFifteen() throws IOException {
        initScanner();
        solvePartOne();
        initScanner();
        solvePartTwo();
    }

    private void solvePartOne() {
        // That damn reindeer!
        // Oh well, guess we'll fix it ourselves.
        // there's only one line of input, so we'll just parse it immediately
        long verificationNumber = 0;
        String line = sc.nextLine();
        for (String s : line.split(",")) { // splitting on the commas generates all the hashes
            verificationNumber += hashForString(s);
        }
        System.out.println("part 1 verification number: " + verificationNumber);
    }

    private void solvePartTwo() {
        // this is a bit trickier
        // we now have labels for lenses (letters) that determine which box it goes into
        // those labels also stick with the lens strength after we insert it,
        // because we may need to REMOVE the lens or SWAP it out
        long focusingPower = 0;
        HashMap<Integer, ArrayList<Lens>> boxes = new HashMap<>();
        for (int i = 0; i < 256; i++)
            boxes.put(i, new ArrayList<Lens>()); // do this setup now, since it'll make things easier later...
        String line = sc.nextLine();
        for (String s : line.split(",")) { // for any given sequence in the input, we need to check and see
            // if it contains a "-" or a "=". It'll be one or the other. It dicatates behavior on what
            // to do with lenses.
            if (s.contains("-")) { // then it's an OP to see if we remove a lens from the box.
                // we want everything in the string up until the "-" to be passed to find box num
                String label = s.substring(0, s.indexOf("-"));
                int boxNum = hashForString(label);
                for (Lens l : boxes.get(boxNum)) {
                    if (l.checkIfLabelMatches(label)) {
                        boxes.get(boxNum).remove(l);
                        break;
                    }
                }
            } else { // equals. Replace an old lens with the same label if there is one
                // otherwise, we add the lens to the box.
                String label = s.substring(0, s.indexOf("="));
                int lensStrength = Integer.parseInt(s.substring(s.indexOf("=") + 1, s.length()));
                // construct a new lens object
                Lens toAdd = new Lens(lensStrength, label);
                int boxNum = hashForString(label);
                boolean added = false;
                for (Lens l : boxes.get(boxNum)) {
                    if (l.checkIfLabelMatches(label)) {
                        // we need to keep track of its index
                        int index = boxes.get(boxNum).indexOf(l);
                        boxes.get(boxNum).remove(l); // out with the old
                        boxes.get(boxNum).add(index, toAdd); // in with the new
                        added = true;
                        break;
                    }
                }
                if (!added) {
                    // in with the new.
                    boxes.get(boxNum).add(toAdd);
                }
            }
        }
        // now go through the boxes.
        for (Integer boxNum : boxes.keySet()) {
            ArrayList<Lens> boxLenses = boxes.get(boxNum);
            for (int i = 0; i < boxLenses.size(); i++) {
                focusingPower += ((boxNum + 1) * (i + 1) * boxLenses.get(i).getPower());
            }
        }

        System.out.println("part 2 focusing power: " + focusingPower);
    }

    class Lens { // data holder class... just makes the boxes easier to deal with.
        private int power;
        private String label;
        public Lens(int p, String l) {
            this.power = p;
            this.label = l;
        }

        public boolean checkIfLabelMatches(String labelGiven) {
            return this.label.equals(labelGiven);
        }

        public String toString() {
            return this.label + " " + this.power;
        }

        public int getPower() {
            return this.power;
        }
    }

    private int hashForString(String s) {
        int currentHashValue = 0;
        for (int i = 0; i < s.length(); i++) {
            // fetch the character, use its ASCII value
            Character c = s.charAt(i);
            currentHashValue += c;
            // multiply by 17
            currentHashValue *= 17;
            // set current value = current value % 256
            currentHashValue = currentHashValue % 256;
        }
        return currentHashValue;
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
        new DayFifteen();
    }
}
