package fifteen_puzzle.util;

public class General {

    // Convert the tile to a string
    public static String tileToString(int tile, int emptyTile) {
        int length = String.valueOf(emptyTile).length();

        if (tile == emptyTile) {
            String formatString = "%" + length + "s";
            return String.format(formatString, "");
        } else {
            String formatString = "%" + length + "d";
            return String.format(formatString, tile);
        }
    }

    // Get the board
    public static String getBoard(int[][] board, int dimension, int emptyTile) {
        String output = "";

        for (int i = 0; i < dimension; i++) {
            output += General.tileToString(board[i][0], emptyTile);
            for (int j = 1; j < dimension; j++) {
                output += " " + General.tileToString(board[i][j], emptyTile);
            }
            output += "\n";
        }

        return output;
    }
}
