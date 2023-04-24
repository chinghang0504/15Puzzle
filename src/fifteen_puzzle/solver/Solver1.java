package fifteen_puzzle.solver;

import fifteen_puzzle.comparator.StateComparator1;
import fifteen_puzzle.state.State1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;

public final class Solver1 extends Solver<State1> {

    // Constructor
    public Solver1(String filePath) {
        super(filePath);
    }

    // Set the initial state
    @Override
    protected void setInitialState(int[][] initialBoard) {
        initialState = new State1(initialBoard);
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

        while (true) {
            // Check interrupted
            if (Thread.interrupted()) {
                return;
            }

            // Get the current state from the open list
            State1 currState = openList.poll();
            if (currState == null) {
                solved = true;
                foundSolution = false;
                return;
            }

            // Add the current state to the closed list
            closedList.add(currState);

            // Check the goal
            if (currState.isGoal()) {
                solved = true;
                foundSolution = true;
                saveSolution(currState);
                return;
            }

            // Generate neighbours
            ArrayList<State1> neighbours = currState.getNeighbours();
            for (State1 neighbour: neighbours) {
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
        return openList.size();
    }

    // Get the closed list size
    @Override
    public int getClosedListSize() {
        if (closedList == null) {
            throw new BadSolverException();
        }
        return closedList.size();
    }
}
