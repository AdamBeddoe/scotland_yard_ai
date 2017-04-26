package uk.ac.bris.cs.scotlandyard.ui.ai;

import uk.ac.bris.cs.scotlandyard.model.Move;
import uk.ac.bris.cs.scotlandyard.model.Player;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYardView;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;

import static uk.ac.bris.cs.scotlandyard.model.Colour.Black;

/**
 * Minimax AI for a detective player, no tree pruning possible.
 * Uses GameTreeBuilder.
 */
class Detective implements Player, AIPlayer {

    private Calculator calculator;
    private final Random random = new Random();
    private GameTreeBuilder builder;
    private Move bestMove;

    /**
     * Make new Detective AI.
     * @param calculator Calculator for detective to use.
     */
    Detective(Calculator calculator) {
        this.calculator = calculator;
        this.builder = new GameTreeBuilder(true, this.calculator);
    }

    /**
     * Makes move for detective.
     * Starts new thread for builder and waits for 14s.
     * Returns via callback the best move from the most detailed provided so far.
     */
    @Override
    public void makeMove(ScotlandYardView view, int location, Set<Move> moves,
                         Consumer<Move> callback) {

        if (view.getPlayerLocation(Black) == 0) {
            this.bestMove = new ArrayList<>(moves).get(random.nextInt(moves.size()));
        }
        else {
            this.calculator.updateNodeHistory(view);

            this.builder.setStartState(new GameState(view,location));
            this.builder.setLookAheadLevels(2);
            this.builder.setNotifyPlayer(this);
            this.builder.setMoves(moves);

            Thread t = new Thread(this.builder);
            t.start();

            long timeAvailable = 14000;
            long start = System.currentTimeMillis();

            try {
                t.join(14000);
                if (((System.currentTimeMillis() - start) > timeAvailable) && t.isAlive()) {
                    builder.stop();

                }
            } catch (InterruptedException e) {
                // do something here
            }
        }

        if (this.bestMove == null) this.bestMove = new ArrayList<>(moves).get(random.nextInt(moves.size()));
        callback.accept(this.bestMove);
    }

    /**
     * Returns the GameTreeBuilder used in this AIPlayer.
     * @return The GameTreeBuilder used in this AIPlayer.
     */
    public GameTreeBuilder getBuilder() {
        return this.builder;
    }

    /**
     * Called by builder to update GameTree as it is calculated iteratively and improved.
     * @param tree The tree passed by the builder.
     */
    public void updateTree(GameTree tree) {
        this.bestMove = selectMove(tree);
    }

    // Selects the best move (with the lowest score) from the GameTree.
    private Move selectMove(GameTree tree) {
        int lowestScore = Integer.MAX_VALUE;
        GameTree bestTree = tree.getChildTrees().get(0);
        for (GameTree currentTree : tree.getChildTrees()) {
            if (currentTree.getScore() < lowestScore) {
                bestTree = currentTree;
                lowestScore = bestTree.getScore();
            }
        }
        return tree.getMove(bestTree);
    }
}
