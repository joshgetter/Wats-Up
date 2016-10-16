package testpackage;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Assert;
import org.junit.Test;

import watsup.TextToAudio;

/**
 * Tester class for TextToAudio.java.
 * 
 * @author Charles
 *
 */
public class TestTextToAudio {

	/**
	 * Tests the text to audio functionality works.
	 */
	@Test
	public final void textToAudioSuccessTest() {

		String spokenText = "The test class works.";

		ByteArrayOutputStream outContent = new ByteArrayOutputStream();

		System.setOut(new PrintStream(outContent));

		TextToAudio textToAudio = new TextToAudio(spokenText);

		textToAudio.run();

		Assert.assertTrue(outContent.toString().contains(
				"Speaking attempt complete!"));
	}
	
	/**
	 * Tests the text to audio functionality fails.
	 */
	@Test
	public final void textToAudioFailTest() {

		String spokenText = null;

		ByteArrayOutputStream outContent = new ByteArrayOutputStream();

		System.setOut(new PrintStream(outContent));

		TextToAudio textToAudio = new TextToAudio(spokenText);

		textToAudio.run();

		Assert.assertTrue(outContent.toString().contains(
				"Speaking failed error was:"));
	}
}
