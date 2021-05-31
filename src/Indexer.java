import java.util.*;
public class Indexer {

    private class webPage {

        private String URL;         // The URL Of the website.
        private int TF;             // The term frequency of the word in the website.
        private int District;       // The position of the word in the website (0 if it's in the body which is the default, other numbers for other districts)

        public void incementTF() {
            TF++;
        }

        public void setDistrict(int D) {
            this.District = D;
        }

        public webPage(String URL, int count) {
            this.URL = URL;
            this.TF = count;
            this.District = 0;
        }
    }
    //Our only data member:
    Map<String, List<webPage>> invertedIndex; ;        //Each entry in the inverted index includes one word and a list of the webpages that contain it (the web space).
    //Note that the term frequency is recorded inside the web page data type, we have the IDF because we have the length of the web space.                                                                         
    public Indexer()
    {
        this.invertedIndex= new HashMap<String, List<webPage>>();
    }
    
    public void Invert( Map<String, List<String>> forwardIndex)  
    {
                                                   
        for (Map.Entry<String, List<String>> FI : forwardIndex.entrySet()) //For each website and its words in the forward index.
        {
        //Map.Entry FI = (Map.Entry) i.next() ;
        String webSite = (String)FI.getKey();                      //Get the website's URL.
        ArrayList<String> Tokens = (ArrayList<String>)FI.getValue();         //Get the list of words in the website.
        for (String s : Tokens)                                     // loop over all words in the website's list of words.
        {
            //String s = s.toLowerCase();

            //Let's see if the word already has an entry (get takes the key which is the word and returns the value which should be the list of webpages)
            List<webPage> webSpace = invertedIndex.get(s);
            if (webSpace != null)                                   //The word already has an existing web space, the website might already be in the web space or might not.
            { 
                boolean isNew = true;                               //Let's assume its a new website (Doesn't exists in the word's web space yet.)
                for (webPage w : webSpace) {
                    if (w.URL.equals(webSite)) 
                    {
                        w.incementTF();                             //The website is already in the web space, just increment the term frequency.
                         isNew= false;                              //The if condition was satisfied hence it isn't new.
                         break;                                     //Job's done, let's see the next word.
                    }
                }

                if (isNew)                          //The word never showed up in the website before (This is the first occurence of the website in the web space of the word.)
                {
                    webSpace.add(new webPage(webSite, 1));
                }

            }

            else                                   //webSpace == null means that it doesn't exist (the word it self has never been observed), so we need to create it.
            {
                webSpace  = new ArrayList<webPage>();     
                webSpace.add(new webPage(webSite, 1));                  //The name of the website and the no. of times the word showed up there (this is the first time, remember).
                invertedIndex.put(s, webSpace);                          //So make a new entry for it (we defined invertedIndex earlier)

            }
        }
    }

        //System.out.println("Just indexed: " + Website );
    }

    public void Search(String input) 
    {
            long start = System.currentTimeMillis();
            ArrayList<String> webSite = new ArrayList<String>();
            ArrayList<Integer> termFrequency = new ArrayList<Integer>();
            //String input = input.toLowerCase();
            int i = 0;
            List<webPage> webSpace = invertedIndex.get(input);
            if (webSpace != null) 
            {
                for (webPage w : webSpace) 
                {   //We can just pring them here if we wish.
                    webSite.add(i, w.URL);
                    termFrequency.add(i, w.TF);
                    i++;
                }

                int resultsFound = 0;
                for (int j = 0; j < i; j++) 
                {
                    System.out.println("URL Name: " + webSite.get(j));
                    System.out.println("Number of Occurences: " + termFrequency.get(j));
                    resultsFound++;
                }
                long end = System.currentTimeMillis();
                System.out.println("About " + resultsFound +" results related to ( " +input+" ), in "+ Long.toString(end-start) + " milliseconds.");
                        }
            else
            {
                System.out.println(" Your search query couldn't be processed, please check your spelling.");
            }

        }





public static void main(String[] args) 
{
    HashMap<String, List<String>> FI = new HashMap<String, List<String>>();           //Sample Forward index.

    ArrayList<String> WordsI = new ArrayList<String>();
    String SI="Annie, are you okay? Will you tell us that you're okay? There's a sound at the window Then he struck you — a crescendo, Annie";
    for (String s : SI.split("\\W+")) 
        WordsI.add(s);

    ArrayList<String> WordsII = new ArrayList<String>();
    String SII="That's why I want to be Free, free like the wind okay blow To fly away just like a sparrow The feeling of letting my hair blow Just take my time wherever I go I 'm in my";
    for (String s : SII.split("\\W+")) 
        WordsII.add(s);

        FI.put("SmoothCriminal.com", WordsI);
        FI.put("Free.org", WordsII);
        Indexer Content=new Indexer();
        Content.Invert(FI);
        Content.Search("okay");

}

}