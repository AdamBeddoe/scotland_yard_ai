package uk.ac.bris.cs.scotlandyard.ui.ai;

/**
 * Observer for the TreeBuilder.
 * Notifies for each step of generation.
 */
public interface TreeBuilderObserver {

    /**
     * Called when the TreeBuilder has the build method called.
     */
    void onTreeBuildStart();

    /**
     * Called when the NextRoundVisitorHasFinished.
     */
    void onNextRoundVisitorComplete();

    /**
     * Called when the ScoreVisitorHasFinished.
     */
    void onScoreVisitorComplete();

    /**
     * Called when the PruneVisitorHasFinished.
     */
    void onBigPruneComplete();

    /**
     * Called when the builder has stopped cycling through the generation steps.
     */
    void onTreeBuildFinish(GameTree tree);

}
