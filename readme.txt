/**************
Notes
***************/

/**************
Usage
***************/
1. Open the project in Eclipse.
2. Update the build path if the code was changed to a new directory.
3. Copy the resources/en-sent.bin to bin/en-sent.bin
4. Play with the code.

/**************
Code architecture
***************/
Default package:
	Command-based interface.
Advance package:
	NLP operation based on attribute-value word pair.
Data structure:
	Data entries in NLP processing.
Processing:
	Low-level NLP operations: bootstrapping extraction, refinement, output, seed generation, sentence splitter, sentiment analysis, stop word filter.

SnowballStemmer:
	Word Stemmer.

Utils:
	Basic DB, File IO, Mathematic, Parameter setting utility files.
	All the file path setting can be find at ParameterSetting.java.



/**************
BootStrapping Extractions
***************/
Run the code at Extraction_bootstrapping.java.
Change the path setting if necessary.
		public static void main(String[] args) {
		DataManager.getInstance();
		// TODO Auto-generated method stub
		for(File input : DataManager.getFilesUnderFolder(ParameterSetting.PATHTOCRAWLEDDATA3)){
			if(input.getName().split("_").length != 3)
				continue;
			long startTime = System.currentTimeMillis();
			ArrayList<String> tmp =  DataManager.getInstance().getSentencesInFile(input);
			int size = tmp.size();
			Extraction_bootstrapping.getInstance().UpdateCorpus(tmp, input.getName());
			long ellapse = System.currentTimeMillis() - startTime;
			//System.out.println(input.getName() + " \t " + size + " Execution time: " + ellapse);

			//break;
		}
		System.out.println(Extraction_bootstrapping.getInstance().corpus.get(414663).UniqueID+"\t" + Extraction_bootstrapping.getInstance().corpus.get(414663).get_senttxt());
		Extraction_bootstrapping.getInstance().StartProcess();
		Extraction_bootstrapping.getInstance().WriteResulttoFile();
		System.out.println("long wait.....it's finished!....");
	}


/**************
Attribute Clustering
***************/
Attribute Clustering results can be found at /clusteringresults.


/********************/
Sentence Splitting:
The sentence splitting in this project is based on Apache OpenNLP in this project.

For performance evaluation, http://ufal.mff.cuni.cz/pbml/98/art-marsik-bojar.pdf also provides a comparison between different sentence splitting frameworks.

sentence splitter DescriptiveStatistics:
n: 1058041
min: 1.0
max: 50.0
mean: 13.857152983674476
std dev: 8.803619413723986
median: 12.0
skewness: 1.0384383831028539
kurtosis: 1.1811509404259999
/********************/


/********************/
BootStrapping:

Template Extraction:

Sample Template:
Everything was  [VALUE] , from the  [ATTRIBUTE]  to the wine to the service. 
Everything was  [VALUE] , from the food to the wine to the  [ATTRIBUTE] . 

Pseudo code:

dist = closest distance between [VALUE] & [ATTRIBUTE] (most compact template).
if(dist < threshold){
	the whole sentence would be template.
}else{
	the template would be the words that occur between this pair.
}


Template statistic in the first iteration:
{[VALUE]  things about this  [ATTRIBUTE] =91, The  [ATTRIBUTE]  was  [VALUE] . =77, [VALUE]  part of the  [ATTRIBUTE] =65, The  [ATTRIBUTE]  is  [VALUE] . =61, This  [ATTRIBUTE]  is  [VALUE] . =56, [VALUE]  thing about this  [ATTRIBUTE] =48,  [VALUE]   [ATTRIBUTE] . =32, [ATTRIBUTE]  was a bit  [VALUE] =25, This  [ATTRIBUTE]  is  [VALUE] ! =24, [ATTRIBUTE]  is just as  [VALUE] =23, [ATTRIBUTE]  is one of the  [VALUE] =22, The  [ATTRIBUTE]  was very  [VALUE] . =19, The  [ATTRIBUTE]  was  [VALUE] ! =17, [ATTRIBUTE]  was a little  [VALUE] =17, Definitely  [VALUE]  the  [ATTRIBUTE] . =15, [ATTRIBUTE]  can get pretty  [VALUE] =15 ...

/********************/

/********************/
Performance Profiling:

The biggest cost in this extraction is the regex matching. I revised the pattern regex for better performance for multiple time. You can check the details in the profiling directory.

To improve the performance, I updated two design in this implementation: 1. String => Stringbuilder. 2. extraction history (never repeat extraction).

/********************/



