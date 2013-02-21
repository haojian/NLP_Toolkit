package processing;

import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

public class SentenceSplitter {

	
	private static SentenceSplitter singleton;

	public static SentenceSplitter getInstance() {
		if (singleton == null)
			singleton = new SentenceSplitter();
		return singleton;
	}
	
	public String[] sentence_split(String paragrah){
		SentenceDetector _sentenceDetector = null;
		InputStream modelIn = null;
		try {
			// if the model file is missiong, copy it from opennlpmodels to bin/nlputil.
		   // Loading sentence detection model
		   modelIn = getClass().getResourceAsStream("../resources/en-sent.bin");
		   final SentenceModel sentenceModel = new SentenceModel(modelIn);
		   modelIn.close();

		   _sentenceDetector = new SentenceDetectorME(sentenceModel);
		   String sentences[] = _sentenceDetector.sentDetect(paragrah);
		   
		   for(String sent : sentences){
			   System.out.println(sent.split(" ").length + "|"+ sent);
			   if(sent.split(" ").length > 100){
				   System.err.println("error, Manually Splitting Required");
			   }
			   
		   }
		   return sentences;
		} catch (final IOException ioe) {
		   ioe.printStackTrace();
		} finally {
		   if (modelIn != null) {
		      try {
		         modelIn.close();
		      } catch (final IOException e) {} // oh well!
		   }
		}
		return null;
	}	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
