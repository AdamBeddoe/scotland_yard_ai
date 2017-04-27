package uk.ac.bris.cs.scotlandyard.ui.ai;

import uk.ac.bris.cs.scotlandyard.model.Colour;
import uk.ac.bris.cs.scotlandyard.model.Move;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static uk.ac.bris.cs.scotlandyard.model.Colour.Black;

/**
 * Visits a GameTree and generate the child trees for all nodes that are not marked as dead.
 */
class NextRoundVisitor extends TreeVisitor {

    private Set<Move> moves;
    private int levels;
    private boolean atStart = true;

    /**
     * Makes a new visitor
     * @param moves The initial set of moves for the tree.
     * @param levels The depth of the tree required.
     */
    NextRoundVisitor(Set<Move> moves, int levels) {
        this.levels = levels;
        this.moves = moves;
    }

    /**
     * Adds child for each node with one move for MrX round.
     * Adds one child for each possible combination of detective moves for detective round.
     * @param tree The GameTree to visit.
     */
    public void visit(GameTree tree) {
        if (tree.getChildTrees().isEmpty()) {
            if (tree.isMrXRound()) addMrXChildren(tree);
            else addDetectivesChildren(tree);
        }
        for (GameTree childTree : tree.getChildTrees()) {
            if (levels > 0 && !childTree.isDeadNode()) {
                levels--;
                visit(childTree);
            }
        }
        this.atStart = false;
        this.levels++;
    }

    // Add child nodes for a MrX
    private void addMrXChildren(GameTree tree) {
        Set<Move> moves;
        if (!atStart) moves = tree.getState().validMoves(Black);
        else moves = this.moves;
        for (Move move : moves) {
            tree.addChild(new GameState(tree.getState(), move), move);
        }
    }

    // Add child nodes for all Detectives
    private void addDetectivesChildren(GameTree tree) {
        Set<Set<Move>> eachDetectiveMoves = new HashSet<>();
        for (Colour colour : tree.getState().getDetectives()) {
            eachDetectiveMoves.add(tree.getState().validMoves(colour));
        }

        Set<Set<Move>> combinedDetectiveMoves = combinations(eachDetectiveMoves);
        for (Set<Move> moveSet : combinedDetectiveMoves) {
            tree.addChild(new GameState(tree.getState(), moveSet), moveSet);
        }
    }

    // Generates a set of all possible combinations of each set in original.
    private Set<Set<Move>> combinations(Set<Set<Move>> original) {
        Set<Set<Move>> combined = new HashSet<>();
        for (Set<Move> set : original) {
            combined = sprinkle(combined,set);
        }
        return combined;
    }

    // Takes a set and makes all the combinations of the sprinkled and that set
    private Set<Set<Move>> sprinkle(Set<Set<Move>> sprinkled, Set<Move> moves) {
        Set<Set<Move>> sprinkledMoves = new HashSet<>();
        if (sprinkled.isEmpty()) {
            for (Move move : moves) {
                Set<Move> newSet = new HashSet<>();
                newSet.add(move);
                sprinkledMoves.add(newSet);
            }
        }
        for (Set<Move> set : sprinkled) {
            for (Move move : moves) {
                Set<Move> newSet = new HashSet<>();
                newSet.addAll(set);
                newSet.add(move);
                sprinkledMoves.add(newSet);
            }
        }

        return sprinkledMoves;
    }
}
