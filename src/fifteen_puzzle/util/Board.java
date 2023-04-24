package fifteen_puzzle.util;

public class Board {

    // Get the board
    public static String getBoard(int[][] board) {
        int dimension = board.length;
        int emptyTile = dimension * dimension;
        String output = "";

        for (int i = 0; i < dimension; i++) {
            output += tileToString(board[i][0], emptyTile);
            for (int j = 1; j < dimension; j++) {
                output += " " + tileToString(board[i][j], emptyTile);
            }
            output += "\n";
        }

        return output;
    }

    // Convert the tile to a string
    private static String tileToString(int tile, int emptyTile) {
        int length = String.valueOf(emptyTile).length();

        if (tile == emptyTile) {
            String formatString = "%" + length + "s";
            return String.format(formatString, "");
        } else {
            String formatString = "%" + length + "d";
            return String.format(formatString, tile);
        }
    }
}
