import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class template {

    private static final String FILE_NAME = ".txt";

    private Scanner sc;

    public template() throws IOException {
        handleFileInput(FILE_NAME);
    }

    public void solvePuzzle() {

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
        new template();
    }
}

