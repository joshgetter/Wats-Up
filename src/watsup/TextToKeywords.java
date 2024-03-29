package watsup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibm.watson.developer_cloud.alchemy.v1.AlchemyLanguage;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Entities;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Entity;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Keyword;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Keywords;
import com.ibm.watson.developer_cloud.http.ServiceCall;

/**
 * A class that pulls keywords out of a string to be used for better web
 * searching.
 * 
 * @author Josh Getter
 * @author Charles Billingsley
 *
 */
public class TextToKeywords {

	/**
	 * Uses the alchemy SDK to pull out the key words.
	 * 
	 * @param inputText the text to be parsed
	 * @return the keyword(s) from the parsed sentence
	 */
	public final String getKeyword(final String inputText) {
		AlchemyLanguage alchemy = new AlchemyLanguage();
		alchemy.setApiKey("8a7ac2a6401df09dbff81fab7ce0f45313c4f364");
		Map<String, Object> params = new HashMap<String, Object>();
		if (inputText == null || inputText.isEmpty()) {
			// Return null now to avoid using api call
			return null;
		}
		params.put(AlchemyLanguage.TEXT, inputText);
		ServiceCall<Keywords> keywordsServiceCall = alchemy.getKeywords(params);
		Keywords keywords = keywordsServiceCall.execute();
		if (keywords.getKeywords().isEmpty()) {
			return null;
		}
		Keyword topKeyword = keywords.getKeywords().get(0);
		return topKeyword.getText();
	}
	
	/**
	 * Makes the call to the alchemy service.
	 * 
	 * @param inputText the unparsed text
	 * @return the keywords
	 */
	public final String getEntity(final String inputText) {
		AlchemyLanguage alchemy = new AlchemyLanguage();
		alchemy.setApiKey("8a7ac2a6401df09dbff81fab7ce0f45313c4f364");
		Map<String, Object> params = new HashMap<String, Object>();
		if (inputText == null || inputText.isEmpty()) {
			// Return null now to avoid using api call
			return null;
		}
		params.put(AlchemyLanguage.TEXT, inputText);
		ServiceCall<Entities> entityServiceCall = alchemy.getEntities(params);
		List<Entity> entities = entityServiceCall.execute().getEntities();
		for (Entity e : entities) {
			if (e.getType().equals("City")) {
				return e.getText();
			}
		}
		return null;
	}

}
