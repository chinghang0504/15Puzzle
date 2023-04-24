package fifteen_puzzle.solver;

import fifteen_puzzle.comparator.StateComparator1;
import fifteen_puzzle.state.State4;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;

public final class Solver4 extends Solver<State4> {

    private int openListCount;
    private int closedListCount;

    // Constructor
    public Solver4(String filePath) {
        super(filePath);
    }

    // Set the initial state
    @Override
    protected void setInitialState(int[][] initialBoard) {
        initialState = new State4(initialBoard, 1);
    }

    // Solve the puzzle
    @Override
    public void solve() {
        if (solved) {
            return;
        }

        // Initialize
        openList = new PriorityQueue<>(new StateComparator1());
        closedList = new HashSet<>();
        solution = new LinkedList<>();

        // Add the initial state into the open list
        openList.add(initialState);

        // Solve the subgoal until it is finished or interrupted
        while (solveSubgoal());
    }

    // Solve the subgoal puzzle
    private boolean solveSubgoal() {
        while (true) {
            // Check interrupted
            if (Thread.interrupted()) {
                return false;
            }

            // Get the current state from the open list
            State4 currState = openList.poll();
            if (currState == null) {
                solved = true;
                foundSolution = false;
                return false;
            }

            // Add the current state to the closed list
            closedList.add(currState);

            // Check the finished signal
            if (currState.isGoal()) {
                solved = true;
                foundSolution = true;
                saveSolution(currState);
                return false;
            } else if (currState.isSubgoal()) {
                openListCount += openList.size();
                openList.clear();
                closedListCount += closedList.size();
                closedList.clear();
                currState.updateSubgoalTile();
                openList.add(currState);
                return true;
            }

            // Generate neighbours
            ArrayList<State4> neighbours = currState.getNeighbours();
            for (State4 neighbour: neighbours) {
                // Check the closed list and the open list
                if (closedList.contains(neighbour) || openList.contains(neighbour)) {
                    continue;
                } else {
                    openList.add(neighbour);
                }
            }
        }
    }

    // Get the open list size
    @Override
    public int getOpenListSize() {
        if (openList == null) {
            throw new BadSolverException();
        }
        return openListCount + openList.size();
    }

    // Get the closed list size
    @Override
    public int getClosedListSize() {
        if (closedList == null) {
            throw new BadSolverException();
        }
        return closedListCount + closedList.size();
    }
}
