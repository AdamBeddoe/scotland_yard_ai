package uk.ac.bris.cs.scotlandyard.ui.ai;


/**
 * Created by Adam on 31/03/2017.
 */
public class ScoreVisitor extends TreeVisitor {

    private MyAI ai;

    public ScoreVisitor(MyAI ai) {
        this.ai = ai;
    }

    public void visit(GameTree tree) {
        if (tree.isMrXRound()) {
            int bestScore = Integer.MIN_VALUE;
            for (GameTree childTree : tree.getChildTrees()) {
                if (!childTree.isDeadNode()) visit(childTree); // vs childTree.accept(this) should be consistent with other visitors
                if (childTree.getScore() > bestScore) {
                    tree.setScore(childTree.getScore());
                    bestScore = childTree.getScore();
                }
            }
            if (tree.getChildTrees().isEmpty()) System.out.println("Assigning score " + this.ai.scoreBoard(tree.getState()));
            if (tree.getChildTrees().isEmpty()) tree.setScore(this.ai.scoreBoard(tree.getState()));
        }

        else {
            int worstScore = Integer.MAX_VALUE;
            for (GameTree childTree : tree.getChildTrees()) {
                if (!childTree.isDeadNode()) visit(childTree);
                if (childTree.getScore() < worstScore) {
                    tree.setScore(childTree.getScore());
                    worstScore = childTree.getScore();
                }
            }
            if (tree.getChildTrees().isEmpty()) tree.setScore(this.ai.scoreBoard(tree.getState()));
        }
        System.out.println("                      Visiting");
    }
}
