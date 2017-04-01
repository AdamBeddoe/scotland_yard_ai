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
        long startTime = System.nanoTime();
        if (tree.isMrXRound()) {
            int bestScore = Integer.MIN_VALUE;
            for (GameTree childTree : tree.getChildTrees()) {
                visit(childTree);
                if (childTree.getScore() > bestScore) tree.setScore(childTree.getScore());
            }

            if (tree.getChildTrees().isEmpty()) tree.setScore(this.ai.scoreBoard(tree.getState()));


        }

        else {
            int worstScore = Integer.MAX_VALUE;
            for (GameTree childTree : tree.getChildTrees()) {
                visit(childTree);
                if (childTree.getScore() < worstScore) tree.setScore(childTree.getScore());
            }
            if (tree.getChildTrees().isEmpty()) tree.setScore(this.ai.scoreBoard(tree.getState()));
        }
        long endTime = System.nanoTime();
        if (((endTime-startTime)/1000000) > 0) {

        }
    }
}
