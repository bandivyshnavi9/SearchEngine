/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.contacteditor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Character.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map.Entry;
import java.util.*;

/**
 *
 * @author bandi vyshnavi
 */
class terms {

    public String Title;
    public static final HashMap<String, Integer> dfr = new HashMap<>();

}

public class Sgml {

    public static int words = 0, maxValueInMap = 0;
    public static terms T = new terms();
    public static Wordcount2 wor1 = new Wordcount2();
    public static HashMap<String, Integer> tok = new HashMap<>();
    public static HashMap<Integer, HashMap<Integer, Double>> sortDocQue = new HashMap<>();
    public static HashMap<Integer, String> docname = new HashMap<Integer, String>();
    public static HashMap<Integer, HashMap<String, Integer>> tokfid = new HashMap<>();
    public static HashMap<Integer, HashMap<String, Double>> idf = new HashMap<>();
    public static HashMap<Integer, HashMap<String, Double>> idf1 = new HashMap<>();//idf
    public static HashMap<Integer, HashMap<String, Double>> tedf = new HashMap<>(); //term frequency
    public static HashMap<Integer, HashMap<String, Double>> wtd = new HashMap<>();//tf-idf
    public static HashMap<String, Double> tdf = new HashMap<>();

    public static void findRelevantDocs(String queryString, boolean abcd) throws FileNotFoundException, IOException {
        if (abcd) {

            File folder = new File("Resources/downloadedFiles");
            File[] listOfFiles = folder.listFiles();
            int k = listOfFiles.length;
            System.out.println("total no of documents " + k);
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    // TODO code application logic hereString data = null;
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String filePath = file.getAbsolutePath();
                    String content;// = new Scanner(new File(filePath)).useDelimiter("\\Z").next();
                    content = new String(Files.readAllBytes(Paths.get(filePath)));
                    String[] fileContent = content.split("#vyshnavi#\n\n");
                    if (fileContent.length != 4) {
                        continue;
                    }
                    //System.out.println(content);
                    StringBuilder builder = new StringBuilder();
                    String data;
                    while ((data = br.readLine()) != null) {
                        builder.append(data).append("\n");
                    }
                    //  System.out.println("builder");
                    // System.out.println(builder);

                    T.Title = fileContent[2];
//System.out.println(T.Title);
                    //String replace = builder.toString().replaceAll("(?s)<AUTHOR>(.*?)</AUTHOR>", "").replaceAll("(?s)<BIBLIO>(.*?)</BIBLIO>", "").replaceAll("(?s)<TITLE>(.*?)</TITLE>", "");

                    //System.out.println(replace);
                    //String data1 = replace.replaceAll("<.*?>", "");
                    String data1 = fileContent[2] + " " + fileContent[2] + " " + fileContent[2] + " " + fileContent[2] + " " + fileContent[2] + " " + fileContent[2] + " " + fileContent[2] + " " + fileContent[2] + " " + fileContent[2] + " " + fileContent[2] + " " + fileContent[3];
                    tok = wor1.frequency(data1);
                    //System.out.println(tok+"\n\n\n"); //token and termfrequency

                    //System.out.println(data1);
                    List<String> charList = new ArrayList<String>();
                    for (int i = 0; i <= 5; i++) {
                        char c = data1.charAt(i);
                        if (isDigit(c)) {
                            charList.add(Character.toString(data1.charAt(i)));
                        } else {

                        }
                    }
                    //System.out.println(String.join("",charList));  //document number
                    //int number = Integer.parseInt(String.join("", charList));
                    int number = Integer.parseInt(fileContent[0]);
                    //System.out.println(number);
                    docname.put(number, T.Title);
                    tokfid.put(number, tok);
                    int c;
                    if (tok.values().isEmpty()) {
                        maxValueInMap = 0;
                        continue;
                    }
                    maxValueInMap = (Collections.max(tok.values()));  // This will return max value in the Hashmap

// System.out.println("MAx value in doc"+number+" "+maxValueInMap);
                    //System.out.println(tok);
// System.out.println(tok.values());
                    int i = 0;
                }
            }

