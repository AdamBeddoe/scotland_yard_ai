package uk.ac.bris.cs.scotlandyard.ui.ai;


import uk.ac.bris.cs.scotlandyard.model.Move;
import uk.ac.bris.cs.scotlandyard.model.Player;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYardView;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Controls move selection for a MrX AI.
 * Creates and calls builder.
 */
class MrX implements Player,AIPlayer {

    private GameTreeBuilder builder;
    private Calculator calculator;
    private Move bestMove;
    private Random random = new Random();

    /**
     * Makes a new MrX player.
     * @param calculator A calculator to use to score the board.
     */
    MrX(Calculator calculator) {
        this.calculator = calculator;
        this.calculator.enableSneakyMode();
        this.builder = new GameTreeBuilder(true, this.calculator);
    }

    @Override
    public void makeMove(ScotlandYardView view, int location, Set<Move> moves,
                         Consumer<Move> callback) {
        this.calculator.updateNodeHistory(view);

        this.builder.setStartState(new GameState(view,location));
        this.builder.setLookAheadLevels(2);
        //this.builder.setThreshold(150);
        this.builder.setMaxDetectiveMoves(5);
        this.builder.setMaxMrXMoves(5);
        this.builder.setNotifyPlayer(this);
        this.builder.setMoves(moves);

        this.builder.build();
        if (this.bestMove == null) this.bestMove = new ArrayList<>(moves).get(random.nextInt(moves.size()));
        callback.accept(bestMove);
    }

    /**
     * Returns a reference to the builder used.
     * @return reference to the builder used in tree generation.
     */
    GameTreeBuilder getBuilder() {
        return this.builder;
    }

    /**
     * Called by the builder to update the current tree.
     * @param tree The tree passed by the builder.
     */
    public void updateTree(GameTree tree) {
        this.bestMove = selectMove(tree);
    }

    // Selects the best move from the current tree.
    private Move selectMove(GameTree tree) {
        int highestScore = Integer.MIN_VALUE;
        GameTree bestTree = tree.getChildTrees().get(0);
        for (GameTree currentTree : tree.getChildTrees()) {
            if (currentTree.getScore() > highestScore) {
                bestTree = currentTree;
                highestScore = bestTree.getScore();
            }
        }
        return tree.getMove(bestTree);
    }
}
