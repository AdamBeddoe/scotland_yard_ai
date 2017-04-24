package uk.ac.bris.cs.scotlandyard.ui.gamemonitor;

/**
 * Created by Adam on 24/04/2017.
 */
public class SpaceNeededVisitor extends DrawTreeVisitor {

    public SpaceNeededVisitor() {

    }

    public void visit(DrawTree tree) {
        int calc = 0;

        if(!tree.isDeadNode()) {
            for(DrawTree child : tree.getChildDrawTrees()) {
                visit(child);
            }

            for (DrawTree child : tree.getChildDrawTrees()) {
                calc = calc + child.getSpaceNeeded();
            }

            tree.setSpaceNeeded(calc);

        }
        else {
            tree.setSpaceNeeded(8); //px
        }

        //if(calc != 0) System.out.println("setSpaceNeeded: " + calc);
    }
}
