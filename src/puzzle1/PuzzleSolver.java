package puzzle1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import puzzle1.PuzzleException.*;

public class PuzzleSolver implements Runnable {

    private int dimension;
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
            if (o1.getHeuristicValue() < o2.getHeuristicValue()) {
                return -1;
            } else if (o1.getHeuristicValue() > o2.getHeuristicValue()) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    // Constructor
    public PuzzleSolver(String fileName) {
        Scanner scanner = loadPuzzleFile(fileName);

        loadPuzzleDimension(scanner);
        checkPuzzleDimension();

        int[][] initialBoard = loadPuzzleBoard(scanner);
        checkPuzzleBoard(initialBoard);
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
    private void loadPuzzleDimension(Scanner scanner) {
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

    // Check the puzzle dimension
    private void checkPuzzleDimension() {
        if (dimension < 2) {
            throw new BadDimensionException("The puzzle dimension cannot be less than 2");
        }
    }

    // Load the puzzle board
    private int[][] loadPuzzleBoard(Scanner scanner) {
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
    private void checkPuzzleBoard(int[][] board) {
        int maxTile = dimension * dimension;
        int[] tileCounts = new int[maxTile];

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (board[i][j] < 1 || board[i][j] > maxTile) {
                    throw new BadBoardException(board[i][j] + " (Invalid tile)");
                }

                tileCounts[board[i][j] - 1]++;
            }
        }

        for (int i = 0; i < maxTile; i++) {
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

    // Get the open list size
    public int getOpenListSize() {
        if (openList == null) {
            throw new BadSolverException();
        }
        return openList.size();
    }

    // Get the closed list size
    public int getClosedListSize() {
        if (closedList == null) {
            throw new BadSolverException();
        }
        return closedList.size();
    }

    // Get the step size in a solution
    public int getSolutionStepSize() {
        if (solution == null) {
            throw new BadSolverException();
        }
        return solution.size();
    }

    // Solve the puzzle
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
            for (PuzzleState neighbour : neighbours) {
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
            throw new BadSolverException();
        } else if (solution.size() == 0) {
            if (foundSolution) {
                return "";
            } else {
                return "This puzzle is no solution\n";
            }
        } else {
            String output = "";
            for (PuzzleMovement puzzleMovement : solution) {
                output += puzzleMovement + "\n";
            }
            return output;
        }
    }

    // Get the result
    public String getResult() {
        if (!solved) {
            throw new BadSolverException();
        }

        String output = "Open List Size: " + openList.size() + "\n";
        output += "Closed List Size: " + closedList.size() + "\n";
        output += "Number of Steps in a Solution: " + solution.size() + "\n";
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
