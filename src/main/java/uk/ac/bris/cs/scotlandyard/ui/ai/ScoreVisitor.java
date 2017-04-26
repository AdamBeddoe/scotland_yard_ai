package uk.ac.bris.cs.scotlandyard.ui.ai;


/**
 * Scores each node in a GameTree.
 */
class ScoreVisitor extends TreeVisitor {

    private Calculator calculator;

    /**
     *
     * @param calculator A calculator used to score the board.
     */
    ScoreVisitor(Calculator calculator) {
        this.calculator = calculator;
    }

    /**
     * Scores the end nodes with the calculator and then propagates the best score if MrX is chosing and the worst
     * if the detectives are moving.
     * @param tree The GameTree to visit.
     */
    void visit(GameTree tree) {
        if (tree.isMrXRound()) {
            int bestScore = Integer.MIN_VALUE;
            for (GameTree childTree : tree.getChildTrees()) {
                if (!childTree.isDeadNode()) visit(childTree);
                if (childTree.getScore() > bestScore) {
                    tree.setScore(childTree.getScore());
                    bestScore = childTree.getScore();
                }
            }
            if (tree.getChildTrees().isEmpty()) tree.setScore(this.calculator.scoreBoard(tree.getState()));
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
            if (tree.getChildTrees().isEmpty()) tree.setScore(this.calculator.scoreBoard(tree.getState()));
        }
    }
}
