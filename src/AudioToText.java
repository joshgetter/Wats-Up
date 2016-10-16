import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

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
	private String transcribedPhrase;
	
	/**
	 * The audio as it's captured.
	 */
	CaptureAudio capture = new CaptureAudio();

	@Override
	public final String call() throws Exception {
		// String toReturn;
		SpeechToText service = new SpeechToText();
		service.setUsernameAndPassword("95c52d86-c897-4870-99bc-e1bcaa6b25d5",
				"dA2xxyWJmgHy");
		List<SpeechModel> models = service.getModels();
		RecognizeOptions options = new RecognizeOptions()
				.contentType(HttpMediaType.AUDIO_RAW + "; rate=16000")
				.continuous(true).interimResults(false);

		BaseRecognizeDelegate delegate = new BaseRecognizeDelegate() {
			@Override
			public void onMessage(final SpeechResults speechResults) {
				System.out.println(speechResults);
				setTranscribedPhrase(speechResults.toString());
			}
		RecognizeOptions options = new RecognizeOptions().contentType(HttpMediaType.AUDIO_RAW + "; rate=16000")
				  .continuous(true).interimResults(false);
		
				BaseRecognizeDelegate delegate = new BaseRecognizeDelegate() {
				    @Override
				    public void onMessage(SpeechResults speechResults) {
				      System.out.println(speechResults);
				      //toReturn = speechResults.toString();
				      List<Transcript> results = speechResults.getResults();
				      for(Transcript t : results){
				    	  toReturn = toReturn + t.getAlternatives().get(0).getTranscript();
				      }
				      System.out.println("Results are:" + toReturn);
				      
				    }
				    	
				    @Override
				    public void onError(Exception e) {
				      e.printStackTrace();
				    }
				  };

			@Override
			public void onError(final Exception e) {
				e.printStackTrace();
			}
		};

		service.recognizeUsingWebSockets(capture.getStream(),
				options, delegate);
		// SpeechResults transcript = service.recognize(capture.getStream(),
		// options);
		return getTranscribedPhrase();
	}

	/**
	 * Stops the stream.
	 */
	public final void endStream() {
		capture.shutDown(null);
	}

	/**
	 * @return the transcribedPhrase
	 */
	public final String getTranscribedPhrase() {
		return transcribedPhrase;
	}

	/**
	 * @param pTranscribedPhrase the transcribedPhrase to set
	 */
	public final void setTranscribedPhrase(final String pTranscribedPhrase) {
		this.transcribedPhrase = pTranscribedPhrase;
	}

}
