package uk.ac.bris.cs.scotlandyard.ui.ai;

import uk.ac.bris.cs.gamekit.graph.Edge;
import uk.ac.bris.cs.gamekit.graph.Graph;
import uk.ac.bris.cs.scotlandyard.model.*;

import java.util.*;
import java.util.stream.Collectors;

import static uk.ac.bris.cs.scotlandyard.model.Colour.Black;
import static uk.ac.bris.cs.scotlandyard.model.Ticket.Double;
import static uk.ac.bris.cs.scotlandyard.model.Ticket.Secret;
import static uk.ac.bris.cs.scotlandyard.model.Ticket.Taxi;

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

    // Creates a set of valid moves for a detective.
    public Set<Move> validMoves(Colour colour) {

        Collection<Edge<Integer,Transport>> edgesFrom;

        if (colour.isMrX()) edgesFrom = this.graph.getEdgesFrom(graph.getNode(mrXLocation));
        else edgesFrom = this.graph.getEdgesFrom(graph.getNode(detectives.get(colour)));

        Set<Move> firstMoves = edgesFrom.stream() // Should be ticket move
                .filter(edge -> !nodeOccupied(edge))
                .map(edge -> new TicketMove(colour, Ticket.fromTransport(edge.data()), edge.destination().value()))
                //.flatMap(ticketMove -> addSecretMoves(ticketMove, player).stream())
                //.filter(move -> player.hasTickets(move.ticket()))
                .collect(Collectors.toSet());


        /*
        Set<DoubleMove> doubleMoves = firstMoves.stream()
                .flatMap(move -> doubleMovesFrom(move, player).stream())
                .filter(doubleMove -> canUseDouble(doubleMove, player))
                .collect(Collectors.toSet());
        */
        //Set<Move> validMoves = new HashSet<>();
        //validMoves.addAll(firstMoves);
        //validMoves.addAll(doubleMoves);

        return firstMoves;
    }

    // Returns all double moves from the firstMove
    private Set<DoubleMove> doubleMovesFrom(TicketMove firstMove, ScotlandYardPlayer player) {
        Collection<Edge<Integer,Transport>> edgesFrom = this.graph.getEdgesFrom(graph.getNode(firstMove.destination()));

        Set<DoubleMove> doubleMovesFrom = edgesFrom.stream()
                .filter(edge -> !nodeOccupied(edge))
                .map(edge -> new TicketMove(firstMove.colour(), Ticket.fromTransport(edge.data()), edge.destination().value()))
                .flatMap(ticketMove -> addSecretMoves(ticketMove, player).stream())
                .map(secondMove -> new DoubleMove(firstMove.colour(), firstMove, secondMove))
                .collect(Collectors.toSet());

        return doubleMovesFrom;
    }

    // Returns a set containing the original move and the corresponding secret move if player has a secret ticket
    private Set<TicketMove> addSecretMoves(TicketMove originalMove, ScotlandYardPlayer player) {
        Set<TicketMove> newMoves = new HashSet<>();
        newMoves.add(originalMove);
        if (player.hasTickets(Secret)) newMoves.add(new TicketMove(Black, Secret, originalMove.destination()));
        return newMoves;
    }

    // Returns true if the player is able to use the double ticket
    private boolean canUseDouble(DoubleMove doubleMove, ScotlandYardPlayer player) {
        boolean sameTicketValid = true;
        if (doubleMove.hasSameTicket()) {
            sameTicketValid = player.hasTickets(doubleMove.firstMove().ticket(),2);
        }
        return (player.hasTickets(doubleMove.firstMove().ticket())
                && player.hasTickets(doubleMove.secondMove().ticket())
                && player.hasTickets(Double)
                && sameTicketValid);
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
