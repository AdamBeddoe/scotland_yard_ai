package uk.ac.bris.cs.scotlandyard.ui.gamemonitor;

import uk.ac.bris.cs.scotlandyard.ui.ai.GameTree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adam on 24/04/2017.
 */
public class DrawTree {
    private List<DrawTree> childTrees = new ArrayList<>();
    private boolean deadNode;
    private int score;
    private int x;
    private int y;
    private int spaceNeeded;
    private boolean isMrXRound;

    public DrawTree(GameTree tree) {
        this.deadNode = tree.isDeadNode();
        this.score = tree.getScore();
        this.spaceNeeded = tree.getChildTrees().size();
        this.isMrXRound = tree.isMrXRound();

        for(GameTree child : tree.getChildTrees()) {
            this.childTrees.add(new DrawTree(child));
        }
    }

    public DrawTree(GameTree tree, int x, int y) {
        this.deadNode = tree.isDeadNode();
        this.score = tree.getScore();
        this.spaceNeeded = tree.getChildTrees().size();
        this.x = x;
        this.y = y;

        for(GameTree child : tree.getChildTrees()) {
            this.childTrees.add(new DrawTree(child));
        }
    }

    public int getScore() {
        return this.score;
    }

    public boolean isDeadNode() {
        return this.deadNode;
    }

    public List<DrawTree> getChildDrawTrees() {
        return this.childTrees;
    }

    public int getSpaceNeeded() {
        return this.spaceNeeded;
    }

    public void setSpaceNeeded(int space) {
        this.spaceNeeded = space;
    }

    public void accept(DrawTreeVisitor visitor) {
        visitor.visit(this);
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public boolean getIsMrXRound() {
        return this.isMrXRound;
    }
}
