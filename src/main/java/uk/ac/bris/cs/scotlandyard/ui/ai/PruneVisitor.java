package uk.ac.bris.cs.scotlandyard.ui.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Prunes a GameTree based on one of 3 methods: Threshold, MaxMrMoves and MaxDetectiveMoves.
 */
class PruneVisitor extends TreeVisitor {

    private int threshold = -1;
    private boolean isUsingThreshold;
    private int maxMrXMoves = -1;
    private boolean isUsingMaxMrXMoves;
    private int maxDetectiveMoves = -1;
    private boolean isUsingMaxDetectiveMoves;

    /**
     * Enables and sets pruning based on a score threshold.
     * All nodes with score lower than that will be pruned.
     * @param threshold
     */
    void setThreshold(int threshold) {
        this.isUsingThreshold = true;
        this.threshold = threshold;
    }

    /**
     * Enables pruning of MrX based on a max number of child trees per node.
     * @param max The max number of child trees for any MrX move.
     */
    void setMaxMrXMoves(int max) {
        this.isUsingMaxMrXMoves = true;
        this.maxMrXMoves = max;
    }

    /**
     * Enables pruning of Detective moves based on a max number of child trees per node.
     * @param max The max number of child trees for any set of Detective moves.
     */
    void setMaxDetectiveMoves(int max) {
        this.isUsingMaxDetectiveMoves = true;
        this.maxDetectiveMoves = max;
    }

    /**
     * Visits the whole tree and runs the pruning methods that are enabled.
     * @param tree The tree to be pruned, must be scored.
     */
    @Override
    public void visit(GameTree tree) {

        if (this.isUsingThreshold) thresholdPrune(tree);
        if (!tree.getChildTrees().isEmpty() && !tree.hasBeenMaxMovesPruned()) {
            if (this.isUsingMaxDetectiveMoves && !tree.isMrXRound()) {
                maxDetectiveMovePrune(tree);
            }
            if (this.isUsingMaxMrXMoves && tree.isMrXRound()) maxMrXMovePrune(tree);
        }
        for (GameTree childTree : tree.getChildTrees()) {
            if (!childTree.isDeadNode()) visit(childTree);
            else if (childTree.isDeadNode() && !childTree.getChildTrees().isEmpty()) childTree.removeChildren();
        }
    }

    // Pruning based on a threshold. Anything lower marked as dead.
    private void thresholdPrune(GameTree childTree) {
        if (childTree.getScore() < threshold) {
            childTree.isDeadNode(true);
        }
    }

    // Pruning based on max number of child trees for any MrX node.
    private void maxDetectiveMovePrune(GameTree tree) {
        List<GameTree> rawTrees = tree.getChildTrees();
        List<GameTree> sortedTrees = new ArrayList<>();
        sortedTrees.addAll(rawTrees);
        sortedTrees.sort(Comparator.comparing(GameTree::getScore));
        for (int i = sortedTrees.size()-1; i>this.maxDetectiveMoves; i--) {
            sortedTrees.get(i).isDeadNode(true);
        }
        tree.setMaxMovesPruned(true);
    }

    // Pruning based on max number of child trees for any Detective node.
    private void maxMrXMovePrune(GameTree tree) {
        List<GameTree> rawTrees = tree.getChildTrees();
        List<GameTree> sortedTrees = new ArrayList<>();
        sortedTrees.addAll(rawTrees);
        sortedTrees.sort(Comparator.comparing(GameTree::getScore));
        for (int i = 0; i<sortedTrees.size()-this.maxMrXMoves; i++) {
            sortedTrees.get(i).isDeadNode(true);
        }
        tree.setMaxMovesPruned(true);
    }
}
