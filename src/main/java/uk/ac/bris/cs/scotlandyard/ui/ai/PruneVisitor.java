package uk.ac.bris.cs.scotlandyard.ui.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Adam on 01/04/2017.
 */
public class PruneVisitor extends TreeVisitor {

    private int threshold = -1;
    private boolean isUsingThreshold;
    private int maxMrXMoves = -1;
    private boolean isUsingMaxMrXMoves;
    private int maxDetectiveMoves = -1;
    private boolean isUsingMaxDetectiveMoves;

    public void setThreshold(int threshold) {
        this.isUsingThreshold = true;
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

        if (this.isUsingThreshold) thresholdPrune(tree);
        if (!tree.getChildTrees().isEmpty() && !hasBeenVisited(tree)) {
            if (this.isUsingMaxDetectiveMoves && !tree.isMrXRound()) maxDetectiveMovePrune(tree);
            if (this.isUsingMaxMrXMoves && tree.isMrXRound()) maxMrXMovePrune(tree);
        }

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
        List<GameTree> rawTrees = tree.getChildTrees();
        List<GameTree> sortedTrees = new ArrayList<>();
        sortedTrees.addAll(rawTrees);
        sortedTrees.sort(Comparator.comparing(GameTree::getScore));
        //System.out.print("[");

        //System.out.println("hi " + (sortedTrees.size()));
        for (int i = (sortedTrees.size()-1); i>=this.maxDetectiveMoves; i--) {
            sortedTrees.get(i).isDeadNode(true);
        //    System.out.println(i);
            //System.out.println(sortedTrees.get(i).isDeadNode());
        }
        for (GameTree treeee : sortedTrees) {
        //    System.out.print(treeee.isDeadNode() +"-" + treeee.getScore() + ",");
        }
        //System.out.print("]\n");
        //printTree(tree);
    }

    private void maxMrXMovePrune(GameTree tree) {
        List<GameTree> allTrees = tree.getChildTrees();
        allTrees.sort(Comparator.comparing(GameTree::getScore));
        List<GameTree> bestTrees;
        if (this.maxMrXMoves > allTrees.size()) bestTrees = allTrees;
        else bestTrees = allTrees.subList(allTrees.size()-this.maxMrXMoves, allTrees.size());

        for (GameTree childTree : tree.getChildTrees()) {
            if (!bestTrees.contains(childTree)) childTree.isDeadNode(true);
        }
    }

    private boolean hasBeenVisited(GameTree tree) {
        for (GameTree child : tree.getChildTrees()) {
            if (child.isDeadNode()) return true;
        }
        return false;
    }

    public void printTree(GameTree tree) {
        if (!tree.getChildTrees().isEmpty()) {
            System.out.print("[");
            for (GameTree treeee : tree.getChildTrees()) {
                System.out.print(treeee.isDeadNode() +"-" + treeee.getScore() + ",");
            }
            System.out.print("]\n");
            for (GameTree child: tree.getChildTrees()) {
                printTree(child);
            }
        }
    }
}
