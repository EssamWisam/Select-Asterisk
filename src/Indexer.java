import java.util.*;
import java.sql.*;
import java.lang.Math;

public class Indexer {

    public class webPage {

        private String URL; // The URL Of the website.
        private int TF; // The term frequency of the word in the website.
        // private int District; // The position of the word in the website (0 if it's
        // in the body which is the default, other numbers for other districts)

        public void incementTF() {
            TF++;
        }

        // public void setDistrict(int D) {
        // this.District = D;
        // }

        public webPage(String URL, int count) {
            this.URL = URL;
            this.TF = count;
            // this.District = 0;
        }
    }

    // Our only data member:
    public Map<String, List<webPage>> invertedIndex; // Each entry in the inverted index includes one word and a list of
                                                     // the webpages that contain it (the web space).
    // Note that the term frequency is recorded inside the web page data type, we
    // have the IDF because we have the length of the web space.

    public Indexer() {
        this.invertedIndex = new HashMap<String, List<webPage>>();
    }

    public void Invert(Map<String, List<String>> forwardIndex) {
        int count = 0;

        for (Map.Entry<String, List<String>> FI : forwardIndex.entrySet()) // For each website and its words in the
                                                                           // forward index.
        {
            count++;
            // Map.Entry FI = (Map.Entry) i.next() ;
            String webSite = (String) FI.getKey(); // Get the website's URL.
            System.out.println("\n" + "Done Url " + "[" + count + "] :" + webSite + "\n");
            ArrayList<String> Tokens = (ArrayList<String>) FI.getValue(); // Get the list of words in the website.
            for (String s : Tokens) // loop over all words in the website's list of words.
            {

                // Let's see if the word already has an entry (get takes the key which is the
                // word and returns the value which should be the list of webpages)
                List<webPage> webSpace = invertedIndex.get(s);
                if (webSpace != null) // The word already has an existing web space, the website might already be in
                                      // the web space or might not.
                {
                    boolean isNew = true; // Let's assume its a new website (Doesn't exists in the word's web space
                                          // yet.)
                    for (webPage w : webSpace) {
                        if (w.URL.equals(webSite)) {
                            w.incementTF(); // The website is already in the web space, just increment the term
                                            // frequency.
                            isNew = false; // The if condition was satisfied hence it isn't new.
                            break; // Job's done, let's see the next word.
                        }
                    }

                    if (isNew) // The word never showed up in the website before (This is the first occurence
                               // of the website in the web space of the word.)
                    {
                        webSpace.add(new webPage(webSite, 1));
                    }

                }

                else // webSpace == null means that it doesn't exist (the word it self has never been
                     // observed), so we need to create it.
                {
                    webSpace = new ArrayList<webPage>();
                    webSpace.add(new webPage(webSite, 1)); // The name of the website and the no. of times the word
                                                           // showed up there (this is the first time, remember).
                    invertedIndex.put(s, webSpace); // So make a new entry for it (we defined invertedIndex earlier)

                }
            }
        }

        // System.out.println("Just indexed: " + Website );
    }

    public void Search(String input) {
        long start = System.currentTimeMillis();
        ArrayList<String> webSite = new ArrayList<String>();
        ArrayList<Integer> termFrequency = new ArrayList<Integer>();
        int i = 0;
        List<webPage> webSpace = invertedIndex.get(input);
        if (webSpace != null) {
            for (webPage w : webSpace) { // We can just pring them here if we wish.
                webSite.add(i, w.URL);
                termFrequency.add(i, w.TF);
                i++;
            }

            int resultsFound = 0;
            for (int j = 0; j < i; j++) {
                System.out.println("URL Name: " + webSite.get(j));
                System.out.println("Number of Occurences: " + termFrequency.get(j));
                resultsFound++;
            }
            long end = System.currentTimeMillis();
            System.out.println("About " + resultsFound + " results related to ( " + input + " ), in "
                    + Long.toString(end - start) + " milliseconds.");
        } else {
            System.out.println(" Your search query couldn't be processed, please check your spelling.");
        }

    }

    public  void databaseAction(HashMap<String, List<String>> forwardIndex, Map<String, List<webPage>> invertedIndex)
            throws Exception {
        // Database connection.
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection Con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Indexer", "root", "@23198631yousif@");
        // Population queries
        String populateWords = " insert into Words (WORD)" + " values (?)";
        String populateWebsites = " insert into Webpages (URL)" + " values (?)";
        String populateRelation = " insert into AppearsIn ()" + " values (?,?,?,?)";
        PreparedStatement wordQuery = Con.prepareStatement(populateWords);
        PreparedStatement websiteQuery = Con.prepareStatement(populateWebsites);
        PreparedStatement relationQuery = Con.prepareStatement(populateRelation);

        // Executing the queries to populate the database.
        for (Map.Entry<String, List<String>> FI : forwardIndex.entrySet()) {
            websiteQuery.setString(1, (String) FI.getKey());
            try {
                websiteQuery.execute();
            } catch (Exception e) {
                System.out.println((String) FI.getKey() + "Is already here.");
            }

        }

        for (Map.Entry<String, List<webPage>> II : invertedIndex.entrySet()) {
            String Word =  II.getKey();
            wordQuery.setString(1, Word);

            try {
                wordQuery.execute();
            } catch (Exception e) {
                System.out.println( Word + "Is already here.");
            }

            ArrayList<webPage> webPages = (ArrayList<webPage>) II.getValue(); // Get the list of words in the website.
            for (webPage w : webPages) {
                relationQuery.setString(1, Word);
                relationQuery.setString(2, w.URL);
                relationQuery.setInt(3, w.TF);
                relationQuery.setFloat(4, (float) Math.log(5000/webPages.size()));

                try {
                    relationQuery.execute();
                } catch (Exception e) {
                    System.out.println("It's prior knowledge.");
                }

            }
        }

        // Printing the invertex index:
        Statement test = Con.createStatement();
        // CREATE VIEW ForwardIndex as select URL, Word from appearsin order by URL ASC;
        // CREATE VIEW InvertedIndex as select Word, Url, TF from appearsin order by
        // word asc;
        ResultSet entry = test.executeQuery("select * from Appearsin");
        while (entry.next())
            System.out.println("The word " + entry.getString(1) + " appears in " + entry.getString(2) + " "
                    + entry.getInt(3) + ((entry.getInt(3) == 1) ? " time." : " times.")+" with IDF "+entry.getFloat(4));

        //DBSearch("Okay");

        Con.close();
    }

    // Searching:
    public static void DBSearch(String Input) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection Con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Indexer", "root", "0110084949");
        Statement search = Con.createStatement();
        long start = System.currentTimeMillis();
        ResultSet entry = search
                .executeQuery("select URL from InvertedIndex where InvertedIndex.Word = '" + Input + "';");
        long end = System.currentTimeMillis();

        int count = 0;
        while (entry.next()) {
            System.out.println(entry.getString(1));
            count = count + 1;
        }
        System.out.println("About " + count + " results related to ( " + Input + " ), in " + Long.toString(end - start)
                + " milliseconds.");

    }

}