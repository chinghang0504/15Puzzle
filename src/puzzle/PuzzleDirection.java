package puzzle;

enum PuzzleDirection {

    UP("U"),
    DOWN("D"),
    LEFT("L"),
    RIGHT("R");

    private final String description;

    public static final int DIRECTION_NUMBER = 4;

    // Constructor
    PuzzleDirection(String description) {
        this.description = description;
    }

    // Get reversed puzzle direction
    public PuzzleDirection getReversedPuzzleDirection() {
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
