package fifteen_puzzle.test;

import fifteen_puzzle.solver.Solver1;
import fifteen_puzzle.solver.Solver2;
import fifteen_puzzle.solver.Solver3;
import fifteen_puzzle.util.SolverResult;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PerformanceTest {

    public final long TIMEOUT_IN_MILLIS;

    public static final int START_PUZZLE_SOLVER_NUMBER = 1;
    public static final int END_PUZZLE_SOLVER_NUMBER = 3;

    private static final String RESULTS_DIRECTORY_NAME = "results";
    private static final String RESULT_FILE_NAME_1 = "puzzle_solver_result_";
    private static final String RESULT_FILE_NAME_2 = ".csv";
    private static final String CSV_HEADER = "File Name,Dimension,Open List Size,Closed List Size,Number of Steps in a Solution,Running Time\n";

    private static final String TESTCASES_DIRECTORY_NAME = "testcases";

    // Constructor
    public PerformanceTest(long timeoutInMillis) {
        TIMEOUT_IN_MILLIS = timeoutInMillis;
    }

    // Run
    public void run(int puzzleSolverNumber) {
        checkPuzzleSolverNumber(puzzleSolverNumber);

        System.out.println("Puzzle Solver " + puzzleSolverNumber + " - Testing all the cases ...\n");
        String resultPath = RESULTS_DIRECTORY_NAME + "/" + RESULT_FILE_NAME_1 + puzzleSolverNumber + RESULT_FILE_NAME_2;

        FileWriter fileWriter = setUpCSVFile(resultPath);
        runAllTestcases(fileWriter, resultPath, puzzleSolverNumber);
        closeCSVFile(fileWriter, resultPath);

        System.out.println("Puzzle Solver " + puzzleSolverNumber + " - Testing is finished");
    }

    // Run all
    public void runAll() {
        for (int i = PerformanceTest.START_PUZZLE_SOLVER_NUMBER; i <= PerformanceTest.END_PUZZLE_SOLVER_NUMBER; i++) {
            run(i);
        }
    }

    // Check the puzzle solver number
    private void checkPuzzleSolverNumber(int puzzleSolverNumber) {
        if (puzzleSolverNumber < START_PUZZLE_SOLVER_NUMBER || puzzleSolverNumber > END_PUZZLE_SOLVER_NUMBER) {
            throw new BadSolverNumberException(puzzleSolverNumber + " (Invalid puzzle solver number");
        }
    }

    // Set up the CSV file
    private FileWriter setUpCSVFile(String resultPath) {
        try {
            FileWriter fileWriter = new FileWriter(resultPath);
            fileWriter.write(CSV_HEADER);
            return fileWriter;
        } catch (IOException e) {
            throw new BadCSVFileException(resultPath + " (The system cannot create or modify the CSV file)");
        }
    }

    // Close a CSV file
    private void closeCSVFile(FileWriter fileWriter, String resultPath) {
        try {
            fileWriter.close();
        } catch (IOException e) {
            throw new BadCSVFileException(resultPath + " (The system cannot close the CSV file)");
        }
    }

    // Run all the testcases
    private void runAllTestcases(FileWriter fileWriter, String resultPath, int puzzleSolverNumber) {
        File file = new File(TESTCASES_DIRECTORY_NAME);
        String[] fileNames = file.list();

        for (String fileName : fileNames) {
            String path = TESTCASES_DIRECTORY_NAME + "/" + fileName;
            SolverResult solverResult = getPuzzleSolver(path, puzzleSolverNumber);
            Thread thread = new Thread((Runnable) solverResult);
            long runningTime = 0;

            long start = System.currentTimeMillis();
            thread.start();
            while (thread.isAlive() && runningTime < TIMEOUT_IN_MILLIS) {
                runningTime = System.currentTimeMillis() - start;
            }
            thread.interrupt();

            boolean finished = runningTime < TIMEOUT_IN_MILLIS;
            double runningTimeInSeconds = runningTime / 1000D;

            printResult(fileName, solverResult, finished, runningTimeInSeconds);
            writeToCSV(fileName, solverResult, finished, runningTimeInSeconds, fileWriter, resultPath);
        }
    }

    // Get the specified puzzle solver
    private SolverResult getPuzzleSolver(String path, int puzzleSolverNumber) {
        switch (puzzleSolverNumber) {
            case 1:
                return new Solver1(path);
            case 2:
                return new Solver2(path);
            case 3:
                return new Solver3(path);
            default:
                return null;
        }
    }

    // Print the result
    private void printResult(String fileName, SolverResult solverResult, boolean finished, double runningTimeInSeconds) {
        String output = "File Name: " + fileName + "\n";
        output += "Dimension: " + solverResult.getDimension() + "\n";
        output += "Open List Size: " + solverResult.getOpenListSize();
        output += finished ? "\n" : " (timeout)\n";
        output += "Closed List Size: " + solverResult.getClosedListSize();
        output += finished ? "\n" : " (timeout)\n";
        output += "Number of Steps in a Solution: ";
        output += finished ? solverResult.getSolutionStepSize() + "\n" : "NA (timeout)\n";
        output += "Running Time: ";
        output += finished ? runningTimeInSeconds + " s\n": "NA (timeout)\n";
        output += "\n";

        System.out.print(output);
    }

    // Write the result to the CSV file
    private void writeToCSV(String fileName, SolverResult solverResult, boolean finished, double runningTimeInSeconds, FileWriter fileWriter, String resultPath) {
        String output = fileName;
        output += "," + solverResult.getDimension();
        output += "," + solverResult.getOpenListSize();
        output += "," + solverResult.getClosedListSize();
        output += finished ? "," + solverResult.getSolutionStepSize() : ",";
        output += finished ? "," + runningTimeInSeconds : ",";
        output += "\n";

        try {
            fileWriter.write(output);
        } catch (IOException e) {
            throw new BadCSVFileException(resultPath + " (The system cannot modify the CSV file)");
        }
    }
}
