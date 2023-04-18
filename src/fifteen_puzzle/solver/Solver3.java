package fifteen_puzzle.solver;

import fifteen_puzzle.util.Direction;
import fifteen_puzzle.util.Movement;
import fifteen_puzzle.util.Position;
import fifteen_puzzle.util.SolverResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Solver3 implements SolverResult, Runnable {

    private int dimension;
    private int emptyTile;
    private PuzzleState initialPuzzleState;
    private int subgoalTile = 1;

    private PriorityQueue<PuzzleState> openList;
    private int openListCount;
    private HashSet<PuzzleState> closedList;
    private int closedListCount;
    private LinkedList<Movement> solution;

    private boolean solved;
    private boolean foundSolution;

    private class PuzzleState {

        private final int[][] board;

        private int manhattanDistance;
        private Position emptyTilePosition;

        private PuzzleState prevPuzzleState;
        private Movement prevMovement;

        // Constructor (Initial State)
        public PuzzleState(int[][] board) {
            this.board = board;

            updateManhattanDistance();
        }

        // Constructor (Next State)
        public PuzzleState(PuzzleState prevPuzzleState, Position nextPosition, Direction direction) {
            board = new int[dimension][dimension];
            for (int i = 0; i < dimension; i++) {
                System.arraycopy(prevPuzzleState.board[i], 0, board[i], 0, dimension);
            }

            int nextTile = board[nextPosition.row][nextPosition.col];
            board[prevPuzzleState.emptyTilePosition.row][prevPuzzleState.emptyTilePosition.col] = nextTile;
            board[nextPosition.row][nextPosition.col] = emptyTile;

            updateManhattanDistance();

            this.prevPuzzleState = prevPuzzleState;
            prevMovement = new Movement(nextTile, direction.getReversed());
        }

        // Update the manhattan distance
        public void updateManhattanDistance() {
            Position subgoalPosition = getGoalPuzzlePosition(subgoalTile);

            if (subgoalPosition.row < dimension - 2) {
                if (subgoalPosition.col < dimension - 2) {
                    updateManhattanDistanceOneTile();
                } else {
                    updateManhattanDistanceTwoTiles(true);
                }
            } else if (subgoalPosition.col < dimension - 2) {
                updateManhattanDistanceTwoTiles(false);
            } else {
                updateManhattanDistanceLastThreeTiles();
            }
        }

        // Update the manhattan distance (One Tile)
        private void updateManhattanDistanceOneTile() {
            Position subgoalPosition = getGoalPuzzlePosition(subgoalTile);
            Position currPosition = null;

            manhattanDistance = 0;
            boolean keepSearching = true;
            for (int i = 0; i < dimension && keepSearching; i++) {
                for (int j = 0; j < dimension; j++) {
                    if (board[i][j] == emptyTile) {
                        emptyTilePosition = new Position(i, j);
                    } else if (board[i][j] == subgoalTile) {
                        currPosition = new Position(i, j);
                    }
                    
                    if (emptyTilePosition != null && currPosition != null) {
                        keepSearching = false;
                        break;
                    }
                }
            }

            if (currPosition.equals(subgoalPosition)) {
                manhattanDistance = 0;
            } else {
                manhattanDistance += Position.getManhattanDistance(currPosition, subgoalPosition);
                manhattanDistance += Position.getManhattanDistance(currPosition, emptyTilePosition);
            }
        }

        // Update the manhattan distance (Two Tiles)
        private void updateManhattanDistanceTwoTiles(boolean isRow) {
            int subgoalTile2 = isRow ? subgoalTile + 1 : subgoalTile + dimension;
            Position subgoalPosition1 = getGoalPuzzlePosition(subgoalTile);
            Position currPosition1 = null;
            Position subgoalPosition2 = getGoalPuzzlePosition(subgoalTile2);
            Position currPosition2 = null;

            manhattanDistance = 0;
            boolean keepSearching = true;
            for (int i = 0; i < dimension && keepSearching; i++) {
                for (int j = 0; j < dimension; j++) {
                    if (board[i][j] == emptyTile) {
                        emptyTilePosition = new Position(i, j);
                    } else if (board[i][j] == subgoalTile) {
                        currPosition1 = new Position(i, j);
                    } else if (board[i][j] == subgoalTile2) {
                        currPosition2 = new Position(i, j);
                    }

                    if (emptyTilePosition != null && currPosition1 != null && currPosition2 != null) {
                        keepSearching = false;
                        break;
                    }
                }
            }

            if (currPosition1.equals(subgoalPosition1) && currPosition2.equals(subgoalPosition2)) {
                manhattanDistance = 0;
            } else {
                manhattanDistance += Position.getManhattanDistance(currPosition1, subgoalPosition1);
                manhattanDistance += Position.getManhattanDistance(currPosition2, subgoalPosition2);
                manhattanDistance += Position.getManhattanDistance(currPosition1, emptyTilePosition);
                manhattanDistance += Position.getManhattanDistance(currPosition2, emptyTilePosition);
                manhattanDistance += Position.getManhattanDistance(currPosition1, currPosition2);
            }
        }

        // Update the manhattan distance (Last Three Tiles)
        private void updateManhattanDistanceLastThreeTiles() {
            manhattanDistance = 0;
            for (int i = dimension - 2; i < dimension; i++) {
                for (int j = dimension - 2; j < dimension; j++) {
                    if (board[i][j] == emptyTile) {
                        emptyTilePosition = new Position(i, j);
                    } else {
                        int row = (board[i][j] - 1) / dimension;
                        int col = (board[i][j] - 1) % dimension;
                        manhattanDistance += Math.abs(row - i) + Math.abs(col - j);
                    }
                }
            }
        }

        // Get the subgoal puzzle position
        private Position getGoalPuzzlePosition(int tile) {
            int row = (tile - 1) / dimension;
            int col = (tile - 1) % dimension;
            return new Position(row, col);
        }

        // Get the heuristic value
        public int getHeuristicValue() {
            return manhattanDistance;
        }

        // Get the board
        private String getBoard() {
            String output = "";

            for (int i = 0; i < dimension; i++) {
                output += tileToString(board[i][0]);
                for (int j = 1; j < dimension; j++) {
                    output += " " + tileToString(board[i][j]);
                }
                output += "\n";
            }

            return output;
        }

        // Convert the tile to a string
        private String tileToString(int tile) {
            int length = String.valueOf(dimension * dimension).length();

            if (tile == emptyTile) {
                String formatString = "%" + length + "s";
                return String.format(formatString, "");
            } else {
                String formatString = "%" + length + "d";
                return String.format(formatString, tile);
            }
        }

        // Get the info
        private String getInfo() {
            String output = "Manhattan Distance: " + manhattanDistance + "\n";
            output += "Heuristic Value: " + getHeuristicValue() + "\n";
            output += "Previous Puzzle State: ";
            output += prevPuzzleState == null ? null : Integer.toHexString(prevPuzzleState.hashCode());
            output += "\n";
            output += "Previous Movement: " + prevMovement + "\n";
            return output;
        }

        // Get the neighbours
        public ArrayList<PuzzleState> getNeighbours() {
            Direction[] directions = Direction.values();
            ArrayList<PuzzleState> output = new ArrayList<>(directions.length);

            for (Direction direction : directions) {
                if (prevMovement != null && prevMovement.direction.equals(direction)) {
                    continue;
                }

                Position nextPosition = emptyTilePosition.getNext(direction);
                if (isValidPuzzlePosition(nextPosition)) {
                    output.add(new PuzzleState(this, nextPosition, direction));
                }
            }

            return output;
        }

        // Is a valid puzzle position
        private boolean isValidPuzzlePosition(Position position) {
            // Out of boundary
            if (position.row < 0 || position.row >= dimension || position.col < 0 || position.col >= dimension) {
                return false;
            }

            // Subgoal tile boundary
            Position subgoalPosition = getGoalPuzzlePosition(subgoalTile);
            if (position.row < subgoalPosition.row) {
                return false;
            }
            if (position.row == subgoalPosition.row && position.col < subgoalPosition.col) {
                return false;
            }
            if (subgoalPosition.row >= dimension - 2 && position.col < subgoalPosition.col) {
                return false;
            }

            return true;
        }

        // Is the goal puzzle state
        public boolean isGoal() {
            return manhattanDistance == 0;
        }

        // To string
        @Override
        public String toString() {
            String output = "Puzzle State: " + Integer.toHexString(hashCode()) + "\n";
            output += getBoard();
            output += getInfo();
            return output;
        }

        // Equals
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PuzzleState that = (PuzzleState) o;
            return manhattanDistance == that.manhattanDistance && Arrays.deepEquals(board, that.board);
        }

        // Hash code
        @Override
        public int hashCode() {
            int result = Objects.hash(manhattanDistance);
            result = 31 * result + Arrays.deepHashCode(board);
            return result;
        }
    }

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
    public Solver3(String path) {
        Scanner scanner = loadPuzzleFile(path);

        loadPuzzleDimension(scanner);
        checkPuzzleDimension();

        int[][] initialBoard = loadPuzzleBoard(scanner);
        checkPuzzleBoard(initialBoard);
        initialPuzzleState = new PuzzleState(initialBoard);

        scanner.close();
    }

    // Load the puzzle file
    private Scanner loadPuzzleFile(String path) {
        File file = new File(path);

        try {
            return new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new BadFileException(path + " (The system cannot find the puzzle file)");
        }
    }

    // Load the puzzle dimension
    private void loadPuzzleDimension(Scanner scanner) {
        String line = null;
        try {
            line = scanner.nextLine();
            dimension = Integer.valueOf(line);
            emptyTile = dimension * dimension;
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
        int length = String.valueOf(emptyTile).length();
        String tileString = null;

        try {
            for (int i = 0; i < dimension; i++) {
                String line = scanner.nextLine();
                for (int j = 0; j < dimension; j++) {
                    int begin = j * (length + 1);
                    int end = begin + length;
                    tileString = line.substring(begin, end).replaceAll(" ", "");
                    board[i][j] = tileString.isEmpty() ? emptyTile : Integer.valueOf(tileString);
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

    // Solve the puzzle
    public void solve() {
        if (solved) {
            return;
        }

        // Initialize
        openList = new PriorityQueue<>(new PuzzleStateComparator());
        closedList = new HashSet<>();
        solution = new LinkedList<>();

        // Add the initial puzzle state into the open list
        openList.add(initialPuzzleState);

        int time = dimension * (dimension - 2);
        for (int i = 0; i < time; i++) {
            if (!solveSubgoal(false)) {
                return;
            }
        }
        solveSubgoal(true);
    }

    // Solve the subgoal of puzzle
    private boolean solveSubgoal(boolean lastLoop) {
        while (true) {
            // Check interrupted
            if (Thread.interrupted()) {
                return false;
            }

            // Get the current puzzle state from the open list
            PuzzleState currPuzzleState = openList.poll();
            if (lastLoop && currPuzzleState == null) {
                solved = true;
                foundSolution = false;
                return false;
            }

            // Add the current puzzle state to the closed list
            closedList.add(currPuzzleState);

            // Check the goal
            if (currPuzzleState.isGoal()) {
                if (lastLoop) {
                    solved = true;
                    foundSolution = true;
                    saveSolution(currPuzzleState);
                } else {
                    if (subgoalTile % dimension != dimension - 1) {
                        subgoalTile += 1;
                    } else {
                        subgoalTile += 2;
                    }
                    openListCount += openList.size();
                    openList.clear();
                    closedListCount += closedList.size();
                    closedList.clear();
                    currPuzzleState.updateManhattanDistance();
                    openList.add(currPuzzleState);
                }

                return true;
            }

            // Generate neighbours
            ArrayList<PuzzleState> neighbours = currPuzzleState.getNeighbours();
            for (PuzzleState neighbour : neighbours) {
                // Check the closed list and the open list
                if (closedList.contains(neighbour) || openList.contains(neighbour)) {
                    continue;
                } else {
                    openList.add(neighbour);
                }
            }
        }
    }

    // Save the solution
    private void saveSolution(PuzzleState currPuzzleState) {
        while (currPuzzleState.prevPuzzleState != null) {
            solution.addFirst(currPuzzleState.prevMovement);
            currPuzzleState = currPuzzleState.prevPuzzleState;
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
        return openListCount + openList.size();
    }

    // Get the closed list size
    public int getClosedListSize() {
        if (closedList == null) {
            throw new BadSolverException();
        }
        return closedListCount + closedList.size();
    }

    // Get the step size in a solution
    public int getSolutionStepSize() {
        if (solution == null) {
            throw new BadSolverException();
        }
        return solution.size();
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
            for (Movement movement : solution) {
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
        output += initialPuzzleState.getBoard();
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
