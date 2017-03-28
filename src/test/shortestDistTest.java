import org.junit.Test;
import uk.ac.bris.cs.gamekit.graph.Graph;
import uk.ac.bris.cs.scotlandyard.model.Player;
import uk.ac.bris.cs.scotlandyard.ui.ai.MyAI;

import static uk.ac.bris.cs.scotlandyard.model.Colour.Black;

/**
 * Created by adam on 28/03/2017.
 */
public class shortestDistTest extends AITestBase{

    @Test
    public void testShortestDist() {
        Graph graph = defaultGraph();

        MyAI testAI = new MyAI();
        Player testPlayer = testAI.createPlayer(Black);

        assertEquals(testAI.shortestDist())
    }
}
