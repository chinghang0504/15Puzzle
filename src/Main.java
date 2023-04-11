import test.PuzzleTest;

public class Main {

    private static final long TIMEOUT_IN_MILLIS = 10000;

    // Main
    public static void main(String[] args) {
        PuzzleTest puzzleTest = new PuzzleTest(TIMEOUT_IN_MILLIS);
//        for (int i = PuzzleTest.START_PUZZLE_SOLVER_NUMBER; i <= PuzzleTest.END_PUZZLE_SOLVER_NUMBER; i++) {
//            puzzleTest.run(i);
//        }
        puzzleTest.run(1);
        puzzleTest.run(2);
        puzzleTest.run(3);
        puzzleTest.run(4);
        puzzleTest.run(6);
    }
}
