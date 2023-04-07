package puzzle.puzzle1;

class PuzzleMovement {

    private final int TILE;
    private final PuzzleDirection PUZZLE_DIRECTION;

    // Constructor
    public PuzzleMovement(int tile, PuzzleDirection puzzleDirection) {
        TILE = tile;
        PUZZLE_DIRECTION = puzzleDirection;
    }

    // To string
    @Override
    public String toString() {
        return TILE + " " + PUZZLE_DIRECTION;
    }
}
