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

    static final int MAX_PAGES_TO_SEARCH = 5000;
    LinkedList<String> URLsFrontier = new LinkedList<String>();
    Set<String> VisitedURLs = new HashSet<String>();
    MongoDB.MongoHandler mdb = new MongoDB.MongoHandler();
    public void seedSet(List<String> seedSet) {
        seed_size = seedSet.size();
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
                boolean done = crawler.Crawel(currentURL);
                if (done) {
                    synchronized (this) {
                        crawled++;
                        mdb.UpdatePagesNum(crawled);
                        if (crawled == 5000)
                            break;
                        URLsFrontier.addAll(crawler.getHyperLinks());
                        mdb.UpdateList(URLsFrontier);
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
            url = "stop";
           do {

               if (URLsFrontier.size() == 0) {
                   if(seed_size==0)
                   {
                       exit(-1);
                   }
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
