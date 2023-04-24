package fifteen_puzzle.generator;

// Bad Input Argument Exception
class BadInputArgumentException extends RuntimeException {

    // Constructor
    public BadInputArgumentException(String message) {
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
