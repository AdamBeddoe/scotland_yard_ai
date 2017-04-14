import org.junit.Test;
import uk.ac.bris.cs.gamekit.graph.Edge;
import uk.ac.bris.cs.gamekit.graph.Graph;
import uk.ac.bris.cs.gamekit.graph.Node;
import uk.ac.bris.cs.gamekit.graph.UndirectedGraph;
import uk.ac.bris.cs.scotlandyard.model.*;
import uk.ac.bris.cs.scotlandyard.ui.ai.GameState;
import uk.ac.bris.cs.scotlandyard.ui.ai.MyAI;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static uk.ac.bris.cs.scotlandyard.model.Colour.Black;
import static uk.ac.bris.cs.scotlandyard.model.Colour.Blue;
import static uk.ac.bris.cs.scotlandyard.model.Colour.Red;
import static uk.ac.bris.cs.scotlandyard.model.Transport.Taxi;

/**
 * Created by Adam on 30/03/2017.
 */
public class scoreBoardTests extends AITestBase{
    MyAI testAI = new MyAI();
    MyAI flatAI = new FlatAI();

    @Test
    public void scoreValidReturnType() {
        Graph graph = defaultGraph();

        Map<Colour,Integer> detectives = new HashMap<>();
        detectives.put(Blue, 20);

        GameState testState = new GameState(graph, detectives, 10);
        assertThat(testAI.scoreBoard(testState), instanceOf (Integer.class));
    }

    @Test
    public void testCapturedScore() {
        Graph graph = defaultGraph();

        Map<Colour,Integer> detectives = new HashMap<>();
        detectives.put(Blue, 20);

        GameState testState = new GameState(graph, detectives, 20);
        assertEquals(-1000, testAI.scoreBoard(testState));
    }

    @Test
    public void testCapturedScoreWithMultipleDetectives() {
        Graph graph = defaultGraph();

        Map<Colour,Integer> detectives = new HashMap<>();
        detectives.put(Blue, 20);
        detectives.put(Red, 30);

        GameState testState = new GameState(graph, detectives, 20);
        assertEquals(-1000, testAI.scoreBoard(testState));
    }

    @Test
    public void justMrXScoresCorrect() {
        Graph graph = flatTaxiGraph();

        Map<Colour,Integer> detectives = new HashMap<>();

        // Score should be 6 for each valid move
        GameState testState1 = new GameState(graph, detectives, 3);
        assertEquals(6, testAI.scoreBoard(testState1));
        GameState testState2 = new GameState(graph, detectives, 4);
        assertEquals(6, testAI.scoreBoard(testState2));
    }

    @Test
    public void flatMrXAndDetectiveScoresCorrect() {
        Graph graph = flatTaxiGraph();

        Map<Colour,Integer> detectives = new HashMap<>();
        detectives.put(Blue, 4);

        // Score should be 3 for valid moves + (1^2) away = 4
        GameState testState1 = new GameState(graph, detectives, 3);
        assertEquals(4, flatAI.scoreBoard(testState1));
        // Score should be -1000 as MrX is captured
        GameState testState2 = new GameState(graph, detectives, 4);
        assertEquals(-1000, flatAI.scoreBoard(testState2));
        // Score should be 4 for valid moves + (2^2) away = 8
        GameState testState3 = new GameState(graph, detectives, 2);
        assertEquals(8, flatAI.scoreBoard(testState3));
    }
}

class FlatAI extends MyAI {
    int graphDistances[][] = new int[8][8];

    public FlatAI() {
        Graph<Integer,Transport> flatTaxiGraph = new UndirectedGraph();
        //TODO sort thing out
        flatTaxiGraph.addNode(new Node<>(1));
        flatTaxiGraph.addNode(new Node<>(2));
        flatTaxiGraph.addNode(new Node<>(3));
        flatTaxiGraph.addNode(new Node<>(4));
        flatTaxiGraph.addNode(new Node<>(5));
        flatTaxiGraph.addNode(new Node<>(6));
        flatTaxiGraph.addNode(new Node<>(7));
        flatTaxiGraph.addEdge(new Edge<Integer,Transport>(new Node(1), new Node<>(2),Taxi));
        flatTaxiGraph.addEdge(new Edge<Integer,Transport>(new Node(2), new Node<>(3),Taxi));
        flatTaxiGraph.addEdge(new Edge<Integer,Transport>(new Node(3), new Node<>(4),Taxi));
        flatTaxiGraph.addEdge(new Edge<Integer,Transport>(new Node(4), new Node<>(5),Taxi));
        flatTaxiGraph.addEdge(new Edge<Integer,Transport>(new Node(5), new Node<>(6),Taxi));
        flatTaxiGraph.addEdge(new Edge<Integer,Transport>(new Node(6), new Node<>(7),Taxi));
        for (int i = 1; i < 8; i++) {
            for (int j = 1; j < 8; j++) {
                this.graphDistances[i][j] = dijkstra(flatTaxiGraph, i, j);
            }
        }
    }

    @Override
    public int getGraphDistances(int x, int y) {
        return this.graphDistances[x][y];
    }
}
