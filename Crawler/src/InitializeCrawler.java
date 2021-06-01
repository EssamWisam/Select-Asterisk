import java.util.*;

public class InitializeCrawler implements  Runnable
{
    int crawled=0;
    static final int MAX_PAGES_TO_SEARCH = 5000;
    LinkedList<String> URLsFrontier = new LinkedList<String>();
    Set<String> VisitedURLs = new HashSet<String>();
    MongoDB.MongoHandler mdb = new MongoDB.MongoHandler();
    public void seedSet(List<String> seedSet) {
        for (int i = 0; i < seedSet.size(); i++) {
            URLsFrontier.add(seedSet.get(i));
        }
    }

    public void run()
    {
        System.out.println("ana"+ Thread.currentThread());
        String currentURL;
        while(crawled<= MAX_PAGES_TO_SEARCH)
        {
            currentURL = getURl();
            if(currentURL.compareTo("stop")==0)
            {
                System.out.println("URLsFrontier finished");
                break;
            }

            Crawler crawler = new Crawler();
            boolean done = crawler.Crawel(currentURL);
            if(done)
            {
                synchronized (this) {
                    crawled++;
                    if(crawled == 5000)
                        break;
                    URLsFrontier.addAll(crawler.getHyperLinks());
                    notifyAll();
                }
                System.out.printf("Crawled Websites Successfully: %d  \n",crawled);
            }


        }
        if(Thread.interrupted())
        {
            System.out.println("A7aaa ana et2at3t");
            mdb.insertInterrupt(URLsFrontier);
        }
    }

   public String getURl()
   {
       String url;
       synchronized (this) {
            url = "stop";
           do {
               if (URLsFrontier.size() == 0) {
                   {
                       try {
                           wait();
                       } catch (InterruptedException e) {
                           e.printStackTrace();
                       }
                   }
               }
               url = URLsFrontier.remove(0);
           } while (url.isEmpty() || VisitedURLs.contains(url));
           VisitedURLs.add(url);
       }

       return  url;
   }
}
