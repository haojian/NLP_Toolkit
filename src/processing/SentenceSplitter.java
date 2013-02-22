package processing;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.util.InvalidFormatException;
import utils.IOOperator;
import utils.ParameterSetting;

public class SentenceSplitter {

	
	private static SentenceSplitter singleton;
	InputStream modelIn = null;
	SentenceDetector _sentenceDetector = null;
	public DescriptiveStatistics stats;
	
	public static SentenceSplitter getInstance() {
		if (singleton == null)
			singleton = new SentenceSplitter();
		return singleton;
	}
	
	public SentenceSplitter(){
		stats = new DescriptiveStatistics();
		try {
			// if the model file is missiong, copy it from opennlpmodels to bin/nlputil.
			// Loading sentence detection model
			modelIn = getClass().getResourceAsStream("/en-sent.bin");
			final SentenceModel sentenceModel = new SentenceModel(modelIn);
			modelIn.close();
			_sentenceDetector = new SentenceDetectorME(sentenceModel);
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//sent splitter could be refined with multiple level splitting.
	public ArrayList<String> sentence_split(String paragraph){
		ArrayList<String> res = new ArrayList<String>();

		String sentences[] = _sentenceDetector.sentDetect(paragraph);
		for(String sent : sentences){
			if(sent.split(" ").length > 50){
				//IOOperator.getInstance().writeToFile(ParameterSetting.PATHTOLOG, sent.split(" ").length + "|"+ sent + "\n", true);
				res.addAll(Arrays.asList(sent.split(".")));
			}
			else
				res.add(sent);
		}
		for(String str : res){
			stats.addValue(str.split(" ").length);
			if(str.split(" ").length < 5){
				IOOperator.getInstance().writeToFile(ParameterSetting.PATHTOLOG, str.split(" ").length + "|"+ str + "\n", true);
			}
		}
		return res;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
