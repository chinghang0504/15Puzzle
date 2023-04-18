package fifteen_puzzle.util;

public class Movement {

    public final int tile;
    public final Direction direction;

    // Constructor
    public Movement(int tile, Direction direction) {
        this.tile = tile;
        this.direction = direction;
    }

    // Get the movement from the string
    public static Movement getMovement(String movement) {
        String[] items = movement.split(" ");
        int tile;
        Direction direction;

        try {
            tile = Integer.valueOf(items[0]);
            direction = Direction.getDirection(items[1]);
        } catch (NumberFormatException e) {
            throw new BadMovementException(movement + " (Invalid movement)");
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new BadMovementException(movement + " (Invalid movement)");
        }

        return new Movement(tile, direction);
    }

    // To string
    @Override
    public String toString() {
        return tile + " " + direction.symbol;
    }
}
