package uk.ac.bris.cs.scotlandyard.ui.ai;

import uk.ac.bris.cs.scotlandyard.model.Move;

import java.util.Set;

/**
 * Created by Adam on 31/03/2017.
 */
public class NextRoundVisitor extends TreeVisitor {

    private Set<Move> moves;

    public NextRoundVisitor(Set<Move> moves) {
        this.moves = moves;
    }

    public void visit(GameTree tree) {
        for (Move move : this.moves) {
            tree.addChild(new GameState(tree.getState(), move));
        }

    }
}
