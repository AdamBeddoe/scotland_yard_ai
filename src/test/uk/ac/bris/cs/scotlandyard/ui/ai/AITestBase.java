package uk.ac.bris.cs.scotlandyard.ui.ai;

import org.junit.BeforeClass;
import uk.ac.bris.cs.gamekit.graph.Edge;
import uk.ac.bris.cs.gamekit.graph.Graph;
import uk.ac.bris.cs.gamekit.graph.Node;
import uk.ac.bris.cs.gamekit.graph.UndirectedGraph;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYardGraphReader;
import uk.ac.bris.cs.scotlandyard.model.Transport;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static uk.ac.bris.cs.scotlandyard.model.Transport.Taxi;

/**
 * Contains constructions needed for testing.
 * To be extended by test classes.
 */
public abstract class AITestBase {
    private static Graph<Integer, Transport> defaultGraph;
    private static Graph<Integer, Transport> flatTaxiGraph;

    @BeforeClass
    public static void setUp() {
        generateDefaultGraph();
        generateFlatTaxiGraph();

    }

    /**
     * Generates the default graph used by the game.
     */
    private static void generateDefaultGraph() {
        try {
            defaultGraph = ScotlandYardGraphReader.fromLines(Files.readAllLines(
                    Paths.get(AITestBase.class.getResource("/game_graph.txt").toURI())));

        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Generates a flat graph
     */
    private static void generateFlatTaxiGraph() {
        flatTaxiGraph = new UndirectedGraph<>();
        //TODO sort thing out
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
    }


    /**
     * Returns the default graph used in the actual game
     *
     * @return The graph. Never null
     */
    static Graph<Integer, Transport> defaultGraph() {
        return defaultGraph;
    }

    /**
     * Returns a flat graph.
     *
     * @return The graph. Never null.
     */
    static Graph<Integer, Transport> flatTaxiGraph() {
        return flatTaxiGraph;
    }


}
