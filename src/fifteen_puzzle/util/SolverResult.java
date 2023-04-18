package fifteen_puzzle.util;

public interface SolverResult {

    int getDimension();
    int getOpenListSize();
    int getClosedListSize();
    int getSolutionStepSize();
}
