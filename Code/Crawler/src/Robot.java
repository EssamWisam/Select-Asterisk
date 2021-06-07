import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class Robot {
    public  static  boolean  RobotAllowed(URL url) throws IOException {
        String host = url.getHost(); //get the host name
        //-------setting up the robot url------//
        String RobotString = url.getProtocol()+"://"+host+
                (url.getPort()>-1?":"+url.getPort():"")+"/robots.txt";
        URL RobotUrl;
        try{
            RobotUrl= new URL(RobotString);
        }
        catch (MalformedURLException e)
        {
            return  false;
        }
        String path= url.getPath();
        System.out.println("Robot : "+RobotString+"  is to be scanned now");
        BufferedReader in;
        //-------visiting and open the robot url------//
        try{
            in = new BufferedReader(
                    new InputStreamReader(RobotUrl.openStream()));
        }
        catch (IOException e)
        {
            return  false;
        }
        String content=new String();
        boolean start_checking= false; //false until i reach user-agent:*
        while ((content = in.readLine()) != null)
        {
            content =content.trim();
            if((!start_checking)&&content.toLowerCase().startsWith("user-agent"))
            {
                int start = content.indexOf(":") + 1;
                int end   = content.length();
                String agent= content.substring(start, end).trim();
                if(agent.equals("*"))
                    start_checking = true;
            }
            else if(start_checking && content.toLowerCase().startsWith("user-agent"))
            {
                //finished User-agent: *
                in.close();
                return  true;
            }
            else if(start_checking && content.toLowerCase().startsWith("disallow")) //if i reached Disallow:
            {

                int start = content.indexOf(":") + 1;
                int end   = content.length();
                String disallowedPath= content.substring(start, end).trim();
                if(disallowedPath.equals("/")) //disallow every thing
                {

                    in.close();
                    return false;
                }
                if(disallowedPath.length()==0)  //Disallow:
                {

                    in.close();
                    return true;
                }
                if(disallowedPath.length()<=path.length())
                {
                    String subPath= path.substring(0, disallowedPath.length());
                    if(subPath.equals(disallowedPath))
                    {
                        in.close();
                        return  false;
                    }
                }
            }
            else if(start_checking && content.toLowerCase().startsWith("allow")) //if i reached allow:
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
