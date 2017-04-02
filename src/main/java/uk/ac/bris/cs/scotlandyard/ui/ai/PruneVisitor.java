package uk.ac.bris.cs.scotlandyard.ui.ai;

/**
 * Created by Adam on 01/04/2017.
 */
public class PruneVisitor extends TreeVisitor {

    private int threshold;

    public PruneVisitor(int threshold) {
        this.threshold = threshold;
    }

    @Override
    public void visit(GameTree tree) {
        for (GameTree childTree : tree.getChildTrees()) {
            if (!childTree.isDeadNode()) visit(childTree);
            if (childTree.getScore() < threshold) childTree.isDeadNode(true);
        }
    }
}
