package uk.ac.bris.cs.scotlandyard.ui.gamemonitor;

/**
 * Visitor that transforms all the pixel locations of the nodes in the tree.
 */
class TransformVisitor extends DrawTreeVisitor {
    private int transform;

    /**
     * Makes a new TransformVisitor.
     * @param transform The amount of pixels to move right.
     */
    TransformVisitor(int transform) {
        this.transform = transform;
    }

    /**
     * Visits a DrawTree and transforms the nodes.
     * @param tree The tree to visit.
     */
    public void visit(DrawTree tree) {
        for (DrawTree child : tree.getChildDrawTrees()) {
            child.setX(child.getX() + transform);

            visit(child);
        }
    }
}
