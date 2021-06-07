import com.mongodb.BasicDBObject;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClient;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import java.util.LinkedList;
import java.util.List;


public class MongoDB {
    public static  class MongoHandler
    {
       static MongoCollection collection;
        static MongoDatabase database;
        public  void ConnecttoDB()
        {
            MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));

             mongoClient = new MongoClient();
                 database = mongoClient.getDatabase("Crawler");
                collection = database.getCollection("Crawleing");


        }
        //-------------------------------------------------------------//
        public  void  insert(String url , String html ,String title)
        {
            Document document = new Document()
                    .append("Url", url)
                    .append("html", html)
                    .append("title",title);
            collection.insertOne(document);
        }
        //-------------------------------------------------------------//
        public FindIterable<Document>  getDocuments() {
            return collection.find();
        }
        public void InsertList(List<String> urls)
        {
            MongoCollection state= database.getCollection("State");
           state = database.getCollection("State");
            state.deleteOne(Filters.eq("_id", "Links"));
            state.deleteOne(Filters.eq("_id", "Pages"));
            BasicDBObject doc = new BasicDBObject("_id","Links").append("data", urls);
            state.insertOne(new Document(doc.toMap()));
            doc = new BasicDBObject("_id","Pages").append("num", 0);
            state.insertOne(new Document(doc.toMap()));
        }
        //-------------------------------------------------------------//
        public void UpdateList(LinkedList<String> urls)
        {
            MongoCollection state= database.getCollection("State");
            Document document = new Document("_id","Links");
            BasicDBObject doc = new BasicDBObject("_id","Links").append("data", urls);
            state.replaceOne(document,new Document(doc.toMap()));
        }
        //-------------------------------------------------------------//
        public  void UpdatePagesNum(int crawled)
        {
            MongoCollection state= database.getCollection("State");
            BasicDBObject doc = new BasicDBObject("_id", "Pages").append("num", crawled);
            state.updateOne(Filters.eq("_id", "Pages"), new Document("$set", new Document("num", crawled)));
        }
        //-------------------------------------------------------------//
        public  List<String> getLinks()
        {
            MongoCollection retrieve = database.getCollection("State");
            List<String> linksSet= new LinkedList<String>();
            FindIterable<Document> links= retrieve.find(Filters.eq("_id","Links"));
            for (Document doc : links) {
              linksSet.addAll ((List) doc.get("data"));
            }
            return  linksSet;
        }
        //-------------------------------------------------------------//
        public  int getPagesNum()
        {
            int x=0;
            MongoCollection retrieve = database.getCollection("State");
            FindIterable<Document> links= retrieve.find(Filters.eq("_id","Pages"));
            for (Document doc : links) {
               x=(Integer) doc.get("num");
            }
            return  x;
        }
        //-------------------------------------------------------------//
        public void dropCollection()
        {
            collection.drop();
        }
    }
}
