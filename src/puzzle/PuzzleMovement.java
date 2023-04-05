package puzzle;

class PuzzleMovement {

    private int tile;
    private PuzzleDirection puzzleDirection;

    // Constructor
    public PuzzleMovement(int tile, PuzzleDirection puzzleDirection) {
        this.tile = tile;
        this.puzzleDirection = puzzleDirection;
    }

    // To string
    @Override
    public String toString() {
        return tile + " " + puzzleDirection;
    }
}
