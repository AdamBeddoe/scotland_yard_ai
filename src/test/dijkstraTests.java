import org.junit.Test;
import uk.ac.bris.cs.gamekit.graph.Graph;
import uk.ac.bris.cs.scotlandyard.model.Player;
import uk.ac.bris.cs.scotlandyard.ui.ai.MyAI;

import static org.junit.Assert.assertEquals;
import static uk.ac.bris.cs.scotlandyard.model.Colour.Black;

/**
 * Created by adam on 28/03/2017.
 */
public class dijkstraTests extends AITestBase{

    @Test
    public void testStandardCaseDijkstra() {
        Graph graph = defaultGraph();

        assertEquals(1, MyAI.dijkstra(graph, 69, 86));
        assertEquals(4, MyAI.dijkstra(graph, 69, 129));
        assertEquals(4, MyAI.dijkstra(graph, 1, 99));
        assertEquals(8, MyAI.dijkstra(graph, 20, 30));
        assertEquals(5, MyAI.dijkstra(graph, 1, 199));
    }

    @Test
    public void trivialDistanceTest() {
        Graph graph = defaultGraph();

        // First node
        assertEquals(0, MyAI.dijkstra(graph, 1, 1));
        // Random nodes
        assertEquals(0, MyAI.dijkstra(graph, 3, 3));
        assertEquals(0, MyAI.dijkstra(graph, 69, 69));
        // Last node
        assertEquals(0, MyAI.dijkstra(graph, 199, 199));
    }


    @Test(expected = IllegalArgumentException.class)        //TODO Placing in a valid dijkstra call still passes test?
    public void invalidNodeShouldThrow() {
        Graph graph = defaultGraph();

        MyAI.dijkstra(graph, 199, 200);
        MyAI.dijkstra(graph, 200, 199);
        MyAI.dijkstra(graph, 0, 1);
        MyAI.dijkstra(graph, 1, 0);
        MyAI.dijkstra(graph, 1, -1);
        MyAI.dijkstra(graph, -1, 1);
    }
}
