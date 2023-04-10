package puzzle.puzzle3;

// Bad File Exception
class BadFileException extends RuntimeException {

    // Constructor
    public BadFileException(String message) {
        super(message);
        printStackTrace();
        System.exit(0);
    }
}

// Bad Dimension Exception
class BadDimensionException extends RuntimeException {

    // Constructor
    public BadDimensionException(String message) {
        super(message);
        printStackTrace();
        System.exit(0);
    }
}

// Bad Board Exception
class BadBoardException extends RuntimeException {

    // Constructor
    public BadBoardException(String message) {
        super(message);
        printStackTrace();
        System.exit(0);
    }
}

// Bad Solver Exception
class BadSolverException extends RuntimeException {

    // Constructor
    public BadSolverException() {
        super("This puzzle is not solved yet");
        printStackTrace();
        System.exit(0);
    }
}
