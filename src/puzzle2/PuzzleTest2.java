package puzzle2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import puzzle2.PuzzleException.*;

public class PuzzleTest2 {

    private FileWriter fileWriter;

    private final static long TIMEOUT_IN_MILLIS = 60000;

    private final static String RESULT_FILE_NAME = "puzzle_result_2.csv";
    private final static String CSV_HEADER = "File Name,Dimension,Open List Size,Closed List Size,Number of Steps in a Solution,Running Time\n";
    private final static String TESTCASES_DIRECTORY_NAME = "testcases";

    // Run
    public void run() {
        System.out.println("Running all the testcases ...\n");
        writeCSVHeader();
        runAllTestcases();
        closeCSVFile();
        System.out.println("\nThe program is finished.");
    }

    // Write a CSV header
    private void writeCSVHeader() {
        try {
            fileWriter = new FileWriter(RESULT_FILE_NAME);
            fileWriter.write(CSV_HEADER);
        } catch (IOException e) {
            throw new BadFileException(RESULT_FILE_NAME + " (The system cannot modify the CSV file)");
        }
    }

    // Close a CSV file
    private void closeCSVFile() {
        try {
            fileWriter.close();
        } catch (IOException e) {
            throw new BadFileException(RESULT_FILE_NAME + " (The system cannot close the CSV file)");
        }
    }

    // Run all the testcases
    private void runAllTestcases() {
        File file = new File(TESTCASES_DIRECTORY_NAME);
        String[] fileNames = file.list();

        for (String fileName : fileNames) {
            PuzzleSolver puzzleSolver = new PuzzleSolver(TESTCASES_DIRECTORY_NAME + "/" + fileName);
            Thread thread = new Thread(puzzleSolver);
            long runningTime = 0;

            long start = System.currentTimeMillis();
            thread.start();

            while (thread.isAlive() && runningTime < TIMEOUT_IN_MILLIS) {
                runningTime = System.currentTimeMillis() - start;
            }
            thread.interrupt();

            printResult(fileName, puzzleSolver, runningTime);
            writeToCSV(fileName, puzzleSolver, runningTime);
        }
    }

    // Print the result
    private void printResult(String fileName, PuzzleSolver puzzleSolver, long runningTime) {
        boolean finished = runningTime < TIMEOUT_IN_MILLIS;

        String output = "File Name: " + fileName + "\n";
        output += "Dimension: " + puzzleSolver.getDimension() + "\n";
        output += "Open List Size: " + puzzleSolver.getOpenListSize();
        output += finished ? "\n" : " (timeout)\n";
        output += "Closed List Size: " + puzzleSolver.getClosedListSize();
        output += finished ? "\n" : " (timeout)\n";
        output += "Number of Steps in a Solution: ";
        output += finished ? puzzleSolver.getSolutionStepSize() : "NA (timeout)";
        output += "\n";
        output += "Running Time: ";
        output += finished ? (runningTime / 1000D) + " s": "NA (timeout)";
        output += "\n\n";

        System.out.print(output);
    }

    // Write the result to the CSV file
    private void writeToCSV(String fileName, PuzzleSolver puzzleSolver, long runningTime) {
        boolean finished = runningTime < TIMEOUT_IN_MILLIS;

        String output = fileName;
        output += "," + puzzleSolver.getDimension();
        output += "," + puzzleSolver.getOpenListSize();
        output += "," + puzzleSolver.getClosedListSize();
        output += ",";
        if (finished) {
            output += puzzleSolver.getSolutionStepSize();
        }
        output += ",";
        if (finished) {
            output += (runningTime / 1000D);
        }
        output += "\n";

        try {
            fileWriter.write(output);
        } catch (IOException e) {
            throw new BadFileException(RESULT_FILE_NAME + " (The system cannot modify the CSV file)");
        }
    }
}
