package puzzle;

import java.io.File;

class PuzzleTest {

    private static final String TESTCASES_DIRECTORY = "testcases/";
    private static final int TIMEOUT_IN_MILLIS = 300000;

    // Run a test
    private static void runTest() {
        File file = new File(TESTCASES_DIRECTORY);
        String[] fileNames = file.list();
        long runningTime = 0;

        for (String fileName: fileNames) {
            PuzzleSolver puzzleSolver = new PuzzleSolver(TESTCASES_DIRECTORY + fileName);
            Thread thread = new Thread(puzzleSolver);

            boolean timeout = false;
            long start = System.currentTimeMillis();
            thread.start();
            while (thread.isAlive() && !timeout) {
                runningTime = System.currentTimeMillis() - start;
                if (runningTime > TIMEOUT_IN_MILLIS) {
                    timeout = true;
                }
            }
            if (timeout) {
                thread.interrupt();
            }

            System.out.print(getResult(fileName, puzzleSolver, timeout, runningTime));
            System.out.println("");
        }
    }

    // Get a result
    private static String getResult(String fileName, PuzzleSolver puzzleSolver, boolean timeout, long runningTime) {
        String output = "File Name: " + fileName + "\n";
        output += puzzleSolver.getStats(timeout);
        output += "Running Time: ";
        output += timeout ? "timeout" : (runningTime / 1000d);
        output += "\n";
        return output;
    }

    // Main
    public static void main(String[] args) {
        runTest();
    }
}
