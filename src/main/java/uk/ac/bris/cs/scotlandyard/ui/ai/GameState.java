package uk.ac.bris.cs.scotlandyard.ui.ai;

import uk.ac.bris.cs.gamekit.graph.Edge;
import uk.ac.bris.cs.gamekit.graph.Graph;
import uk.ac.bris.cs.gamekit.graph.Node;
import uk.ac.bris.cs.scotlandyard.model.*;

import java.util.*;

import static uk.ac.bris.cs.scotlandyard.model.Colour.Black;
import static uk.ac.bris.cs.scotlandyard.model.Ticket.Double;
import static uk.ac.bris.cs.scotlandyard.model.Ticket.Secret;

/**
 * Created by Adam on 30/03/2017.
 */
public class GameState implements MoveVisitor {
    private Graph graph;
    private Map<Colour,Integer> detectives = new HashMap<>();
    private MrX mrX = new MrX();

    public GameState(Graph graph, Map<Colour,Integer> detectives, int mrXLocation) {
        this.graph = graph;
        this.mrX.setLocation(mrXLocation);
        this.detectives = detectives;
    }

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

    public GameState(ScotlandYardView view, int location, Move move) {
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

        move.visit(this);
    }

    public GameState(GameState state, Move move) {
        this.graph = state.getGraph();
        for (Colour colour : state.getDetectives()) {
            if (colour != Black) detectives.put(colour,state.getDetectiveLocation(colour));
        }
        this.mrX.setLocation(state.getMrXLocation());
        this.mrX.setLastKnownLocation(state.getMrXLocation());

        move.visit(this);
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

    public Set<Move> validMoves(Colour colour) {
        Set<Move> validMoves = new HashSet<>();
        int loc;

        if (colour.isMrX()) loc = this.mrX.getLocation();
        else {
            loc = this.detectives.get(colour);
        }


        Node node = this.graph.getNode(loc);
        Collection<Edge> edges = this.graph.getEdgesFrom(node);

        for (Edge edge : edges) {
            Transport t = (Transport) edge.data();
            Ticket ticket = Ticket.fromTransport(t);
            if (/*player.hasTickets(ticket) &&*/ !nodeOccupied(edge)) {
                Move move = new TicketMove(colour, ticket, (Integer) edge.destination().value());
                validMoves.add(move);
            }
        }

        if (colour.isMrX()) {
            Set<Move> secondMoves = new HashSet<>();
            //Node node = this.graph.getNode(loc);

            Collection<Edge> firstEdges = this.graph.getEdgesFrom(node);
            for (Edge firstEdge : firstEdges) {
                if (/*this.mrX.hasTickets(Secret) && */!nodeOccupied(firstEdge)) {
                    Move regularMove = new TicketMove(Black, Secret, (Integer) firstEdge.destination().value());
                    validMoves.add(regularMove);
                }

                //if (this.mrX.hasTickets(Double) && this.roundNum <= (rounds.size()-2)) {
                for (Move firstMove : validMoves) {
                    for (Move secondMove : validMovesFrom(firstMove)) {
                        Move doubleMove = new DoubleMove(Black, (TicketMove) firstMove, (TicketMove) secondMove);
                        secondMoves.add(doubleMove);
                    }
                }
                //}
            }
            validMoves.addAll(secondMoves);
        }
        return validMoves;
    }

    private Set<Move> validMovesFrom(Move move) {
        Set<Move> validMoves = new HashSet<>();
        if (move instanceof TicketMove) {
            Node node = this.graph.getNode(((TicketMove) move).destination());
            Collection<Edge> edges = this.graph.getEdgesFrom(node);
            int numTickets;

            for (Edge edge : edges) {
                Transport t = (Transport) edge.data();
                Ticket ticket = Ticket.fromTransport(t);
                if (((TicketMove) move).ticket().equals(ticket)) numTickets = 2;
                else numTickets = 1;

                if (/*currentPlayer.hasTickets(ticket, numTickets)  &&*/ !nodeOccupied(edge)) {
                    Move newMove = new TicketMove(Black, ticket, (Integer) edge.destination().value());
                    //TODO Fix this usage of black ^^

                    validMoves.add(newMove);
                    //if (currentPlayer.hasTickets(Secret)) {
                        Move secretMove = new TicketMove(Black, Secret, (Integer) edge.destination().value());
                        validMoves.add(secretMove);
                    //}
                }
            }
        }
        return validMoves;
    }

    // Returns true if the destination of an edge is occupied by a detective.
    private Boolean nodeOccupied(Edge edge) {
        for (Colour colour : detectives.keySet()) {
            if (edge.destination().value() == detectives.get(colour)) return true;
        }
        return false;
    }
    public void visit(TicketMove move) {
        this.mrX.setLocation(move.destination());
    }

    public void visit(DoubleMove move) {
        this.mrX.setLocation(move.finalDestination());
    }

    public void visit(PassMove move) {

    }

}
