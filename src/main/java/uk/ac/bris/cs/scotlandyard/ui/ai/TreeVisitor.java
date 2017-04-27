package uk.ac.bris.cs.scotlandyard.ui.ai;

/**
 * An abstract visitor for classes that need to visit a GameTree.
 */
public abstract class TreeVisitor {

    public abstract void visit (GameTree tree);
}
