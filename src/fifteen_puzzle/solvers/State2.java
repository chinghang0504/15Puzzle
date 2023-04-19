package fifteen_puzzle.solvers;

import fifteen_puzzle.solvers.base.State;
import fifteen_puzzle.util.Direction;
import fifteen_puzzle.util.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class State2 extends State {

    // Constructor (Initial State)
    public State2(int[][] board) {
        super(board);
    }

    // Constructor (Next State)
    public State2(State2 prevState, Position nextPosition, Direction direction) {
        super(prevState, nextPosition, direction);
    }

    // Update the manhattan distance
    @Override
    protected void updateManhattanDistance() {
        manhattanDistance = 0;

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (board[i][j] == emptyTile) {
                    emptyTilePosition = new Position(i, j);
                } else {
                    int row = (board[i][j] - 1) / dimension;
                    int col = (board[i][j] - 1) % dimension;
                    manhattanDistance += Position.getManhattanDistance(i, j, row, col);
                }
            }
        }
    }

    // Get the heuristic value
    @Override
    protected int getHeuristicValue() {
        return manhattanDistance;
    }

    // Get the info
    @Override
    protected String getInfo() {
        String output = "Manhattan Distance: " + manhattanDistance + "\n";
        output += "Heuristic Value: " + getHeuristicValue() + "\n";
        output += "Previous State: ";
        output += prevState == null ? null : Integer.toHexString(prevState.hashCode());
        output += "\n";
        output += "Previous Movement: " + prevMovement + "\n";
        return output;
    }

    // Get the neighbours
    @Override
    protected ArrayList<State> getNeighbours() {
        ArrayList<State> output = new ArrayList<>(Direction.size);

        for (Direction direction : Direction.values()) {
            if (prevMovement != null && prevMovement.direction.equals(direction)) {
                continue;
            }

            Position nextPosition = emptyTilePosition.getNext(direction);
            if (isValidPosition(nextPosition)) {
                output.add(new State2(this, nextPosition, direction));
            }
        }

        return output;
    }

    // Is a valid puzzle position
    @Override
    protected boolean isValidPosition(Position position) {
        if (position.row < 0 || position.row >= dimension) {
            return false;
        }

        if (position.col < 0 || position.col >= dimension) {
            return false;
        }

        return true;
    }

    // Equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State2 that = (State2) o;
        return dimension == that.dimension && manhattanDistance == that.manhattanDistance && Arrays.deepEquals(board, that.board);
    }

    // Hash code
    @Override
    public int hashCode() {
        int result = Objects.hash(dimension, manhattanDistance);
        result = 31 * result + Arrays.deepHashCode(board);
        return result;
    }
}
