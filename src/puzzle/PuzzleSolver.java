package puzzle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import puzzle.PuzzleException.*;

public class PuzzleSolver {

    private PuzzleState initialState;

    private PriorityQueue<PuzzleState> openList;
    private HashSet<PuzzleState> closedList;
    private LinkedList<PuzzleAction> solution;

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
        int[][] initialBoard;

        Scanner scanner = loadPuzzleFile(fileName);

        int order = loadPuzzleOrder(scanner);
        checkPuzzleOrder(order);

        initialBoard = loadPuzzleBoard(scanner, order);
        checkPuzzleBoard(initialBoard, order);
        initialState = new PuzzleState(initialBoard);

        openList = new PriorityQueue<>(new PuzzleStateComparator());
        closedList = new HashSet<>();
        solution = new LinkedList<>();
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

    // Load the puzzle order
    private int loadPuzzleOrder(Scanner scanner) {
        String line = null;
        try {
            line = scanner.nextLine();
            return Integer.valueOf(line);
        } catch (NoSuchElementException e) {
            throw new BadOrderException("The system cannot find the puzzle order");
        } catch (NumberFormatException e) {
            throw new BadOrderException(line + " (Invalid puzzle order)");
        }
    }

    // Check the puzzle order
    private void checkPuzzleOrder(int order) {
        if (order < 2) {
            throw new BadOrderException("The puzzle order cannot be less than 2");
        }
    }

    // Load the puzzle board
    private int[][] loadPuzzleBoard(Scanner scanner, int order) {
        int[][] board = new int[order][order];
        String tileString = null;

        try {
            for (int i = 0; i < order; i++) {
                String line = scanner.nextLine();
                for (int j = 0; j < order; j++) {
                    int begin = j * 3;
                    int end = begin + 2;
                    tileString = line.substring(begin, end).replaceAll(" ", "");
                    board[i][j] = tileString.isEmpty() ? order * order : Integer.valueOf(tileString);
                }
            }
        } catch (NoSuchElementException e) {
            throw new BadBoardException("There must be " + order + " rows of tiles");
        } catch (IndexOutOfBoundsException e) {
            throw new BadBoardException("There must be " + order + " tiles in a row");
        } catch (NumberFormatException e) {
            throw new BadBoardException(tileString + " (Invalid tile)");
        }

        return board;
    }

    // Check the puzzle board
    private void checkPuzzleBoard(int[][] board, int order) {
        int maxTile = order * order;
        int[] tileCounts = new int[maxTile];

        for (int i = 0; i < order; i++) {
            for (int j = 0; j < order; j++) {
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

        // Add the initial state into the open list
        openList.add(initialState);

        while (true) {
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
                if (!closedList.contains(neighbour) && !openList.contains(neighbour)) {
                    openList.add(neighbour);
                }
            }
        }
    }

    // Save the solution
    private void saveSolution(PuzzleState puzzleState) {
        while (puzzleState.getPrevPuzzleState() != null) {
            solution.addFirst(puzzleState.getPrevPuzzleAction());
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
            for (PuzzleAction puzzleAction: solution) {
                output += puzzleAction + "\n";
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
}
