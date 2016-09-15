import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;
import com.ibm.watson.developer_cloud.text_to_speech.v1.util.WaveUtils;
public class WatsUp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Welcome to Wats Up!");
		TextToSpeech service = new TextToSpeech();
		service.setUsernameAndPassword("04bd6bbf-2415-4071-a59a-0285a6a253fa", "cL3Yon8JUQ4f");
		
		//Try to speak
		try{
			InputStream stream = service.synthesize("Hello World", Voice.EN_ALLISON);
			InputStream in = WaveUtils.reWriteWaveHeader(stream);
			OutputStream out = new FileOutputStream("Hello-World.wav");
			byte[] buffer = new byte[1024];
			  int length;
			  while ((length = in.read(buffer)) > 0) {
			    out.write(buffer, 0, length);
			  }
			  out.close();
			  in.close();
			  stream.close();
			
		}catch(Exception e){
		System.out.println(e);	
		}
		
	}

}