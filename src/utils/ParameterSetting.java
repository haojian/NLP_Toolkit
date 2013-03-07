package utils;

import java.io.File;

public class ParameterSetting {
	//extraction parameters
	public static int BOOTSTRAPPINGTHRESHOLD = 6;
	public static int MAXSEEDSNOUN = 50;
	public static int MAXSEEDSADJ = 4;
	public static int EXTRACTIONDISTANCETHRESHOLD = 3;
	
	//path parameters.
	public static String PATHTOSWN = "resources" + File.separator + "SentiWordNet_3.0.0.txt";
	public static String PATHTOSTOPWORDS = "resources" + File.separator + "stopwords.txt";
	public static String PATHTOPOSTAGGEDDATA = "/Users/snz5pal/Documents/pastwork/RevTxt/backup";
	public static String PATHTOCRAWLEDDATA = "/Users/snz5pal/Documents/pastwork/RevTxt/crawleddata";
	public static String PATHTOLOG = "./log.txt";
	public static String PATHTODEBUG = "./debug.txt";
	public static String PATHTOSEEDFILE = "./seed.txt";
	public static String PATHTOITERATIONRESULTDIR = "./Iterations";
	
	//REGX String variables
	public static String REGXWORDPATTERN_V1 = "(.*)";
	public static String REGXWORDPATTERN_V2 = "([a-z]{3,15})";
	public static String REGXWORDPATTERN_V3 = "([a-z]{3,15}?)";
}
