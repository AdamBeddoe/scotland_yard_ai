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
    void visit(GameTree tree) {
        if (tree.getChildTrees().isEmpty() && tree.isMrXRound()) {
            for (Move move : this.moves) {
                tree.addChild(new GameState(tree.getState(), move), move);
            }
        }

        if (!tree.isMrXRound()) {
            tree.addChild(new GameState(tree.getState(), this.moves), this.moves);
        }

        for (GameTree childTree : tree.getChildTrees()) {
            if (levels > 0 && !childTree.isDeadNode()) {
                if (childTree.isMrXRound()) {
                    this.moves = childTree.getState().validMoves(Black);
                    this.levels--;
                    visit(childTree);
                } else {

                    Set<Set<Move>> eachDetectiveMoves = new HashSet<>();
                    for (Colour colour : childTree.getState().getDetectives()) {
                        eachDetectiveMoves.add(childTree.getState().validMoves(colour));
                    }

                    Set<Set<Move>> combinedDetectiveMoves = combinations(eachDetectiveMoves);
                    for (Set moveSet : combinedDetectiveMoves) {
                        this.moves = moveSet;
                        this.levels--;
                        visit(childTree);
                    }
                }
            }
        }
        this.levels++;
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
