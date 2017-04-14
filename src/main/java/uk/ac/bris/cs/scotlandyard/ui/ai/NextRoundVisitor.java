package uk.ac.bris.cs.scotlandyard.ui.ai;

import uk.ac.bris.cs.scotlandyard.model.Colour;
import uk.ac.bris.cs.scotlandyard.model.Move;

import java.util.*;
import java.util.function.BinaryOperator;
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
                    childTree.accept(visitor);
                } else {

                    // new way (doesn't work)
                    /*
                    Set<Set<Move>> eachDetectiveMoves = new HashSet<>();
                    for (Colour colour : childTree.getState().getDetectives()) {
                        eachDetectiveMoves.add(childTree.getState().validMoves(colour));
                    }

                    Set<Set<Move>> empty = new HashSet<>();
                    Set<Set<Move>> detectiveMovesSets = eachDetectiveMoves.stream()
                            .reduce(empty, (set1,set2) -> f(set1,set2));
                    */


                    // Old way (wrong)
                    Set<Move> detectiveMoves = new HashSet<>();
                    for (Move move : this.moves) {
                        tree.addChild(new GameState(tree.getState(), move), move);
                    }
                    NextRoundVisitor detectivesMovesAdder = new NextRoundVisitor(detectiveMoves, (levels - 1));
                    childTree.accept(detectivesMovesAdder);

                }
            }
        }
    }

    private Set<Set<Move>> f(Set<Move> toSprinkle, Set<Set<Move>> current) {
        return current.stream().flatMap(set -> sprinkle(toSprinkle,set).stream()).collect(Collectors.toSet());
    }

    private Set<Set<Move>> sprinkle(Set<Move> sprinkled, Set<Move> moves) {
        Set<Set<Move>> sprinkledMoves = new HashSet<>();
        for (Move move : sprinkled) {
            Set<Move> newSet = new HashSet<>();
            newSet.addAll(moves);
            newSet.add(move);
            sprinkledMoves.add(newSet);
        }
        return sprinkledMoves;
    }
}
