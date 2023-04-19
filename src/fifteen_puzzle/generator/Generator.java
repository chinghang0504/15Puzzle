package fifteen_puzzle.generator;

import fifteen_puzzle.util.Direction;
import fifteen_puzzle.util.Position;
import fifteen_puzzle.util.General;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Generator {

    private static int puzzleCount = 0;

    private static final int FIXED_STEP_SIZE = 100000;
    private static final int OUTPUT_FILE_NUMBER_SIZE = 3;

    private static final String TESTCASES_DIRECTORY_NAME = "testcases";
    private static final String OUTPUT_FILE_NAME_1 = "board";
    private static final String OUTPUT_FILE_NAME_2 = ".txt";

    private static int dimension;
    private static int emptyTile;
    private static int stepSize;
    private static String fileName;
    private static FileWriter fileWriter;
    private static int[][] board;
    private static Position emptyTilePosition;

    // Static constructor
    // Delete all the testcases
    static {
        File directory = new File(TESTCASES_DIRECTORY_NAME);
        File[] files = directory.listFiles();
        for (File file: files) {
            file.delete();
        }
    }

    // Generate a puzzle file according to the specified step size
    public static void generate(int dimension, int stepSize) {
        checkDimension(dimension);
        Generator.dimension = dimension;
        Generator.emptyTile = dimension * dimension;
        checkStepSize(stepSize);
        Generator.stepSize = stepSize;

        createPuzzleFile();

        createBoard();
        shuffleTiles();
        writePuzzleFile();

        closePuzzleFile();
    }

    // Generate a puzzle file
    public static void generate(int dimension) {
        generate(dimension, FIXED_STEP_SIZE);
    }

    // Check the dimension
    private static void checkDimension(int dimension) {
        if (dimension < 2) {
            throw new BadDimensionException("The dimension of a puzzle cannot be less than 2");
        }
    }

    // Check the step size
    private static void checkStepSize(int stepSize) {
        if (stepSize < 0) {
            throw new BadStepSizeException("The step size of a generator cannot be less than 0");
        }
    }

    // Create a puzzle file
    private static void createPuzzleFile() {
        String formatString = "%0" + OUTPUT_FILE_NUMBER_SIZE + "d";
        fileName = OUTPUT_FILE_NAME_1 + String.format(formatString, puzzleCount+1) + OUTPUT_FILE_NAME_2;
        String filePath = TESTCASES_DIRECTORY_NAME + "/" + fileName;

        try {
            fileWriter = new FileWriter(filePath);
        } catch (IOException e) {
            throw new BadFileException(fileName + " (The system cannot create this file)");
        }
    }

    // Close the puzzle file
    private static void closePuzzleFile() {
        try {
            fileWriter.close();
            puzzleCount++;
        } catch (IOException e) {
            throw new BadFileException(fileName + " (The system cannot close this file)");
        }
    }

    // Create a board
    private static void createBoard() {
        board = new int[dimension][dimension];

        int tile = 1;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                board[i][j] = tile++;
            }
        }

        emptyTilePosition = new Position(dimension-1, dimension-1);
    }

    // Shuffle the tiles
    private static void shuffleTiles() {
        Random random = new Random();

        for (int i = 0; i < stepSize; i++) {
            ArrayList<Position> neighbours = new ArrayList<>(Direction.size);

            for (Direction direction : Direction.values()) {
                Position nextPosition = emptyTilePosition.getNext(direction);
                if (isValidPosition(nextPosition)) {
                    neighbours.add(nextPosition);
                }
            }

            int index = random.nextInt(neighbours.size());
            Position nextPosition = neighbours.get(index);
            move(nextPosition);
        }
    }

    // Is valid position
    private static boolean isValidPosition(Position position) {
        if (position.row < 0 || position.row >= dimension) {
            return false;
        }

        if (position.col < 0 || position.col >= dimension) {
            return false;
        }

        return true;
    }

    // Move the empty tile
    private static void move(Position nextPosition) {
        int tile = board[nextPosition.row][nextPosition.col];
        board[nextPosition.row][nextPosition.col] = board[emptyTilePosition.row][emptyTilePosition.col];
        board[emptyTilePosition.row][emptyTilePosition.col] = tile;
        emptyTilePosition = nextPosition;
    }

    // Write the puzzle file
    private static void writePuzzleFile() {
        String output = dimension + "\n";
        output += General.getBoard(board, dimension, emptyTile);

        try {
            fileWriter.write(output);
        } catch (IOException e) {
            throw new BadFileException(fileName + " (The system cannot modify this file)");
        }
    }
}
