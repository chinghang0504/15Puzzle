package puzzle;

import puzzle.exceptions.BadBoardException;
import puzzle.exceptions.BadSizeException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

// Puzzle Solver
public class PuzzleSolver {

    private int size;
    private PuzzleState initialState;
    private PuzzleState goalState;

    private PriorityQueue<PuzzleState> openList;
    private LinkedList<PuzzleState> closedList;
    private LinkedList<PuzzleAction> solution;

    private boolean solved = false;
    private boolean foundSolution = false;

    // Puzzle State
    private class PuzzleState {

        int[][] board;
        int incorrectCount;
        int estimatedCount;

        PuzzleState prevPuzzleState;
        PuzzleAction prevPuzzleAction;

        // Constructor (Initial State and Goal State)
        PuzzleState(int[][] board, boolean update) {
            this.board = board;

            if (update) {
                updateIncorrectCount();
                updateEstimatedCount();
            }
        }

        // Constructor (Next State)
        PuzzleState(PuzzleState origin, PuzzlePosition target, PuzzlePosition next, int direction) {
            board = new int[size][size];

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    board[i][j] = origin.board[i][j];
                }
            }

            int targetTile = origin.board[target.row][target.col];
            int nextTile = origin.board[next.row][next.col];
            board[next.row][next.col] = targetTile;
            board[target.row][target.col] = nextTile;

            prevPuzzleState = origin;
            if (direction == 0) {
                prevPuzzleAction = new PuzzleAction(nextTile, 1);
            } else if (direction == 1) {
                prevPuzzleAction = new PuzzleAction(nextTile, 0);
            } else if (direction == 2) {
                prevPuzzleAction = new PuzzleAction(nextTile, 3);
            } else if (direction == 3) {
                prevPuzzleAction = new PuzzleAction(nextTile, 2);
            }

