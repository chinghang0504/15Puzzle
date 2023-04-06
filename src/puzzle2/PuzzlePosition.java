package puzzle2;

class PuzzlePosition {

    public final int row;
    public final int col;

    // Constructor
    public PuzzlePosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    // Get the next puzzle position
    public PuzzlePosition getNext(PuzzleDirection puzzleDirection) {
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

    // Is a valid puzzle position
    public boolean isValid(int dimension) {
        if (row < 0 || row >= dimension) {
            return false;
        }
        if (col < 0 || col >= dimension) {
            return false;
        }

        return true;
    }

    // Get the tile
    public int getTile(int[][] board) {
        return board[row][col];
    }

    // Set the tile
    public void setTile(int[][] board, int tile) {
        board[row][col] = tile;
    }
}
