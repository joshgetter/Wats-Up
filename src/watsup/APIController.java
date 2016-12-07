/**
 * 
 */
package watsup;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.Scanner;

import org.json.JSONObject;

/**
 * @author Charles Billingsley
 * @author Josh Getter
 *
 */
public class APIController {
	
	/**
	 * The length of the zipcode for weather API.
	 */
	private static final int ZIP_CODE_LENGTH = 5;
	
	/**
	 * The text that has been analyzed.
	 */
	private TextToKeywords textAnalysis;
	
	/**
	 * Constructor for the controller.
	 */
	public APIController() {
		textAnalysis = new TextToKeywords();
	}

	/**
	 * Analyzes a string for keywords that might want to use a different API.
	 * 
	 * @param unparsedString
	 *            the string of spoken words to be checked for keywords
	 */
	public final void analyze(final String unparsedString) {
		
		// --------------------------Weather---------------------------------
		if (unparsedString.toLowerCase().contains("weather")) {
			String geoLookupString = "http://api.wunderground.com"
					+ "/api/aec09f336e561232/geolookup/q/";
			String weatherUrl = "http://api.wunderground.com"
					+ "/api/aec09f336e561232/conditions/q/";
			String numbersOnly = unparsedString.replaceAll("[^0-9]", "");
			if (numbersOnly.length() == ZIP_CODE_LENGTH) {
				// Is a zip code (assuming)
				// Send API call to lookup weather location
				geoLookupString = geoLookupString + numbersOnly + ".json";
			} else {
				geoLookupString = geoLookupString
						+ textAnalysis.getEntity(unparsedString) + ".json";
			}
			String geoJson = getJson(geoLookupString);
			try {
				String requestUrl = new JSONObject(geoJson)
						.getJSONObject("location").getString("requesturl")
						.replace(".html", "");
				weatherUrl = weatherUrl + requestUrl + ".json";
				String weatherJson = getJson(weatherUrl);
				String city = new JSONObject(weatherJson)
						.getJSONObject("current_observation")
						.getJSONObject("display_location").getString("city");
				JSONObject weatherObj = new JSONObject(weatherJson)
						.getJSONObject("current_observation");
				System.out.println(weatherObj);
				int temp = (int) weatherObj.getDouble("temp_f");
				String conditions = weatherObj.getString("weather");
				int feelsLike = (int) weatherObj.getDouble("feelslike_f");
				String outputWeather = "The current weather for " + city
						+ " is " + temp + " degrees with " + conditions
						+ " conditions. It feels like " + feelsLike
						+ " degrees.";
				TextToAudio textToAudio = new TextToAudio(outputWeather);
				textToAudio.run();
				return;
			} catch (Exception e) {
				System.out.println("Could not find weather location");
			}

			// --------------------------------Sports---------------------------
		} else if (unparsedString.contains("Sports")) {
			//TODO find sport api for here
			openBrowser(unparsedString);
			return;
			// --------------------------------Everything else------------------
		} else {
			String keywords = textAnalysis.getKeyword(unparsedString);
			if (keywords == null) {
				// Open browser based on request.
				openBrowser(unparsedString);
				return;
			} else {
				System.out.println("Keywords are: " + keywords);
				String url = "https://en.wikipedia.org"
						+ "/w/api.php?format=json&action=query"
						+ "&prop=extracts&exintro=" + "&explaintext=&titles=";
				String json = getJson(url + keywords);
				System.out.println(json);
				try {
					JSONObject top = new JSONObject(json);
					JSONObject query = top.getJSONObject("query");
					JSONObject pages = query.getJSONObject("pages");

					@SuppressWarnings("unchecked")
					Iterator<String> keys = pages.keys();
					String extract = (String) keys.next();

					JSONObject first = pages.getJSONObject(extract);

					String finalOutput = first.getString("extract");
					System.out.println(finalOutput);
					String[] sArray = finalOutput.split("(?<=[a-z])\\.\\s+");
					TextToAudio toAudio = 
							new TextToAudio(sArray[0] + sArray[1]);
					toAudio.run();
					return;
				} catch (Exception e) {
					System.out.println("Wikipedia Found No Results");
					// openBrowser(unparsedString);
				}
			}
		}
		openBrowser(unparsedString);
	}

	/**
	 * Opens the browser based on the current request.
	 * 
	 * @param transcribedPhrase
	 *            the phrase that was transcribed
	 */
	private void openBrowser(final String transcribedPhrase) {
		// Speaks request
		Runnable runner = new TextToAudio("Searching for " + transcribedPhrase);
		Thread thread = new Thread(runner);
		thread.start();

		if (Desktop.isDesktopSupported()) {
			String searchString = "http://www.google.com/search?q="
					+ transcribedPhrase.replaceAll("\\s+", "%20");
			try {
				Desktop.getDesktop().browse(new URI(searchString));
				System.out.println("Success Browser Opened");
			} catch (IOException | URISyntaxException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * A private helper method to get JSON data from a website.
	 * 
	 * @param url the url to be accessed
	 * @return the JSON object as a string
	 */
	private String getJson(final String url) {
		String json = "";
		Scanner scan = null;
		try {
			URL theURL = new URL(url.replaceAll("\\s+", "%20"));
			scan = new Scanner(theURL.openStream());
			while (scan.hasNextLine()) {
				json += scan.nextLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (scan != null) {
				scan.close();
			}
		}
		return json;
	}
}
