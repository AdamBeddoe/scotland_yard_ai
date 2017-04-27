package uk.ac.bris.cs.scotlandyard.ui.ai;

import org.junit.Test;
import uk.ac.bris.cs.gamekit.graph.Graph;

import static org.junit.Assert.assertEquals;

/**
 * Tests the calculation of the dijkstra algorithm.
 */
public class dijkstraTests extends AITestBase{

    /**
     * Tests dijkstra on random locations.
     */
    @Test
    public void testStandardCaseDijkstra() {
        Graph graph = defaultGraph();

        assertEquals(1, Calculator.dijkstra(graph, 69, 86));
        assertEquals(4, Calculator.dijkstra(graph, 69, 129));
        assertEquals(4, Calculator.dijkstra(graph, 1, 99));
        assertEquals(8, Calculator.dijkstra(graph, 20, 30));
        assertEquals(5, Calculator.dijkstra(graph, 1, 199));
    }

    /**
     * Tests dijkstra on trivial distances.
     */
    @Test
    public void trivialDistanceTest() {
        Graph graph = defaultGraph();

        // First node
        assertEquals(0, Calculator.dijkstra(graph, 1, 1));
        // Random nodes
        assertEquals(0, Calculator.dijkstra(graph, 3, 3));
        assertEquals(0, Calculator.dijkstra(graph, 69, 69));
        // Last node
        assertEquals(0, Calculator.dijkstra(graph, 199, 199));
    }


    /**
     * Tests that invalid nodes should throw an IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void invalidNodeShouldThrow() {
        Graph graph = defaultGraph();

        Calculator.dijkstra(graph, 199, 200);
        Calculator.dijkstra(graph, 200, 199);
        Calculator.dijkstra(graph, 0, 1);
        Calculator.dijkstra(graph, 1, 0);
        Calculator.dijkstra(graph, 1, -1);
        Calculator.dijkstra(graph, -1, 1);
    }
}
