package my.contacteditor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.Map.Entry;

/**
*
* @author bandi vyshnavi
*/
public class Wordcount1 {
	private String[] splitter;
	private int[] counter;
	HashMap map = new HashMap();
	public static HashMap<String, Integer> tokens = new HashMap<>();
	int co=0;

	/**
	* @param args the command line arguments
	*/

	public int countWords(String text){
		// remove any '\n' characters that may occur
		String temp = text.replaceAll("[\\n]", " ");
		// replace any grammatical characters and split the String into an array
		splitter = temp.split("[^A-Za-z]");
		//co=co+1;
		// intialize an int array to hold count of each word
		counter= new int[splitter.length];

		// loop through the sentence
		for(int i =0; i< splitter.length; i++){

			if(splitter[i].equals(""))
				continue;
			// hold current word in the sentence in temp variable
			temp = splitter[i];
			co=co+1;
			for (int k=0; k<splitter.length; k++){
				if(temp.equalsIgnoreCase(splitter[k]))
				{
					counter[k]++;
				}
			}
		}

		printResults();
		return co;

	}



	private void printResults()
	{
		for(int i=0; i< splitter.length; i++)
		{
			String s=splitter[i].toLowerCase();
			map.put(s, counter[i]);
			if(!s.equals("")){
				if(tokens.get(s) == null)
					tokens.put(s, 1);
				else
				{
					tokens.put(s, (int)tokens.get(s)+1);
				}
			}
		}
		Iterator it = map.keySet().iterator();
	}

	public static void frequency(String args) throws FileNotFoundException, IOException{
		String str;
		int k=0;
		int words=0;
		File folder = new File("./src/my/contacteditor/downloadedFiles");
		File[] listOfFiles = folder.listFiles();
		//for (File file : listOfFiles) {
		//	if (file.isFile()) {
		//		FileReader fileReader = new FileReader(file);
		//		BufferedReader br= new BufferedReader(fileReader);
		//		str = br.readLine();
                                str = args;
				Wordcount1 wc = new Wordcount1();
				words += wc.countWords(str);
		//	}
		//}
		System.out.println("Total No of words: "+words);
		Map<String, Integer> t = new TreeMap(tokens);
		Set<Entry<String, Integer>> set = tokens.entrySet();
		List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(set);
		//List<Entry<String, Integer>> portList = new ArrayList<Entry<String, Integer>>();
		HashMap<String, Integer> portMap = new HashMap<>();
		List<String> arr;
		arr = Files.readAllLines(Paths.get("./src/stopwords.txt")); //stop words are read and stored in arr file.
		Set<String> intersect = new HashSet<String>(arr);
		Collections.sort( list, new Comparator<Map.Entry<String, Integer>>()
		{
			public int compare( Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2 )
			{
				return (o2.getValue()).compareTo( o1.getValue() );
			}
		} );
//unique words in the collection
		List<String> abc = new ArrayList<>();
		int uni = 0;
		for(Map.Entry<String, Integer> entry:list){
			uni++;
			abc.add(entry.getKey());
			Set<String> it = new HashSet<String>(Arrays.asList(entry.getKey()));
			intersect.retainAll(it);
		}
		System.out.println("\n\nTotal number of unique words: "+uni);
// top 20 words from unique words
		System.out.println("\n\nTop 20 words are: ");
		for(int i=0;i<20;i++)
		{
			System.out.println((i+1)+"==="+abc.get(i));
		}
		int av = 1;

		System.out.println("\n\nStop words in top 20: ");
		for(Map.Entry<String, Integer> entry:list){
			if(arr.indexOf(entry.getKey()) != -1)
				System.out.println(av+" ==== "+entry.getKey());
			av++;
			if(av>=20)
			{
				break;
			}
		}
		int z=1;
		HashSet stop = new HashSet(arr);  //arr contains given stopwords.txt
		HashSet unique = new HashSet(abc); // abc contains given unique words of collection
		Set<String> intersection = new HashSet<String>(unique);
		intersection.retainAll(stop);  //intersection set is used to compare the stop and unique sets to retain intersect words from two sets.
		System.out.println("\n\nPercentage:");
		wordPercentage(list,15); //displays the words which make 15% of total collection.
		for (Entry<String, Integer> count : list) {
			Porter o=new Porter(); //integrate potter stemmer code
			int c = count.getValue();
			String s = count.getKey();
			String str1 = o.portingFunction(s);
			if(!portMap.containsKey(str1))
			{
				portMap.put(str1, c);
			}
			else
			{
				int m = portMap.get(str1);
				portMap.put(str1, c+m);
			}
		}
		int portUnique = 0;
		int totword = 0;
		Map<String, Integer> portMap1 = sortByValues(portMap);

		for(String abv:portMap1.keySet())
		{
			if(arr.contains(abv))
			{
				continue;
			}
			//System.out.println(abv+" "+portMap1.get(abv));
			portUnique++;
			totword += portMap1.get(abv);
		}
                int itter = 0;
                System.out.println("\n\nNo. of words after Ported Stemmer: "+totword);
		System.out.println("\nNo. of unique words after Ported Stemmer: "+portUnique);
                System.out.println("\nTop 20 words after Porter Stemmer and stop word removal:");
                for(String abv:portMap1.keySet())
                {
                    if(arr.contains(abv))
                    {
                            continue;
                    }
                    System.out.println(abv+" === "+portMap1.get(abv));
                    itter++;
                    if(itter>20)
                        break;
                }
                System.out.println("\n\nPercentage:");
                wordPercentage(portMap1, 15, arr);
                //return SortedHashMap;
	}

	public static void wordPercentage(List<Entry<String, Integer>> abc, int n)
	{
		double s = 0;
		int i = 0;
		int total = 0;
		for (Entry<String, Integer> count : abc){
			total += count.getValue();
		}
		for (Entry<String, Integer> count : abc) {
			s += (double)count.getValue();
			double p = s/total * 100.0;
			i++;
			System.out.println(count.getKey() + " === " + count.getValue());
			if(p>=15)
			{
				System.out.println("\n\n"+i+" words accounts for "+n+"% of the total "+total+" words.");
				return;
			}
		}
	}
        
        public static void wordPercentage(Map<String, Integer> abc,int n, List<String> arr)
	{
		double s = 0;
		int i = 0;
		int total = 0; //total collection of words
		for (String ab:abc.keySet()){
                    if(arr.contains(ab))
                    {
                        continue;
                    }
                    total += abc.get(ab);
		}
		for (String ab:abc.keySet()) {
                    if(arr.contains(ab))
                    {
                        continue;
                    }
                    s += (double)abc.get(ab);
                    double p = s/total * 100.0;
                    i++;
                    System.out.println(ab + " === " + abc.get(ab));
                    if(p>=15)
                    {
                            System.out.println("\n\n"+i+" words accounts for "+n+"% of the total "+total+" words.");
                            return;
                    }
		}
	}

	private static HashMap sortByValues(HashMap map) { 
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
                //frequency(sortedHashMap);
		return sortedHashMap; // ordered the words basing on the frequency
	}
}
