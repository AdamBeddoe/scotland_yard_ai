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
 * Created by Adam on 01/04/2017.
 */
public class Detective implements Player {

    private MyAI ai;
    private final Random random = new Random();
    public GameTreeBuilder builder = new GameTreeBuilder(false);

    public Detective(MyAI ai) {
        this.ai = ai;
    }

    @Override
    public void makeMove(ScotlandYardView view, int location, Set<Move> moves,
                         Consumer<Move> callback) {
        Move bestMove;
        if (view.getPlayerLocation(Black) == 0) {
            bestMove = new ArrayList<>(moves).get(random.nextInt(moves.size()));
        }
        else {
            this.builder.setStartState(new GameState(view,location));
            this.builder.setLookAheadLevels(2);
            this.builder.setThreshold(100);
            this.builder.setAI(this.ai);
            this.builder.setMoves(moves);

            GameTree tree = this.builder.build();
            bestMove = selectMove(tree);
        }
        callback.accept(bestMove);
    }

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
