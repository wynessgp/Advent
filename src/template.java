import java.io.File;
import java.io.IOException;
import java.util.*;

public class template {

    private static final String FILE_NAME = "inputfiles/<replaceme>.txt";

    private Scanner sc;

    public template() throws IOException {
        initScanner();
        solvePartOne();
        initScanner();
        solvePartTwo();
    }

    private void solvePartOne() {

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
        new template();
    }
}

