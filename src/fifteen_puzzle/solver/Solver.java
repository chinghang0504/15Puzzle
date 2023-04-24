package fifteen_puzzle.solver;

import fifteen_puzzle.state.State;
import fifteen_puzzle.util.Movement;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public abstract class Solver<T extends State> implements Runnable {

    private int dimension;
    protected T initialState;

    protected PriorityQueue<T> openList;
    protected HashSet<T> closedList;
    protected LinkedList<Movement> solution;

    protected boolean solved;
    protected boolean foundSolution;

    private Scanner scanner;

    protected abstract void setInitialState(int[][] initialBoard);
    public abstract void solve();
    public abstract int getOpenListSize();
    public abstract int getClosedListSize();

    // Constructor
    protected Solver(String filePath) {
        loadPuzzleFile(filePath);

        loadDimension();
        checkDimension();

        int[][] initialBoard = loadBoard();
        checkBoard(initialBoard);
        setInitialState(initialBoard);

        closePuzzleFile();
    }

    // Load the puzzle file
    private void loadPuzzleFile(String filePath) {
        File file = new File(filePath);

        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new BadFileException(filePath + " (The system cannot find the puzzle file)");
        }
    }

    // Close the puzzle file
    private void closePuzzleFile() {
        scanner.close();
    }

    // Load the dimension
    private void loadDimension() {
        String line = null;
        try {
            line = scanner.nextLine();
            dimension = Integer.valueOf(line);
        } catch (NoSuchElementException e) {
            throw new BadDimensionException("The system cannot find the puzzle dimension");
        } catch (NumberFormatException e) {
            throw new BadDimensionException(line + " (Invalid puzzle dimension)");
        }
    }

    // Check the dimension
    private void checkDimension() {
        if (dimension < 2) {
            throw new BadDimensionException("The dimension cannot be less than 2");
        }
    }

    // Load the board
    private int[][] loadBoard() {
        int[][] board = new int[dimension][dimension];
        String tileString = null;

        try {
            for (int i = 0; i < dimension; i++) {
                String line = scanner.nextLine();
                int length = line.length() + 1;
                if (length % dimension != 0) {
                    throw new BadBoardException(line + " (All the tiles in a row should have the same character size)");
                }
                int size = length / dimension;

                for (int j = 0; j < dimension; j++) {
                    int begin = j * size;
                    int end = begin + size - 1;
                    tileString = line.substring(begin, end).replaceAll(" ", "");
                    board[i][j] = tileString.isEmpty() ? dimension * dimension : Integer.valueOf(tileString);
                }
            }
        } catch (NoSuchElementException e) {
            throw new BadBoardException("There must be " + dimension + " rows of tiles");
        } catch (IndexOutOfBoundsException e) {
            throw new BadBoardException("There must be " + dimension + " tiles in a row");
        } catch (NumberFormatException e) {
            throw new BadBoardException(tileString + " (Invalid tile)");
        }

        return board;
    }

    // Check the puzzle board
    private void checkBoard(int[][] board) {
        int emptyTile = dimension * dimension;
        int[] tileCounts = new int[emptyTile];

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (board[i][j] < 1 || board[i][j] > emptyTile) {
                    throw new BadBoardException(board[i][j] + " (Invalid tile)");
                } else {
                    tileCounts[board[i][j] - 1]++;
                }
            }
        }

        for (int i = 0; i < emptyTile; i++) {
            if (tileCounts[i] < 1) {
                throw new BadBoardException((i + 1) + " (This tile is missing)");
            } else if (tileCounts[i] > 1) {
                throw new BadBoardException((i + 1) + " (This tile is repeated)");
            }
        }
    }

    // Get the dimension
    public int getDimension() {
        return dimension;
    }

    // Get the step size in a solution
    public int getSolutionStepSize() {
        if (solution == null) {
            throw new BadSolverException();
        }
        return solution.size();
    }

    // Save the solution
    protected void saveSolution(State currState) {
        while (currState.prevState != null) {
            solution.addFirst(currState.prevMovement);
            currState = currState.prevState;
        }
    }

    // Get the solution
    public String getSolution() {
        if (!solved) {
            throw new BadSolverException();
        } else if (solution.size() == 0) {
            if (foundSolution) {
                return "";
            } else {
                return "No solution\n";
            }
        } else {
            String output = "";
            for (Movement movement: solution) {
                output += movement + "\n";
            }
            return output;
        }
    }

    // Get the result
    public String getResult() {
        if (!solved) {
            throw new BadSolverException();
        }

        String output = "Open List Size: " + getOpenListSize() + "\n";
        output += "Closed List Size: " + getClosedListSize() + "\n";
        output += "Number of Steps in a Solution: " + getSolutionStepSize() + "\n";
        return output;
    }

    // To string
    @Override
    public String toString() {
        if (!solved) {
            throw new BadSolverException();
        }

        String output = "Initial State:\n";
        output += initialState.getBoard();
        output += "\nSolution:\n";
        output += getSolution();
        output += "\nResult:\n";
        output += getResult();
        return output;
    }

    // Run
    @Override
    public void run() {
        solve();
    }
}
