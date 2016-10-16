package testPackage;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Assert;
import org.junit.Test;

import watsUp.AudioToText;
public class TestAudioToText {
	@Test
	public void AudioToTextTest(){
	System.out.println("Speak to transcribe");

	ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	System.setOut(new PrintStream(outContent));
	AudioToText audioToText = new AudioToText();
	try {
		audioToText.call();
	} catch (Exception e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	try {
		Thread.sleep(1000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	audioToText.endStream();
	Assert.assertTrue(outContent.toString().contains("Success Browser Opened"));
	}
	
}
