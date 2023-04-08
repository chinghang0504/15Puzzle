package test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import puzzle.PuzzleResult;
import test.PuzzleTestException.*;

public class PuzzleTest {

    private static int puzzleSolverNumber;
    private static long timeoutInMillis;
    private static FileWriter fileWriter;

    private final static String RESULTS_DIRECTORY_NAME = "results";
    private final static String TESTCASES_DIRECTORY_NAME = "testcases";

    private final static String RESULT_FILE_NAME_1 = "puzzle_result_";
    private final static String RESULT_FILE_NAME_2 = ".csv";
    private final static String CSV_HEADER = "File Name,Dimension,Open List Size,Closed List Size,Number of Steps in a Solution,Running Time\n";

    // Run
    public void run(int puzzleSolverNumber, long timeoutInMillis) {
        System.out.println("Puzzle Solver " + puzzleSolverNumber + " - Testing all the cases ...\n");

        this.puzzleSolverNumber = puzzleSolverNumber;
        this.timeoutInMillis = timeoutInMillis;
        String resultPath = RESULTS_DIRECTORY_NAME + "/" + RESULT_FILE_NAME_1 + puzzleSolverNumber + RESULT_FILE_NAME_2;

        setUpCSVFile(resultPath);
        runAllTestcases(resultPath);
        closeCSVFile(resultPath);

        System.out.println("Puzzle Solver " + puzzleSolverNumber + " - Testing is finished");
    }

    // Set up the CSV file
    private void setUpCSVFile(String resultPath) {
        try {
            fileWriter = new FileWriter(resultPath);
            fileWriter.write(CSV_HEADER);
        } catch (IOException e) {
            throw new BadCSVFileException(resultPath + " (The system cannot create or modify the CSV file)");
        }
    }

    // Close a CSV file
    private void closeCSVFile(String resultPath) {
        try {
            fileWriter.close();
        } catch (IOException e) {
            throw new BadCSVFileException(resultPath + " (The system cannot close the CSV file)");
        }
    }

    // Run all the testcases
    private void runAllTestcases(String resultPath) {
        File file = new File(TESTCASES_DIRECTORY_NAME);
        String[] fileNames = file.list();

        for (String fileName : fileNames) {
            String path = TESTCASES_DIRECTORY_NAME + "/" + fileName;
            PuzzleResult puzzleResult = generatePuzzleSolver(path);

            Thread thread = new Thread((Runnable)puzzleResult);
            long runningTime = 0;
            long start = System.currentTimeMillis();

            thread.start();
            while (thread.isAlive() && runningTime < timeoutInMillis) {
                runningTime = System.currentTimeMillis() - start;
            }
            thread.interrupt();

            printResult(fileName, puzzleResult, runningTime);
            writeToCSV(fileName, puzzleResult, runningTime, resultPath);
        }
    }

    // Generate the specified puzzle solver
    private PuzzleResult generatePuzzleSolver(String path) {
        switch (puzzleSolverNumber) {
            case 1:
                return new puzzle.puzzle1.PuzzleSolver(path);
            case 2:
                return new puzzle.puzzle2.PuzzleSolver(path);
            case 3:
                return new puzzle.puzzle3.PuzzleSolver(path);
            case 4:
                return new puzzle.puzzle4.PuzzleSolver(path);
            default:
                throw new BadSolverNumberException(puzzleSolverNumber + " (Invalid puzzle solver number");
        }
    }

    // Print the result
    private void printResult(String fileName, PuzzleResult puzzleResult, long runningTime) {
        boolean finished = runningTime < timeoutInMillis;

        String output = "File Name: " + fileName + "\n";
        output += "Dimension: " + puzzleResult.getDimension() + "\n";
        output += "Open List Size: " + puzzleResult.getOpenListSize();
        output += finished ? "\n" : " (timeout)\n";
        output += "Closed List Size: " + puzzleResult.getClosedListSize();
        output += finished ? "\n" : " (timeout)\n";
        output += "Number of Steps in a Solution: ";
        output += finished ? puzzleResult.getSolutionStepSize() : "NA (timeout)";
        output += "\n";
        output += "Running Time: ";
        output += finished ? (runningTime / 1000D) + " s\n": "NA (timeout)\n";
        output += "\n";

        System.out.print(output);
    }

    // Write the result to the CSV file
    private void writeToCSV(String fileName, PuzzleResult puzzleResult, long runningTime, String resultPath) {
        boolean finished = runningTime < timeoutInMillis;

        String output = fileName;
        output += "," + puzzleResult.getDimension();
        output += "," + puzzleResult.getOpenListSize();
        output += "," + puzzleResult.getClosedListSize();
        output += ",";
        if (finished) {
            output += puzzleResult.getSolutionStepSize();
        }
        output += ",";
        if (finished) {
            output += (runningTime / 1000D);
        }
        output += "\n";

        try {
            fileWriter.write(output);
        } catch (IOException e) {
            throw new BadCSVFileException(resultPath + " (The system cannot modify the CSV file)");
        }
    }
}
