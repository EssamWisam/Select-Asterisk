import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Crawler {
    private List<String> links = new LinkedList<String>();
    private Document document;

    public boolean Crawel(String url) {
        //Initialize MongoDB
        MongoDB.MongoHandler mdb = new MongoDB.MongoHandler();

        try {
            //Try to connect to Url using Jsoup
            Connection connection = Jsoup.connect(url);
            //Get the Page document
            document = connection.get();
            if (connection.response().statusCode() == 200) {
                String []type = connection.response().contentType().split(";");
                if(type[0].compareTo("text/html")!=0)
                {
                    System.out.println("not HTML page...");
                    return false;
                }
                //If connection made successfully store the html code of the page to mongoDB
                System.out.printf("Done retrieving from %s..\n", url);
                mdb.insert(url, document.outerHtml());
                //Extract All hyperlinks from the Page
                Elements hyperLinkes = document.select("a[href]");
                //Store them to a list
                for (Element link : hyperLinkes) {
                    this.links.add(link.absUrl("href"));
                }
            } else {
                System.out.printf("Failed to retrieve from %s..\n", url);
                return false;
            }

        } catch (IOException ioe) {
            System.out.println("Failed to make http request");
            System.out.println(ioe.toString());
            return false;
        }
        return true;
    }
    //Function to get hyperlinks from the page
    public List<String> getHyperLinks() {
        return this.links;
    }
}
