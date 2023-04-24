package fifteen_puzzle.state;

import fifteen_puzzle.util.Direction;
import fifteen_puzzle.util.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public final class State4 extends State {

    private int subgoalTile;
    private int nextSubgoalTile;
    private Position subgoalTilePosition = null;

    private static int FINISHED_SUBGOAL_TILE = -1;

    // Constructor (Initial State)
    public State4(int[][] board, int subgoalTile) {
        super(board);
        this.subgoalTile = subgoalTile;
        updateMD();
    }

    // Constructor (Next State)
    public State4(State4 prevState, Position nextPosition, Direction direction, int subgoalTile) {
        super(prevState, nextPosition, direction);
        this.subgoalTile = subgoalTile;
        updateMD();
    }

    // Update the subgoal tile
    public void updateSubgoalTile() {
        subgoalTile = nextSubgoalTile;
        updateMD();
    }

    // Is finished
    public boolean isSubgoal() {
        return manhattanDistance == 0;
    }

    // Is the goal state
    @Override
    public boolean isGoal() {
        return nextSubgoalTile == FINISHED_SUBGOAL_TILE;
    }

    // Update the manhattan distance
    @Override
    public void updateMD() {
        int dimension = board.length;
        subgoalTilePosition = getSubgoalTilePosition(subgoalTile);

        if (subgoalTilePosition.row < dimension - 2) {
            if (subgoalTilePosition.col < dimension - 2) {
                updateMD_OneTile();
            } else {
                updateMD_TwoTiles(true);
            }
        } else if (subgoalTilePosition.col < dimension - 2) {
            updateMD_TwoTiles(false);
        } else {
             updateMD_LastThreeTiles();
        }
    }

    // Update the manhattan distance (One Tile)
    private void updateMD_OneTile() {
        int dimension = board.length;
        int emptyTile = dimension * dimension;
        Position currTilePosition = null;

        int i = 0, j = 0;
        while (emptyTilePosition == null || currTilePosition == null) {
            if (board[i][j] == emptyTile) {
                emptyTilePosition = new Position(i, j);
            } else if (board[i][j] == subgoalTile) {
                currTilePosition = new Position(i, j);
            }

            if (j == dimension - 1) {
                i++;
                j = 0;
            } else {
                j++;
            }
        }

        if (currTilePosition.equals(subgoalTilePosition)) {
            manhattanDistance = 0;
            nextSubgoalTile = subgoalTile + 1;
        } else {
            manhattanDistance = Position.getManhattanDistance(currTilePosition, subgoalTilePosition);
            manhattanDistance += Position.getManhattanDistance(currTilePosition, emptyTilePosition);
        }
    }

    // Update the manhattan distance (Two Tiles)
    private void updateMD_TwoTiles(boolean isRow) {
        int dimension = board.length;
        int emptyTile = dimension * dimension;
        int subgoalTile2 = isRow ? subgoalTile + 1 : subgoalTile + dimension;
        Position subgoalPosition2 = getSubgoalTilePosition(subgoalTile2);
        Position currTilePosition = null;
        Position currTilePosition2 = null;

        int i = 0, j = 0;
        while (emptyTilePosition == null || currTilePosition == null || currTilePosition2 == null) {
            if (board[i][j] == emptyTile) {
                emptyTilePosition = new Position(i, j);
            } else if (board[i][j] == subgoalTile) {
                currTilePosition = new Position(i, j);
            } else if (board[i][j] == subgoalTile2) {
                currTilePosition2 = new Position(i, j);
            }

            if (j == dimension - 1) {
                i++;
                j = 0;
            } else {
                j++;
            }
        }

        if (currTilePosition.equals(subgoalTilePosition) && currTilePosition2.equals(subgoalPosition2)) {
            manhattanDistance = 0;
            nextSubgoalTile = isRow ? subgoalTile + 2 : subgoalTile + 1;
        } else {
            manhattanDistance = Position.getManhattanDistance(currTilePosition, subgoalTilePosition);
            manhattanDistance += Position.getManhattanDistance(currTilePosition2, subgoalPosition2);
            manhattanDistance += Position.getManhattanDistance(currTilePosition, emptyTilePosition);
            manhattanDistance += Position.getManhattanDistance(currTilePosition2, emptyTilePosition);
            manhattanDistance += Position.getManhattanDistance(currTilePosition, currTilePosition2);
        }
    }

    // Update the manhattan distance (Last Three Tiles)
    private void updateMD_LastThreeTiles() {
        int dimension = board.length;
        int emptyTile = dimension * dimension;
        manhattanDistance = 0;

        for (int i = dimension - 2; i < dimension; i++) {
            for (int j = dimension - 2; j < dimension; j++) {
                Position currPosition = new Position(i, j);
                if (board[i][j] == emptyTile) {
                    emptyTilePosition = currPosition;
                } else {
                    Position subgoalTilePosition = getSubgoalTilePosition(board[i][j]);
                    manhattanDistance += Position.getManhattanDistance(currPosition, subgoalTilePosition);
                }
            }
        }

        if (manhattanDistance == 0) {
            nextSubgoalTile = FINISHED_SUBGOAL_TILE;
        }
    }

    // Get the heuristic value
    @Override
    public int getHeuristicValue() {
        return manhattanDistance;
    }

    // Get the neighbours
    @Override
    public ArrayList<State4> getNeighbours() {
        ArrayList<State4> output = new ArrayList<>(Direction.size);

        for (Direction direction: Direction.values()) {
            if (prevMovement != null && prevMovement.direction.equals(direction)) {
                continue;
            }

            Position nextPosition = emptyTilePosition.getNext(direction);
            if (isValidPosition(nextPosition)) {
                output.add(new State4(this, nextPosition, direction, subgoalTile));
            }
        }

        return output;
    }

    // Is a valid puzzle position
    @Override
    protected boolean isValidPosition(Position position) {
        // Out of boundary
        if (!position.isInBoundary(board.length)) {
            return false;
        }

        // Subgoal tile boundary
        // Above row
        if (position.row < subgoalTilePosition.row) {
            return false;
        }
        // Same row
        if (position.row == subgoalTilePosition.row && position.col < subgoalTilePosition.col) {
            return false;
        }
        // Left column
        if (subgoalTilePosition.row >= board.length - 2 && position.col < subgoalTilePosition.col) {
            return false;
        }

        return true;
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

    // Equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State4 that = (State4) o;
        return manhattanDistance == that.manhattanDistance && Arrays.deepEquals(board, that.board);
    }

    // Hash code
    @Override
    public int hashCode() {
        int result = Objects.hash(manhattanDistance);
        result = 31 * result + Arrays.deepHashCode(board);
        return result;
    }
}
