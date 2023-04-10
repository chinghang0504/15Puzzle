package puzzle.puzzle2;

class PuzzlePosition {

    public final int ROW;
    public final int COL;

    // Constructor
    public PuzzlePosition(int row, int col) {
        ROW = row;
        COL = col;
    }

    // Get the next puzzle position
    public PuzzlePosition getNext(PuzzleDirection puzzleDirection) {
        switch (puzzleDirection) {
            case UP:
                return new PuzzlePosition(ROW-1, COL);
            case DOWN:
                return new PuzzlePosition(ROW+1, COL);
            case LEFT:
                return new PuzzlePosition(ROW, COL-1);
            case RIGHT:
                return new PuzzlePosition(ROW, COL+1);
            default:
                return null;
        }
    }
}
