import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static java.lang.System.exit;

public class InitializeCrawler implements  Runnable
{
    int crawled=0;
    int seed_size=0;
    InitializeCrawler(int x)
    {
        crawled =x;
    }
    //Max number of pages to be crawled
    static final int MAX_PAGES_TO_SEARCH = 5000;
    //Queue to store all the links and hyperlinks
    LinkedList<String> URLsFrontier = new LinkedList<String>();
    //Hashset to store the visited Urls in order to not to visit them again
    Set<String> VisitedURLs = new HashSet<String>();
    //Initialize MongoDB to store the Urls that are in URLsForntier to database to know crawler state
    MongoDB.MongoHandler mdb = new MongoDB.MongoHandler();
    //Function to set the UrlForntier with the SeedSet
    public void seedSet(List<String> seedSet) {
        seed_size = seedSet.size();
        for (int i = 0; i < seedSet.size(); i++) {
            URLsFrontier.add(seedSet.get(i));
        }
    }

    public void run()
    {
        String currentURL;
        while(crawled<= MAX_PAGES_TO_SEARCH)
        {
            //Get the url from URls Queue (UrlForntier) and this should be synchronized to avoid race conditons
            currentURL = getURl();
            //here checking the robots.txt and if false DO NOT CRAWL (donot do the rest od the code and print that this website is disallowed
            boolean robotSafe=false;
            try {
                robotSafe=RobotAllowed(new URL(currentURL));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(robotSafe) {
                Crawler crawler = new Crawler();
                //If the link was robot safe .. send it to crawling class
                boolean done = crawler.Crawel(currentURL);
                if (done) {
                    //if the link is successfully crawled without any errors in connection
                    //increase the number of crawled pages by one
                    //And add the hyperlinks to UrlForntier
                    //This block should be synchronized to avoid race conditions
                    synchronized (this) {
                        if (crawled == 5000)
                            break;
                        crawled++;
                        //Store the number of pages to database
                        mdb.UpdatePagesNum(crawled);
                        //Add hyperlinks
                        URLsFrontier.addAll(crawler.getHyperLinks());
                        //Store the unvisited links to database
                        if(URLsFrontier.size() <=5000)
                        mdb.UpdateList(URLsFrontier);
                        else
                        {
                            LinkedList<String> UrlsTruncated = URLsFrontier;
                            UrlsTruncated.subList(5001,UrlsTruncated.size()).clear();
                            mdb.UpdateList(UrlsTruncated);
                        }

                        //notify all threads that a new links are added if anyone was waiting
                        notifyAll();
                    }
                    System.out.printf("Crawled Websites Successfully: %d  \n", crawled);
                }
            }
            else
            {
                synchronized (this){
                    seed_size--;
                }
                System.out.println("ROBOT  ALERT !!:" +currentURL +"is disallowed by robot.txt");

            }


        }
       
    }

   public String getURl()
   {
       String url;
       synchronized (this) {
            url = "";
           do {
               if (URLsFrontier.size() == 0) {
                   //If Queue is empty and seed_Size equal to zero this means that all seedset links
                   //Blocks the crawler from crawling so there is no hyperlinks
                   //So all threads should be terminated
                   if(seed_size==0)
                   {
                       exit(-1);
                   }
                   else
                   {
                       //else the thread should wait and release the lock until any hyperlinks added to Queue
                       try {
                           wait();
                       } catch (InterruptedException e) {
                           e.printStackTrace();
                       }
                   }
               }
               //Get the urls from queue and delete it
               url = URLsFrontier.remove(0);
               // if the links was empty string or already crawled before , skip it and take another one
           } while (url.isEmpty() || VisitedURLs.contains(url));
           //When we take a link from the queue we add them to hashset in order to not to crawle it again
           VisitedURLs.add(url);
       }

       return  url;
   }

    public  static  boolean  RobotAllowed(URL url) throws IOException {
        String host = url.getHost();
        /*
        System.out.println(url.getProtocol());
        System.out.println(url.getPath());
        */

        String RobotString = url.getProtocol()+"://"+host+
                (url.getPort()>-1?":"+url.getPort():"")+"/robots.txt";
        URL RobotUrl;
        try{
            RobotUrl= new URL(RobotString);
        }
        catch (MalformedURLException e)
        {
            //System.out.println("Mesh Merta7lak");
            return  false;
        }
        String path= url.getPath();
        System.out.println("Robot : "+RobotString+"  is to be scanned now");
        BufferedReader in;
        try{
            in = new BufferedReader(
                    new InputStreamReader(RobotUrl.openStream()));
        }
        catch (IOException e)
        {
            //System.out.println("Mesh 3aref a2ra l robot txt, samy allah w crawl w nta w 7zak");
            return  false;
        }
        String content=new String();
        boolean start_checking= false;
        while ((content = in.readLine()) != null)
        {
            //System.out.println(host+" "+content);
            content =content.trim();
            if((!start_checking)&&content.toLowerCase().startsWith("user-agent"))
            {
                int start = content.indexOf(":") + 1;
                int end   = content.length();
                String agent= content.substring(start, end).trim();
                if(agent.equals("*"))
                    start_checking = true;
                //System.out.println("1: "+agent);
            }
            else if(start_checking && content.toLowerCase().startsWith("user-agent"))
            {
                //finished User-agent: *
                in.close();
                //System.out.println("2: allowed b3d ma 5alast kolooo\n\n\n");
                return  true;
            }
            else if(start_checking && content.toLowerCase().startsWith("disallow"))
            {
                int start = content.indexOf(":") + 1;
                int end   = content.length();
                String disallowedPath= content.substring(start, end).trim();
                if(disallowedPath.equals("/")) //disallow every thing
                {
                    //System.out.println("Disallowd Because of /");
                    in.close();
                    return false;
                }
                if(disallowedPath.length()==0)  //Disallow:
                {
                    //System.out.println("allowd Because of null");
                    in.close();
                    return true;
                }
                //System.out.println("Disallowed Path: "+disallowedPath);

                if(disallowedPath.length()<=path.length())
                {
                    String subPath= path.substring(0, disallowedPath.length());
                    //System.out.println("subPath: "+subPath);
                    if(subPath.equals(disallowedPath))
                    {
                        in.close();
                        return  false;
                    }
                }
            }
            else if(start_checking && content.toLowerCase().startsWith("allow"))
            {
                int start = content.indexOf(":") + 1;
                int end   = content.length();
                String allowedPath= content.substring(start, end).trim();
                if(allowedPath.equals("/")) //disallow every thing
                {
                    in.close();
                    return true;
                }
                if(allowedPath.length()==0) //allow no thing, Allow:
                {
                    in.close();
                    return false;
                }

            }
        }
        in.close();
        return  true;
    }
}
