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

    private MyAI ai;
    private int nodeHistory[] = new int[200];
    public GameTreeBuilder builder = new GameTreeBuilder(true);

    public MrX(MyAI ai) {
        this.ai = ai;
    }

    @Override
    public void makeMove(ScotlandYardView view, int location, Set<Move> moves,
                         Consumer<Move> callback) {
        updateNodeHistory(view);
        this.builder.setStartState(new GameState(view,location));
        this.builder.setLookAheadLevels(2);
        this.builder.setThreshold(100);
        this.builder.setAI(this.ai);
        this.builder.setMoves(moves);

        GameTree tree = this.builder.build();
        Move bestMove = selectMove(tree);

        callback.accept(bestMove);
    }

    private void updateNodeHistory(ScotlandYardView view) {
        for (Colour player : view.getPlayers()) {
            this.nodeHistory[view.getPlayerLocation(player)]++;
        }
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
