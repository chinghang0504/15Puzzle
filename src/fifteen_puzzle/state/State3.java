package fifteen_puzzle.state;

import fifteen_puzzle.util.Direction;
import fifteen_puzzle.util.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public final class State3 extends State {

    public final int stepCount;

    // Constructor (Initial State)
    public State3(int[][] board) {
        super(board);
        stepCount = 0;
        updateMD();
    }

    // Constructor (Next State)
    public State3(State3 prevState, Position nextPosition, Direction direction) {
        super(prevState, nextPosition, direction);
        stepCount = prevState.stepCount + 1;
        updateMD();
    }

    // Is the goal state
    @Override
    public boolean isGoal() {
        return manhattanDistance == 0;
    }

    // Update the manhattan distance
    @Override
    public void updateMD() {
        int dimension = board.length;
        int emptyTile = dimension * dimension;
        manhattanDistance = 0;

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                Position currPosition = new Position(i, j);
                if (board[i][j] == emptyTile) {
                    emptyTilePosition = currPosition;
                } else {
                    Position subgoalPosition = getSubgoalTilePosition(board[i][j]);
                    manhattanDistance += Position.getManhattanDistance(currPosition, subgoalPosition);
                }
            }
        }
    }

    // Get the heuristic value
    @Override
    public int getHeuristicValue() {
        return stepCount + manhattanDistance;
    }

    // Get the neighbours
    @Override
    public ArrayList<State3> getNeighbours() {
        ArrayList<State3> output = new ArrayList<>(Direction.size);

        for (Direction direction: Direction.values()) {
            if (prevMovement != null && prevMovement.direction.equals(direction)) {
                continue;
            }

            Position nextPosition = emptyTilePosition.getNext(direction);
            if (isValidPosition(nextPosition)) {
                output.add(new State3(this, nextPosition, direction));
            }
        }

        return output;
    }

    // Is a valid puzzle position
    @Override
    protected boolean isValidPosition(Position position) {
        return position.isInBoundary(board.length);
    }

    // Get the info
    @Override
    protected String getInfo() {
        String output = "Step Count: " + stepCount + "\n";
        output += "Manhattan Distance: " + manhattanDistance + "\n";
        output += "Heuristic Value: " + getHeuristicValue() + "\n";
        output += "Previous State: ";
        output += prevState == null ? null : Integer.toHexString(prevState.hashCode());
        output += "\n";
        output += "Previous Movement: " + prevMovement + "\n";
        return output;
    }

    // Equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State3 that = (State3) o;
        return manhattanDistance == that.manhattanDistance && stepCount == that.stepCount && Arrays.deepEquals(board, that.board);
    }

    // Hash code
    @Override
    public int hashCode() {
        int result = Objects.hash(manhattanDistance, stepCount);
        result = 31 * result + Arrays.deepHashCode(board);
        return result;
    }
}
