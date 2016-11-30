package watsup;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.Callable;

import com.ibm.watson.developer_cloud.http.HttpMediaType;
import com.ibm.watson.developer_cloud.speech_to_text.v1.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.Transcript;
import com.ibm.watson.developer_cloud.speech_to_text.v1.websocket.BaseRecognizeDelegate;

/**
 * Class that takes the inputed audio and sends it off to Watson to be changed
 * to text.
 * 
 * @author Josh Getter
 * @author Charles Billingsley
 * @author Brent Willman
 *
 */
public class AudioToText implements Callable<String> {

	/**
	 * The string that was transcribed by Watson.
	 */
	private String finalTranscription = "";

	/**
	 * The audio as it's captured.
	 */
	private CaptureAudio capture = new CaptureAudio();

	@Override
	public final String call() throws Exception {
		SpeechToText service = new SpeechToText();
		service.setUsernameAndPassword("95c52d86-c897-4870-99bc-e1bcaa6b25d5",
				"dA2xxyWJmgHy");
		RecognizeOptions options = new RecognizeOptions()
				.contentType(HttpMediaType.AUDIO_RAW + "; rate=16000")
				.continuous(true).interimResults(false);

		BaseRecognizeDelegate delegate = new BaseRecognizeDelegate() {
			@Override
			public void onMessage(final SpeechResults speechResults) {
				String transcribedPhrase = "";
				// String currentPhrase = "";
				List<Transcript> results = speechResults.getResults();
				for (Transcript t : results) {
					transcribedPhrase = transcribedPhrase.concat(t
							.getAlternatives().get(0).getTranscript());
				}
				System.out.println("Results are:" + transcribedPhrase);
				TextToKeywords textAnalysis = new TextToKeywords();
				String keywords = textAnalysis.getKeyword(transcribedPhrase);
				if(keywords == null){
					// Open browser based on request.
					openBrowser(transcribedPhrase);
				}else{
					System.out.println("Keywords are: "+keywords);
					//Send to wikipedia API
				}
			}

			@Override
			public void onError(final Exception e) {
				e.printStackTrace();
			}
		};

		service.recognizeUsingWebSockets(
				capture.getStream(), options, delegate);
		return finalTranscription;
	}

	/**
	 * Stops the stream.
	 */
	public final void endStream() {
		capture.shutDown(null);
	}

	/**
	 * Opens the browser based on the current request.
	 * 
	 * @param transcribedPhrase the phrase that was transcribed
	 */
	public final void openBrowser(final String transcribedPhrase) {
		// Speaks request
		Runnable runner = new TextToAudio("Searching for " + transcribedPhrase);
		Thread thread = new Thread(runner);
		thread.start();

		if (Desktop.isDesktopSupported()) {
			String searchString = "http://www.google.com/search?q="
					+ transcribedPhrase.replaceAll("\\s+", "%20");
			try {
				Desktop.getDesktop().browse(new URI(searchString));
				System.out.println("Success Browser Opened");
			} catch (IOException | URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
