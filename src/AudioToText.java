import java.util.List;
import java.util.concurrent.Callable;

import com.ibm.watson.developer_cloud.http.HttpMediaType;
import com.ibm.watson.developer_cloud.speech_to_text.v1.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechModel;
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
public class AudioToText implements Callable {

	/**
	 * The string that was transcribed by Watson.
	 */

	/**
	 * The audio as it's captured.
	 */
	CaptureAudio capture = new CaptureAudio();

	@Override
	public final String call() throws Exception {
		SpeechToText service = new SpeechToText();
		service.setUsernameAndPassword("95c52d86-c897-4870-99bc-e1bcaa6b25d5",
				"dA2xxyWJmgHy");
		List<SpeechModel> models = service.getModels();
		System.out.println(models);
		RecognizeOptions options = new RecognizeOptions()
				.contentType(HttpMediaType.AUDIO_RAW + "; rate=16000")
				.continuous(true).interimResults(false);

		BaseRecognizeDelegate delegate = new BaseRecognizeDelegate() {
			@Override
			public void onMessage(final SpeechResults speechResults) {
				String transcribedPhrase = "";
				System.out.println(speechResults);
				// String currentPhrase = "";
				List<Transcript> results = speechResults.getResults();
				for (Transcript t : results) {
					transcribedPhrase += t.getAlternatives().get(0)
							.getTranscript();
				}
				System.out.println("Results are:" + transcribedPhrase);
			}

			@Override
			public void onError(final Exception e) {
				e.printStackTrace();
			}
		};

		service.recognizeUsingWebSockets(capture.getStream(), options, delegate);
		// SpeechResults transcript = service.recognize(capture.getStream(),
		// options);
//		return transcribedPhrase;
		return null;
	}

	/**
	 * Stops the stream.
	 */
	public final void endStream() {
		capture.shutDown(null);
	}


}
