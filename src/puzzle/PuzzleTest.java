package puzzle;

import java.io.File;
import java.util.ArrayList;

public class PuzzleTest {
    private static final String directoryName = "testcases";

    // Run all the testcases
    private static void run() {
        File file = new File(directoryName);
        String[] fileNames = file.list();

        for (String fileName : fileNames) {
            String path = directoryName + "/" + fileName;
            PuzzleSolver puzzleSolver = new PuzzleSolver(path);

            long start = System.currentTimeMillis();
            puzzleSolver.solve();
            long end = System.currentTimeMillis();
            double timeTaken = (double) (end - start) / 1000;

            System.out.println("File Name: " + fileName);
            System.out.print(puzzleSolver.getStats());
            System.out.println("Time Taken: " + timeTaken);
            System.out.println("");
        }
    }

    // Main
    public static void main(String[] args) {
        run();
    }
}
