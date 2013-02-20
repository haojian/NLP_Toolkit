package processing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

public class SentimentAnalyzer {

	private HashMap<String, Double> _dict;
	private static SentimentAnalyzer singleton;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Double output = SentimentAnalyzer.getInstance().extract("happy", "a");
		System.out.println(output);
	}

	
	public static SentimentAnalyzer getInstance(){
		if (singleton == null)
			singleton = new SentimentAnalyzer();
		return singleton;
	} 
	
	public SentimentAnalyzer(){
		_dict = new HashMap<String, Double>();
		HashMap<String, Vector<Double>> _temp = new HashMap<String, Vector<Double>>();
		try{
			BufferedReader csv =  new BufferedReader(new FileReader(utils.ParameterSetting.PATHTOSWN));
			String line = "";			
			while((line = csv.readLine()) != null)
			{
				if(line.startsWith("#") || line.trim().equals("#"))
					continue;
				
				String[] data = line.split("\t");
				Double score = Double.parseDouble(data[2])-Double.parseDouble(data[3]);
				String[] words = data[4].split(" ");
				for(String w:words)
				{
					String[] w_n = w.split("#");
					w_n[0] += "#"+data[0];
					int index = Integer.parseInt(w_n[1])-1;
					if(_temp.containsKey(w_n[0]))
					{
						Vector<Double> v = _temp.get(w_n[0]);
						if(index>v.size())
							for(int i = v.size();i<index; i++)
								v.add(0.0);
						v.add(index, score);
						_temp.put(w_n[0], v);
					}
					else
					{
						Vector<Double> v = new Vector<Double>();
						for(int i = 0;i<index; i++)
							v.add(0.0);
						v.add(index, score);
						_temp.put(w_n[0], v);
					}
				}
			}
			Set<String> temp = _temp.keySet();
			for (Iterator<String> iterator = temp.iterator(); iterator.hasNext();) {
				String word = (String) iterator.next();
				Vector<Double> v = _temp.get(word);
				double score = 0.0;
				double sum = 0.0;
				for(int i = 0; i < v.size(); i++)
					score += ((double)1/(double)(i+1))*v.get(i);
				for(int i = 1; i<=v.size(); i++)
					sum += (double)1/(double)i;
				score /= sum;
				_dict.put(word, score);
			}
			System.out.println("SentiWordNet initialized!");
		}
		catch(Exception e){e.printStackTrace();}
	}
	
	public double extract(String word, String pos)
	{
		return _dict.get(word.toLowerCase()+"#"+pos.toLowerCase());
	}
}
