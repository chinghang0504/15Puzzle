package fifteen_puzzle.generator;

// Bad Dimension Exception
class BadDimensionException extends RuntimeException {

    // Constructor
    public BadDimensionException(String message) {
        super(message);
        printStackTrace();
        System.exit(0);
    }
}

// Bad File Exception
class BadFileException extends RuntimeException {

    // Constructor
    public BadFileException(String message) {
        super(message);
        printStackTrace();
        System.exit(0);
    }
}

// Bad Step Size Exception
class BadStepSizeException extends RuntimeException {

    // Constructor
    public BadStepSizeException(String message) {
        super(message);
        printStackTrace();
        System.exit(0);
    }
}
