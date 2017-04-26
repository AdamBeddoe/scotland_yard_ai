package uk.ac.bris.cs.scotlandyard.ui.gamemonitor;


import javafx.application.Platform;
import uk.ac.bris.cs.scotlandyard.model.DoubleMove;
import uk.ac.bris.cs.scotlandyard.model.Move;
import uk.ac.bris.cs.scotlandyard.model.MoveVisitor;
import uk.ac.bris.cs.scotlandyard.model.TicketMove;
import uk.ac.bris.cs.scotlandyard.ui.ai.GameTree;
import uk.ac.bris.cs.scotlandyard.ui.ai.TreeBuilderObserver;

/**
 * Created by Adam on 18/04/2017.
 */
public class GameMonitorModel implements TreeBuilderObserver {
    private long startTime, nextRoundTime, scoreVisitorTime, pruneTime, treeBuildFinishTime;
    private GameMonitorView view;

    public GameMonitorModel(GameMonitorView view) {
        this.view = view;
    }

    @Override
    public void onTreeBuildStart() {
        this.startTime = System.nanoTime();
    }

    @Override
    public void onNextRoundVisitorComplete() {
        this.nextRoundTime = System.nanoTime();
        long roundTime = (this.nextRoundTime - this.startTime)/1000000;
        view.nextRoundTime(roundTime);
    }

    @Override
    public void onScoreVisitorComplete() {
        this.scoreVisitorTime = System.nanoTime();
        long scoreTime = (this.scoreVisitorTime - this.nextRoundTime)/1000000;
        view.scoreTime(scoreTime);
    }

    @Override
    public void onBigPruneComplete() {
        this.pruneTime = System.nanoTime();
        long pruneTime = (this.pruneTime - this.scoreVisitorTime)/1000000;
        view.pruneTime(pruneTime);
    }

    @Override
    public void onTreeBuildFinish(GameTree tree) {
        this.treeBuildFinishTime = System.nanoTime();
        long treeFinishTime = (this.treeBuildFinishTime - this.pruneTime)/1000000;

        view.treeFinishTime(treeFinishTime);
        Platform.runLater(() -> view.drawTree(tree));
    }
}
