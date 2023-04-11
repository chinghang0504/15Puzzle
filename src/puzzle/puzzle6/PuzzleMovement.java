package puzzle.puzzle6;

class PuzzleMovement {

    public final int TILE;
    public final PuzzleDirection PUZZLE_DIRECTION;

    // Constructor
    public PuzzleMovement(int tile, PuzzleDirection puzzleDirection) {
        TILE = tile;
        PUZZLE_DIRECTION = puzzleDirection;
    }

    // To string
    @Override
    public String toString() {
        return TILE + " " + PUZZLE_DIRECTION.DESCRIPTION;
    }
}
