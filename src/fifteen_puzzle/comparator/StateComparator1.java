package fifteen_puzzle.comparator;

import fifteen_puzzle.state.State;

import java.util.Comparator;

public class StateComparator1 implements Comparator<State> {

    // Compare
    @Override
    public int compare(State o1, State o2) {
        if (o1.getHeuristicValue() < o2.getHeuristicValue()) {
            return -1;
        } else if (o1.getHeuristicValue() > o2.getHeuristicValue()) {
            return 1;
        } else {
            return 0;
        }
    }
}
