package uk.ac.bris.cs.scotlandyard.ui.gamemonitor;

import uk.ac.bris.cs.scotlandyard.ui.ai.GameTree;

import java.util.ArrayList;
import java.util.List;

/**
 * Tree data-structure containing information on the drawn tree.
 */
class DrawTree {
    private List<DrawTree> childTrees = new ArrayList<>();
    private boolean deadNode;
    private int score;
    private int x;
    private int y;
    private int spaceNeeded;
    private boolean isMrXRound;
    private boolean maxPruned;

    /**
     * Creates a DrawTree from the
     * @param tree The GameTree to generate a DrawTree from.
     * @param x The x position of the first node in pixels.
     * @param y The y position of the first node in pixels.
     */
    DrawTree(GameTree tree, int x, int y) {
        this.deadNode = tree.isDeadNode();
        this.score = tree.getScore();
        this.spaceNeeded = tree.getChildTrees().size();
        this.x = x;
        this.y = y;
        this.maxPruned = tree.hasBeenMaxMovesPruned();

        for(GameTree child : tree.getChildTrees()) {
            this.childTrees.add(new DrawTree(child));
        }
    }

    /**
     * Creates a DrawTree from a GameTree. Used for internal recursion.
     * @param tree The GameTree to create the DrawTreeFrom.
     */
    private DrawTree(GameTree tree) {

        this.deadNode = tree.isDeadNode();
        this.score = tree.getScore();
        this.spaceNeeded = tree.getChildTrees().size();
        this.isMrXRound = tree.isMrXRound();

        for(GameTree child : tree.getChildTrees()) {
            this.childTrees.add(new DrawTree(child));
        }
    }

    /**
     * Returns the score at a node of the DrawTree.
     * @return The score at a node of the DrawTree.
     */
    int getScore() {
        return this.score;
    }

    /**
     * Returns whether ths node is a dead node.
     * @return Whether ths node is a dead node.
     */
    boolean isDeadNode() {
        return this.deadNode;
    }

    /**
     * Returns a list of all child DrawTrees.
     * @return List of ChildDrawTrees. Not null.
     */
    List<DrawTree> getChildDrawTrees() {
        return this.childTrees;
    }

    /**
     * Returns the space needed for a node, that is the combined space needed for all child trees.
     * @return The space needed for a node.
     */
    int getSpaceNeeded() {
        return this.spaceNeeded;
    }

    /**
     * Sets the amount of space needed for this tree.
     * @param space The amount of space needed for this tree in pixels.
     */
    void setSpaceNeeded(int space) {
        this.spaceNeeded = space;
    }


    void accept(DrawTreeVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Sets the X location of the tree node.
     * @param x The X location of the tree node.
     */
    void setX(int x) {
        this.x = x;
    }

    /**
     * Sets the Y location of the tree node.
     * @param y The Y location of the tree node.
     */
    void setY(int y) {
        this.y = y;
    }

    /**
     * Gets the X location of the tree node.
     * @return The X location of the tree node.
     */
    int getX() {
        return this.x;
    }

    /**
     * Gets the Y location of the tree node.
     * @return The Y location of the tree node.
     */
    int getY() {
        return this.y;
    }

    /**
     * Returns whether this DrawTree represents a MrX round.
     * @return Whether this DrawTree represents a MrX round.
     */
    boolean getIsMrXRound() {
        return this.isMrXRound;
    }

    boolean getMaxPruned() {
        return this.maxPruned;
    }
}
