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
 * Created by Adam on 31/03/2017.
 */
public class NextRoundVisitor extends TreeVisitor {

    private Set<Move> moves;
    private int levels;

    public NextRoundVisitor(Set<Move> moves, int levels) {
        this.levels = levels;
        this.moves = moves;
    }

    public void visit(GameTree tree) {
        if (tree.getChildTrees().isEmpty()) {
            for (Move move : this.moves) {
                tree.addChild(new GameState(tree.getState(), move), move);
            }
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

    private Set<Set<Move>> combinations(Set<Set<Move>> original) {
        Set<Set<Move>> combined = new HashSet<>();
        for (Set<Move> set : original) {
            combined = sprinkle(combined,set);
        }
        return combined;
    }

    private Set<Set<Move>> sprinkle(Set<Set<Move>> sprinkled, Set<Move> moves) {
        Set<Set<Move>> sprinkledMoves = new HashSet<>();
        if (sprinkled.isEmpty()) {
            for (Move move : moves) {
                Set<Move> newSet = new HashSet<>();
                newSet.add(move);
                sprinkledMoves.add(newSet);
            }
        }
        else {
            for (Set<Move> set : sprinkled) {
                for (Move move : moves) {
                    Set<Move> newSet = new HashSet<>();
                    newSet.addAll(set);
                    newSet.add(move);
                    sprinkledMoves.add(newSet);
                }
            }
        }

        return sprinkledMoves;
    }
}
