package fifteen_puzzle.state;

import fifteen_puzzle.util.Direction;
import fifteen_puzzle.util.Movement;
import fifteen_puzzle.util.Position;
import fifteen_puzzle.util.Board;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class State {

    protected final int[][] board;

    protected Position emptyTilePosition = null;
    protected int manhattanDistance;

    public final State prevState;
    public final Movement prevMovement;

    public abstract boolean isGoal();
    public abstract void updateMD();
    public abstract int getHeuristicValue();
    public abstract <T extends State> ArrayList<T> getNeighbours();
    protected abstract boolean isValidPosition(Position position);
    protected abstract String getInfo();
    public abstract boolean equals(Object o);
    public abstract int hashCode();

    // Constructor (Initial State)
    protected State(int[][] board) {
        this.board = board;
        prevState = null;
        prevMovement = null;
    }

    // Constructor (Next State)
    protected State(State prevState, Position nextPosition, Direction direction) {
        int dimension = prevState.board.length;

        board = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            System.arraycopy(prevState.board[i], 0, board[i], 0, dimension);
        }

        int nextTile = board[nextPosition.row][nextPosition.col];
        board[prevState.emptyTilePosition.row][prevState.emptyTilePosition.col] = nextTile;
        board[nextPosition.row][nextPosition.col] = dimension * dimension;

        this.prevState = prevState;
        prevMovement = new Movement(nextTile, direction.getReversed());
    }

    // Get the subgoal tile position
    protected Position getSubgoalTilePosition(int tile) {
        int row = (tile - 1) / board.length;
        int col = (tile - 1) % board.length;
        return new Position(row, col);
    }

    // Get the board
    public String getBoard() {
        return Board.getBoard(board);
    }

    // Is equal board
    public boolean isEqualBoard(State state) {
        return Arrays.deepEquals(board, state.board);
    }

    // To string
    @Override
    public String toString() {
        String output = "State: " + Integer.toHexString(hashCode()) + "\n";
        output += getBoard();
        output += getInfo();
        return output;
    }
}