            double d = 0.0;
            int doc = 0;
            for (Map.Entry<Integer, HashMap<String, Integer>> entry : tokfid.entrySet()) //termfrequency
            {
                HashMap<String, Double> ee = new HashMap<>();
                //System.out.println("Document: "+(++doc));
                if (entry.getValue().isEmpty()) {
                    continue;
                }
                double maxValue = (Collections.max(entry.getValue().values()));
                for (String e : entry.getValue().keySet()) {
                    d = (entry.getValue().get(e) / maxValue);
                    ee.put(e, d);
                    //System.out.println(entry.getValue().get(e)+"/"+maxValue+": "+d);
                }
                tedf.put(entry.getKey(), ee);
            }
//System.out.println("term frequency");
/*for(Map.Entry<Integer,HashMap<String,Double>> entry:tedf.entrySet())
{
    HashMap<String, Double> k1 = entry.getValue();
    System.out.println("\n\n"+entry.getKey());
    for(String s1:k1.keySet())
        System.out.println(s1+": "+entry.getValue().get(s1));
}*/

            for (Map.Entry<Integer, HashMap<String, Integer>> entry : tokfid.entrySet()) //inverse freq
            {
                for (String e : entry.getValue().keySet()) {
                    if (tdf.get(e) == null) {
                        tdf.put(e, 1.0);
                    } else {
                        tdf.replace(e, tdf.get(e), tdf.get(e) + 1.0);
                    }
                }
            }
//System.out.println("document frequency:\n");
//System.out.println(tdf);

            int i = 1;
            /*
               Set set2=tokfid.entrySet();
               Iterator ite=set2.iterator();
               while(ite.hasNext()){
                    Map.Entry gm=(Map.Entry)ite.next();
                    System.out.println("docnumber is:"+gm.getKey()+"Tokens are:");
                    System.out.println(gm.getValue());
                   
               }*/
//System.out.println("weighted  ");

            for (Map.Entry<Integer, HashMap<String, Double>> entry : tedf.entrySet()) {
                //System.out.println("\n\nDocument "+i+":\n\n");
                HashMap<String, Double> m1 = new HashMap<>();
                for (Map.Entry<String, Double> e1 : entry.getValue().entrySet()) {
                    //double df=idf1.get(entry.getKey()).get(e1.getKey());
                    double df = Math.log(1400 / tdf.get(e1.getKey())) / Math.log(2.0);
                    double tf = e1.getValue();
                    double tf_idf = tf * df;
                    m1.put(e1.getKey(), tf_idf);
                    // System.out.println(tf_idf);
                }
                wtd.put(entry.getKey(), m1);
            }
            /*
        for (Map.Entry<Integer, HashMap<String, Double>> entry : wtd.entrySet()) {
            for (Map.Entry<String, Double> e1 : entry.getValue().entrySet());
        }
        
        System.out.println("\n\nTF-IDF\n\n");
        for (Map.Entry<Integer, HashMap<String, Double>> entry : wtd.entrySet()) {
            System.out.println("\n\n Document number:" + entry.getKey() + ":\n");
            for (Map.Entry<String, Double> e1 : entry.getValue().entrySet()) {
                System.out.println(e1.getKey() + "===" + e1.getValue());
            }
        }*/
//System.out.println(e1.getKey()+": "+e1.getValue());
            //i++;

