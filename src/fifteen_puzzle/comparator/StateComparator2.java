package fifteen_puzzle.comparator;

import fifteen_puzzle.state.State3;

import java.util.Comparator;

public class StateComparator2 implements Comparator<State3> {

    // Compare
    @Override
    public int compare(State3 o1, State3 o2) {
        if (o1.getHeuristicValue() < o2.getHeuristicValue()) {
            return -1;
        } else if (o1.getHeuristicValue() > o2.getHeuristicValue()) {
            return 1;
        } else if (o1.stepCount < o2.stepCount) {
            return -1;
        } else if (o1.stepCount > o2.stepCount) {
            return 1;
        } else {
            return 0;
        }
    }
}
