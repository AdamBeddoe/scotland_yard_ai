import org.junit.Test;
import uk.ac.bris.cs.gamekit.graph.Graph;
import uk.ac.bris.cs.gamekit.graph.Node;
import uk.ac.bris.cs.scotlandyard.model.Colour;
import uk.ac.bris.cs.scotlandyard.model.Move;
import uk.ac.bris.cs.scotlandyard.model.Ticket;
import uk.ac.bris.cs.scotlandyard.model.TicketMove;
import uk.ac.bris.cs.scotlandyard.ui.ai.GameState;
import uk.ac.bris.cs.scotlandyard.ui.ai.GameTree;
import uk.ac.bris.cs.scotlandyard.ui.ai.NextRoundVisitor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static uk.ac.bris.cs.scotlandyard.model.Colour.Black;
import static uk.ac.bris.cs.scotlandyard.model.Colour.Blue;
import static uk.ac.bris.cs.scotlandyard.model.Colour.Green;
import static uk.ac.bris.cs.scotlandyard.model.Ticket.Taxi;

/**
 * Created by Adam on 14/04/2017.
 */
public class NextRoundVisitorTests extends AITestBase{



    @Test
    public void justRun() {
        Graph graph = defaultGraph();

        Map<Colour,Integer> detectives = new HashMap<>();
        detectives.put(Blue, 4);
        detectives.put(Green, 7);

        GameState testState1 = new GameState(graph, detectives, 3);

        GameTree tree = new GameTree(testState1,true);

        Set<Move> moves = new HashSet<>();
        moves.add(new TicketMove(Black, Taxi , 20));
        NextRoundVisitor testRoundVisitor = new NextRoundVisitor(moves, 4);
        //tree.accept(testRoundVisitor); also doesn't work for some reason
    }
}
