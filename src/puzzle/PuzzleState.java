package puzzle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

class PuzzleState {

    private final int[][] board;
    private final int dimension;
    private final int maxTile;

    private int stepCount;
    private int manhattanDistance;
    private PuzzlePosition maxTilePuzzlePosition;

    private PuzzleState prevPuzzleState;
    private PuzzleMovement prevPuzzleMovement;

    // Constructor (Initial State)
    public PuzzleState(int[][] board) {
        this.board = board;
        dimension = this.board.length;
        maxTile = dimension * dimension;

        updateManhattanDistance();
    }

    // Constructor (Next State)
    public PuzzleState(PuzzleState prevPuzzleState, PuzzlePosition nextPuzzlePosition, PuzzleDirection nextPuzzleDirection) {
        dimension = prevPuzzleState.dimension;
        maxTile = prevPuzzleState.maxTile;
        board = new int[dimension][dimension];

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                board[i][j] = prevPuzzleState.board[i][j];
            }
        }

        int nextTile = board[nextPuzzlePosition.row][nextPuzzlePosition.col];
        board[prevPuzzleState.maxTilePuzzlePosition.row][prevPuzzleState.maxTilePuzzlePosition.col] = nextTile;
        board[nextPuzzlePosition.row][nextPuzzlePosition.col] = maxTile;

        this.prevPuzzleState = prevPuzzleState;
        prevPuzzleMovement = new PuzzleMovement(nextTile, nextPuzzleDirection);

        stepCount = prevPuzzleState.stepCount + 1;
        updateManhattanDistance();
    }

    // Update the manhattan distance
    private void updateManhattanDistance() {
        manhattanDistance = 0;

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (board[i][j] == maxTile) {
                    maxTilePuzzlePosition = new PuzzlePosition(i, j);
                    continue;
                } else {
                    int row = (board[i][j] - 1) / dimension;
                    int col = (board[i][j] - 1) % dimension;
                    manhattanDistance += Math.abs(row - i) + Math.abs(col - j);
                }
            }
        }
    }

    // Get the step count
    public int getStepCount() {
        return stepCount;
    }

    // Get the manhattan distance
    public int getManhattanDistance() {
        return manhattanDistance;
    }

    // Get the estimated cost
    public int getEstimatedCost() {
        return stepCount + manhattanDistance;
    }

    // Get the previous puzzle state
    public PuzzleState getPrevPuzzleState() {
        return prevPuzzleState;
    }

    // Get the previous puzzle movement
    public PuzzleMovement getPrevPuzzleMovement() {
        return prevPuzzleMovement;
    }

    // Convert tile to string
    private String tileToString(int tile) {
        if (tile == maxTile) {
            return "  ";
        } else {
            String output = String.valueOf(tile);
            if (output.length() == 1) {
                output = " " + output;
            }
            return output;
        }
    }

    // Get the board
    private String getBoard() {
        String output = "";

        for (int i = 0; i < dimension; i++) {
            output += tileToString(board[i][0]);
            for (int j = 1; j < dimension; j++) {
                output += " " + tileToString(board[i][j]);
            }
            output += "\n";
        }

        return output;
    }

    // Get the info
    private String getInfo() {
        String output = "Step Count: " + stepCount + "\n";
        output += "Manhattan Distance: " + manhattanDistance + "\n";
        output += "Estimated Cost: " + getEstimatedCost() + "\n";
        output += "Previous Puzzle State: ";
        output += prevPuzzleState == null ? null : Integer.toHexString(prevPuzzleState.hashCode());
        output += "\n";
        output += "Previous Movement: " + prevPuzzleMovement + "\n";
        return output;
    }

    // Is goal
    public boolean isGoal() {
        return manhattanDistance == 0;
    }

    // Get neighbours
    public ArrayList<PuzzleState> getNeighbours() {
        ArrayList<PuzzleState> output = new ArrayList<>(PuzzleDirection.DIRECTION_NUMBER);

        for (PuzzleDirection puzzleDirection: PuzzleDirection.values()) {
            PuzzlePosition nextPuzzlePosition = maxTilePuzzlePosition.getNextPuzzlePosition(puzzleDirection);
            if (nextPuzzlePosition.isValidPuzzlePosition(dimension)) {
                output.add(new PuzzleState(this, nextPuzzlePosition, puzzleDirection.getReversedPuzzleDirection()));
            }
        }

        return output;
    }

    // Is same board
    public boolean isSameBoard(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PuzzleState that = (PuzzleState) o;
        return dimension == that.dimension && manhattanDistance == that.manhattanDistance && Arrays.deepEquals(board, that.board);
    }

    // To string
    @Override
    public String toString() {
        String output = "Puzzle State: " + Integer.toHexString(hashCode()) +  "\n";
        output += getBoard();
        output += getInfo();
        return output;
    }

    // Equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PuzzleState that = (PuzzleState) o;
        return dimension == that.dimension && stepCount == that.stepCount && manhattanDistance == that.manhattanDistance && Arrays.deepEquals(board, that.board);
    }

    // Hash code
    @Override
    public int hashCode() {
        int result = Objects.hash(dimension, stepCount, manhattanDistance);
        result = 31 * result + Arrays.deepHashCode(board);
        return result;
    }
}
