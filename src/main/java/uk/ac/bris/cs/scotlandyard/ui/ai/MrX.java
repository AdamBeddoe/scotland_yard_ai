package uk.ac.bris.cs.scotlandyard.ui.ai;


import uk.ac.bris.cs.scotlandyard.model.Colour;
import uk.ac.bris.cs.scotlandyard.model.Move;
import uk.ac.bris.cs.scotlandyard.model.Player;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYardView;

import java.util.Set;
import java.util.function.Consumer;


/**
 * Created by Adam on 30/03/2017.
 */
public class MrX implements Player {

    public GameTreeBuilder builder;
    public Calculator calculator;

    public MrX(Calculator calculator) {
        this.calculator = calculator;
        this.builder = new GameTreeBuilder(true, this.calculator);
    }

    @Override
    public void makeMove(ScotlandYardView view, int location, Set<Move> moves,
                         Consumer<Move> callback) {
        this.calculator.updateNodeHistory(view);
        this.builder.setStartState(new GameState(view,location));
        this.builder.setLookAheadLevels(2);
        this.builder.setThreshold(100);
        this.builder.setMoves(moves);

        GameTree tree = this.builder.build();
        Move bestMove = selectMove(tree);

        callback.accept(bestMove);
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
