package uk.ac.bris.cs.scotlandyard.ui.ai;

import uk.ac.bris.cs.scotlandyard.model.Move;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Adam on 20/04/2017.
 */
public class GameTreeBuilder {
    private GameState startState;
    private boolean playerIsMrX;
    private int levels;
    private Set<Move> moves;
    private Calculator calculator;
    private List<TreeBuilderObserver> observers = new ArrayList<>();
    private int threshold;
    private boolean isUsingThreshold;
    private int maxMrXMoves;
    private boolean isUsingMaxMrXMoves;
    private int maxDetectiveMoves;
    private boolean isUsingMaxDetectiveMoves;

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

    public GameTree build() {
        notifyLoop(observer -> observer.onTreeBuildStart());
        GameTree tree = new GameTree(this.startState, this.playerIsMrX);

        for (int i = 1; i <= this.levels; i++) {

            NextRoundVisitor tilo = new NextRoundVisitor(this.moves, i);
            tree.accept(tilo);
            notifyLoop(observer -> observer.onNextRoundVisitorComplete());

            ScoreVisitor nick = new ScoreVisitor(this.calculator);
            tree.accept(nick);
            notifyLoop(observer -> observer.onScoreVisitorComplete());

            PruneVisitor bigPrune = new PruneVisitor();
            if (this.isUsingMaxDetectiveMoves) bigPrune.setMaxDetectiveMoves(this.maxDetectiveMoves);
            if (this.isUsingMaxMrXMoves) bigPrune.setMaxMrXMoves(this.maxMrXMoves);
            if (this.isUsingThreshold) bigPrune.setThreshold(this.threshold);
            tree.accept(bigPrune);
            notifyLoop(observer -> observer.onBigPruneComplete());

            this.observers.forEach(TreeBuilderObserver::onBigPruneComplete);
        }

        notifyLoop(observer -> observer.onTreeBuildFinish(tree));
        return tree;
    }

    private void notifyLoop(NotifyFunction function) {
        for (TreeBuilderObserver observer : observers){
            function.notifyFunc(observer);
        }
    }
}

interface NotifyFunction {
    void notifyFunc(TreeBuilderObserver observer);
}
