package testpackage;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import javax.sound.sampled.AudioInputStream;

import org.junit.Test;

import watsup.CaptureAudio;

/**
 * Tester class for CaptureAudio.java.
 * 
 * @author Charles Billingsley
 *
 */
public class TestCaptureAudio {

	/**
	 * Tests getStream() returns an audioInputStream.
	 */
	@Test
	public final void testValidGetStream() {
		CaptureAudio captureInstance = new CaptureAudio();

		assertTrue(captureInstance.getStream().getClass()
				== AudioInputStream.class);
//		captureInstance.shutDown(null);
	}
	
	/**
	 * Tests shutDown() that prints a message.
	 */
	@Test
	public final void testValidShutdownWithMessage() {
		CaptureAudio captureInstance = new CaptureAudio();
		
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();

		System.setErr(new PrintStream(outContent));
		
		captureInstance.getStream();

		captureInstance.shutDown("Hello from testValidShutdownWithMessage");
		
		assertTrue(outContent.toString()
				.contains("Hello from testValidShutdownWithMessage"));
	}
	
	/**
	 * Tests shutDown() that doesn't print a message.
	 */
	@Test
	public final void testValidShutdownWithoutMessage() {
		CaptureAudio captureInstance = new CaptureAudio();
		
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();

		System.setErr(new PrintStream(outContent));
		
		captureInstance.getStream();

		captureInstance.shutDown(null);
		
		assertTrue(!outContent.toString()
				.contains("Hello from testValidShutdownWithMessage"));
	}
}
