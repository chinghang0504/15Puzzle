package puzzle;

enum PuzzleDirection {

    UP("U"),
    DOWN("D"),
    LEFT("L"),
    RIGHT("R");

    private final String description;

    // Constructor
    PuzzleDirection(String description) {
        this.description = description;
    }

    // Get the reversed puzzle direction
    public PuzzleDirection getReversed() {
        switch (this) {
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
            default:
                return null;
        }
    }

    // To string
    @Override
    public String toString() {
        return description;
    }
}
