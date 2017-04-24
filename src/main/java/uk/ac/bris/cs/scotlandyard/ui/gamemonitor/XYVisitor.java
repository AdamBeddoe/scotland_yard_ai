package uk.ac.bris.cs.scotlandyard.ui.gamemonitor;

/**
 * Created by Adam on 24/04/2017.
 */
public class XYVisitor extends DrawTreeVisitor {

    public void visit(DrawTree tree) {
        int tempX = (tree.getX()) - (tree.getSpaceNeeded()/2);

        for (DrawTree child : tree.getChildDrawTrees()) {
            tempX = tempX + child.getSpaceNeeded();

            child.setX(tempX);
            child.setY(tree.getY() + 150);

            visit(child);

            //System.out.println("X: " + child.getX());
            //System.out.println("spaceNeeded: " + child.getSpaceNeeded() + " " + child.isDeadNode());
        }
    }
}
