import com.mongodb.client.FindIterable;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IndexerMain {

        public static void main(String[] args) throws Exception {
            ArrayList<String> wordsList = new ArrayList<String>();
            List<String> seedSet;
            HashMap<String, List<String>> FI = new HashMap<String, List<String>>();
            HashMap<String,String> URL_content = new HashMap<String,String>();
            HashMap<String,String> title = new HashMap<String,String>();
            MongoDB.MongoHandler mdb = new MongoDB.MongoHandler();

            // Connect to MongoDB
            mdb.ConnecttoDB();

            System.out.println("Now Indexing......");
            
            FindIterable<Document> documents = mdb.getDocuments();
            int counter = 0;
            for (Document doc : documents) {
                String url = doc.get("Url").toString();
                String text = doc.get("html").toString();
                title.put(url ,doc.get("title").toString());
                text = Stemming.html2text(text);
                URL_content.put(url , text);
                String words[] = Stemming.cleanContent(text);
                wordsList = Stemming.removeStopWords(words);
                wordsList = Stemming.PorterStemming(wordsList);

                counter++;
                FI.put(url, wordsList);
                System.out.println("[" + counter + "]" + url + "\n");
                if (counter == 100)
                    break;

            }

            System.out.println(FI.size());

            // Inverting the index.

            Indexer c = new Indexer();
            c.Invert(FI);
            c.databaseAction(FI, c.invertedIndex, URL_content, title);

        }




}
