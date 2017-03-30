package uk.ac.bris.cs.scotlandyard.ui.ai;

import java.util.*;
import java.util.function.Consumer;

import uk.ac.bris.cs.gamekit.graph.Edge;
import uk.ac.bris.cs.gamekit.graph.Graph;
import uk.ac.bris.cs.gamekit.graph.Node;
import uk.ac.bris.cs.scotlandyard.ai.ManagedAI;
import uk.ac.bris.cs.scotlandyard.ai.PlayerFactory;
import uk.ac.bris.cs.scotlandyard.model.*;

import static java.util.Arrays.fill;

// TODO name the AI
@ManagedAI("Name me!")
public class MyAI implements PlayerFactory {

	// TODO create a new player here
	@Override
	public Player createPlayer(Colour colour) {
		return new MyPlayer();
	}

	// TODO A sample player that selects a random move
	private static class MyPlayer implements Player {

		private final Random random = new Random();

		@Override
		public void makeMove(ScotlandYardView view, int location, Set<Move> moves,
				Consumer<Move> callback) {
			// TODO do something interesting here; find the best move
			// picks a random move
			GameState state = new GameState(view, location);
            System.out.println("Move scores: " + scoreBoard(state));
			callback.accept(new ArrayList<>(moves).get(random.nextInt(moves.size())));

			//Graph graph = view.getGraph();
		}

	}


	public static int scoreBoard(GameState state) {
		int total = 0;
		Graph graph = state.getGraph();
		int mrXLocation = state.getMrXLocation();
		for (Colour colour : state.getDetectives()) {
			System.out.println("Called dijkstra on " + mrXLocation + " " + state.getDetectiveLocation(colour));
			total = +(dijkstra(graph, mrXLocation, state.getDetectiveLocation(colour))) ^ 2;
				if (dijkstra(graph, mrXLocation, state.getDetectiveLocation(colour)) == 0) total = -1000;
		}
		return total;
	}

	public static int dijkstra(Graph graph, int src, int dest) {
		int[] distances = new int[graph.size()+1];
		Arrays.fill(distances, Integer.MAX_VALUE);
		boolean[] checked = new boolean[graph.size()+1];
		Arrays.fill(checked, false);

		distances[src] = 0;
		int current = src;
		int[] cameFrom = new int[graph.size()+1];
		Arrays.fill(cameFrom, -1);


		while (current != dest) {
			checked[current] = true;
			Collection<Edge> edgesOut = graph.getEdgesFrom(new Node(current));

			for (Edge e : edgesOut) {
				cameFrom[(int) e.destination().value()] = current;
				if (distances[current] + 1 < distances[(int) e.destination().value()]){
					distances[(int) e.destination().value()] = distances[current] + 1;
				}
			}
			current = findSmallest(distances, checked);
		}

		return distances[current];
	}

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
}

