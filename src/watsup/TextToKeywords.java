package watsup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import com.ibm.watson.developer_cloud.alchemy.v1.AlchemyLanguage;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Keyword;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Keywords;

public class TextToKeywords{

	public String getKeyword(String inputText){
		// TODO Auto-generated method stub
		AlchemyLanguage alchemy = new AlchemyLanguage();
		alchemy.setApiKey("8a7ac2a6401df09dbff81fab7ce0f45313c4f364");
		Map <String, Object> params = new HashMap<String, Object>();
		if(inputText == null || inputText.isEmpty()){
			//Return null now to avoid using api call
			return null;
		}
		params.put(AlchemyLanguage.TEXT, inputText);
		Keywords keywords = alchemy.getKeywords(params);
		if(keywords.getKeywords().isEmpty()){
			return null;
		}
		Keyword topKeyword = keywords.getKeywords().get(0);
		return topKeyword.getText();
	}

}