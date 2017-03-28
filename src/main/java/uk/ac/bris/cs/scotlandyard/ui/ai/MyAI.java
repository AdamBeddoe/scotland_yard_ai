package uk.ac.bris.cs.scotlandyard.ui.ai;

import java.util.*;
import java.util.function.Consumer;

import uk.ac.bris.cs.gamekit.graph.Edge;
import uk.ac.bris.cs.gamekit.graph.Graph;
import uk.ac.bris.cs.gamekit.graph.Node;
import uk.ac.bris.cs.scotlandyard.ai.ManagedAI;
import uk.ac.bris.cs.scotlandyard.ai.PlayerFactory;
import uk.ac.bris.cs.scotlandyard.model.Colour;
import uk.ac.bris.cs.scotlandyard.model.Move;
import uk.ac.bris.cs.scotlandyard.model.Player;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYardView;

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
			callback.accept(new ArrayList<>(moves).get(random.nextInt(moves.size())));

			//Graph graph = view.getGraph();
		}

	}

	public int dijkstra(Graph graph, int src, int dest) {
		int[] distances = new int[graph.size()+1];
		Arrays.fill(distances, Integer.MAX_VALUE);
		boolean[] inPath = new boolean[graph.size()+1];
		Arrays.fill(inPath, false);

		distances[src] = 0;
		int current = src;


		while (current != dest) {
			inPath[current] = true;

			Collection<Edge> edgesOut = graph.getEdgesFrom(new Node(current));

			for (Edge e : edgesOut) {
				if (distances[current] + 1 < distances[(int) e.destination().value()]){
					distances[(int) e.destination().value()] = distances[current] + 1;
				}
			}

			current = findSmallest(distances, inPath);
		}

		return distances[current];
	}

	private int findSmallest(int[] distances, boolean[] inPath) {
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
