package puzzle;

// Puzzle Position
public class PuzzlePosition {

    int row;
    int col;

    static final int DIRECTION_SIZE = 4;

    // Constructor
    public PuzzlePosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    // Get the next puzzle position
    public PuzzlePosition getNext(int direction) {
        if (direction == 0) {
            // Up
            return new PuzzlePosition(row-1, col);
        } else if (direction == 1) {
            // Down
            return new PuzzlePosition(row+1, col);
        } else if (direction == 2) {
            // Left
            return new PuzzlePosition(row, col-1);
        } else if (direction == 3) {
            // Right
            return new PuzzlePosition(row, col+1);
        }

        return null;
    }
}
