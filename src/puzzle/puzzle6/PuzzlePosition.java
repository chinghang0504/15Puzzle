package puzzle.puzzle6;

import java.util.Objects;

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

    // Get the manhattan distance
    public static int getManhattanDistance(PuzzlePosition pp1, PuzzlePosition pp2) {
        return Math.abs(pp1.ROW - pp2.ROW) + Math.abs(pp1.COL - pp2.COL);
    }

    // Equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PuzzlePosition that = (PuzzlePosition) o;
        return ROW == that.ROW && COL == that.COL;
    }

    // Hash code
    @Override
    public int hashCode() {
        return Objects.hash(ROW, COL);
    }
}
