/*
 *
 * Copyright (c) 1999 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Sun grants you ("Licensee") a non-exclusive, royalty free,
 * license to use, modify and redistribute this software in 
 * source and binary code form, provided that i) this copyright
 * notice and license appear on all copies of the software; and 
 * ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 *
 * This software is provided "AS IS," without a warranty
 * of any kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS
 * AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE 
 * HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR 
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT
 * WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT
 * OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, 
 * INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS
 * OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY
 * TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGES.

 This software is not designed or intended for use in on-line
 control of aircraft, air traffic, aircraft navigation or
 aircraft communications; or in the design, construction,
 operation or maintenance of any nuclear facility. Licensee 
 represents and warrants that it will not use or redistribute 
 the Software for such purposes.
 */

/*  The above copyright statement is included because this 
 * program uses several methods from the JavaSoundDemo
 * distributed by SUN. In some cases, the sound processing methods
 * unmodified or only slightly modified.
 * All other methods copyright Steve Potts, 2002
 */

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

/**
 * SimpleSoundCapture Example. This is a simple program to record sounds and
 * play them back. It uses some methods from the CapturePlayback program in the
 * JavaSoundDemo. For licensizing reasons the disclaimer above is included.
 * 
 * @author Steve Potts
 * 
 *         Modified by
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
	 * The stream of audio entering the computer.
	 */
	AudioInputStream audioInputStream;

	TargetDataLine line;

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
