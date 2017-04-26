package uk.ac.bris.cs.scotlandyard.ui.ai;

/**
 * An abstract visitor for classes that need to visit a GameTree.
 */
abstract class TreeVisitor {

    abstract void visit (GameTree tree);
}
