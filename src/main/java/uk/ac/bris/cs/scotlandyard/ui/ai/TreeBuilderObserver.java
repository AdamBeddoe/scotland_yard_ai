package uk.ac.bris.cs.scotlandyard.ui.ai;

/**
 * Created by Adam on 18/04/2017.
 */
public interface TreeBuilderObserver {

    void onTreeBuildStart();

    void onNextRoundVisitorComplete();

    void onScoreVisitorComplete();

    void onBigPruneComplete();

    void onTreeBuildFinish(GameTree tree);

}
