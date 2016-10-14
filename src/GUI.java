import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
 * @author Brent Willman
 *
 */
public class GUI extends Application {

	/**
	 * The width of the GUI.
	 */
	static final int GUI_WIDTH = 600;

	/**
	 * The height of the GUI.
	 */
	static final int GUI_HEIGHT = 250;
	
	/**
	 * The X layout for the speak button.
	 */
	static final int SPEAK_BUTTON_LAYOUT_X = 20;
	
	/**
	 * The X layout for the listen button.
	 */
	static final int LISTEN_BUTTON_LAYOUT_X = 320;

	/**
	 * AudioToText instance to transcribe audio.
	 */
	AudioToText audioToText;

	/**
	 * Indicates whether the button was clicked to stop the input stream.
	 */
	private boolean stopListenClick = false;

	/**
	 * Getter method for stopListenClick.
	 * 
	 * @return true if stream should be stopped
	 */
	public final boolean isStopListenClicked() {
		return stopListenClick;
	}

	/**
	 * Setter method for stopListenClicked.
	 * 
	 * @param shouldStop
	 *            whether or not to stop the input stream.
	 */
	public final void setStopListenClick(final boolean shouldStop) {
		this.stopListenClick = shouldStop;
	}

	/**
	 * The main method that launched the args.
	 * 
	 * @param args
	 *            the arguments passed
	 */
	public static void main(final String[] args) {
		launch(args);

	}

	@Override
	public final void start(final Stage primaryStage) {

		primaryStage.setTitle("WatsUp");

		Button watsonSpeakButton = new Button();

		Button watsonListenButton = new Button();

		watsonSpeakButton.setText("Make Watson Speak");

		watsonListenButton.setText("Begin Listening");

		watsonListenButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent event) {
				// TODO Auto-generated method stub
				if (stopListenClick) {
					// Click intended to stop listening.
					audioToText.endStream();
					watsonListenButton.setText("Begin Listening");
					stopListenClick = false;
				} else {
					// Click intended to start listening.
					audioToText = new AudioToText();
					ExecutorService executor = Executors
							.newSingleThreadExecutor();
					Future<String> result = executor.submit(audioToText);
					watsonListenButton.setText("Stop Listening");
					stopListenClick = true;

				}
			}
		});
		watsonSpeakButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(final ActionEvent event) {
				Runnable runner = new TextToAudio(
						"Hello! This is Watson, how may I assist you?");
				Thread thread = new Thread(runner);
				thread.start();
			}
		});

		watsonSpeakButton.setLayoutX(SPEAK_BUTTON_LAYOUT_X);
		watsonListenButton.setLayoutX(LISTEN_BUTTON_LAYOUT_X);
		StackPane root = new StackPane();
		root.getChildren().add(watsonSpeakButton);
		root.getChildren().add(watsonListenButton);
		primaryStage.setScene(new Scene(root, GUI_WIDTH, GUI_HEIGHT));
		primaryStage.show();
	}
}
