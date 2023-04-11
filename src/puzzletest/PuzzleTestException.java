package puzzletest;

// Bad CSV File Exception
class BadCSVFileException extends RuntimeException {

    // Constructor
    public BadCSVFileException(String message) {
        super(message);
        printStackTrace();
        System.exit(0);
    }
}

// Bad Solver Number Exception
class BadSolverNumberException extends RuntimeException {

    // Constructor
    public BadSolverNumberException(String message) {
        super(message);
        printStackTrace();
        System.exit(0);
    }
}
