package puzzle.puzzle2;

enum PuzzleDirection {

    UP("U"),
    DOWN("D"),
    LEFT("L"),
    RIGHT("R");

    public final String DESCRIPTION;

    // Constructor
    PuzzleDirection(String description) {
        DESCRIPTION = description;
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
        return DESCRIPTION;
    }
}
