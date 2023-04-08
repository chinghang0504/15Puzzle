import test.PuzzleTest;

public class Main {

    // Main
    public static void main(String[] args) {
        long timeout = 60000;
        int start = 1;
        int end = 4;

        PuzzleTest puzzleTest = new PuzzleTest();
        for (int i = start; i <= end; i++) {
            puzzleTest.run(i, timeout);
        }
    }
}
