package puzzle;

// Puzzle Action
public class PuzzleAction {

    private int tile;
    private int direction;

    private static final char[] DIRECTION_WORDS = { 'U', 'D', 'L', 'R' };

    // Constructor
    public PuzzleAction(int tile, int direction) {
        this.tile = tile;
        this.direction = direction;
    }

    // To string
    @Override
    public String toString() {
        return tile + " " + DIRECTION_WORDS[direction];
    }
}
