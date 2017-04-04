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
        MyAI testAI = new MyAI();

        assertEquals(1, MyAI.dijkstra(graph, 69, 86));
        assertEquals(4, MyAI.dijkstra(graph, 69, 129));
        assertEquals(4, MyAI.dijkstra(graph, 1, 99));
        assertEquals(5, MyAI.dijkstra(graph, 1, 199));
    }

    @Test
    public void trivialDistanceTest() {
        Graph graph = defaultGraph();
        MyAI testAI = new MyAI();


        // First node
        assertEquals(0, MyAI.dijkstra(graph, 1, 1));
        // Random nodes
        assertEquals(0, MyAI.dijkstra(graph, 3, 3));
        assertEquals(0, MyAI.dijkstra(graph, 69, 69));
        // Last node
        assertEquals(0, MyAI.dijkstra(graph, 199, 199));
    }


    @Test
    public void invalidNodeShouldThrow() {

    }
}
