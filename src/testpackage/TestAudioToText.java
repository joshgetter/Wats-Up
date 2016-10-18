package testpackage;

import java.io.ByteArrayOutputStream;

import java.io.PrintStream;

import org.junit.Assert;
import org.junit.Test;

import watsup.AudioToText;

/**
 * Test class for AudioToText.java.
 * 
 * @author Josh Getter
 *
 */
public class TestAudioToText {

	/**
	 * The amount of time to wait until closing the thread.
	 */
	private static final long SLEEP_TIMER = 1000;

	/**
	 * Test a valid audioToText transaction.
	 */
	@Test
	public final void audioToTextTest() {
		System.out.println("Speak to transcribe");

		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		AudioToText audioToText = new AudioToText();
		try {
			audioToText.call();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			Thread.sleep(SLEEP_TIMER);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		audioToText.endStream();
		Assert.assertTrue(outContent.toString().contains(
				"Success Browser Opened"));
	}

}
