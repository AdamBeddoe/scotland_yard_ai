package uk.ac.bris.cs.scotlandyard.ui.gamemonitor;

/**
 * Created by Adam on 24/04/2017.
 */
public class XYVisitor extends DrawTreeVisitor {

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
