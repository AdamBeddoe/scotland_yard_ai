package uk.ac.bris.cs.scotlandyard.ui.ai;

import uk.ac.bris.cs.scotlandyard.model.Move;
import uk.ac.bris.cs.scotlandyard.model.Player;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYardView;

import java.util.Set;
import java.util.function.Consumer;

/**
 * Created by Adam on 30/03/2017.
 */
public class MrX implements Player {

    @Override
    public void makeMove(ScotlandYardView view, int location, Set<Move> moves,
                         Consumer<Move> callback) {


        GameTree tree = new GameTree(new GameState(view, location));
        NextRoundVisitor tilo = new NextRoundVisitor(moves);
        tree.accept(tilo);
        ScoreVisitor nick = new ScoreVisitor();
        tree.accept(nick);


        int highestScore = 0;
        GameTree bestTree = tree.getChildTrees().get(0);
        for (GameTree currentTree : tree.getChildTrees()) {
            if (currentTree.getScore() > highestScore) {
                bestTree = currentTree;
                highestScore = bestTree.getScore();
            }
        }

        Move selectedMove = tree.getMove(bestTree);

        callback.accept(selectedMove);
    }
}
