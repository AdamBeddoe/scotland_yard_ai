import org.junit.Test;
import uk.ac.bris.cs.gamekit.graph.Graph;
import uk.ac.bris.cs.scotlandyard.model.Player;
import uk.ac.bris.cs.scotlandyard.ui.ai.MyAI;

import static org.junit.Assert.assertEquals;
import static uk.ac.bris.cs.scotlandyard.model.Colour.Black;

/**
 * Created by adam on 28/03/2017.
 */
public class shortestDistTest extends AITestBase{

    @Test
    public void testShortestDist() {
        Graph graph = defaultGraph();

        MyAI testAI = new MyAI();

        assertEquals(0, testAI.dijkstra(graph, 3, 3));
        assertEquals(1, testAI.dijkstra(graph, 69, 86));
        assertEquals(4, testAI.dijkstra(graph, 69, 129));
    }
}
