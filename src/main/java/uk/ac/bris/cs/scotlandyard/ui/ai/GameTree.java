package uk.ac.bris.cs.scotlandyard.ui.ai;

import uk.ac.bris.cs.scotlandyard.model.Move;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Adam on 31/03/2017.
 */
public class GameTree {
    private List<GameTree> childTrees = new ArrayList();
    private List<Move> childMoves = new ArrayList();
    private GameState state;
    private int score;
    private boolean isMrXRound;

    public GameTree(GameState state, boolean isMrXRound) {
        this.state = state;
        this.isMrXRound = isMrXRound;
    }

    public void addChild(GameState state, Move move) {
        this.childTrees.add(new GameTree(state,!this.isMrXRound));
        this.childMoves.add(move);
    }

    public boolean isMrXRound() {
        return this.isMrXRound;
    }

    public List<GameTree> getChildTrees() {
        return this.childTrees;
    }

    public Move getMove(GameTree tree) {
        return childMoves.get(childTrees.indexOf(tree));
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
