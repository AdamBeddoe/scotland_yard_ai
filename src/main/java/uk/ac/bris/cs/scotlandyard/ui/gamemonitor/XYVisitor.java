package uk.ac.bris.cs.scotlandyard.ui.gamemonitor;

/**
 * Visitor that calculates the XY coordinates of the nodes in a DrawTree.
 * Must be called after SpaceNeeded visitor.
 */
public class XYVisitor extends DrawTreeVisitor {

    /**
     * Calculates the XY position of nodes.
     * Must be called after a SpaceNeeded visitor.
     * @param tree The tree to calculate the values of.
     */
    public void visit(DrawTree tree) {
        int tempX = (tree.getX()) - (tree.getSpaceNeeded()/2);
        int previousXSpan = 0;

        for (DrawTree child : tree.getChildDrawTrees()) {
            tempX = tempX + (child.getSpaceNeeded()/2) + (previousXSpan/2);
            previousXSpan = child.getSpaceNeeded();

            child.setX(tempX);
            child.setY(tree.getY() + 150);

            visit(child);
        }
    }
}
