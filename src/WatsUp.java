import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;
import com.ibm.watson.developer_cloud.text_to_speech.v1.util.WaveUtils;

/**
 * The class that holds the main method for the WatsUp app.
 * 
 * @author Josh Getter
 * @author Charles Billingsley
 *
 */

public final class WatsUp {
	
	/**
	 * Private constructor to overwrite the default constructor.
	 */
	private WatsUp() {
		return;
	}

	/**
	 * The main method of the WatsUp application.
	 * @param args the arguments passed in
	 */
	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Welcome to Wats Up!");
		TextToSpeech service = new TextToSpeech();
		service.setUsernameAndPassword("04bd6bbf-2415-4071-a59a-0285a6a253fa",
				"cL3Yon8JUQ4f");

		// Try to speak
		System.out.println("Trying to speak");
		try {
			InputStream stream = service.synthesize("Hello World",
					Voice.EN_ALLISON);
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

		} catch (Exception e) {
			System.out.println("Speaking failed error was: " + e);
		} finally {
			System.out.println("Speaking attempt complete!");
		}

	}

}