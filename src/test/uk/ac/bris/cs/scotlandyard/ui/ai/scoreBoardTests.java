package uk.ac.bris.cs.scotlandyard.ui.ai;

import org.junit.Test;
import uk.ac.bris.cs.gamekit.graph.Edge;
import uk.ac.bris.cs.gamekit.graph.Graph;
import uk.ac.bris.cs.gamekit.graph.Node;
import uk.ac.bris.cs.gamekit.graph.UndirectedGraph;
import uk.ac.bris.cs.scotlandyard.model.*;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static uk.ac.bris.cs.scotlandyard.model.Colour.Blue;
import static uk.ac.bris.cs.scotlandyard.model.Colour.Red;
import static uk.ac.bris.cs.scotlandyard.model.Transport.Taxi;

/**
 * Tests the scoring function.
 */
public class scoreBoardTests extends AITestBase{
    private Calculator calculator = new Calculator();
    private Calculator flatCalculator = new FlatCalculator();

    /**
     * Tests that the score returns an integer.
     */
    @Test
    public void scoreValidReturnType() {
        Graph graph = defaultGraph();

        Map<Colour,Integer> detectives = new HashMap<>();
        detectives.put(Blue, 20);

        GameState testState = new GameState(graph, detectives, 10);
        assertThat(calculator.scoreBoard(testState), instanceOf (Integer.class));
    }

    /**
     * Tests that the captured score is -1000.
     */
    @Test
    public void testCapturedScore() {
        Graph graph = defaultGraph();

        Map<Colour,Integer> detectives = new HashMap<>();
        detectives.put(Blue, 20);

        GameState testState = new GameState(graph, detectives, 20);
        assertEquals(-1000, calculator.scoreBoard(testState));
    }

    /**
     * Tests that the captured score is -1000 with multiple detectives.
     */
    @Test
    public void testCapturedScoreWithMultipleDetectives() {
        Graph graph = defaultGraph();

        Map<Colour,Integer> detectives = new HashMap<>();
        detectives.put(Blue, 20);
        detectives.put(Red, 30);

        GameState testState = new GameState(graph, detectives, 20);
        assertEquals(-1000, calculator.scoreBoard(testState));
    }

    /**
     * Test that a game with just MrX scores correct.
     */
    @Test
    public void justMrXScoresCorrect() {
        Graph graph = flatTaxiGraph();

        Map<Colour,Integer> detectives = new HashMap<>();

        // Score should be 6 for each valid move
        GameState testState1 = new GameState(graph, detectives, 3);
        assertEquals(6, calculator.scoreBoard(testState1));
        GameState testState2 = new GameState(graph, detectives, 4);
        assertEquals(6, calculator.scoreBoard(testState2));
    }

    /**
     * Tests some simple flat cases of the scoring with multiple detectives.
     */
    @Test
    public void flatMrXAndDetectiveScoresCorrect() {
        Graph graph = flatTaxiGraph();

        Map<Colour,Integer> detectives = new HashMap<>();
        detectives.put(Blue, 4);

        // Score should be 3 for valid moves + (1^2) away = 4
        GameState testState1 = new GameState(graph, detectives, 3);
        assertEquals(4, flatCalculator.scoreBoard(testState1));
        // Score should be -1000 as MrX is captured
        GameState testState2 = new GameState(graph, detectives, 4);
        assertEquals(-1000, flatCalculator.scoreBoard(testState2));
        // Score should be 4 for valid moves + (2^2) away = 8
        GameState testState3 = new GameState(graph, detectives, 2);
        assertEquals(8, flatCalculator.scoreBoard(testState3));
    }
}

/**
 * A version of the calculator which can calculate on a FlatGraph.
 */
class FlatCalculator extends Calculator {
    private int graphDistances[][] = new int[8][8];


    /**
     * Makes a new calculator.
     */
    FlatCalculator() {
        Graph<Integer,Transport> flatTaxiGraph = new UndirectedGraph<>();
        flatTaxiGraph.addNode(new Node<>(1));
        flatTaxiGraph.addNode(new Node<>(2));
        flatTaxiGraph.addNode(new Node<>(3));
        flatTaxiGraph.addNode(new Node<>(4));
        flatTaxiGraph.addNode(new Node<>(5));
        flatTaxiGraph.addNode(new Node<>(6));
        flatTaxiGraph.addNode(new Node<>(7));
        flatTaxiGraph.addEdge(new Edge<>(new Node<>(1), new Node<>(2),Taxi));
        flatTaxiGraph.addEdge(new Edge<>(new Node<>(2), new Node<>(3),Taxi));
        flatTaxiGraph.addEdge(new Edge<>(new Node<>(3), new Node<>(4),Taxi));
        flatTaxiGraph.addEdge(new Edge<>(new Node<>(4), new Node<>(5),Taxi));
        flatTaxiGraph.addEdge(new Edge<>(new Node<>(5), new Node<>(6),Taxi));
        flatTaxiGraph.addEdge(new Edge<>(new Node<>(6), new Node<>(7),Taxi));
        for (int i = 1; i < 8; i++) {
            for (int j = 1; j < 8; j++) {
                this.graphDistances[i][j] = Calculator.dijkstra(flatTaxiGraph, i, j);
            }
        }
    }

    /**
     * Returns the flat graph distances.
     * @param x First location.
     * @param y Second location.
     * @return The pre-calculated distances of a FlatGraph.
     */
    @Override
    public int getGraphDistances(int x, int y) {
        return this.graphDistances[x][y];
    }
}
