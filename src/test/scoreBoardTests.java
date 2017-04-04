import org.junit.Test;
import uk.ac.bris.cs.gamekit.graph.Graph;
import uk.ac.bris.cs.scotlandyard.model.Colour;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYardGame;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYardModel;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYardView;
import uk.ac.bris.cs.scotlandyard.ui.ai.GameState;
import uk.ac.bris.cs.scotlandyard.ui.ai.MyAI;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static uk.ac.bris.cs.scotlandyard.model.Colour.Blue;

/**
 * Created by Adam on 30/03/2017.
 */
public class scoreBoardTests extends AITestBase{

    @Test
    public void scoreValidReturnType() {
        Graph graph = defaultGraph();

        MyAI testAI = new MyAI();


        Map<Colour,Integer> detectives = new HashMap<>();
        detectives.put(Blue, 20);

        GameState testState = new GameState(graph, detectives, 10);
        assertThat(testAI.scoreBoard(testState), instanceOf (Integer.class));


    }

    @Test
    public void testCapturedScore() {
        Graph graph = defaultGraph();

        MyAI testAI = new MyAI();


        Map<Colour,Integer> detectives = new HashMap<>();
        detectives.put(Blue, 20);

        GameState testState = new GameState(graph, detectives, 20);
        assertEquals(-1000, testAI.scoreBoard(testState));
    }
}
