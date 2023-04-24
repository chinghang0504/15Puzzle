package fifteen_puzzle.solver;

import fifteen_puzzle.comparator.StateComparator2;
import fifteen_puzzle.state.State3;

import java.util.*;

public final class Solver3 extends Solver<State3> {

    // Constructor
    public Solver3(String filePath) {
        super(filePath);
    }

    // Set the initial state
    @Override
    protected void setInitialState(int[][] initialBoard) {
        initialState = new State3(initialBoard);
    }

    // Solve the puzzle
    @Override
    public void solve() {
        if (solved) {
            return;
        }

        // Initialize
        openList = new PriorityQueue<>(new StateComparator2());
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
            State3 currState = openList.poll();
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
            ArrayList<State3> neighbours = currState.getNeighbours();
            for (State3 neighbour: neighbours) {
                // Check the closed list
                if (closedList.contains(neighbour)) {
                    continue;
                }

                // Check the open list
                boolean addToOpenList = false;
                Iterator<State3> it = openList.iterator();
                while(it.hasNext()) {
                    State3 openListState = it.next();
                    if (neighbour.isEqualBoard(openListState)) {
                        if (neighbour.stepCount < openListState.stepCount) {
                            it.remove();
                            addToOpenList = true;
                        }
                        break;
                    }
                }

                // Add the neighbour into the open list
                if (!addToOpenList) {
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
