package uk.ac.bris.cs.scotlandyard.ui.ai;

import uk.ac.bris.cs.scotlandyard.model.Move;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Iteratively generates a GameTree.
 */
class GameTreeBuilder {
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

    /**
     * Makes a new GameTreeBuilder.
     * @param playerIsMrX Whether MrX is is the current player at point of generation.
     * @param calculator A calculator for scoring the board.
     */
    GameTreeBuilder(boolean playerIsMrX, Calculator calculator) {
        this.playerIsMrX = playerIsMrX;
        this.calculator = calculator;
    }

    /**
     * Adds a TreeBuilderObserver to be notified.
     * @param observer The observer to be added.
     */
    void registerObserver(TreeBuilderObserver observer) {
        this.observers.add(observer);
    }

    /**
     * Removes a TreeBuilderObserver to be notified.
     * @param observer The observer to be removed.
     */
    void deregisterObserver(TreeBuilderObserver observer) {
        this.observers.remove(observer);
    }

    /**
     * Sets the initial state for the GameTree.
     * @param state The initial state of the GameTree.
     */
    void setStartState(GameState state) {
        this.startState = state;
    }

    /**
     * Sets the number of levels in the tree to be generated.
     * @param levels The number of levels in the tree to be generated.
     */
    void setLookAheadLevels(int levels) {
        this.levels = levels;
    }

    /**
     * Set the initial set of moves for the current player.
     * @param moves Initial set of moves for the current player.
     */
    void setMoves(Set<Move> moves) {
        this.moves = moves;
    }

    /**
     * Set the player to notify with each iteration of the tree.
     * @param player A class implementing AIPlayer, receives updated tree iterations.
     */
    void setNotifyPlayer(AIPlayer player) {
     this.player = player;
    }

    /**
     *  Sets the threshold to use for the pruning, if done via score.
     * @param threshold Minimum score for a tree before it is marked as dead.
     */
    void setThreshold(int threshold) {
        this.isUsingThreshold = true;
        this.threshold = threshold;
    }

    /**
     * Sets the maximum number of moves in any tree branch for MrX.
     * @param max The maximum number of moves.
     */
    void setMaxMrXMoves(int max) {
        this.isUsingMaxMrXMoves = true;
        this.maxMrXMoves = max;
    }

    /**
     * Sets the maximum number of moves in any tree branch for Detectives.
     * @param max The maximum number of moves.
     */
    void setMaxDetectiveMoves(int max) {
        this.isUsingMaxDetectiveMoves = true;
        this.maxDetectiveMoves = max;
    }

    /**
     * Builds the game tree.
     * Calls the NextRoundVisitor, ScoreVisitor and PruneVisitor respectively on the GameTree repeatedly.
     * Stops when reaches required level or when stop has been called.
     * After the ScoreVisitor calls updateTree on the player.
     * Notifies observers about each stage.
     *
     */
    void build() {
        this.observers.forEach(TreeBuilderObserver::onTreeBuildStart);
        GameTree tree = new GameTree(this.startState, this.playerIsMrX);

        for (int i = 0; i <= this.levels && !this.stopped; i++) {

            NextRoundVisitor nextRoundVisitorTilo = new NextRoundVisitor(this.moves, i);
            tree.accept(nextRoundVisitorTilo);
            this.observers.forEach(TreeBuilderObserver::onNextRoundVisitorComplete);

            ScoreVisitor scoreVisitorNick = new ScoreVisitor(this.calculator);
            tree.accept(scoreVisitorNick);
            this.observers.forEach(TreeBuilderObserver::onScoreVisitorComplete);

            PruneVisitor pruneVisitorDave = new PruneVisitor();
            if (this.isUsingMaxDetectiveMoves) pruneVisitorDave.setMaxDetectiveMoves(this.maxDetectiveMoves);
            if (this.isUsingMaxMrXMoves) pruneVisitorDave.setMaxMrXMoves(this.maxMrXMoves);
            if (this.isUsingThreshold) pruneVisitorDave.setThreshold(this.threshold);
            tree.accept(pruneVisitorDave);
            this.observers.forEach(TreeBuilderObserver::onBigPruneComplete);
        }
        player.updateTree(tree);
        this.observers.forEach(observer -> observer.onTreeBuildFinish(tree));
    }

    /**
     * Sets the builder to stopped state, will finish tree generation when current build iteration is complete.
     */
    void stop() {
        this.stopped = true;
    }
}