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

    // Is a valid puzzle position
    public boolean isValid(int dimension) {
        if (ROW < 0 || ROW >= dimension) {
            return false;
        }
        if (COL < 0 || COL >= dimension) {
            return false;
        }

        return true;
    }

    // Get the tile
    public int getTile(int[][] board) {
        return board[ROW][COL];
    }

    // Set the tile
    public void setTile(int[][] board, int tile) {
        board[ROW][COL] = tile;
    }
}
