package misc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CalculateKappa {

	public static void main(String[] args) throws IOException {
		
		String one = args[0];
		String two = args[1];
		int total = Integer.parseInt(args[2]);

		Kappa(one, two, total);
	}
	
	public static void Kappa(String one, String two, int total) throws IOException {
		double Kappa = 0.0;
		int yesMatches = 0;
		int noMatches = 0;
		int oneOnlyYes = 0;
		int twoOnlyYes = 0;
		
		ArrayList<ArrayList<String>> oneGroups =new ArrayList<ArrayList<String>>();
		ArrayList<String> onePairs =new ArrayList<String>();
		ArrayList<ArrayList<String>> twoGroups =new ArrayList<ArrayList<String>>();
		ArrayList<String> twoPairs =new ArrayList<String>();		
		//read groups
        List<String> lines = Files.readAllLines(Paths.get(one));
        for (int i=0; i<lines.size(); i++) {
        	String line = lines.get(i);
        	ArrayList<String> oneGroup = new ArrayList<String>();
        	for(String article: line.split(",")){
        		oneGroup.add(article);
        	}
        	oneGroups.add(oneGroup);
        }
        //construct pairs
        for (ArrayList<String> gr : oneGroups) {
            for (int i=0; i<gr.size(); i++) {
            	for (int j=i+1; j<gr.size(); j++){
            		String pair = gr.get(i)+","+gr.get(j);
            		onePairs.add(pair);
            	}
            }
        }
 
		//read system groups
        List<String> linesSys = Files.readAllLines(Paths.get(two));
        for (int i=0; i<linesSys.size(); i++) {
        	String line = linesSys.get(i);
        	ArrayList<String> oneGroup = new ArrayList<String>();
        	for(String article: line.split(",")){
        		oneGroup.add(article);
        	}
        	twoGroups.add(oneGroup);
        	
        }
        //construct pairs
        for (ArrayList<String> gr : twoGroups) {
            for (int i=0; i<gr.size(); i++) {
            	for (int j=i+1; j<gr.size(); j++){
            		String pair = gr.get(i)+","+gr.get(j);
            		twoPairs.add(pair);
            	}
            }
        }
        
        //check Matches
        for (String base : onePairs){
        	for(String sys : twoPairs){
        		if(base.contentEquals(sys)){
        			yesMatches++;
        		}
        	}
        }
        int totalPairs = 0;
        if(total%2 == 0){
        	totalPairs = total/2*(total-1);
        }else{
        	totalPairs = total*((total-1)/2);
        }
        
		oneOnlyYes = onePairs.size()-yesMatches;
		twoOnlyYes = twoPairs.size()-yesMatches;
		noMatches = totalPairs-(yesMatches+oneOnlyYes+twoOnlyYes);
		
        double PA = (yesMatches+noMatches)*1.0/totalPairs;
        double PNo = ((totalPairs-onePairs.size())+(totalPairs-twoPairs.size()))*1.0/(totalPairs*2);
		double PYes = (onePairs.size()+twoPairs.size())/(totalPairs*2);
		double PE = PNo*PNo + PYes*PYes;
		Kappa = (PA - PE)/(1-PE);
				
        System.out.println("");
        System.out.println(one+" & "+two);
        System.out.println("total pairs: "+totalPairs);
        System.out.println("one Pairs: "+ onePairs.size());
        System.out.println("two Pairs: "+ twoPairs.size());
        System.out.println("Yes Matches: "+yesMatches);
        System.out.println("One 0nly yes: "+oneOnlyYes);
        System.out.println("Two 0nly yes: "+twoOnlyYes);
        System.out.println("No Matches: "+noMatches);
        System.out.println("PA:"+PA+" PE:"+PE);
        System.out.println("Kappa: "+ Kappa);

	
	}

}
