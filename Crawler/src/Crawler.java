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
    public boolean Crawel(String url)
    {
        MongoDB.MongoHandler mdb = new MongoDB.MongoHandler();

        try{
            Connection connection = Jsoup.connect(url);
            document = connection.get();
            if(connection.response().statusCode() == 200)
            {
                System.out.printf("Done retrieving from %s..\n",url);
               mdb.insert(url,document.outerHtml());
                Elements hyperLinkes = document.select("a[href]");
                for(Element link : hyperLinkes)
                {
                    this.links.add(link.absUrl("href"));
                }
            }
            else
            {
                System.out.printf("Failed to retrieve from %s..\n",url);
                return false;
            }

        }catch (IOException ioe)
        {
            System.out.println("Failed to make http request");
            System.out.println(ioe.toString());
            return  false;
        }
        return  true;
    }
    public List<String> getHyperLinks()
    {
        return this.links;
    }
}
