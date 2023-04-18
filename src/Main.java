import fifteen_puzzle.generator.Generator;
import fifteen_puzzle.test.PerformanceTest;

public class Main {

    private static final long TIMEOUT_IN_MILLIS = 5000;

    // Main
    public static void main(String[] args) {
        // Generate
        for (int i = 2; i <= 10; i++) {
            for (int j = 0; j < 5; j++) {
                Generator.generate(i);
            }
        }

        PerformanceTest performanceTest = new PerformanceTest(TIMEOUT_IN_MILLIS);
        performanceTest.runAll();
    }
}