            updateIncorrectCount();
            updateEstimatedCount();
        }

        // Update the incorrect count
        void updateIncorrectCount() {
            incorrectCount = 0;

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (board[i][j] != size * size) {
                        if (board[i][j] != goalState.board[i][j]) {
                            incorrectCount++;
                        }
                    }
                }
            }
        }

        // Update the estimated count
        void updateEstimatedCount() {
            estimatedCount = 0;

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (board[i][j] != size * size) {
                        PuzzlePosition target = goalState.getTarget(board[i][j]);
                        estimatedCount += Math.abs(target.row - i) + Math.abs(target.col - j);
                    }
                }
            }
        }

        // Get the target puzzle position
        PuzzlePosition getTarget(int target) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (board[i][j] == target) {
                        return new PuzzlePosition(i, j);
                    }
                }
            }

            return null;
        }

        // Get the total cost
        int getTotalCost() {
            return incorrectCount + estimatedCount;
        }

        // Get the board
        String getBoard() {
            String output = "";

            for (int i = 0; i < size; i++) {
                output += tileToString(board[i][0]);
                for (int j = 1; j < size; j++) {
                    output += " " + tileToString(board[i][j]);
                }
                output += "\n";
            }

            return output;
        }

        // Print the board
        void printBoard() {
            System.out.print(getBoard());
        }

        // Convert tile to string
        String tileToString(int tile) {
            if (tile == size * size) {
                return "  ";
            }

            String output = String.valueOf(tile);
            if (output.length() == 1) {
                output = " " + output;
            }

            return output;
        }

        // Get the info
        String getInfo() {
            String output = "Incorrect Count: " + incorrectCount + "\n";
            output += "Estimated Count: " + estimatedCount + "\n";
            output += "Total Cost: " + getTotalCost() + "\n";

            if (prevPuzzleState != null) {
                output += "Previous Puzzle State: " + Integer.toHexString(prevPuzzleState.hashCode()) + "\n";
            } else {
                output += "Previous Puzzle State: " + null + "\n";
            }

            output += "Previous Action: " + prevPuzzleAction + "\n";
            return output;
        }

        // Print the info
        void printInfo() {
            System.out.print(getInfo());
        }

        // Get all the information
        String getAllInfo() {
            String output = "Puzzle State: " + Integer.toHexString(hashCode()) + "\n";
            output += getBoard();
            output += getInfo();
            return output;
        }

        // Print all the information
        void printAllInfo() {
            System.out.print(getAllInfo());
        }

        // Equals
        @Override
        public boolean equals(Object o) {
            PuzzleState that = (PuzzleState) o;
            if (incorrectCount != that.incorrectCount || estimatedCount != that.estimatedCount) {
                return false;
            }

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (board[i][j] != that.board[i][j])
                        return false;
                }
            }

            return true;
        }

        // Is goal
        boolean isGoal() {
            return equals(goalState);
        }

        // Get neighbours
        LinkedList<PuzzleState> getNeighbours() {
            LinkedList<PuzzleState> neighbours = new LinkedList<>();
            PuzzlePosition target = getTarget(size * size);

            for (int i = 0; i < PuzzlePosition.DIRECTION_SIZE; i++) {
                PuzzlePosition next = target.getNext(i);
                if (checkPuzzlePosition(next)) {
                    neighbours.add(new PuzzleState(this, target, next, i));
                }
            }

            return neighbours;
        }

        // Check puzzle position
        boolean checkPuzzlePosition(PuzzlePosition puzzlePosition) {
            if (puzzlePosition.row < 0 || puzzlePosition.row >= size) {
                return false;
            } else if (puzzlePosition.col < 0 || puzzlePosition.col >= size) {
                return false;
            }

            return true;
        }
    }

    // Puzzle State Comparator
    private class PuzzleStateComparator implements Comparator<PuzzleState> {

        // Compare
        @Override
        public int compare(PuzzleState o1, PuzzleState o2) {
            if (o1.getTotalCost() < o2.getTotalCost()) {
                return -1;
            } else if (o1.getTotalCost() > o2.getTotalCost()) {
                return 1;
            } else if (o1.estimatedCount < o2.estimatedCount) {
                return -1;
            } else if (o1.estimatedCount > o2.estimatedCount) {
                return 1;
            }

            return 0;
        }
    }

    // Constructor
    public PuzzleSolver(String fileName) {
        int[][] goalBoard = null;
        int[][] initialBoard = null;

        try {
            Scanner scanner = loadPuzzleFile(fileName);

            loadPuzzleSize(scanner);
            checkPuzzleSize();

            goalBoard = generateGoalBoard();
            initialBoard = loadPuzzleBoard(scanner);
            checkPuzzleBoard(initialBoard);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

        goalState = new PuzzleState(goalBoard, false);
        initialState = new PuzzleState(initialBoard, true);

        openList = new PriorityQueue<>(new PuzzleStateComparator());
        closedList = new LinkedList<>();
        solution = new LinkedList<>();
    }

    // Load the puzzle file
    private Scanner loadPuzzleFile(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        return new Scanner(file);
    }

    // Load the puzzle size
    private void loadPuzzleSize(Scanner scanner) throws BadSizeException {
        String line = null;
        try {
            line = scanner.nextLine();
            size = Integer.valueOf(line);
        } catch (NoSuchElementException e) {
            throw new BadSizeException("The system cannot find the puzzle size");
        } catch (NumberFormatException e) {
            throw new BadSizeException(line + " (Invalid puzzle size)");
        }
    }

    // Check the puzzle size
    private void checkPuzzleSize() throws BadSizeException {
        if (size < 2) {
            throw new BadSizeException("The puzzle size cannot be less than 2");
        }
    }

    // Generate a goal board
    private int[][] generateGoalBoard() {
        int[][] board = new int[size][size];

        int tile = 1;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = tile++;
            }
        }

        return board;
    }

    // Load the puzzle board
    private int[][] loadPuzzleBoard(Scanner scanner) throws BadBoardException {
        int[][] board = new int[size][size];
        String tileString = null;

        try {
            for (int i = 0; i < size; i++) {
                String line = scanner.nextLine();
                for (int j = 0; j < size; j++) {
                    int begin = j * 3;
                    int end = begin + 2;
                    tileString = line.substring(begin, end).replaceAll(" ", "");
                    board[i][j] = tileString.isEmpty() ? size * size : Integer.valueOf(tileString);
                }
            }
        } catch (NoSuchElementException e) {
            throw new BadBoardException("There must be " + size + " rows of tiles");
        } catch (IndexOutOfBoundsException e) {
            throw new BadBoardException("There must be " + size + " tiles in a row");
        } catch (NumberFormatException e) {
            throw new BadBoardException(tileString + " (Invalid tile)");
        }

        return board;
    }

    // Check the puzzle board
    private void checkPuzzleBoard(int[][] board) throws BadBoardException {
        int totalSize = size * size;
        int[] tileCounts = new int[totalSize];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] < 1 || board[i][j] > totalSize) {
                    throw new BadBoardException(board[i][j] + " (This tile is invalid)");
                }
                tileCounts[board[i][j] - 1]++;
            }
        }

        for (int i = 0; i < totalSize - 1; i++) {
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
            LinkedList<PuzzleState> neighbours = currPuzzleState.getNeighbours();
            for (PuzzleState neighbour: neighbours) {
                if (!closedList.contains(neighbour) && !openList.contains(neighbour)) {
                    openList.add(neighbour);
                }
            }
        }
    }

    // Save the solution
    private void saveSolution(PuzzleState puzzleState) {
        while (puzzleState.prevPuzzleState != null) {
            solution.addFirst(puzzleState.prevPuzzleAction);
            puzzleState = puzzleState.prevPuzzleState;
        }
    }

    // Get the solution
    public String getSolution() {
        String output = null;

        if (!solved) {
            output = "This puzzle is not solved yet\n";
        } else if (solution.size() == 0) {
            if (foundSolution) {
                output = "The initial state is the goal state\n";
            } else {
                output = "This puzzle is no solution\n";
            }
        } else {
            output = "";
            for (PuzzleAction puzzleAction: solution) {
                output += puzzleAction + "\n";
            }
        }

        return output;
    }

    // Print the solution
    public void printSolution() {
        System.out.print(getSolution());
    }

    // Get the stats
    public String getStats() {
        String output = "Open List Size: " + openList.size() + "\n";
        output += "Closed List Size: " + closedList.size() + "\n";
        return output;
    }

    // Print the stats
    public void printStats() {
        System.out.print(getStats());
    }
}
