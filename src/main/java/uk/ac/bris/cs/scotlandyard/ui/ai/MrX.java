package uk.ac.bris.cs.scotlandyard.ui.ai;

import uk.ac.bris.cs.scotlandyard.model.Move;
import uk.ac.bris.cs.scotlandyard.model.Player;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYardView;
import java.util.Set;
import java.util.function.Consumer;


/**
 * Created by Adam on 30/03/2017.
 */
public class MrX implements Player {
    private MyAI ai;

    public MrX(MyAI ai) {
        this.ai = ai;
    }

    @Override
    public void makeMove(ScotlandYardView view, int location, Set<Move> moves,
                         Consumer<Move> callback) {

        GameTree tree = new GameTree(new GameState(view, location),true);

        for (int i = 1; i <= 3; i++) {
            NextRoundVisitor tilo = new NextRoundVisitor(moves, i);
            long startTime = System.nanoTime();
            tree.accept(tilo);
            long endTime = System.nanoTime();
            System.out.println("Tilo Time: "+(endTime-startTime)/1000000);


            ScoreVisitor nick = new ScoreVisitor(this.ai);

            long startTimeNick = System.nanoTime();
            tree.accept(nick);
            long endTimeNick = System.nanoTime();
            System.out.println("Nick nacs: "+(endTimeNick-startTimeNick)/1000000);

            PruneVisitor bigPrune = new PruneVisitor(100);
            tree.accept(bigPrune);
        }


        callback.accept(selectMove(tree));
    }

    private Move selectMove(GameTree tree) {
        System.out.println("Starting Selection: ");
        int highestScore = 0;
        GameTree bestTree = tree.getChildTrees().get(0);
        for (GameTree currentTree : tree.getChildTrees()) {
            System.out.print("t: ");
            System.out.print(" " + currentTree.getScore() + " ");
            if (currentTree.getScore() > highestScore) {
                bestTree = currentTree;
                highestScore = bestTree.getScore();
                System.out.print("!! ");
            }
        }
        System.out.println("\nBest score is " + highestScore);
        return tree.getMove(bestTree);
    }
}