            Set set1 = docname.entrySet(); //title and docnumber
            Iterator iterator = set1.iterator();
            while (iterator.hasNext()) {
                Map.Entry mentry = (Map.Entry) iterator.next();
                //System.out.print("docnumber is: "+ mentry.getKey() + " Title is: ");
                //System.out.println(mentry.getValue());
            }
        } else {
            /* File f = new File("C:\\Users\\bandi\\OneDrive\\Documents\\NetBeansProjects\\sgml\\src\\sgml\\queries.txt");
        BufferedReader br = new BufferedReader(new FileReader(f));
        StringBuilder builder = new StringBuilder();*/
            String data;
            int c = 0;
            int maxValue = 0;
            HashMap<String, Integer> qt = new HashMap<>();
            HashMap<Integer, HashMap<String, Integer>> qtf = new HashMap<>();
            HashMap<Integer, HashMap<String, Double>> qqt = new HashMap<>();
            HashMap<Integer, HashMap<String, Double>> qwt = new HashMap<>();
            //while ((data = br.readLine()) != null) {

            data = queryString;
            c++;
            qt.clear();
            //  System.out.println(data);
            qt = wor1.frequency(data);
            // maxValue=(Collections.max(qt.values()));
            // System.out.println(maxValue);
            qtf.put(c, wor1.frequency(data));
            // System.out.println(qt+"\n\n");
            //}
            //System.out.println("query frequency "+qtf+"\n\n");
            double d = 0;
            for (Map.Entry<Integer, HashMap<String, Integer>> entry : qtf.entrySet()) //termfrequency
            {
                maxValue = Collections.max(entry.getValue().values());
                HashMap<String, Double> h1 = new HashMap<>();

                for (String e : entry.getValue().keySet()) {
                    d = (entry.getValue().get(e) / (double) maxValue);
                    h1.put(e, d);

                }
                qqt.put(entry.getKey(), h1);
            }
            /*
   for(Map.Entry<Integer,HashMap<String,Double>> entry:qqt.entrySet()) //normalized term frequency
{
   System.out.println("\n\n query:"+entry.getKey()+":\n");
    for(Map.Entry<String,Double> e1:entry.getValue().entrySet())
    {
    System.out.println(e1.getKey()+"==="+e1.getValue());
    }
}*/

            for (Map.Entry<Integer, HashMap<String, Double>> entry : qqt.entrySet()) {
                // System.out.println("\nQuery: "+entry.getKey()+"\n");
                HashMap<String, Double> m1 = new HashMap<>();
                for (Map.Entry<String, Double> e1 : entry.getValue().entrySet()) {
                    //double df=idf1.get(entry.getKey()).get(e1.getKey());
                    if (tdf.get(e1.getKey()) != null) {
                        double df = Math.log(1400 / tdf.get(e1.getKey())) / Math.log(2.0);
                        double tf = e1.getValue();
                        //        System.out.println(e1.getKey()+": "+tf);
                        double tf_idf = tf * df;
                        m1.put(e1.getKey(), tf_idf);
                        //  System.out.println(tdf.get(e1.getKey()));
                    }
                }
                qwt.put(entry.getKey(), m1);
            }

            /* for(Map.Entry<Integer,HashMap<String,Double>> entry:qwt.entrySet()) //query weights
{
  System.out.println("\n\n query:"+entry.getKey()+":\n");
    for(Map.Entry<String,Double> e1:entry.getValue().entrySet())
    {
    System.out.println(e1.getKey()+"==="+e1.getValue());
    }
}*/
            HashMap<Integer, HashMap<Integer, Double>> cs = new HashMap<>();

            for (Map.Entry<Integer, HashMap<String, Double>> entry1 : qwt.entrySet()) {
                HashMap<String, Double> Q = entry1.getValue();
                HashMap<Integer, Double> c1 = new HashMap<>();

                double modQ = modf(Q);
                for (Map.Entry<Integer, HashMap<String, Double>> entry : wtd.entrySet()) {
                    HashMap<String, Double> D = entry.getValue();

                    double modD = modf(D);
                    double cossim = 0.0;

                    double k1 = 0.0;
                    for (Map.Entry<String, Double> e2 : entry1.getValue().entrySet()) {
                        if (D.get(e2.getKey()) != null) {
                            k1 += e2.getValue() * entry.getValue().get(e2.getKey());
                            cossim = k1 / (modD * modQ);
                        }
                        c1.put(entry.getKey(), cossim);
                    }
                    cs.put(entry1.getKey(), c1);
                }
            }

            for (Map.Entry<Integer, HashMap<Integer, Double>> entry : cs.entrySet()) {
                sortDocQue.put(entry.getKey(), sortByValues(entry.getValue()));
            }

            // System.out.println("\n\n Query_id and Document_id pair\n\n");
            PrintWriter writer = null;
            writer = new PrintWriter("Resources/searchResults.txt", "UTF-8");
            int noOfResults = 0;
            for (Map.Entry<Integer, HashMap<Integer, Double>> entry : sortDocQue.entrySet()) //sort cosine sim print
            {
                //System.out.println("\n\n query num:"+entry.getKey()+":\n");
                for (Map.Entry<Integer, Double> e1 : entry.getValue().entrySet()) {
                    System.out.println("(" + entry.getKey() + "," + e1.getKey() + ")");//+"==="+e1.getValue());"document number:"+
                    String content1;// = new Scanner(new File("Resources/downloadedFiles/" + e1.getKey() + ".txt")).useDelimiter("\\Z").next();
                    content1 = new String(Files.readAllBytes(Paths.get("Resources/downloadedFiles/" + e1.getKey() + ".txt")));
                    String[] fileContent1 = content1.split("#vyshnavi#\n\n");
                    writer.println(fileContent1[1] + "#vyshnavi#" + fileContent1[2]);
                    if (++noOfResults >= 50) {
                        break;
                    }
                }
            }
            writer.close();
            /*Scanner s = new Scanner(new File("./src/my/contacteditor/relevance.txt"));
            int in = 0, nu = 0;
            int q = 0;
            HashMap<Integer, LinkedList<Integer>> m = new HashMap<>();

            while (s.hasNextLine()) {
                String li = s.nextLine();
                String[] de = li.split(" ");
                q = Integer.parseInt(de[0]);
                nu = Integer.parseInt(de[1]);
                if (m.get(q) == null) {
                    LinkedList<Integer> l = new LinkedList<>();
                    l.add(nu);
                    m.put(q, l);
                } else {
                    LinkedList<Integer> a1 = m.get(q);
                    a1.add(nu);
                    m.replace(q, a1);
                }
            }*/

            //System.out.println("Average Precession Values: \n\n");
            /*double p1 = 0.0;
            for (Map.Entry<Integer, HashMap<Integer, Double>> e : sortDocQue.entrySet()) {
                p1 += pr(10, e.getValue(), m.get(e.getKey()));

            }
            //System.out.println("Documents: 10\t" + "average precision:" + p1 / 10);

            double p2 = 0.0;
            for (Map.Entry<Integer, HashMap<Integer, Double>> e : sortDocQue.entrySet()) {
                p2 += pr(50, e.getValue(), m.get(e.getKey()));

            }
            //System.out.println("Documents: 50\t" + " precision:" + p2 / 10);

            double p3 = 0.0;
            for (Map.Entry<Integer, HashMap<Integer, Double>> e : sortDocQue.entrySet()) {
                p3 += pr(100, e.getValue(), m.get(e.getKey()));

            }
            //System.out.println("Documents: 100\t " + " precision:" + p3 / 10);

            double p4 = 0.0;
            for (Map.Entry<Integer, HashMap<Integer, Double>> e : sortDocQue.entrySet()) {
                p4 += pr(500, e.getValue(), m.get(e.getKey()));

            }
            //System.out.println("Documents: 500\t" + " precision:" + p4 / 10);

            //System.out.println("\n\n Average Recall Values: \n\n");
            double r1 = 0.0;
            for (Map.Entry<Integer, HashMap<Integer, Double>> e : sortDocQue.entrySet()) {
                r1 += re(10, e.getValue(), m.get(e.getKey()));

            }
            System.out.println("Documents: 10\t " + " recall:" + r1 / 10);

            double r2 = 0.0;
            for (Map.Entry<Integer, HashMap<Integer, Double>> e : sortDocQue.entrySet()) {
                r2 += re(50, e.getValue(), m.get(e.getKey()));

            }
            //System.out.println("Documents: 50\t" + " recall:" + r2 / 10);

            double r3 = 0.0;
            for (Map.Entry<Integer, HashMap<Integer, Double>> e : sortDocQue.entrySet()) {
                r3 += re(100, e.getValue(), m.get(e.getKey()));

            }
            //System.out.println("Documents: 100\t" + " recall:" + r3 / 10);

            double r4 = 0.0;
            for (Map.Entry<Integer, HashMap<Integer, Double>> e : sortDocQue.entrySet()) {
                r4 += re(500, e.getValue(), m.get(e.getKey()));

            }*/
        }
        // System.out.println("Documents: 500\t" + " recall:" + r4 / 10);
    }

    public static double pr(int n, HashMap<Integer, Double> m1, LinkedList<Integer> a) //precision
    {
        double p = 0.0;
        double c = 0;
        int i = 0;
        HashMap<Integer, Double> e = new HashMap<>();
        for (Map.Entry<Integer, Double> e1 : m1.entrySet()) {
            i++;
            if (a.contains(e1.getKey())) {
                c++;
            }
            if (i >= n) {
                break;
            }
        }
        //System.out.println(c+": "+n+": "+c/n);
        p = c / n;
        return p;
    }

    public static double re(int n, HashMap<Integer, Double> m1, LinkedList<Integer> a) //precision
    {
        double p = 0.0;
        double c = 0;
        int i = 0;
        HashMap<Integer, Double> e = new HashMap<>();
        for (Map.Entry<Integer, Double> e1 : m1.entrySet()) {
            i++;
            if (a.contains(e1.getKey())) {
                c++;
            }
            if (i >= n) {
                break;
            }
        }
        //System.out.println(c+": "+n+": "+c/n);
        p = c / (double) a.size();
        return p;
    }

    public static double modf(HashMap<String, Double> m1) {
        double k2 = 0.0;
        for (Map.Entry<String, Double> e : m1.entrySet()) {
            if (e.getKey() != null) {
                k2 += Math.pow(e.getValue(), 2);
            }
        }
        return Math.sqrt(k2);
    }

    private static HashMap<Integer, Double> sortByValues(HashMap map) {
        List list = new LinkedList(map.entrySet());
        Collections.sort(list, new Comparator() {
            public int compare(Object o2, Object o1) {
                return ((Comparable) ((Map.Entry) (o1)).getValue()).compareTo(((HashMap.Entry) (o2)).getValue());
            }
        });
        HashMap sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            HashMap.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }

        return sortedHashMap; // ordered the words basing on the frequency
    }
}

