package uk.ac.bris.cs.scotlandyard.ui.ai;

/**
 * Interface required for AI players to use GameTreeBuilder.
 */
public interface AIPlayer {

    /**
     *  Called by the builder when the next iteration of the tree has been generated.
     *
     * @param tree The tree passed by the builder.
     */
    void updateTree(GameTree tree);

}
