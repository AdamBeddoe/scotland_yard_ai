package uk.ac.bris.cs.scotlandyard.ui.ai;

import uk.ac.bris.cs.gamekit.graph.Graph;
import uk.ac.bris.cs.scotlandyard.model.Colour;
import uk.ac.bris.cs.scotlandyard.model.Move;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYardView;

import java.util.*;

import static uk.ac.bris.cs.scotlandyard.model.Colour.Black;

/**
 * Created by Adam on 30/03/2017.
 */
public class GameState {
    private Graph graph;
    private Map<Colour,Integer> detectives = new HashMap<>();
    private MrX mrX = new MrX();

    public GameState(ScotlandYardView view, int location) {
        this.graph = view.getGraph();
        if (view.getCurrentPlayer().isMrX()) {
            this.mrX.setLocation(location);
            for (Colour colour : view.getPlayers()) {
                if (colour != Black) detectives.put(colour,view.getPlayerLocation(colour));
            }
            this.mrX.setLastKnownLocation(view.getPlayerLocation(Black));
        }

        else {

        }
    }

    public List<Move> validMoves(Colour colour) {
        return new ArrayList<>();
    }

    public int getDetectiveLocation(Colour colour) {
        return this.detectives.get(colour);
    }

    public Set<Colour> getDetectives() {
        return this.detectives.keySet();
    }

    public int getMrXLocation() {
        return this.mrX.getLocation();
    }

    public Graph getGraph() {
        return this.graph;
    }
}
