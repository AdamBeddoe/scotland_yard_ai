package uk.ac.bris.cs.scotlandyard.ui.ai;


import uk.ac.bris.cs.scotlandyard.model.Move;
import uk.ac.bris.cs.scotlandyard.model.Player;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYardView;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Created by Adam on 30/03/2017.
 */
public class MrX implements Player,AIPlayer {

    private GameTreeBuilder builder;
    private Calculator calculator;
    private Move bestMove;
    private Random random = new Random();;

    public MrX(Calculator calculator) {
        this.calculator = calculator;
        this.calculator.enableSneakyMode();
        this.builder = new GameTreeBuilder(true, this.calculator);
    }

    @Override
    public void makeMove(ScotlandYardView view, int location, Set<Move> moves,
                         Consumer<Move> callback) {
        this.calculator.updateNodeHistory(view);

        this.builder.setStartState(new GameState(view,location));
        this.builder.setLookAheadLevels(5);
        this.builder.setThreshold(100);
        //this.builder.setMaxDetectiveMoves(3);
        //this.builder.setMaxMrXMoves(3);
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

        if (this.bestMove == null) this.bestMove = new ArrayList<>(moves).get(random.nextInt(moves.size()));
        callback.accept(bestMove);
    }

    public GameTreeBuilder getBuilder() {
        return this.builder;
    }

    public void updateTree(GameTree tree) {
        this.bestMove = selectMove(tree);
    }

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
