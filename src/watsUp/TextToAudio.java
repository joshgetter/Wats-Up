package watsUp;

import java.io.InputStream;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;
import com.ibm.watson.developer_cloud.text_to_speech.v1.util.WaveUtils;

/**
 * A class that converts written text to audio by sending it to the Watson API.
 * 
 * @author Josh Getter
 * @author Charles Billingsley
 * @author Brent Willman
 *
 */
public class TextToAudio implements Runnable {

	/**
	 * The string to be spoken by Watson.
	 */
	private String spokenPhrase;

	/**
	 * Constructor for the TextToAudio class.
	 * 
	 * @param passedSpokenPhrase
	 *            the phrase to be spoken
	 */
	public TextToAudio(final String passedSpokenPhrase) {
		this.spokenPhrase = passedSpokenPhrase;
	}

	@Override
	public final void run() {
		TextToSpeech service = new TextToSpeech();
		service.setUsernameAndPassword("04bd6bbf-2415-4071-a59a-0285a6a253fa",
				"cL3Yon8JUQ4f");

		// Try to speak
		System.out.println("Trying to speak");
		try {
			Clip audioClip = AudioSystem.getClip();
			InputStream stream = service.synthesize(spokenPhrase,
					Voice.EN_ALLISON);
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
