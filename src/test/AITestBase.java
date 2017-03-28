import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import uk.ac.bris.cs.gamekit.graph.Graph;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYardGraphReader;
import uk.ac.bris.cs.scotlandyard.model.Transport;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by adam on 28/03/2017.
 */
public abstract class AITestBase {
    private static Graph<Integer, Transport> defaultGraph;

    @BeforeClass
    public static void setUp() {
        //System.out.println(AITestBase.class.getResource("/game_graph.txt"));
        try {
            defaultGraph = ScotlandYardGraphReader.fromLines(Files.readAllLines(
                    Paths.get(AITestBase.class.getResource("/game_graph.txt").toURI())));

        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Returns the default graph used in the actual game
     *
     * @return the graph; never null
     */
    static Graph<Integer, Transport> defaultGraph() {
        return defaultGraph;
    }

}
