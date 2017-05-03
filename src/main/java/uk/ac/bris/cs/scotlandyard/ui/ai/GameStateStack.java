package uk.ac.bris.cs.scotlandyard.ui.ai;


/**
 * Created by Adam on 03/05/2017.
 */
public class GameStateStack {
    private GameState[] stack;
    private int[] scores;
    private int size;
    private int top;

    public GameStateStack(int size) {
        this.top = -1;
        this.size = size;
        stack = new GameState[size+1];
        scores = new int[size+1];
    }

    public void push(GameState state, int score) {
        if (top >= size-1) {
            shuffleDown();
        }
        top++;
        stack[top] = state;
        scores[top] = score;
    }

    private void shuffleDown(){
        for (int i = 0; i< size-1; i++) {
            stack[i] = stack[i+1];
            scores[i] = scores[i+1];
        }
        top--;
    }

    public int getScore(GameState state) {
        for (int i = 0; i<size; i++) {
            if (state.equals(stack[i])) return scores[i];
        }
        return -1;
    }

    public boolean contains(GameState state) {
        for (int i = 0; i<size; i++) {
            if (state.equals(stack[i])) return true;
        }
        return false;
    }

    public void clear() {
        stack = new GameState[size];
    }
}
