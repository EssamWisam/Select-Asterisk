import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.jsoup.Jsoup;

import ca.rmen.porterstemmer.PorterStemmer;

public class Stemming {
	static Set<String> stopWordsSet = new HashSet<String>();
	
	// parameter : String
	// using Jsoup liberary work to convert HTML file to text
	// remove html tags by replacing it with space
	// remove any thing not alphapitic char
	// Return type : String
	public static String html2text(String html) {
		return Jsoup.parse(html).text().replaceAll("[^A-Za-z ]", "");
		 
	}

	// parameter : array of string words
	// this function work to remove all stops words from array of strings
	// it reads file with different stops words in English
	// Return type : ArrayList<String>
	public static ArrayList<String> removeStopWords(String[] words) throws IOException {
		String line;
		ArrayList<String> wordsList = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new FileReader("stop_words_english.txt"))) {
			while ((line = br.readLine()) != null) {
				stopWordsSet.add(line.toUpperCase());
			}
		}
		stopWordsSet.add("/");

		for (String word : words) {
			String wordCompare = word.toUpperCase();
			if (!stopWordsSet.contains(wordCompare) && !wordCompare.isEmpty()) {
				wordsList.add(word);
			}
		}
		return wordsList;
	}
	// parameter: ArrayList<String>
	   // this function works using PorterStemmer liberary to stem array list of string 	
	   // Return type: ArrayList<String>
		
		public static ArrayList<String> PorterStemming(ArrayList<String> wordsList) {
			ArrayList<String> finalwords = new ArrayList<String>();
			PorterStemmer p = new PorterStemmer();
			for (String str : wordsList)
				finalwords.add(p.stemWord(str));
			
			return finalwords;
		}
		

}
