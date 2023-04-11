package puzzlesolver;

public interface PuzzleSolverResult {

    int getDimension();
    int getOpenListSize();
    int getClosedListSize();
    int getSolutionStepSize();
}
