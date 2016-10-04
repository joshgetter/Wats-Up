import java.io.InputStream;
import java.util.concurrent.Callable;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;
import com.ibm.watson.developer_cloud.text_to_speech.v1.util.WaveUtils;

public class TextToAudio implements Runnable {

	String toSpeack;

	public TextToAudio(String toSpeack) {
		this.toSpeack = toSpeack;
	}

	@Override
	public void run() {
		TextToSpeech service = new TextToSpeech();
		service.setUsernameAndPassword("04bd6bbf-2415-4071-a59a-0285a6a253fa", "cL3Yon8JUQ4f");

		// Try to speak
		System.out.println("Trying to speak");
		try {
			Clip audioClip = AudioSystem.getClip();
			InputStream stream = service.synthesize(toSpeack, Voice.EN_ALLISON);
			InputStream in = WaveUtils.reWriteWaveHeader(stream);
			audioClip.open(AudioSystem.getAudioInputStream(in));
			audioClip.start();
			/*
			 * OutputStream out = new FileOutputStream("Hello-World.wav");
			 * byte[] buffer = new byte[1024]; int length; while ((length =
			 * in.read(buffer)) > 0) { out.write(buffer, 0, length); }
			 * out.close(); in.close(); stream.close();
			 */

		} catch (Exception e) {
			System.out.println("Speaking failed error was: " + e);
		} finally {
			System.out.println("Speaking attempt complete!");
		}
	}

}
