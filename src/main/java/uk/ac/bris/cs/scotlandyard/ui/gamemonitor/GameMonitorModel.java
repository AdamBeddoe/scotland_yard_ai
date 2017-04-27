package uk.ac.bris.cs.scotlandyard.ui.gamemonitor;


import javafx.application.Platform;
import uk.ac.bris.cs.scotlandyard.ui.ai.GameTree;
import uk.ac.bris.cs.scotlandyard.ui.ai.TreeBuilderObserver;

/**
 * Controls the logic for calculating what is displayed by the GameMonitor.
 */
public class GameMonitorModel implements TreeBuilderObserver {
    private long startTime, nextRoundTime, scoreVisitorTime, pruneTime, treeBuildFinishTime;
    private GameMonitorView view;

    /**
     * Makes a new GameMonitorModel linked to a GameMonitorView.
     * @param view A GameMonitorView.
     */
    public GameMonitorModel(GameMonitorView view) {
        this.view = view;
    }

    /**
     * Records the time when tree building was started.
     */
    @Override
    public void onTreeBuildStart() {
        this.startTime = System.nanoTime();
    }

    /**
     * Calculates the time taken by the NextRoundVisitor and updates view.
     */
    @Override
    public void onNextRoundVisitorComplete() {
        this.nextRoundTime = System.nanoTime();
        long roundTime = (this.nextRoundTime - this.startTime)/1000000;
        view.nextRoundTime(roundTime);
    }

    /**
     * Calculates the time taken by the ScoreVisitor and updates view.
     */
    @Override
    public void onScoreVisitorComplete() {
        this.scoreVisitorTime = System.nanoTime();
        long scoreTime = (this.scoreVisitorTime - this.nextRoundTime)/1000000;
        view.scoreTime(scoreTime);
    }

    /**
     * Calculates the time taken by the PruneVisitor and updates view.
     */
    @Override
    public void onBigPruneComplete() {
        this.pruneTime = System.nanoTime();
        long pruneTime = (this.pruneTime - this.scoreVisitorTime)/1000000;
        view.pruneTime(pruneTime);
        view.cumulativeTime((this.pruneTime-this.startTime)/1000000);
    }

    /**
     * Calculates the final build time and updates view.
     * Calls the view to draw the GameTree.
     * @param tree The final GameTree of an iteration.
     */
    @Override
    public void onTreeBuildFinish(GameTree tree) {
        this.treeBuildFinishTime = System.nanoTime();
        long treeFinishTime = (this.treeBuildFinishTime - this.startTime)/1000000;

        view.treeFinishTime(treeFinishTime);
        Platform.runLater(() -> view.drawTree(tree));
    }
}
