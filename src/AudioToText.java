import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

public class AudioToText implements Callable {
String toReturn;
CaptureAudio capture = new CaptureAudio();
	@Override
	public String call() throws Exception {
		// TODO Auto-generated method stub
		//String toReturn;
		SpeechToText service = new SpeechToText();
		service.setUsernameAndPassword("95c52d86-c897-4870-99bc-e1bcaa6b25d5", "dA2xxyWJmgHy");
		List<SpeechModel> models = service.getModels();
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


		service.recognizeUsingWebSockets(capture.getStream(), options, delegate);
		//SpeechResults transcript = service.recognize(capture.getStream(), options);
		return toReturn;
	}
	public void endStream(){
		capture.shutDown(null);
	}

}


