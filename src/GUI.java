
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * The user interface for the Watson app.
 * 
 * @author Josh Getter
 * @author Charles Billingsley
 * @author Brent
 *
 */
public class GUI extends Application {
	
	/**
	 * The width of the GUI.
	 */
	static final int GUI_WIDTH = 300;
	
	/**
	 * The height of the GUI.
	 */
	static final int GUI_HEIGHT = 250;

	/**
	 * The main method that launched the args.
	 * 
	 * @param args the arguments passed
	 */
	public static void main(final String[] args) {
		launch(args);

	}

	@Override
	public final void start(final Stage primaryStage) {
		primaryStage.setTitle("WatsUp");
		Button btn = new Button();
		btn.setText("Make Watson Speak");
		btn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(final ActionEvent event) {
				Runnable r = new TextToAudio(
						"Hello! This is Watson, how may I assist you?");
				Thread t = new Thread(r);
				t.start();
			}
		});

		StackPane root = new StackPane();
		root.getChildren().add(btn);
		primaryStage.setScene(new Scene(root, GUI_WIDTH, GUI_HEIGHT));
		primaryStage.show();
	}
}
