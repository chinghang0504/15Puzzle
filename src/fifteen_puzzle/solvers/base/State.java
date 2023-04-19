package fifteen_puzzle.solvers.base;

import fifteen_puzzle.util.Direction;
import fifteen_puzzle.util.Movement;
import fifteen_puzzle.util.Position;
import fifteen_puzzle.util.General;

import java.util.ArrayList;

public abstract class State {

    protected int dimension;
    protected int emptyTile;
    protected int[][] board;

    protected Position emptyTilePosition;
    protected int manhattanDistance;

    protected State prevState;
    protected Movement prevMovement;

    protected abstract void updateManhattanDistance();
    protected abstract int getHeuristicValue();
    protected abstract String getInfo();
    protected abstract ArrayList<State> getNeighbours();
    protected abstract boolean isValidPosition(Position position);
    public abstract boolean equals(Object o);
    public abstract int hashCode();

    // Constructor (Initial State)
    protected State(int[][] board) {
        dimension = board.length;
        emptyTile = dimension * dimension;
        this.board = board;

        updateManhattanDistance();
    }

    // Constructor (Next State)
    protected State(State prevState, Position nextPosition, Direction direction) {
        dimension = prevState.dimension;
        emptyTile = prevState.emptyTile;
        board = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            System.arraycopy(prevState.board[i], 0, board[i], 0, dimension);
        }

        int nextTile = board[nextPosition.row][nextPosition.col];
        board[prevState.emptyTilePosition.row][prevState.emptyTilePosition.col] = nextTile;
        board[nextPosition.row][nextPosition.col] = emptyTile;

        this.prevState = prevState;
        prevMovement = new Movement(nextTile, direction.getReversed());

        updateManhattanDistance();
    }

    // Is the goal state
    public boolean isGoal() {
        return manhattanDistance == 0;
    }

    // To string
    @Override
    public String toString() {
        String output = "State: " + Integer.toHexString(hashCode()) + "\n";
        output += General.getBoard(board, dimension, emptyTile);
        output += getInfo();
        return output;
    }
}