class Wordcount2 {

    private String[] splitter;
    private int[] counter;
    HashMap map = new HashMap();
    public static HashMap<String, Integer> tokens = new HashMap<>();
    int co = 0;

    /**
     * @param args the command line arguments
     */
    public int countWords(String text) {
        // remove any '\n' characters that may occur
        String temp = text.replaceAll("[\\n]", " ");
        // replace any grammatical characters and split the String into an array
        splitter = temp.split("[^A-Za-z]");
        //co=co+1;
        // intialize an int array to hold count of each word
        counter = new int[splitter.length];

        // loop through the sentence
        for (int i = 0; i < splitter.length; i++) {

            if (splitter[i].equals("")) {
                continue;
            }
            // hold current word in the sentence in temp variable
            temp = splitter[i];
            co = co + 1;
            for (int k = 0; k < splitter.length; k++) {
                if (temp.equalsIgnoreCase(splitter[k])) {
                    counter[k]++;
                }
            }
        }

        printResults();
        return co;
    }

    private void printResults() {
        for (int i = 0; i < splitter.length; i++) {
            String s = splitter[i].toLowerCase();
            map.put(s, counter[i]);
            if (!s.equals("")) {
                if (tokens.get(s) == null) {
                    tokens.put(s, 1);
                } else {
                    tokens.put(s, (int) tokens.get(s) + 1);
                }
            }
        }
        Iterator it = map.keySet().iterator();
    }

