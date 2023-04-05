package puzzle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import puzzle.PuzzleException.*;

public class PuzzleSolver implements Runnable {

    private PuzzleState initialState;

    private PriorityQueue<PuzzleState> openList;
    private HashSet<PuzzleState> closedList;
    private LinkedList<PuzzleMovement> solution;

    private boolean solved;
    private boolean foundSolution;

    private class PuzzleStateComparator implements Comparator<PuzzleState> {

        // Compare
        @Override
        public int compare(PuzzleState o1, PuzzleState o2) {
            if (o1.getEstimatedCost() < o2.getEstimatedCost()) {
                return -1;
            } else if (o1.getEstimatedCost() > o2.getEstimatedCost()) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    // Constructor
    public PuzzleSolver(String fileName) {
        Scanner scanner = loadPuzzleFile(fileName);

        int dimension = loadPuzzleDimension(scanner);
        checkPuzzleDimension(dimension);

        int[][] initialBoard = loadPuzzleBoard(scanner, dimension);
        checkPuzzleBoard(initialBoard, dimension);
        initialState = new PuzzleState(initialBoard);
    }

    // Load the puzzle file
    private Scanner loadPuzzleFile(String fileName) {
        File file = new File(fileName);

        try {
            return new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new BadFileException(fileName + " (The system cannot find the puzzle file)");
        }
    }

    // Load the puzzle dimension
    private int loadPuzzleDimension(Scanner scanner) {
        String line = null;
        try {
            line = scanner.nextLine();
            return Integer.valueOf(line);
        } catch (NoSuchElementException e) {
            throw new BadDimensionException("The system cannot find the puzzle dimension");
        } catch (NumberFormatException e) {
            throw new BadDimensionException(line + " (Invalid puzzle dimension)");
        }
    }

    // Check the puzzle dimension
    private void checkPuzzleDimension(int dimension) {
        if (dimension < 2) {
            throw new BadDimensionException("The puzzle dimension cannot be less than 2");
        }
    }

    // Load the puzzle board
    private int[][] loadPuzzleBoard(Scanner scanner, int dimension) {
        int[][] board = new int[dimension][dimension];
        String tileString = null;

        try {
            for (int i = 0; i < dimension; i++) {
                String line = scanner.nextLine();
                for (int j = 0; j < dimension; j++) {
                    int begin = j * 3;
                    int end = begin + 2;
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
    private void checkPuzzleBoard(int[][] board, int dimension) {
        int maxTile = dimension * dimension;
        int[] tileCounts = new int[maxTile];

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (board[i][j] < 1 || board[i][j] > maxTile) {
                    throw new BadBoardException(board[i][j] + " (This tile is invalid)");
                }

                tileCounts[board[i][j] - 1]++;
            }
        }

        for (int i = 0; i < maxTile - 1; i++) {
            if (tileCounts[i] < 1) {
                throw new BadBoardException((i + 1) + " (This tile is missing)");
            } else if (tileCounts[i] > 1) {
                throw new BadBoardException((i + 1) + " (This tile is repeated)");
            }
        }
    }

    // Solve the puzzle problem
    public void solve() {
        if (solved) {
            return;
        }

        // Initialize
        openList = new PriorityQueue<>(new PuzzleStateComparator());
        closedList = new HashSet<>();
        solution = new LinkedList<>();

        // Add the initial state into the open list
        openList.add(initialState);

        while (true) {
            // Check interrupted
            if (Thread.interrupted()) {
                return;
            }

            // Get the current state from the open list
            PuzzleState currPuzzleState = openList.poll();
            if (currPuzzleState == null) {
                solved = true;
                foundSolution = false;
                return;
            }

            // Add the current state to the closed list
            closedList.add(currPuzzleState);

            // Check the goal
            if (currPuzzleState.isGoal()) {
                solved = true;
                foundSolution = true;
                saveSolution(currPuzzleState);
                return;
            }

            // Generate neighbours
            ArrayList<PuzzleState> neighbours = currPuzzleState.getNeighbours();
            for (PuzzleState neighbour: neighbours) {
                // Check in the closed list
                if (closedList.contains(neighbour)) {
                    continue;
                }

                // Check in the open list
                boolean addNeighbour = true;
                Iterator<PuzzleState> puzzleStateIterator = openList.iterator();
                while (puzzleStateIterator.hasNext()) {
                    PuzzleState puzzleState = puzzleStateIterator.next();
                    if (puzzleState.isSameBoard(neighbour)) {
                        if (puzzleState.getStepCount() <= neighbour.getStepCount()) {
                            addNeighbour = false;
                        } else {
                            puzzleStateIterator.remove();
                        }
                        break;
                    }
                }

                if (addNeighbour) {
                    openList.add(neighbour);
                }
            }
        }
    }

    // Save the solution
    private void saveSolution(PuzzleState puzzleState) {
        while (puzzleState.getPrevPuzzleState() != null) {
            solution.addFirst(puzzleState.getPrevPuzzleMovement());
            puzzleState = puzzleState.getPrevPuzzleState();
        }
    }

    // Get the solution
    public String getSolution() {
        if (!solved) {
            return "This puzzle is not solved yet\n";
        } else if (solution.size() == 0) {
            if (foundSolution) {
                return "The initial state is the goal state\n";
            } else {
                return "This puzzle is no solution\n";
            }
        } else {
            String output = "";
            for (PuzzleMovement puzzleMovement: solution) {
                output += puzzleMovement + "\n";
            }
            return output;
        }
    }

    // Get the stats
    public String getStats() {
        String output = "Open List Size: " + openList.size() + "\n";
        output += "Closed List Size: " + closedList.size() + "\n";
        output += "Solution Size: " + solution.size() + "\n";
        return output;
    }

    // Get the stats
    public String getStats(boolean timeout) {
        String output = "Open List Size: " + openList.size() + "\n";
        output += "Closed List Size: " + closedList.size() + "\n";
        output += "Solution Size: ";
        output += timeout ? "Unknown" : solution.size();
        output += "\n";
        return output;
    }

    // To string
    @Override
    public String toString() {
        String output = "Solution:\n";
        output += getSolution();
        output += "\nStatistics:\n";
        output += getStats();
        return output;
    }

    // Run
    @Override
    public void run() {
        solve();
    }
}
