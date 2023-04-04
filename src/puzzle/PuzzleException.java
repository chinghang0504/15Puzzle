package puzzle;

class PuzzleException {

    // Bad File Exception
    public static class BadFileException extends RuntimeException {

        // Constructor
        public BadFileException(String message) {
            super(message);
            printStackTrace();
            System.exit(0);
        }
    }

    // Bad Order Exception
    public static class BadOrderException extends RuntimeException {

        // Constructor
        public BadOrderException(String message) {
            super(message);
            printStackTrace();
            System.exit(0);
        }
    }

    // Bad Board Exception
    public static class BadBoardException extends RuntimeException {

        // Constructor
        public BadBoardException(String message) {
            super(message);
            printStackTrace();
            System.exit(0);
        }
    }
}
