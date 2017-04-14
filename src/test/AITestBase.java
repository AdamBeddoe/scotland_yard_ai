import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
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
 * Created by adam on 28/03/2017.
 */
public abstract class AITestBase {
    private static Graph<Integer, Transport> defaultGraph;
    private static Graph<Integer, Transport> flatTaxiGraph;


    @BeforeClass
    public static void setUp() {
        generateDefaultGraph();
        generateFlatTaxiGraph();

    }


    private static void generateDefaultGraph() {
        try {
            defaultGraph = ScotlandYardGraphReader.fromLines(Files.readAllLines(
                    Paths.get(AITestBase.class.getResource("/game_graph.txt").toURI())));

        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static void generateFlatTaxiGraph() {
        flatTaxiGraph = new UndirectedGraph();
        flatTaxiGraph.addNode(new Node<>(1));
        flatTaxiGraph.addNode(new Node<>(2));
        flatTaxiGraph.addNode(new Node<>(3));
        flatTaxiGraph.addNode(new Node<>(4));
        flatTaxiGraph.addNode(new Node<>(5));
        flatTaxiGraph.addNode(new Node<>(6));
        flatTaxiGraph.addEdge(new Edge<Integer,Transport>(new Node(1), new Node<>(2),Taxi));
        flatTaxiGraph.addEdge(new Edge<Integer,Transport>(new Node(2), new Node<>(3),Taxi));
        flatTaxiGraph.addEdge(new Edge<Integer,Transport>(new Node(3), new Node<>(4),Taxi));
        flatTaxiGraph.addEdge(new Edge<Integer,Transport>(new Node(4), new Node<>(5),Taxi));
        flatTaxiGraph.addEdge(new Edge<Integer,Transport>(new Node(5), new Node<>(6),Taxi));
    }


    /**
     * Returns the default graph used in the actual game
     *
     * @return the graph; never null
     */
    static Graph<Integer, Transport> defaultGraph() {
        return defaultGraph;
    }

    static Graph<Integer, Transport> flatTaxiGraph() {
        return flatTaxiGraph;
    }


}
