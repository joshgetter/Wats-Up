import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.Callable;

import com.ibm.watson.developer_cloud.speech_to_text.v1.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechModel;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.websocket.BaseRecognizeDelegate;

public class AudioToText implements Callable {

	@Override
	public Object call() throws Exception {
		// TODO Auto-generated method stub
		SpeechToText service = new SpeechToText();
		service.setUsernameAndPassword("95c52d86-c897-4870-99bc-e1bcaa6b25d5", "dA2xxyWJmgHy");
		List<SpeechModel> models = service.getModels();
		System.out.println(models);
		RecognizeOptions options = new RecognizeOptions().contentType("audio/wav")
				  .continuous(true).interimResults(false);
		Callable c = new CaptureAudio();
		CaptureAudio capture = new CaptureAudio();
		
		service.recognizeUsingWebSockets(capture.getStream(), options, delegate);
				BaseRecognizeDelegate delegate = new BaseRecognizeDelegate() {
				    @Override
				    public void onMessage(SpeechResults speechResults) {
				      System.out.println(speechResults);
				    }
				    	
				    @Override
				    public void onError(Exception e) {
				      e.printStackTrace();
				    }
				  };

				try {
				  service.recognizeUsingWebSockets(new FileInputStream("audio-file.wav"),
				    options, delegate);
				}
				catch (FileNotFoundException e) {
				  e.printStackTrace();
				}
		return null;
	}

}


