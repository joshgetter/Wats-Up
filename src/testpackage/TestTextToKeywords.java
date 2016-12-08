package testpackage;

import org.junit.Assert;

import org.junit.Test;

import watsup.TextToKeywords;

/**
 * Tests for the TextToKeywords class.
 * 
 * @author Charles Billingsley
 *
 */
public class TestTextToKeywords {

	/**
	 * Test to confirm that a valid input would produce a keyword.
	 */
	@Test
	public final void testValidGetKeyWord() {
		TextToKeywords textToKeywords = new TextToKeywords();
		
		Assert.assertTrue(textToKeywords.getKeyword("Hello my name is Bob")
				.length() != 0);
	}
	
	/**
	 * Test to confirm that an empty input would produce null.
	 */
	@Test
	public final void testEmptyGetKeyWord() {
		TextToKeywords textToKeywords = new TextToKeywords();
		
		Assert.assertTrue(textToKeywords.getKeyword("")
				== null);
	}
	
	/**
	 * Test to confirm that a null input would produce null.
	 */
	@Test
	public final void testNullGetKeyWord() {
		TextToKeywords textToKeywords = new TextToKeywords();
		
		Assert.assertTrue(textToKeywords.getKeyword(null)
				== null);
	}
	
	/**
	 * Test to confirm that a valid input would produce a city.
	 */
	@Test
	public final void testValidCityGetKeyEntity() {
		TextToKeywords textToKeywords = new TextToKeywords();
		String entity = textToKeywords.getEntity("Lansing");
		
		if (entity != null) {
			Assert.assertTrue(entity.length() != 0);
		}
	}
	
	/**
	 * Test to confirm that an invalid input would produce null.
	 */
	@Test
	public final void testInvalidGetKeyEntity() {
		TextToKeywords textToKeywords = new TextToKeywords();
		String entity = textToKeywords.getEntity("Red wings");
		
			Assert.assertTrue(entity == null);
	}
	
	/**
	 * Test to confirm that an empty input would produce null.
	 */
	@Test
	public final void testEmptyGetEntity() {
		TextToKeywords textToKeywords = new TextToKeywords();
		
		Assert.assertTrue(textToKeywords.getEntity("")
				== null);
	}
	
	/**
	 * Test to confirm that a null input would produce null.
	 */
	@Test
	public final void testNullGetEntity() {
		TextToKeywords textToKeywords = new TextToKeywords();
		
		Assert.assertTrue(textToKeywords.getEntity(null)
				== null);
	}
}
