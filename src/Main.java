import puzzletest.PuzzleTest;

public class Main {

    private static final long TIMEOUT_IN_MILLIS = 10000;

    // Main
    public static void main(String[] args) {
        PuzzleTest puzzleTest = new PuzzleTest(TIMEOUT_IN_MILLIS);
        puzzleTest.runAll();
    }
}
