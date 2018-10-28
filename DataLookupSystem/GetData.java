import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class GetData {

	ArrayList<ArrayList<String>> allData;
	ArrayList<ArrayList<Integer>> weights;
	ArrayList<String> blackList;
	String[] labels;
	String dir;
	float[] categories;
	int totalDigits;
	ArrayList<Integer> sums;

	public static void main(String[] args) {
		String input = args[0];//"I really don't know why my device isn't working. Please help me, this is getting frustrating and I've been on hold for several hours now. I have a iPhone 10s and a Samsung Galaxy S8 on my daughter's account, and we share the same family data plan";
		String dir = args[1].substring(0,args[1].length()-1);
		GetData gd = new GetData(dir);
		String[] results = gd.getInfo(input);
		//for (String s : results)
		//	if (s != null)
		//		System.out.println(s);
	}

	public GetData(String dir) {
		this.dir = dir;
		allData = new ArrayList<>();
		weights = new ArrayList<>();
		blackList = new ArrayList<>();
		labels = new String[] { "Account", "Alcatel", "Android", "Apple", "Coolpad", "Devices", "Digits", "Galaxy",
				"HTC", "LG", "Magenta", "Motorola", "Network" };
		categories = new float[13];
		sums = new ArrayList<>();

	}

	public String[] getInfo(String input) {
		
		String[] results = new String[10];
		HashMap<String,Integer> uniqList = new HashMap<>();
		try {
			Scanner acnt = new Scanner(new File(dir+"/Account\""));
			wordList(acnt);
			Scanner alca = new Scanner(new File(dir+"/Alcatel\""));
			wordList(alca);
			Scanner android = new Scanner(new File(dir+"/Android\""));
			wordList(android);
			Scanner apple = new Scanner(new File(dir+"/AppleData\""));
			wordList(apple);
			Scanner coolpad = new Scanner(new File(dir+"/Coolpad\""));
			wordList(coolpad);
			Scanner devices = new Scanner(new File(dir+"/Devices\""));
			wordList(devices);
			Scanner digits = new Scanner(new File(dir+"/Digits\""));
			wordList(digits);
			Scanner galaxy = new Scanner(new File(dir+"/Galaxy\""));
			wordList(galaxy);
			Scanner htc = new Scanner(new File(dir+"/HTC\""));
			wordList(htc);
			Scanner lg = new Scanner(new File(dir+"/LG\""));
			wordList(lg);
			Scanner magenta = new Scanner(new File(dir+"/Magenta\""));
			wordList(magenta);
			Scanner motorola = new Scanner(new File(dir+"/Motorola\""));
			wordList(motorola);
			Scanner network = new Scanner(new File(dir+"/Network\""));
			wordList(network);
			Scanner blackListS = new Scanner(new File(dir+"/blacklist\""));
			while(blackListS.hasNextLine()){
				blackList.add(blackListS.nextLine().toLowerCase());
			}
			Scanner inputParser = new Scanner(input);
			while (inputParser.hasNext()) {
				String check = inputParser.next().toLowerCase();
				for (int i = 0; i < categories.length; i++) {
					if(!blackList.contains(check)){
					for(int j = 0; j< allData.get(i).size();j++){
						if(allData.get(i).get(j).equalsIgnoreCase(check)){
						categories[i] += (((float)weights.get(i).get(j)))/(sums.get(i)) * Math.log((float)totalDigits/sums.get(i));
						//System.out.println(labels[i] + " " + check + " "+ ((float)weights.get(i).get(j))*Math.log((float)totalDigits/sums.get(i)) + " " + categories[i]);

						if(uniqList.containsKey(check)){
							uniqList.put(check, uniqList.get(check)+weights.get(i).get(j));
						}else{
							uniqList.put(allData.get(i).get(j),weights.get(i).get(j));
						}
					}else{
						//categories[i]-=.5f;
					}
										
				}
					}
				//System.out.println("");
			}
			}
			float first = -100;
			float second = -100;
			int fI = 0;
			int sI = 0;
			for(int i =0;i<categories.length;i++){
				if(categories[i]>second){
					if(categories[i]>first){
						second = first;
						first = categories[i];
						sI = fI;
						fI = i;
					}else{
						sI = i;
						second = categories[i];
					}
				}
			//	System.out.println(labels[i]+" " + categories[i]);
			}
			ArrayList<Float> dataClone = new ArrayList<>();
			for(float f : categories)
				dataClone.add(f);
			Arrays.sort(categories);
			for(int i=12;i>=8;i--){
				System.out.print(labels[dataClone.indexOf(categories[i])]+ " ");
			}
			//if(Math.abs(first - second) < 30){
			//	System.out.println("Ask a questions specifying " + labels[fI] + " and " + labels[sI]);
			//}
			//System.out.println("\n"+ " " + (first - second));
			System.out.println();
			for(String s : uniqList.keySet()){
				System.out.print(s+ " ");
			}
			uniqList.keySet().toArray(results);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return results;
	}

	public void wordList(Scanner scn) {
		ArrayList<String> temp = new ArrayList<>();
		ArrayList<Integer> tempI = new ArrayList<>();
		int total = 0;
		while (scn.hasNextLine()) {
			String[] line = scn.nextLine().split("\t");
			// System.out.println(line[0]);
			temp.add(line[1]);
			tempI.add(Integer.parseInt(line[0]));
			total += Integer.parseInt(line[0]);
		}
		totalDigits += total;
		sums.add(total);
		weights.add(tempI);
		allData.add(temp);
	}
	
	
	public static String[] getSynonyms(String wordInput) {
	    String[] resultArray = {};
	    if (wordInput.equals("")) {
	        return resultArray;
	    }
	    try {
	        URL url = new URL("http://www.thesaurus.com/browse/" + wordInput);
	        URLConnection yc = url.openConnection();
	        String foundWords;
	        try (BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()))) {
	            String inputLine;
	            foundWords = "";
	            boolean foundListStart = false;
	            while ((inputLine = in.readLine()) != null) {
	                String iLine = inputLine.trim();
	                if (iLine.equals("<div class=\"relevancy-list\">")) {
	                    foundListStart = true;
	                }
	                if (foundListStart) {
	                    if (iLine.equals("</div>")) {
	                        foundListStart = false;
	                        break;
	                    }
	                    if (iLine.startsWith("<li><a href=")) {
	                        String[] codeLines = iLine.split(" ");
	                        int index = codeLines[1].lastIndexOf('/');
	                        String word = codeLines[1].substring(index + 1, codeLines[1].length());
	                        word = word.replace("%27", "'").replace("%20", " ").replace("\"", "");
	                        if (foundWords.equals("")) {
	                            foundWords += word;
	                        } else {
	                            foundWords += "," + word;
	                        }
	                    }
	                }
	            }
	        }
	        // Convert built comma delimited string to String Array
	        if (!foundWords.equals("")) {
	            resultArray = foundWords.split(",");
	        }
	    } catch (MalformedURLException ex) {
	        // Do what you want with exception
	        ex.printStackTrace();
	    } catch (IOException ex) {
	        // Do what you want with exception
	        ex.printStackTrace();
	    }
	    return resultArray;
	}


	public static String[] getAntonyms(String wordInput) {
	    String[] resultArray = {};
	    if (wordInput.equals("")) {
	        return resultArray;
	    }
	    try {
	        URL url = new URL("http://www.thesaurus.com/browse/" + wordInput);
	        URLConnection yc = url.openConnection();
	        String foundWords;
	        try (BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()))) {
	            String inputLine;
	            foundWords = "";
	            boolean foundListStart = false;
	            int divCount = 0;
	            while ((inputLine = in.readLine()) != null) {
	                String iLine = inputLine.trim();
	                if (iLine.equals("<section class=\"container-info antonyms\" >")) {
	                    foundListStart = true;
	                }
	                if (iLine.equals("</div>") && foundListStart) {
	                    divCount++;
	                }
	                if (foundListStart) {
	                    if (iLine.equals("</div>") && divCount == 2) {
	                        foundListStart = false;
	                        break;
	                    }
	                    if (iLine.startsWith("<li><a href=")) {
	                        String[] codeLines = iLine.split(" ");
	                        int index = codeLines[1].lastIndexOf('/');
	                        String word = codeLines[1].substring(index + 1, codeLines[1].length());
	                        word = word.replace("%27", "'").replace("%20", " ").replace("\"", "");
	                        if (foundWords.equals("")) {
	                            foundWords += word;
	                        } else {
	                            foundWords += "," + word;
	                        }
	                    }
	                }
	            }
	        }
	        // Convert built comma delimited string to String Array
	        if (!foundWords.equals("")) {
	            resultArray = foundWords.split(",");
	        }
	    } catch (MalformedURLException ex) {
	        // Do whatever you want with exception.
	        ex.printStackTrace();
	    } catch (IOException ex) {
	        // Do whatever you want with exception.
	        ex.printStackTrace();
	    }
	    return resultArray;
	}
}
