package uk.ac.bris.cs.scotlandyard.ui.ai;

import uk.ac.bris.cs.scotlandyard.model.Move;
import uk.ac.bris.cs.scotlandyard.model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Adam on 20/04/2017.
 */
public class GameTreeBuilder implements Runnable {
    private GameState startState;
    private boolean playerIsMrX;
    private int levels;
    private Set<Move> moves;
    private Calculator calculator;
    private List<TreeBuilderObserver> observers = new ArrayList<>();
    private AIPlayer player;
    private boolean stopped;

    private int threshold;
    private boolean isUsingThreshold;
    private int maxMrXMoves;
    private boolean isUsingMaxMrXMoves;
    private int maxDetectiveMoves;
    private boolean isUsingMaxDetectiveMoves;

    public void run() {
        build();
    }

    public GameTreeBuilder(boolean playerIsMrX, Calculator calculator) {
        this.playerIsMrX = playerIsMrX;
        this.calculator = calculator;
    }

    public void registerObserver(TreeBuilderObserver observer) {
        this.observers.add(observer);
    }

    public void deregisterObserver(TreeBuilderObserver observer) {
        this.observers.remove(observer);
    }

    public void setStartState(GameState state) {
        this.startState = state;
    }

    public void setLookAheadLevels(int levels) {
        this.levels = levels;
    }

    public void setMoves(Set<Move> moves) {
        this.moves = moves;
    }

    public void setNotifyPlayer(AIPlayer player) {
     this.player = player;
    }

    public void setThreshold(int threshold) {
        this.isUsingThreshold = true;
        this.threshold = threshold;
    }

    public void setMaxMrXMoves(int max) {
        this.isUsingMaxMrXMoves = true;
        this.maxMrXMoves = max;
    }

    public void setMaxDetectiveMoves(int max) {
        this.isUsingMaxDetectiveMoves = true;
        this.maxDetectiveMoves = max;
    }

    public void build() {
        this.observers.forEach(TreeBuilderObserver::onTreeBuildStart);
        GameTree tree = new GameTree(this.startState, this.playerIsMrX);

        for (int i = 1; i <= this.levels && !this.stopped; i++) {

            NextRoundVisitor nextRoundVisitorTilo = new NextRoundVisitor(this.moves, i);
            tree.accept(nextRoundVisitorTilo);
            this.observers.forEach(TreeBuilderObserver::onNextRoundVisitorComplete);

            ScoreVisitor scoreVisitorNick = new ScoreVisitor(this.calculator);
            tree.accept(scoreVisitorNick);
            this.observers.forEach(TreeBuilderObserver::onScoreVisitorComplete);

            player.updateTree(tree);

            PruneVisitor pruneVisitorDave = new PruneVisitor();
            if (this.isUsingMaxDetectiveMoves) pruneVisitorDave.setMaxDetectiveMoves(this.maxDetectiveMoves);
            if (this.isUsingMaxMrXMoves) pruneVisitorDave.setMaxMrXMoves(this.maxMrXMoves);
            if (this.isUsingThreshold) pruneVisitorDave.setThreshold(this.threshold);
            tree.accept(pruneVisitorDave);
            this.observers.forEach(TreeBuilderObserver::onBigPruneComplete);
        }

        this.observers.forEach(observer -> observer.onTreeBuildFinish(tree));
    }

    public void stop() {
        this.stopped = true;
    }
}