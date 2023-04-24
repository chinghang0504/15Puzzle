package fifteen_puzzle.util;

// Bad Direction Symbol Exception
class BadDirectionSymbolException extends RuntimeException {

    // Constructor
    public BadDirectionSymbolException(String message) {
        super(message);
        printStackTrace();
        System.exit(0);
    }
}

// Bad Movement Exception
class BadMovementException extends RuntimeException {

    // Constructor
    public BadMovementException(String message) {
        super(message);
        printStackTrace();
        System.exit(0);
    }
}
