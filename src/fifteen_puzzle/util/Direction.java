package fifteen_puzzle.util;

public enum Direction {

    Up("U"),
    Down("D"),
    Left("L"),
    Right("R");

    public final String symbol;

    public static int size = Direction.values().length;

    // Constructor
    Direction(String symbol) {
        this.symbol = symbol;
    }

    // Get the reversed direction
    public Direction getReversed() {
        switch (this) {
            case Up:
                return Down;
            case Down:
                return Up;
            case Left:
                return Right;
            case Right:
                return Left;
            default:
                return null;
        }
    }

    // Get the direction from the string
    public static Direction getDirection(String symbol) {
        switch (symbol) {
            case "U":
                return Up;
            case "D":
                return Down;
            case "L":
                return Left;
            case "R":
                return Right;
            default:
                throw new BadDirectionSymbolException(symbol + " (Invalid direction symbol)");
        }
    }
}
