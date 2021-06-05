import java.util.*;

import com.mongodb.client.FindIterable;

import org.bson.Document;

public class main {
    public static void main(String[] args) throws Exception {
        ArrayList<String> wordsList = new ArrayList<String>();
        List<String> seedSet;
        HashMap<String, List<String>> FI = new HashMap<String, List<String>>();
        MongoDB.MongoHandler mdb = new MongoDB.MongoHandler();
        // Connect to MongoDB
        mdb.ConnecttoDB();
        // Get the number of crawled pages from previous run
      /*  int pages = mdb.getPagesNum();
        System.out.println("Now " + pages);
        //if the number of pages >= 5000 or = 0  then the last run was completed successfully
        //So we should start from our seed set
        if (pages >= 5000 || pages == 0) {
            pages = 0;
            seedSet = Arrays.asList(
                    "https://www.quora.com/",
                    "https://www.geeksforgeeks.org/",
                    "https://www.linkedin.com/",
                    "https://www.edx.org/",
                    "https://www.bbc.com/news",
                    "https://www.coursera.org/courses?query=free",
                    "https://en.wikipedia.org/wiki/Main_Page",
                    "https://www.bbc.com/sport",
                    "https://en.khanacademy.org/"

            );
            mdb.dropCollection();
            mdb.InsertList(seedSet);   // add seedset to database
        } else {
            //if the number of pages from last run is less than 5000
            //So the last run was interrupted and we need to complete it
            //So we will retrieve the links from database that the last run stopped at and continue from them
            System.out.println("Starting from " + pages);
            seedSet = mdb.getLinks();
        }
        //send the current number of pages to initialize crawler
        //And also send to them the links that it should start from
        InitializeCrawler initializeCrawler = new InitializeCrawler(pages);
        initializeCrawler.seedSet(seedSet);
        Scanner sc= new Scanner(System.in);
        System.out.print("Choose the number of threads ... : ");
        int numOfThreads = sc.nextInt();
        Thread threads[] = new Thread[numOfThreads];
        for (int i = 0; i < numOfThreads; i++) {
            threads[i] = new Thread(initializeCrawler);
            threads[i].start();
        }
        for (int i = 0; i < numOfThreads; i++) {
            threads[i].join();
        }

*/
        System.out.println("Now Indexing......");
        FindIterable<Document> documents = mdb.getDocuments();
        int counter = 0;
        for (Document doc : documents) {
            String url = doc.get("Url").toString();
            String text = doc.get("html").toString();
            String words[] = Stemming.html2text(text);
            wordsList = Stemming.removeStopWords(words);
            wordsList = Stemming.PorterStemming(wordsList);
            counter++;
            FI.put(url, wordsList);
            System.out.println("[" + counter + "]" + url + "\n");
            if (counter == 1000)
                break;

        }

        System.out.println(FI.size());

        // Inverting the index.

        Indexer c = new Indexer();
        c.Invert(FI);
        c.databaseAction(FI, c.invertedIndex);

    }


    static String stemWord(String word) {
        ArrayList<String> searchMe = new ArrayList<String>();
        searchMe.add(word);
        searchMe = Stemming.PorterStemming(searchMe);
        return searchMe.get(0);
    }

}