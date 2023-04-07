package puzzle;

public interface PuzzleResult {

    int getDimension();
    int getOpenListSize();
    int getClosedListSize();
    int getSolutionStepSize();
}