    public static HashMap<String, Integer> frequency(String args) throws FileNotFoundException, IOException {
        String str;
        int k = 0;
        int words = 0;
        tokens.clear();
        //File folder = new File("Resources/downloadedFiles");
        //File[] listOfFiles = folder.listFiles();
        //for (File file : listOfFiles) {
        //	if (file.isFile()) {
        //		FileReader fileReader = new FileReader(file);
        //		BufferedReader br= new BufferedReader(fileReader);
        //		str = br.readLine();
        str = args;
        Wordcount2 wc = new Wordcount2();
        words += wc.countWords(str);
        //	}
        //}
        //System.out.println("Total No of words: "+words);
        Map<String, Integer> t = new TreeMap(tokens);
        Set<Entry<String, Integer>> set = tokens.entrySet();
        List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(set);
        //List<Entry<String, Integer>> portList = new ArrayList<Entry<String, Integer>>();
        HashMap<String, Integer> portMap = new HashMap<>();
        List<String> arr;
        arr = Files.readAllLines(Paths.get("Resources/stopwords.txt")); //stop words are read and stored in arr file.
        Set<String> intersect = new HashSet<String>(arr);
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });
//unique words in the collection
        List<String> abc = new ArrayList<>();
        int uni = 0;
        for (Map.Entry<String, Integer> entry : list) {
            uni++;
            abc.add(entry.getKey());
            Set<String> it = new HashSet<String>(Arrays.asList(entry.getKey()));
            intersect.retainAll(it);
        }
        //System.out.println("\n\nTotal number of unique words: "+uni);
