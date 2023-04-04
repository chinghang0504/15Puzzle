package puzzle;

class PuzzlePosition {

    public final int row;
    public final int col;

    // Constructor
    public PuzzlePosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    // Get the next puzzle position
    public PuzzlePosition getNextPuzzlePosition(PuzzleDirection puzzleDirection) {
        switch (puzzleDirection) {
            case UP:
                return new PuzzlePosition(row-1, col);
            case DOWN:
                return new PuzzlePosition(row+1, col);
            case LEFT:
                return new PuzzlePosition(row, col-1);
            case RIGHT:
                return new PuzzlePosition(row, col+1);
            default:
                return null;
        }
    }

    // Is valid puzzle position
    public boolean isValid(int order) {
        if (row < 0 || row >= order) {
            return false;
        }
        if (col < 0 || col >= order) {
            return false;
        }

        return true;
    }
}
