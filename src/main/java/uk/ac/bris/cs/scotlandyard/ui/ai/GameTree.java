package uk.ac.bris.cs.scotlandyard.ui.ai;

import uk.ac.bris.cs.scotlandyard.model.Move;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Adam on 31/03/2017.
 */
public class GameTree {
    private List<GameTree> childTrees = new ArrayList();
    private GameState state;
    private int score;

    public GameTree(GameState state) {
        this.state = state;
    }

    public void addChild(GameState state) {
        this.childTrees.add(new GameTree(state));
    }

    public List<GameTree> getChildTrees() {
        return this.childTrees;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return this.score;
    }

    public GameState getState() {
        return this.state;
    }

    public void accept(TreeVisitor visitor) {
        visitor.visit(this);
    }
}
