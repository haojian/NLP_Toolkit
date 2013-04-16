package snowballstemmer;

import snowballstemmer.ext.englishStemmer;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Class stemClass;
		try {

			SnowballStemmer stemmer = (SnowballStemmer) new englishStemmer();
			stemmer.setCurrent("places");
			stemmer.stem();
			System.out.println(stemmer.getCurrent());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
