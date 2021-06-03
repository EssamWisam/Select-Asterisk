import java.util.*;


public class main {
    public static void main(String[] args) throws Exception {
        ArrayList<String> wordsList = new ArrayList<String>();
        List<String> seedSet;
        HashMap<String, ArrayList<String>> FI = new HashMap<String, ArrayList<String>>();
        MongoDB.MongoHandler mdb = new MongoDB.MongoHandler();
        mdb.ConnecttoDB();
        int pages = mdb.getPagesNum();
        System.out.println("Now "+pages);
        if(pages >=5000|| pages==0) {
            pages=0;
            seedSet = Arrays.asList(
                    "https://www.quora.com/",
                    "https://www.geeksforgeeks.org/",
                    "https://www.linkedin.com/",
                    "https://www.edx.org/",
                    "https://www.coursera.org/courses?query=free"
            );
            mdb.InsertList(seedSet);
        }
        else
        {
            System.out.println("Starting from "+pages);
            seedSet =mdb.getLinks();
        }

        InitializeCrawler initializeCrawler = new InitializeCrawler(pages);
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

/*
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
*/

    }
}