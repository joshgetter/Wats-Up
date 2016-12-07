/**
 * 
 */
package watsup;

import java.awt.Desktop;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Charles
 * @author Josh
 *
 */
public class APIController {
	TextToKeywords textAnalysis;

	public APIController() {
		textAnalysis = new TextToKeywords();
	}

	public void analyze(String unparsedString) {
		if (unparsedString.toLowerCase().contains("weather")) {
			// weather api here
			String geoLookupString = "http://api.wunderground.com/api/aec09f336e561232"
					+ "/geolookup/q/";
			String weatherUrl = "http://api.wunderground.com/api/aec09f336e561232/conditions/q/";
			String numbersOnly = unparsedString.replaceAll("[^0-9]", "");
			if (numbersOnly.length() == 5) {
				//Is a zip code (assuming)
				// Send API call to lookup weather location
				geoLookupString = geoLookupString + numbersOnly + ".json";
			} else {
				geoLookupString = geoLookupString 
						+ textAnalysis.getEntity(unparsedString) + ".json";
			}
			String geoJson = getJson(geoLookupString);
			try{
				String requestUrl = new JSONObject(geoJson)
						.getJSONObject("location")
						.getString("requesturl")
						.replace(".html", "");
				weatherUrl = weatherUrl + requestUrl + ".json";
				String weatherJson = getJson(weatherUrl);
				String city = new JSONObject(weatherJson)
						.getJSONObject("current_observation")
						.getJSONObject("display_location")
						.getString("city");
				JSONObject weatherObj = new JSONObject(weatherJson)
						.getJSONObject("current_observation");
				System.out.println(weatherObj);
				int temp = (int) weatherObj.getDouble("temp_f");
				String conditions = weatherObj.getString("weather");
				int feelsLike = (int) weatherObj.getDouble("feelslike_f");
				String outputWeather = "The current weather for " + city + " is "
						+ temp + " degrees with " + conditions 
						+ " conditions. It feels like " 
						+ feelsLike + " degrees.";
				TextToAudio textToAudio = new TextToAudio(outputWeather);
				textToAudio.run();
				return;
			}catch(Exception e){
				System.out.println("Could not find weather location");
			}
		} else if (unparsedString.contains("Sports")) {
			// weather api here
		} else {
			String keywords = textAnalysis.getKeyword(unparsedString);
			if (keywords == null) {
				// Open browser based on request.
				openBrowser(unparsedString);
				return;
			} else {
				System.out.println("Keywords are: " + keywords);
				String url = "https://en.wikipedia.org" + "/w/api.php?format=json&action=query"
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
					TextToAudio toAudio = new TextToAudio(sArray[0] + sArray[1]);
					toAudio.run();
					return;
				} catch (Exception e) {
					System.out.println("Wikipedia Found No Results");
					//openBrowser(unparsedString);
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
	private final void openBrowser(final String transcribedPhrase) {
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private String getJson(String url) {
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
