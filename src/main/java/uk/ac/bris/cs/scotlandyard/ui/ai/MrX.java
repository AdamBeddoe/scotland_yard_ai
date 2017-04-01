package uk.ac.bris.cs.scotlandyard.ui.ai;

import uk.ac.bris.cs.scotlandyard.model.Colour;
import uk.ac.bris.cs.scotlandyard.model.Move;
import uk.ac.bris.cs.scotlandyard.model.Player;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYardView;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import static uk.ac.bris.cs.scotlandyard.model.Colour.Black;

/**
 * Created by Adam on 30/03/2017.
 */
public class MrX implements Player {

    @Override
    public void makeMove(ScotlandYardView view, int location, Set<Move> moves,
                         Consumer<Move> callback) {

        GameTree tree = new GameTree(new GameState(view, location),true);

        generateRounds(tree, 4, moves, view);



        ScoreVisitor nick = new ScoreVisitor();
        tree.accept(nick);

        callback.accept(selectMove(tree));
    }

    private Move selectMove(GameTree tree) {
        int highestScore = 0;
        GameTree bestTree = tree.getChildTrees().get(0);
        for (GameTree currentTree : tree.getChildTrees()) {
            if (currentTree.getScore() > highestScore) {
                bestTree = currentTree;
                highestScore = bestTree.getScore();
            }
        }

        return tree.getMove(bestTree);
    }

    public void generateRounds(GameTree tree, int n, Set<Move> moves, ScotlandYardView view) {

        NextRoundVisitor initialAdder = new NextRoundVisitor(moves);
        tree.accept(initialAdder);

        for (int i = 0; i < (n-1); i++) {
            Set<Move> detectiveMoves = new HashSet<>();
            for (Colour colour : view.getPlayers()) {
                detectiveMoves.addAll(tree.getState().validMoves(colour));
            }
            NextRoundVisitor detectivesMovesAdder = new NextRoundVisitor(detectiveMoves);
            tree.accept(detectivesMovesAdder);

            NextRoundVisitor mrXMovesAdder = new NextRoundVisitor(tree.getState().validMoves(Black));
            tree.accept(mrXMovesAdder);
        }
    }
}
