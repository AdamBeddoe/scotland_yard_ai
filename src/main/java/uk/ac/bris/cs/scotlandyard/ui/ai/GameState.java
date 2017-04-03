package uk.ac.bris.cs.scotlandyard.ui.ai;

import org.glassfish.grizzly.streams.Stream;
import sun.security.jca.GetInstance;
import uk.ac.bris.cs.gamekit.graph.Edge;
import uk.ac.bris.cs.gamekit.graph.Graph;
import uk.ac.bris.cs.gamekit.graph.Node;
import uk.ac.bris.cs.scotlandyard.model.*;

import java.util.*;
import java.util.stream.Collectors;

import static uk.ac.bris.cs.scotlandyard.model.Colour.Black;
import static uk.ac.bris.cs.scotlandyard.model.Ticket.Secret;

/**
 * Created by Adam on 30/03/2017.
 */
public class GameState implements MoveVisitor {
    private Graph graph;
    private Map<Colour,Integer> detectives = new HashMap<>();
    private int mrXLocation;

    public GameState(Graph graph, Map<Colour,Integer> detectives, int mrXLocation) {
        this.graph = graph;
        this.mrXLocation = mrXLocation;
        this.detectives = detectives;
    }

    public GameState(ScotlandYardView view, int location) {
        this.graph = view.getGraph();
        if (view.getCurrentPlayer().isMrX()) {
            this.mrXLocation = location;
            for (Colour colour : view.getPlayers()) {
                if (colour != Black) detectives.put(colour,view.getPlayerLocation(colour));
            }
        }

        else {

        }
    }

    public GameState(ScotlandYardView view, int location, Move move) {
        this.graph = view.getGraph();
        if (view.getCurrentPlayer().isMrX()) {
            this.mrXLocation = location;
            for (Colour colour : view.getPlayers()) {
                if (colour != Black) detectives.put(colour,view.getPlayerLocation(colour));
            }
        }

        else {

        }

        move.visit(this);
    }

    public GameState(GameState state, Move move) {
        this.graph = state.getGraph();
        for (Colour colour : state.getDetectives()) {
            if (colour != Black) detectives.put(colour,state.getDetectiveLocation(colour));
        }
        this.mrXLocation = state.getMrXLocation();

        move.visit(this);
    }

    public int getDetectiveLocation(Colour colour) {
        return this.detectives.get(colour);
    }

    public Set<Colour> getDetectives() {
        return this.detectives.keySet();
    }

    public int getMrXLocation() {
        return this.mrXLocation;
    }

    public Graph getGraph() {
        return this.graph;
    }

    public Set<Move> validMoves(Colour colour) {

        Collection<Edge<Integer,Transport>> edgesFrom;
        if (colour.isMrX()) edgesFrom = this.graph.getEdgesFrom(graph.getNode(this.mrXLocation));
        else edgesFrom = this.graph.getEdgesFrom(graph.getNode(detectives.get(colour)));

        Set<TicketMove> firstMoves = edgesFrom.parallelStream()
                .filter(edge -> !nodeOccupied(edge))
                //.filter(edge -> player.hasTickets(Ticket.fromTransport(edge.data())))
                .map(edge -> new TicketMove(colour, Ticket.fromTransport(edge.data()), edge.destination().value()))
                .collect(Collectors.toSet());

        Set<DoubleMove> doubleMoves = firstMoves.parallelStream()
                .map(move -> doubleMovesFrom(move))
                .flatMap(moveSet -> moveSet.stream())
                .collect(Collectors.toSet());

        Set<Move> validMoves = new HashSet<>();
        validMoves.addAll(firstMoves);
        validMoves.addAll(doubleMoves);

        return validMoves;
    }

    private Set<DoubleMove> doubleMovesFrom(TicketMove firstMove) {
        Collection<Edge<Integer,Transport>> edgesFrom = this.graph.getEdgesFrom(graph.getNode(firstMove.destination()));

        Set<DoubleMove> doubleMovesFrom = edgesFrom.parallelStream()
                .filter(edge -> !nodeOccupied(edge))
                .map(edge -> new TicketMove(firstMove.colour(), Ticket.fromTransport(edge.data()), edge.destination().value()))
                .map(secondMove -> new DoubleMove(firstMove.colour(), firstMove, secondMove))
                .collect(Collectors.toSet());

        return doubleMovesFrom;
    }

    // Returns true if the destination of an edge is occupied by a detective.
    private Boolean nodeOccupied(Edge edge) {
        for (Colour colour : detectives.keySet()) {
            if (edge.destination().value() == detectives.get(colour)) return true;
        }
        return false;
    }

    public void visit(TicketMove move) {
        this.mrXLocation = move.destination();
    }

    public void visit(DoubleMove move) {
        this.mrXLocation = move.finalDestination();
    }

    public void visit(PassMove move) {

    }

}
