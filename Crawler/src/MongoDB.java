import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClient;
import com.mongodb.DBCollection;

import org.bson.BsonArray;
import org.bson.Document;

import java.util.LinkedList;
import java.util.List;


public class MongoDB {
    public static  class MongoHandler
    {
       static MongoCollection collection;
        static MongoClient mongoClient;
        static MongoDatabase database;
        public  void ConnecttoDB()
        {
            MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
            /*MongoClientURI uri = new MongoClientURI(
                    "mongodb+srv://WebCrawler:apt2021@cluster0.dcrhg.mongodb.net/test?retryWrites=true&w=majority"
            );*/
             mongoClient = new MongoClient();
                 database = mongoClient.getDatabase("Crawler");
                collection = database.getCollection("Crawleing");


        }
        public  void  insert(String url , String html)
        {
            Document document = new Document()
                    .append("Url", url)
                    .append("html", html);
            collection.insertOne(document);
        }
        public FindIterable<Document> getDocuments() {
            return collection.find();
        }
    }
}
