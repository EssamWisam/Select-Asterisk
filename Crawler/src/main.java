import java.util.Arrays;
import java.util.List;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClient;
import com.mongodb.DBCollection;
public class main {
    public static void main(String[] args) throws Exception
    {
        MongoDB.MongoHandler mdb = new MongoDB.MongoHandler();
        mdb.ConnecttoDB();
        System.out.println(mdb.collection);
        List<String> seedSet = Arrays.asList(
                "https://www.quora.com/",
                "https://www.geeksforgeeks.org/",
                "https://stackoverflow.com/questions",
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

    }
}
