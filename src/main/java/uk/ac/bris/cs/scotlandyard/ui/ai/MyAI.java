package uk.ac.bris.cs.scotlandyard.ui.ai;

import javafx.stage.Stage;
import uk.ac.bris.cs.scotlandyard.ai.ManagedAI;
import uk.ac.bris.cs.scotlandyard.ai.PlayerFactory;
import uk.ac.bris.cs.scotlandyard.ai.ResourceProvider;
import uk.ac.bris.cs.scotlandyard.ai.Visualiser;
import uk.ac.bris.cs.scotlandyard.model.*;
import uk.ac.bris.cs.scotlandyard.ui.gamemonitor.GameMonitorController;
import uk.ac.bris.cs.scotlandyard.ui.gamemonitor.GameMonitorModel;
import uk.ac.bris.cs.scotlandyard.ui.gamemonitor.GameMonitorView;

/**
 * The main AI class, responsible for initialising the AIPlayer and starting the visualiser.
 */
@ManagedAI(value = "The Tree Surgeon (World)", visualiserType = ManagedAI.VisualiserType.WINDOWED)
public class MyAI implements PlayerFactory {

	private Calculator calculator;
	private Visualiser visualiser;
	private MrX mrX;

	/**
	 * Makes a new AIPlayer based on the type of the player.
	 * If Detective, close the visualiser window.
	 * @param colour The colour of the player.
	 * @return A Player object.
	 */
	@Override
	public Player createPlayer(Colour colour) {
		if (colour.isMrX()) {
			return this.mrX;
		}
		else {
			Stage stage = (Stage) visualiser.surface().getScene().getWindow();
			stage.close();

			return new Detective(this.calculator);
		}
	}

	/**
	 * Makes a new Calculator object.
	 * Sets up Model-View-Controller objects for visualiser.
	 * Registers builderObserver for MrX for the GameMonitor model.
	 * @param visualiser The visualiser.
	 * @param provider The resource provider.
	 */
	public void ready(Visualiser visualiser, ResourceProvider provider) {
		this.calculator = new Calculator();


		this.visualiser = visualiser;
		GameMonitorController controller = new GameMonitorController();
		GameMonitorView view = new GameMonitorView(visualiser, controller);
		GameMonitorModel model = new GameMonitorModel(view);

		this.mrX = new MrX(this.calculator);
		this.mrX.getBuilder().registerObserver(model);
	}
}

