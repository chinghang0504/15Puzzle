package test;

class PuzzleTestException {

    // Bad CSV File Exception
    public static class BadCSVFileException extends RuntimeException {

        // Constructor
        public BadCSVFileException(String message) {
            super(message);
            printStackTrace();
            System.exit(0);
        }
    }

    // Bad Solver Number Exception
    public static class BadSolverNumberException extends RuntimeException {

        // Constructor
        public BadSolverNumberException(String message) {
            super(message);
            printStackTrace();
            System.exit(0);
        }
    }
}
