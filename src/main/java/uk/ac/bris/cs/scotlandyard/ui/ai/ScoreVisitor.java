package uk.ac.bris.cs.scotlandyard.ui.ai;

import static uk.ac.bris.cs.scotlandyard.ui.ai.MyAI.scoreBoard;

/**
 * Created by Adam on 31/03/2017.
 */
public class ScoreVisitor extends TreeVisitor {

    public void visit(GameTree tree) {
        int bestScore = Integer.MIN_VALUE;
        for (GameTree childTree : tree.getChildTrees()) {
            visit(childTree);
            if (childTree.getScore() > bestScore) tree.setScore(childTree.getScore());
        }
        if (tree.getChildTrees().isEmpty()) tree.setScore(scoreBoard(tree.getState()));
    }
}
