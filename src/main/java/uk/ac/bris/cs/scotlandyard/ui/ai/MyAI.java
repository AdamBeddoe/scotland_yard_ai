package uk.ac.bris.cs.scotlandyard.ui.ai;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;

import javafx.geometry.*;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import uk.ac.bris.cs.gamekit.graph.Edge;
import uk.ac.bris.cs.gamekit.graph.Graph;
import uk.ac.bris.cs.gamekit.graph.Node;
import uk.ac.bris.cs.scotlandyard.ai.ManagedAI;
import uk.ac.bris.cs.scotlandyard.ai.PlayerFactory;
import uk.ac.bris.cs.scotlandyard.ai.ResourceProvider;
import uk.ac.bris.cs.scotlandyard.ai.Visualiser;
import uk.ac.bris.cs.scotlandyard.model.*;
import uk.ac.bris.cs.scotlandyard.ui.gamemonitor.GameMonitorModel;
import uk.ac.bris.cs.scotlandyard.ui.gamemonitor.GameMonitorView;

import static java.util.Arrays.fill;
import static uk.ac.bris.cs.scotlandyard.model.Colour.Black;

// TODO name the AI
@ManagedAI(value = "Name me!", visualiserType = ManagedAI.VisualiserType.WINDOWED)
public class MyAI implements PlayerFactory {

	private final int graphDistances[][] = new int[200][200];
	Visualiser visualiser;
	MrX mrx;

	//TODO take out of constructor
	public MyAI() {
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

	@Override
	public Player createPlayer(Colour colour) {

		if (colour.isMrX()) {
			return this.mrx;
		}
		else {
			Stage stage = (Stage) visualiser.surface().getScene().getWindow();
			stage.close();

			return new Detective();
		}
	}

	public int scoreBoard(GameState state) {
		double total = 0;
		int mrXLocation = state.getMrXLocation();
		total = total + state.validMoves(Black).size();
		boolean captured = false;

		for (Colour colour : state.getDetectives()) {
			int distance = getGraphDistances(mrXLocation,state.getDetectiveLocation(colour));
			total = total + (Math.pow(distance, 2));
			if (distance == 0) captured = true;
		}

		if (captured) return -1000;
		else return (int) total;
	}

	public int getGraphDistances(int x, int y) {
		return this.graphDistances[x][y];
	}

	public static int dijkstra(Graph graph, int src, int dest) {
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

	public void ready(Visualiser visualiser, ResourceProvider provider) {
		this.mrx = new MrX(this);


		this.visualiser = visualiser;
		GameMonitorView view = new GameMonitorView(visualiser);
		GameMonitorModel model = new GameMonitorModel(view);
		this.mrx.builder.registerObserver(model);
	}


}

