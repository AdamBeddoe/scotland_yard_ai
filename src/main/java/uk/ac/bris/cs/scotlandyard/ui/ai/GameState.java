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
 * Stores information about a state of the game at any point.
 */

class GameState implements MoveVisitor {
    private Graph graph;
    private Map<Colour,Integer> detectives = new HashMap<>();
    private int mrXLocation;

    /**
     * Create a new GameState.
     * @param graph A graph of the board.
     * @param detectives A map of detectives and their locations.
     * @param mrXLocation The location of MrX.
     */
    GameState(Graph graph, Map<Colour,Integer> detectives, int mrXLocation) {
        this.graph = graph;
        this.mrXLocation = mrXLocation;
        this.detectives = detectives;
    }

    /**
     * Create a new GameState.
     * @param view The current ScotlandYardView.
     * @param location The location of MrX or current detective.
     */
    GameState(ScotlandYardView view, int location) {
        this.graph = view.getGraph();
        if (view.getCurrentPlayer().isMrX()) {
            this.mrXLocation = location;
            for (Colour colour : view.getPlayers()) {
                if (colour != Black) detectives.put(colour,view.getPlayerLocation(colour));
            }
        }

        else {
            for (Colour colour : view.getPlayers()) {
                if (colour != Black) detectives.put(colour,view.getPlayerLocation(colour));
            }
            this.mrXLocation = view.getPlayerLocation(Black);
        }
    }

    /**
     * Create a new GameState based on a previous GameState and a move.
     * @param state Previous GameState.
     * @param move Move made.
     */
    GameState(GameState state, Move move) {
        this.graph = state.getGraph();
        for (Colour colour : state.getDetectives()) {
            if (colour != Black) detectives.put(colour,state.getDetectiveLocation(colour));
        }
        this.mrXLocation = state.getMrXLocation();

        move.visit(this);
    }

    /**
     * Create a new GameState based on a previous GameState and set of moves.
     * @param state Previous GameState.
     * @param moves Set of moves made.
     */
    GameState(GameState state, Set<Move> moves) {
        this.graph = state.getGraph();
        this.mrXLocation = state.getMrXLocation();
        for (Colour colour : state.getDetectives()) {
            if (colour != Black) detectives.put(colour,state.getDetectiveLocation(colour));
        }

        for(Move move : moves) {
            move.visit(this);
        }
    }

    /**
     * Get the location of a detective.
     * @param colour The colour of the detective.
     * @return The location of the detective.
     */
    int getDetectiveLocation(Colour colour) {
        return this.detectives.get(colour);
    }


    /**
     * Returns a set of detectives.
     * @return The set of detective colours in the state.
     */
    Set<Colour> getDetectives() {
        return this.detectives.keySet();
    }


    /**
     * Returns MrX's location.
     * @return MrX's location.
     */
    int getMrXLocation() {
        return this.mrXLocation;
    }


    /**
     * Returns the graph used in the state.
     * @return The graph used in the state.
     */
    Graph getGraph() {
        return this.graph;
    }

    /**
     * Returns a set of valid moves for any player. Ignores tickets.
     * @param colour The colour of the player.
     * @return A set of available moves.
     */
    Set<Move> validMoves(Colour colour) {

        Collection<Edge<Integer,Transport>> edgesFrom;
        Set<Move> validMoves = new HashSet<>();

        if (colour.isMrX()) edgesFrom = this.graph.getEdgesFrom(graph.getNode(mrXLocation));
        else edgesFrom = this.graph.getEdgesFrom(graph.getNode(detectives.get(colour)));

        Set<TicketMove> firstMoves = edgesFrom.stream()
                .filter(edge -> !nodeOccupied(edge))
                .map(edge -> new TicketMove(colour, Ticket.fromTransport(edge.data()), edge.destination().value()))
                .collect(Collectors.toSet());

        validMoves.addAll(firstMoves);

        if (colour.isMrX()) {
            Set<DoubleMove> doubleMoves = firstMoves.stream()
                    .flatMap(move -> doubleMovesFrom(move).stream())
                    .collect(Collectors.toSet());

            validMoves.addAll(doubleMoves);
        }

        return validMoves;
    }

    // Returns all double moves from the firstMove
    private Set<DoubleMove> doubleMovesFrom(TicketMove firstMove) {
        Collection<Edge<Integer,Transport>> edgesFrom = this.graph.getEdgesFrom(graph.getNode(firstMove.destination()));

        Set<DoubleMove> doubleMovesFrom = edgesFrom.stream()
                .filter(edge -> !nodeOccupied(edge))
                .map(edge -> new TicketMove(firstMove.colour(), Ticket.fromTransport(edge.data()), edge.destination().value()))
                .map(secondMove -> new DoubleMove(firstMove.colour(), firstMove, secondMove))
                .collect(Collectors.toSet());

        return doubleMovesFrom;
    }

    // Returns true if the destination of an edge is occupied by a detective.
    private boolean nodeOccupied(Edge edge) {
        return (detectives.containsValue(edge.destination().value()));
    }

    // Updates the location of the detective/MrX moved.
    public void visit(TicketMove move) {
        if (move.colour().isMrX()) this.mrXLocation = move.destination();
        else {
            this.detectives.replace(move.colour(), move.destination());
        }
    }

    // Updates the location of MrX.
    public void visit(DoubleMove move) {
        this.mrXLocation = move.finalDestination();
    }

    // Required as part of move visitor.
    public void visit(PassMove move) {}

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GameState)) return false;
        if (this.mrXLocation != ((GameState) obj).getMrXLocation()) return false;
        for (Colour colour:detectives.keySet()) {
            if (detectives.get(colour) != ((GameState) obj).getDetectiveLocation(colour)) return false;
        }
        return true;
    }

}
