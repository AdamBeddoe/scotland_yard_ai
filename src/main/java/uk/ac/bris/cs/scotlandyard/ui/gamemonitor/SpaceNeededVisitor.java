package uk.ac.bris.cs.scotlandyard.ui.gamemonitor;

/**
 * Visits a DrawTree and calculates
 */
class SpaceNeededVisitor extends DrawTreeVisitor {

    /**
     * Visits a tree and calculates the space needed for each tree branch.
     * @param tree The DrawTree to visit.
     */
    public void visit(DrawTree tree) {
        int calc = 0;

        if(!tree.getChildDrawTrees().isEmpty()) {
            for(DrawTree child : tree.getChildDrawTrees()) {
                visit(child);
            }

            for (DrawTree child : tree.getChildDrawTrees()) {
                calc = calc + child.getSpaceNeeded();
            }

            tree.setSpaceNeeded(calc);

        }
        else {
            tree.setSpaceNeeded(6); //px
        }

    }
}
