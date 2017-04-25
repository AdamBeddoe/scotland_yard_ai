package uk.ac.bris.cs.scotlandyard.ui.ai;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adam on 01/04/2017.
 */
public class PruneVisitor extends TreeVisitor {

    private int threshold = -1;
    private boolean isUsingThreashold;
    private int maxMrXMoves = -1;
    private boolean isUsingMaxMrXMoves;
    private int maxDetectiveMoves = -1;
    private boolean isUsingMaxDetectiveMoves;

    public void setThreshold(int threshold) {
        this.isUsingThreashold = true;
        this.threshold = threshold;
    }

    public void setMaxMrXMoves(int max) {
        this.isUsingMaxMrXMoves = true;
        this.maxMrXMoves = max;
    }

    public void setMaxDetectiveMoves(int max) {
        this.isUsingMaxDetectiveMoves = true;
        this.maxDetectiveMoves = max;
    }

    @Override
    public void visit(GameTree tree) {
        if (this.isUsingMaxDetectiveMoves && !tree.isMrXRound()) maxDetectiveMovePrune(tree);
        if (this.isUsingMaxMrXMoves) maxMrXMovePrune(tree);
        if (this.isUsingThreashold) thresholdPrune(tree);

        for (GameTree childTree : tree.getChildTrees()) {
            if (!childTree.isDeadNode()) visit(childTree);
            else if (childTree.isDeadNode() && !childTree.getChildTrees().isEmpty()) childTree.removeChildren();
        }
    }

    private void thresholdPrune(GameTree childTree) {
        if (childTree.getScore() < threshold) {
            childTree.isDeadNode(true);
        }
    }

    private void maxDetectiveMovePrune(GameTree tree) {
        int lowestInList = Integer.MAX_VALUE;
        List<GameTree> bestTrees = new ArrayList<>();
        if (tree.getScore() < lowestInList && bestTrees.size() < this.maxDetectiveMoves) {
            bestTrees.add(tree);
            lowestInList = tree.getScore();
        }
        else if (tree.getScore() < lowestInList && bestTrees.size() < this.maxDetectiveMoves) {

        }
    }

    private void maxMrXMovePrune(GameTree tree) {

    }
}
