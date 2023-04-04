package puzzle;

class PuzzleAction {

    private int tile;
    private PuzzleDirection puzzleDirection;

    // Constructor
    public PuzzleAction(int tile, PuzzleDirection puzzleDirection) {
        this.tile = tile;
        this.puzzleDirection = puzzleDirection;
    }

    // To string
    @Override
    public String toString() {
        return tile + " " + puzzleDirection;
    }
}