// top 20 words from unique words
        //System.out.println("\n\nTop 20 words are: ");
        /*for(int i=0;i<20;i++)
		{
			System.out.println((i+1)+"==="+abc.get(i));
                        if(i>=uni-1)
                            break;
		}*/
        int av = 1;

        //System.out.println("\n\nStop words in top 20: ");
        //List<Entry<String, Integer>> list1 = list;
        //int[] indexStop={-1};
        //int in=0;
        List<Map.Entry<String, Integer>> abcd = new ArrayList<>();
        ArrayList<Integer> indexStop = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : list) {
            if (arr.indexOf(entry.getKey()) != -1) {
                abcd.add(entry);
            }
            //indexStop.add(list.indexOf(entry));
            //indexStop[in++] = list.indexOf(entry);
            //System.out.println(av+" ==== "+entry.getKey());
        }
        list.removeAll(abcd);
        int z = 1;
        HashSet stop = new HashSet(arr);  //arr contains given stopwords.txt
        HashSet unique = new HashSet(abc); // abc contains given unique words of collection
        Set<String> intersection = new HashSet<String>(unique);
        intersection.retainAll(stop);  //intersection set is used to compare the stop and unique sets to retain intersect words from two sets.
        //System.out.println("\n\nPercentage:");
        //wordPercentage(list,15); //displays the words which make 15% of total collection.
        for (Entry<String, Integer> count : list) {
            Porter o = new Porter(); //integrate potter stemmer code
            int c = count.getValue();
            String s = count.getKey();
            String str1 = o.portingFunction(s);
            if (!portMap.containsKey(str1)) {
                portMap.put(str1, c);
            } else {
                int m = portMap.get(str1);
                portMap.put(str1, c + m);
            }
        }
        int portUnique = 0;
        int totword = 0;
        Map<String, Integer> portMap1 = sortByValues(portMap);

        for (String abv : portMap1.keySet()) {
            if (arr.contains(abv)) {
                continue;
            }
            //System.out.println(abv+" "+portMap1.get(abv));
            portUnique++;
            totword += portMap1.get(abv);
        }
        int itter = 0;
        //System.out.println("\n\nNo. of words after Ported Stemmer: "+totword);
        //System.out.println("\nNo. of unique words after Ported Stemmer: "+portUnique);
        //System.out.println("\nTop 20 words after Porter Stemmer and stop word removal:");
        /*for(String abv:portMap1.keySet())
                {
                    if(arr.contains(abv))
                    {
                            continue;
                    }
                    System.out.println(abv+" === "+portMap1.get(abv));
                    itter++;
                    if(itter>20)
                        break;
                }*/
        //System.out.println("\n\nPercentage:");
        //wordPercentage(portMap1, 15, arr);
        return portMap;
        //System.out.println(portMap1);
        //return SortedHashMap;
    }

    public static void wordPercentage(List<Entry<String, Integer>> abc, int n) {
        double s = 0;
        int i = 0;
        int total = 0;
        for (Entry<String, Integer> count : abc) {
            total += count.getValue();
        }
        for (Entry<String, Integer> count : abc) {
            s += (double) count.getValue();
            double p = s / total * 100.0;
            i++;
            System.out.println(count.getKey() + " === " + count.getValue());
            if (p >= 15) {
                System.out.println("\n\n" + i + " words accounts for " + n + "% of the total " + total + " words.");
                return;
            }
        }
    }

    public static void wordPercentage(Map<String, Integer> abc, int n, List<String> arr) {
        double s = 0;
        int i = 0;
        int total = 0; //total collection of words
        for (String ab : abc.keySet()) {
            if (arr.contains(ab)) {
                continue;
            }
            total += abc.get(ab);
        }
        for (String ab : abc.keySet()) {
            if (arr.contains(ab)) {
                continue;
            }
            s += (double) abc.get(ab);
            double p = s / total * 100.0;
            i++;
            System.out.println(ab + " === " + abc.get(ab));
            if (p >= 15) {
                System.out.println("\n\n" + i + " words accounts for " + n + "% of the total " + total + " words.");
                return;
            }
        }
    }

    private static HashMap<String, Integer> sortByValues(HashMap map) {
        List list = new LinkedList(map.entrySet());
        Collections.sort(list, new Comparator() {
            public int compare(Object o2, Object o1) {
                return ((Comparable) ((Map.Entry) (o1)).getValue()).compareTo(((Map.Entry) (o2)).getValue());
            }
        });
        HashMap sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }

        return sortedHashMap; // ordered the words basing on the frequency
    }
}
