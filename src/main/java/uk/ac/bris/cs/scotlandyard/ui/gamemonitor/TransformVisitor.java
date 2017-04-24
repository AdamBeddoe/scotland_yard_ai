package uk.ac.bris.cs.scotlandyard.ui.gamemonitor;

import uk.ac.bris.cs.scotlandyard.ui.ai.TreeVisitor;

/**
 * Created by Adam on 24/04/2017.
 */
public class TransformVisitor extends DrawTreeVisitor {
    private int transform;

    public TransformVisitor(int transform) {
        this.transform = transform;
    }

    public void visit(DrawTree tree) {
        for (DrawTree child : tree.getChildDrawTrees()) {
            child.setX(child.getX() + transform);

            visit(child);
        }
    }
}
