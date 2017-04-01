package uk.ac.bris.cs.scotlandyard.ui.ai;

import uk.ac.bris.cs.scotlandyard.model.Colour;
import uk.ac.bris.cs.scotlandyard.model.Move;

import java.util.HashSet;
import java.util.Set;

import static uk.ac.bris.cs.scotlandyard.model.Colour.Black;

/**
 * Created by Adam on 31/03/2017.
 */
public class NextRoundVisitor extends TreeVisitor {

    private Set<Move> moves;
    private int levels;

    public NextRoundVisitor(Set<Move> moves, int levels) {
        this.levels = levels;
        this.moves = moves;
    }

    public void visit(GameTree tree) {
        if (tree.getChildTrees().isEmpty()) {
            for (Move move : this.moves) {
                tree.addChild(new GameState(tree.getState(), move), move);
            }
        }

        for (GameTree childTree : tree.getChildTrees()) {
            if (levels > 0) {
                if (childTree.isMrXRound()) { // know for all
                    NextRoundVisitor visitor = new NextRoundVisitor(childTree.getState().validMoves(Black), (levels-1)); // valid moves only needed at the bottom
                    childTree.accept(visitor);
                }
                else {
                    Set<Move> detectiveMoves = new HashSet<>();
                    for (Colour colour : childTree.getState().getDetectives()) {
                        detectiveMoves.addAll(childTree.getState().validMoves(colour));
                    }
                    NextRoundVisitor detectivesMovesAdder = new NextRoundVisitor(detectiveMoves, (levels-1));
                    childTree.accept(detectivesMovesAdder);
                }
            }
        }
    }
}
