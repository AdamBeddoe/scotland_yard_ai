package uk.ac.bris.cs.scotlandyard.ui.ai;

import uk.ac.bris.cs.scotlandyard.model.Move;

import java.util.Set;

/**
 * Created by Adam on 20/04/2017.
 */
public class GameTreeBuilder {
    private GameState startState;
    private boolean playerIsMrX;
    private int levels;
    private Set<Move> moves;
    private MyAI AI;


    public GameTreeBuilder(boolean playerIsMrX) {
        this.playerIsMrX = playerIsMrX;
    }

    public void setStartState(GameState state) {
        this.startState = state;
    }

    public void setLookAheadLevels(int levels) {
        this.levels = levels;
    }

    public void setMoves(Set<Move> moves) {
        this.moves = moves;
    }

    public void setAI(MyAI myAI) {
        this.AI = myAI;
    }

    public GameTree build() {
        GameTree tree = new GameTree(this.startState,true);

        for (int i = 1; i <= this.levels; i++) {

            NextRoundVisitor tilo = new NextRoundVisitor(this.moves, i);

            long startTime = System.nanoTime();
            tree.accept(tilo);
            long endTime = System.nanoTime();
            System.out.println("Tilo Time: "+(endTime-startTime)/1000000);

            ScoreVisitor nick = new ScoreVisitor(this.AI);

            long startTimeNick = System.nanoTime();
            tree.accept(nick);
            long endTimeNick = System.nanoTime();
            System.out.println("Nick nacs: "+(endTimeNick-startTimeNick)/1000000);

            PruneVisitor bigPrune = new PruneVisitor(100);

            tree.accept(bigPrune);
        }
        return tree;
    }
}
