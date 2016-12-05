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

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Charles
 * @author Josh
 *
 */
public class APIController {

	public APIController() {

	}

	public void analyze(String unparsedString) {
		if (unparsedString.contains("Weather")) {
			// weather api here
		} else if (unparsedString.contains("Sports")) {
			// weather api here
		} else {
			TextToKeywords textAnalysis = new TextToKeywords();
			String keywords = textAnalysis.getKeyword(unparsedString);
			if (keywords == null) {
				// Open browser based on request.
				openBrowser(unparsedString);
			} else {
				System.out.println("Keywords are: " + keywords);
				String json = "";
				String url = "https://en.wikipedia.org"
						+ "/w/api.php?format=json&action=query"
						+ "&prop=extracts&exintro=" + "&explaintext=&titles=";
				Scanner scan = null;
				try {
					URL theURL = new URL(url
							+ keywords.replaceAll("\\s+", "%20"));
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
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Opens the browser based on the current request.
	 * 
	 * @param transcribedPhrase
	 *            the phrase that was transcribed
	 */
	public final void openBrowser(final String transcribedPhrase) {
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
}
