import fifteen_puzzle.generator.Generator;
import fifteen_puzzle.test.PerformanceTest;

public class Main {

    private static int minDimension = 2;
    private static int maxDimension = 30;
    private static int eachTestcaseSize = 10;
    private static int stepSize = 1000000;
    private static int timeoutInMillis = 1000;

    // Start a performance test
    private static void startPerformanceTest() {
        PerformanceTest performanceTest = new PerformanceTest(timeoutInMillis);
        performanceTest.runAll();
    }

    // Start a performance test
    private static void startPerformanceTest(int puzzleSolverNumber) {
        PerformanceTest performanceTest = new PerformanceTest(timeoutInMillis);
        performanceTest.run(puzzleSolverNumber);
    }

    // Main
    public static void main(String[] args) {
        Generator.generate(minDimension, maxDimension, eachTestcaseSize, stepSize);
        startPerformanceTest();
    }
}
