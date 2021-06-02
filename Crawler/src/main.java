import java.util.*;

import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClient;
import com.mongodb.DBCollection;
import org.bson.Document;
public class main {
    public static void main(String[] args) throws Exception {
        ArrayList<String> wordsList = new ArrayList<String>();
        HashMap<String, ArrayList<String>> FI = new HashMap<String, ArrayList<String>>();
        MongoDB.MongoHandler mdb = new MongoDB.MongoHandler();
        mdb.ConnecttoDB();
       /* System.out.println(mdb.collection);
        List<String> seedSet = Arrays.asList(
                "https://www.quora.com/",
                "https://www.geeksforgeeks.org/",
                "https://www.linkedin.com/",
                "https://www.edx.org/",
                "https://www.coursera.org/courses?query=free"
        );
        InitializeCrawler initializeCrawler = new InitializeCrawler();
        initializeCrawler.seedSet(seedSet);
        int numOfThreads =10;
        Thread threads[] = new Thread[numOfThreads];
        for (int i=0 ; i<numOfThreads ; i++)
        {
            threads[i] = new Thread(initializeCrawler);
            threads[i].start();
        }
        for (int i=0 ; i<numOfThreads ; i++)
        {
            threads[i].join();
        }
        */

        System.out.println("Now Indexing......");
        FindIterable<Document> documents = mdb.getDocuments();
        ArrayList<String> fieldkeys = new ArrayList<String>();
        for (Document doc : documents) {
           String url=doc.get("Url").toString();
            String text = doc.get("html").toString();
            String words[] = Stemming.html2text(text).split(" ");

            wordsList = Stemming.removeStopWords(words);
            wordsList = Stemming.PorterStemming(wordsList);

           FI.put(url,wordsList);
           System.out.println(FI);

        }


    }
}