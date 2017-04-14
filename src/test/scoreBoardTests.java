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
import static uk.ac.bris.cs.scotlandyard.model.Colour.Black;
import static uk.ac.bris.cs.scotlandyard.model.Colour.Blue;
import static uk.ac.bris.cs.scotlandyard.model.Colour.Red;

/**
 * Created by Adam on 30/03/2017.
 */
public class scoreBoardTests extends AITestBase{
    MyAI testAI = new MyAI();

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
        detectives.put(Blue, 30);

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

        MyAI testAI = new MyAI();

        Map<Colour,Integer> detectives = new HashMap<>();
        detectives.put(Blue, 4);

        // Score should be something?
        GameState testState1 = new GameState(graph, detectives, 3);
        assertEquals(6, testAI.scoreBoard(testState1));
        // Score should be -1000 as MrX is captured
        GameState testState2 = new GameState(graph, detectives, 4);
        assertEquals(-1000, testAI.scoreBoard(testState2));
        // Score should be something?
        GameState testState3 = new GameState(graph, detectives, 4);
        assertEquals(6, testAI.scoreBoard(testState3));
    }
}
