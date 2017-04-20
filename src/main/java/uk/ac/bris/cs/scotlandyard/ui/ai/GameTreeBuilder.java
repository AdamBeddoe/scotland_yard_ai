package uk.ac.bris.cs.scotlandyard.ui.ai;

import uk.ac.bris.cs.scotlandyard.model.Move;
import uk.ac.bris.cs.scotlandyard.model.Spectator;

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
    private MyAI AI;
    private List<TreeBuilderObserver> observers = new ArrayList<>();



    public GameTreeBuilder(boolean playerIsMrX) {
        this.playerIsMrX = playerIsMrX;
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

    public void setAI(MyAI myAI) {
        this.AI = myAI;
    }

    public GameTree build() {
        notifyLoop(observer -> observer.onTreeBuildStart());
        GameTree tree = new GameTree(this.startState, this.playerIsMrX);

        for (int i = 1; i <= this.levels; i++) {

            NextRoundVisitor tilo = new NextRoundVisitor(this.moves, i);
            tree.accept(tilo);
            notifyLoop(observer -> observer.onNextRoundVisitorComplete());

            ScoreVisitor nick = new ScoreVisitor(this.AI);
            tree.accept(nick);
            notifyLoop(observer -> observer.onScoreVisitorComplete());

            PruneVisitor bigPrune = new PruneVisitor(100);
            tree.accept(bigPrune);
            notifyLoop(observer -> observer.onBigPruneComplete());
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
