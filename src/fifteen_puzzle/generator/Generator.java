package fifteen_puzzle.generator;

import fifteen_puzzle.util.Direction;
import fifteen_puzzle.util.Position;
import fifteen_puzzle.util.Board;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Generator {

    private static int outputFileNumberSize;
    private static int stepSize;

    private static int currPuzzleNumber = 1;

    private static final String TESTCASES_DIRECTORY_NAME = "testcases";
    private static final String OUTPUT_FILE_NAME_1 = "board";
    private static final String OUTPUT_FILE_NAME_2 = ".txt";

    private static int dimension;
    private static String fileName;
    private static FileWriter fileWriter;
    private static int[][] board;
    private static Position emptyTilePosition;

    // Static constructor
    // Delete all the testcases
    static {
        File directoryFile = new File(TESTCASES_DIRECTORY_NAME);
        File[] files = directoryFile.listFiles();
        for (File file: files) {
            file.delete();
        }
    }

    // Generate all testcases
    public static void generate(int minDimension, int maxDimension, int eachTestcaseSize, int stepSize) {
        System.out.println("Generator - Start");

        checkInputArguments(minDimension, maxDimension, eachTestcaseSize, stepSize);

        int totalTestcaseSize = (maxDimension - minDimension + 1) * eachTestcaseSize;
        Generator.outputFileNumberSize = String.valueOf(totalTestcaseSize).length();
        Generator.stepSize = stepSize;

        for (int i = minDimension; i <= maxDimension; i++) {
            for (int j = 0; j < eachTestcaseSize; j++) {
                generatePuzzleFile(i);
            }
        }

        System.out.println("Generator - End");
    }

    // Check the input arguments
    private static void checkInputArguments(int minDimension, int maxDimension, int eachTestcaseSize, int stepSize) {
        // Check the minimum dimension
        if (minDimension < 2) {
            throw new BadInputArgumentException("The minimum dimension cannot be less than 2");
        }

        // Check the maximum dimension
        if (maxDimension < minDimension) {
            throw new BadInputArgumentException("The maximum dimension cannot be less than the minimum dimension");
        }

        // Check each testcase size
        if (eachTestcaseSize < 1) {
            throw new BadInputArgumentException("Each testcase size cannot be less than 1");
        }

        // Check the step size
        if (stepSize < 0) {
            throw new BadInputArgumentException("The step size cannot be less than 0");
        }
    }

    // Generate a puzzle file
    private static void generatePuzzleFile(int dimension) {
        Generator.dimension = dimension;

        createPuzzleFile();

        createBoard();
        shuffleTiles();
        writePuzzleFile();

        closePuzzleFile();
    }

    // Create a puzzle file
    private static void createPuzzleFile() {
        String formatString = "%0" + outputFileNumberSize + "d";
        fileName = OUTPUT_FILE_NAME_1 + String.format(formatString, currPuzzleNumber) + OUTPUT_FILE_NAME_2;
        String filePath = TESTCASES_DIRECTORY_NAME + "/" + fileName;

        System.out.println("Generating a testcase: " + fileName);

        try {
            fileWriter = new FileWriter(filePath);
        } catch (IOException e) {
            throw new BadFileException(fileName + " (The system cannot create a new puzzle file)");
        }
    }

    // Close the puzzle file
    private static void closePuzzleFile() {
        try {
            fileWriter.close();
            currPuzzleNumber++;
        } catch (IOException e) {
            throw new BadFileException(fileName + " (The system cannot close the puzzle file)");
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
                if (nextPosition.isInBoundary(dimension)) {
                    neighbours.add(nextPosition);
                }
            }

            int index = random.nextInt(neighbours.size());
            Position nextPosition = neighbours.get(index);
            moveTile(nextPosition);
        }
    }

    // Move the empty tile
    private static void moveTile(Position nextPosition) {
        board[emptyTilePosition.row][emptyTilePosition.col] = board[nextPosition.row][nextPosition.col];
        board[nextPosition.row][nextPosition.col] = dimension * dimension;
        emptyTilePosition = nextPosition;
    }

    // Write the puzzle file
    private static void writePuzzleFile() {
        String output = dimension + "\n";
        output += Board.getBoard(board);

        try {
            fileWriter.write(output);
        } catch (IOException e) {
            throw new BadFileException(fileName + " (The system cannot modify the puzzle file)");
        }
    }
}
