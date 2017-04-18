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
                if (childTree.isMrXRound()) { // know for all
                    NextRoundVisitor visitor = new NextRoundVisitor(childTree.getState().validMoves(Black), (levels - 1)); // valid moves only needed at the bottom
                    childTree.accept(visitor); // also don't like making new visitor objects for each tree, seems wasteful
                    // probably should be able to do it with visit() with some rejigging
                } else {

                    Set<Set<Move>> eachDetectiveMoves = new HashSet<>();
                    for (Colour colour : childTree.getState().getDetectives()) {
                        eachDetectiveMoves.add(childTree.getState().validMoves(colour));
                    }

                    Set<Set<Move>> combinedDetectiveMoves = combinations(eachDetectiveMoves);

                    for (Set moveSet : combinedDetectiveMoves) {
                        NextRoundVisitor detectivesMovesAdder = new NextRoundVisitor(moveSet, (levels - 1));
                        childTree.accept(detectivesMovesAdder);
                    }
                }
            }
        }
    }

    private Set<Set<Move>> combinations(Set<Set<Move>> original) {
        Set<Set<Move>> combined = new HashSet<>();
        for (Set<Move> set : original) {
            //System.out.println(set);
            //System.out.println("New Sprinkled: " + newSprinkle(combined,set));
            //System.out.println();
            combined = sprinkle(combined,set);
            //for (Set print : combined) {
            //    System.out.println("Set: " + print);
            //}
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
