package puzzle.puzzle3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

class PuzzleState {

    private final int dimension;
    private final int[][] board;

    private int stepCount;
    private int manhattanDistance;
    private PuzzlePosition maxTilePuzzlePosition;

    private PuzzleState prevPuzzleState;
    private PuzzleMovement prevPuzzleMovement;

    // Constructor (Initial State)
    public PuzzleState(int[][] board) {
        this.board = board;
        dimension = this.board.length;

        updateManhattanDistance();
    }

    // Constructor (Next State)
    public PuzzleState(PuzzleState prevPuzzleState, PuzzlePosition nextPuzzlePosition, PuzzleDirection puzzleDirection) {
        dimension = prevPuzzleState.dimension;
        board = new int[dimension][dimension];

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                board[i][j] = prevPuzzleState.board[i][j];
            }
        }

        int nextTile = nextPuzzlePosition.getTile(board);
        nextPuzzlePosition.setTile(board, dimension * dimension);
        prevPuzzleState.maxTilePuzzlePosition.setTile(board, nextTile);

        this.prevPuzzleState = prevPuzzleState;
        prevPuzzleMovement = new PuzzleMovement(nextTile, puzzleDirection.getReversed());

        stepCount = prevPuzzleState.stepCount + 1;
        updateManhattanDistance();
    }

    // Update the manhattan distance
    private void updateManhattanDistance() {
        manhattanDistance = 0;

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (board[i][j] == dimension * dimension) {
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

    // Get the heuristic value
    public int getHeuristicValue() {
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

    // Get the board
    public String getBoard() {
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

    // Convert the tile to a string
    private String tileToString(int tile) {
        if (tile == dimension * dimension) {
            return "  ";
        } else {
            String output = String.valueOf(tile);
            if (output.length() == 1) {
                output = " " + output;
            }
            return output;
        }
    }

    // Get the info
    private String getInfo() {
        String output = "Step Count: " + getStepCount() + "\n";
        output += "Manhattan Distance: " + getManhattanDistance() + "\n";
        output += "Heuristic Value: " + getHeuristicValue() + "\n";
        output += "Previous Puzzle State: ";
        output += prevPuzzleState == null ? null : Integer.toHexString(prevPuzzleState.hashCode());
        output += "\n";
        output += "Previous Movement: " + prevPuzzleMovement + "\n";
        return output;
    }

    // Get the neighbours
    public ArrayList<PuzzleState> getNeighbours() {
        PuzzleDirection[] puzzleDirections = PuzzleDirection.values();
        ArrayList<PuzzleState> output = new ArrayList<>(puzzleDirections.length);

        for (PuzzleDirection puzzleDirection: puzzleDirections) {
            if (prevPuzzleMovement != null && prevPuzzleMovement.PUZZLE_DIRECTION.equals(puzzleDirection)) {
                continue;
            }

            PuzzlePosition nextPuzzlePosition = maxTilePuzzlePosition.getNext(puzzleDirection);
            if (nextPuzzlePosition.isValid(dimension)) {
                output.add(new PuzzleState(this, nextPuzzlePosition, puzzleDirection));
            }
        }

        return output;
    }

    // Is the goal
    public boolean isGoal() {
        return manhattanDistance == 0;
    }

    // To string
    @Override
    public String toString() {
        String output = "Puzzle State: " + Integer.toHexString(hashCode()) + "\n";
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
