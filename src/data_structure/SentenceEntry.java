package data_structure;

import java.util.HashMap;
import java.util.Map;

public class SentenceEntry {
	private String _senttxt = "";
	public Map<Template, Extraction> Extractionmap;
	
	public SentenceEntry(String sent){
		_senttxt = sent;
		Extractionmap = new HashMap<Template, Extraction>();
	}
	
	
}
