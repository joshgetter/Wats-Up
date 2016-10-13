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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

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
 * Modified by
 * @author Charles Billingsley
 * @author Josh Getter
 * @author Brent Willman
 */
public class CaptureAudio {

	final int bufSize = 16384;

	AudioInputStream audioInputStream;

	String errStr;

	double duration, seconds;

	File file;

	TargetDataLine line;

	Thread thread;


	public void shutDown(String message) {
		line.stop();
		line.close();
		if ((errStr = message) != null && thread != null) {
			thread = null;
			System.err.println(errStr);
		}
		thread = null;
	}

public AudioInputStream getStream() {

		duration = 0;
		audioInputStream = null;

		// define the required attributes for our line,
		// and make sure a compatible line is supported.

		AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
		float rate = 16000;
		int channels = 2;
		int frameSize = 4;
		int sampleSize = 16;
		boolean bigEndian = true;

		/*AudioFormat format = new AudioFormat(encoding, rate, sampleSize,
				channels, (sampleSize / 8) * channels, rate, bigEndian);*/
		AudioFormat format = new AudioFormat(16000,16, 1, true, false);

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
			// JavaSound.showInfoDialog();
			return null;
		} catch (Exception ex) {
			shutDown(ex.toString());
			return null;
		}
		AudioInputStream audioInputStream = new AudioInputStream(line);
		return audioInputStream;
		
		/*long milliseconds = (long) ((audioInputStream.getFrameLength() * 1000) / format
				.getFrameRate());
		duration = milliseconds / 1000.0;*/

		/*try {
			audioInputStream.reset();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}*/

	}
}
