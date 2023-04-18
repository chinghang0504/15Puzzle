package fifteen_puzzle.util;

public interface SolverResult {

    // Get the dimension of the puzzle
    int getDimension();

    // Get the size of the open list
    int getOpenListSize();

    // Get the size of the closed list
    int getClosedListSize();

    // Get the step size of the solution
    int getSolutionStepSize();
}
