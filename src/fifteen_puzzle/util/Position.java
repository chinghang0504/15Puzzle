package fifteen_puzzle.util;

import java.util.Objects;

public class Position {

    public final int row;
    public final int col;

    // Constructor
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    // Get the next position
    public Position getNext(Direction direction) {
        switch (direction) {
            case Up:
                return new Position(row-1, col);
            case Down:
                return new Position(row+1, col);
            case Left:
                return new Position(row, col-1);
            case Right:
                return new Position(row, col+1);
            default:
                return null;
        }
    }

    // Is in the boundary
    public boolean isInBoundary(int dimension) {
        return row >= 0 && row < dimension && col >= 0 && col < dimension;
    }

    // Get the manhattan distance between two positions
    public static int getManhattanDistance(Position p1, Position p2) {
        return Math.abs(p1.row - p2.row) + Math.abs(p1.col - p2.col);
    }

    // Equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position that = (Position) o;
        return row == that.row && col == that.col;
    }

    // Hash code
    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
