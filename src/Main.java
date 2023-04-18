import fifteen_puzzle.generator.Generator;
import fifteen_puzzle.solver.Solver3;
import fifteen_puzzle.test.PerformanceTest;

public class Main {

    private static final long MAX_DIMENSION = 20;
    private static final long EACH_DIMENSION_TESTCASE_SIZE = 10;
    private static final long TIMEOUT_IN_MILLIS = 1000;

    // Generate testcases
    private static void generateTestCases() {
        for (int i = 2; i <= MAX_DIMENSION; i++) {
            for (int j = 0; j < EACH_DIMENSION_TESTCASE_SIZE; j++) {
                Generator.generate(i);
            }
        }
    }

    // Start a performance test
    private static void startPerformanceTest() {
        PerformanceTest performanceTest = new PerformanceTest(TIMEOUT_IN_MILLIS);
        performanceTest.runAll();
    }

    // Start a performance test
    private static void startPerformanceTest(int puzzleSolverNumber) {
        PerformanceTest performanceTest = new PerformanceTest(TIMEOUT_IN_MILLIS);
        performanceTest.run(puzzleSolverNumber);
    }

    // Main
    public static void main(String[] args) {
         generateTestCases();
         startPerformanceTest();
    }
}
