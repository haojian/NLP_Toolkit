package data_structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SentenceEntry {
	private String _senttxt = "";
	public ArrayList<Template> CandidateTemplates;
	public Map<Template, Extraction> Extractionmap;
	
	public SentenceEntry(String sent){
		set_senttxt(sent);
		CandidateTemplates = new ArrayList<Template>();
		Extractionmap = new HashMap<Template, Extraction>();
	}

	public String get_senttxt() {
		return _senttxt;
	}

	public void set_senttxt(String _senttxt) {
		this._senttxt = _senttxt;
	}
	
	
}
