package uk.ac.bris.cs.scotlandyard.ui.ai;

/**
 * Created by Adam on 18/04/2017.
 */
public interface TreeBuilderObserver {

    default void onTreeBuildStart() {}

    default void onNextRoundVisitorComplete() {}

    default void onScoreVisitorComplete() {}

    default void onBigPruneComplete() {}

    default void onTreeBuildFinish(GameTree tree) {}

}
