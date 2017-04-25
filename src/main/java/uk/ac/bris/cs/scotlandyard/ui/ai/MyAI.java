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


// TODO name the AI
@ManagedAI(value = "Name me!", visualiserType = ManagedAI.VisualiserType.WINDOWED)
public class MyAI implements PlayerFactory {

	private Calculator calculator;
	private Visualiser visualiser;
	private MrX mrX;

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

	public void ready(Visualiser visualiser, ResourceProvider provider) {
		this.calculator = new Calculator();

		this.mrX = new MrX(this.calculator);

		this.visualiser = visualiser;
		GameMonitorController controller = new GameMonitorController();
		GameMonitorView view = new GameMonitorView(visualiser, controller);
		GameMonitorModel model = new GameMonitorModel(view);
		this.mrX.builder.registerObserver(model);
	}
}

