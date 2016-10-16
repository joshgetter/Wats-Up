package watsUp;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

/**
 * Captures the audio for Watson to analyze.
 * 
 * @author Charles Billingsley
 * @author Josh Getter
 * @author Brent Willman
 */
public class CaptureAudio {

	/**
	 * The sample rate in which the audio should be captured.
	 */
	private static final float RATE = 16000;

	/**
	 * The sample size of which the audio should be captured.
	 */
	private static final int SAMPLE_SIZE = 16;

	/**
	 * The number of audio channels to be captured.
	 */
	private static final int CHANNELS = 1;
	
	/**
	 * The line coming into the computer.
	 */
	private TargetDataLine line;

	/**
	 * Closes the line and collects any error messages.
	 * 
	 * @param message
	 *            the error message being passed into the method
	 */
	public final void shutDown(final String message) {
		String errStr = message;
		line.stop();
		line.close();
		if (errStr != null) {
			System.err.println(errStr);
		}
	}

	/**
	 * Captures the audio from our microphone.
	 * 
	 * @return the audio data that is coming through the computer
	 */
	public final AudioInputStream getStream() {
		
		AudioInputStream audioInputStream;

		audioInputStream = null;

		AudioFormat format = new AudioFormat(RATE, SAMPLE_SIZE, CHANNELS, true,
				false);

		DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

		if (!AudioSystem.isLineSupported(info)) {
			shutDown("Line matching " + info + " not supported.");
			return null;
		}

		// get and open the target data line for capture.

		try {
			line = (TargetDataLine) AudioSystem.getLine(info);
			line.open(format);
			line.start();
		} catch (LineUnavailableException ex) {
			shutDown("Unable to open the line: " + ex);
			return null;
		} catch (SecurityException ex) {
			shutDown(ex.toString());
			return null;
		} catch (Exception ex) {
			shutDown(ex.toString());
			return null;
		}

		audioInputStream = new AudioInputStream(line);

		return audioInputStream;

	}
}
