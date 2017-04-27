package uk.ac.bris.cs.scotlandyard.ui.ai;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Runs a suit of tests for the AI.
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({
        dijkstraTests.class,
        scoreBoardTests.class,
        NextRoundVisitorTests.class
})
public class AITest {
}
