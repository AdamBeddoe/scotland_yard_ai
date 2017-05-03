package uk.ac.bris.cs.scotlandyard.ui.ai;

import uk.ac.bris.cs.gamekit.graph.Edge;
import uk.ac.bris.cs.gamekit.graph.Graph;
import uk.ac.bris.cs.gamekit.graph.Node;
import uk.ac.bris.cs.scotlandyard.model.Colour;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYardGraphReader;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYardView;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Stack;

import static uk.ac.bris.cs.scotlandyard.model.Colour.Black;

/**
 * Responsible for generic board calculations and scoring used by multiple parts of AI.
 * Pre-calculates all possible distances on initialisation.
 * Stores information on how often nodes have been visited by detectives, must be updated by a player.
 */

class Calculator {

    private final int graphDistances[][] = new int[200][200];
    private int nodeHistory[] = new int[200];
    private boolean sneakyMode;
    private GameStateStack stack = new GameStateStack(10);

    /**
     * Make a new Calculator, pre-calculates distances.
     */
    Calculator() {
        preCalculateDistances();
    }

    /**
     * Enables the use of how often nodes have been visited by detectives in scoring function.
     */
    void enableSneakyMode() {
        this.sneakyMode = true;
    }

    /**
     * Scores the board.
     * @param state A GameState to be scored.
     * @return The score.
     */
    int scoreBoard(GameState state) {
        if (this.stack.contains(state)) {
            return this.stack.getScore(state);
        }
        else {
            double total = 0;
            int mrXLocation = state.getMrXLocation();
            total = total + state.validMoves(Black).size();
            boolean captured = false;

            for (Colour colour : state.getDetectives()) {
                int distance = getGraphDistances(mrXLocation,state.getDetectiveLocation(colour));
                total = total + (Math.pow(distance, 2));
                if (distance == 0) captured = true;
            }
            if (sneakyMode) total = total + (nodeHistory[state.getMrXLocation()]*10);
            if (captured) return -1000;
            stack.push(state, (int) total);
            return (int) total;
        }
    }

    /**
     * Gets the distance from one node to another, already pre-calculated. Ignores tickets.
     * @param x First location.
     * @param y Second location.
     * @return The maximum distance in between the two locations.
     */
    public int getGraphDistances(int x, int y) {
        return this.graphDistances[x][y];
    }

    /**
     * Calculates the distance between two nodes on a given graph.
     * @param graph The graph to calculate the distance over.
     * @param src First location.
     * @param dest Second location.
     * @return The maximum distance in between the two locations on the given graph.
     */
    static int dijkstra(Graph graph, int src, int dest) {
        if(src>199 || src<1) throw new IllegalArgumentException("Source node not in graph");
        if(dest>199 || src<1) throw new IllegalArgumentException("Destination node not in graph");

        int[] distances = new int[graph.size()+1];
        Arrays.fill(distances, Integer.MAX_VALUE);
        boolean[] checked = new boolean[graph.size()+1];
        Arrays.fill(checked, false);

        distances[src] = 0;
        int current = src;

        while (current != dest) {
            checked[current] = true;
            Collection<Edge> edgesOut = graph.getEdgesFrom(new Node(current));

            for (Edge e : edgesOut) {
                if (distances[current] + 1 < distances[(int) e.destination().value()]){
                    distances[(int) e.destination().value()] = distances[current] + 1;
                }
            }
            current = findSmallest(distances, checked);
        }

        return distances[current];
    }

    // Finds the smallest distance in the path.
    private static int findSmallest(int[] distances, boolean[] inPath) {
        int current = Integer.MAX_VALUE;
        int node = -1;

        for (int n = 1; n<distances.length; n++) {

            if ((distances[n] < current) && !inPath[n]) {
                current = distances[n];
                node = n;
            }
        }
        return node;
    }


    // Pre-calculates the distances, uses the resource containing standard game graph.
    private void preCalculateDistances() {
        try {
            Graph defaultGraph = ScotlandYardGraphReader.fromLines(Files.readAllLines(
                    Paths.get(MyAI.class.getResource("/game_graph.txt").toURI())));
            for (int i = 1; i < 200; i++) {
                for (int j = 1; j < 200; j++) {
                    this.graphDistances[i][j] = dijkstra(defaultGraph,i,j);
                }
            }
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates the history of the nodes of the board.
     * Should only be called once per round.
     * @param view The ScotlandYardView of the round.
     */
    void updateNodeHistory(ScotlandYardView view) {
        for (Colour player : view.getPlayers()) {
            this.nodeHistory[view.getPlayerLocation(player)]++;
        }
    }

    public void clearStack() {
        this.stack.clear();
    }
}
