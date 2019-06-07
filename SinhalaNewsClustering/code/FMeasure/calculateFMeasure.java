package FMeasure;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class calculateFMeasure {
	
	public static void main(String[] args) throws Exception {
		
		calculateFMeasure cf = new calculateFMeasure();
		
		String manual = args[0]+"manual.txt";
		String base = args[0]+"base.txt";
		String titleW2 = args[0]+"TW2.txt";
		String titleW3 = args[0]+"TW3.txt";
		String titleW4 = args[0]+"TW4.txt";
		String titleW5 = args[0]+"TW5.txt";
		
		String DW = args[0]+"DW.txt";		
		String DWtitleW = args[0]+"DWTW.txt";
		String woStopW = args[0]+"woStopwords.txt";
		String NipathaStopW = args[0]+"NipathaStopwords.txt";
		String titleOnly = args[0]+"titleOnly.txt";
		String properNoun = args[0]+"properNoun.txt";

		cf.FMeasure(manual, base);
		cf.FMeasure(manual, titleW2);
		cf.FMeasure(manual, titleW3);
		cf.FMeasure(manual, titleW4);
		cf.FMeasure(manual, titleW5);

		cf.FMeasure(manual, DW);
		cf.FMeasure(manual, DWtitleW);
		cf.FMeasure(manual, woStopW);
		cf.FMeasure(manual, NipathaStopW);
		cf.FMeasure(manual, titleOnly);
		cf.FMeasure(manual, properNoun);
	}
	
	public double FMeasure(String baseGroupingPath, String systemGroupingPath) throws IOException{
		double Fscore = 0.0;
		int matchPairs = 0;
		int groupedArticles = 0;
		int groups = 0;
		
		ArrayList<ArrayList<String>> baseGroups =new ArrayList<ArrayList<String>>();
		ArrayList<String> basePairs =new ArrayList<String>();
		ArrayList<ArrayList<String>> sysGroups =new ArrayList<ArrayList<String>>();
		ArrayList<String> sysPairs =new ArrayList<String>();		
		//read groups
        List<String> lines = Files.readAllLines(Paths.get(baseGroupingPath));
        for (int i=0; i<lines.size(); i++) {
        	String line = lines.get(i);
        	ArrayList<String> oneGroup = new ArrayList<String>();
        	for(String article: line.split(",")){
        		oneGroup.add(article);
        	}
        	baseGroups.add(oneGroup);
        }
        //construct pairs
        for (ArrayList<String> gr : baseGroups) {
    		if(gr.size() > 1){
    			groupedArticles += gr.size();
    			groups++;
    		}
            for (int i=0; i<gr.size(); i++) {
            	for (int j=i+1; j<gr.size(); j++){
            		String pair = gr.get(i)+","+gr.get(j);
            		basePairs.add(pair);
            	}
            }
        }
 
		//read system groups
        if(!Paths.get(systemGroupingPath).toFile().exists()){
        	System.out.println("No file: "+systemGroupingPath);
        	return 0.0;
        }
        List<String> linesSys = Files.readAllLines(Paths.get(systemGroupingPath));
        for (int i=0; i<linesSys.size(); i++) {
        	String line = linesSys.get(i);
        	ArrayList<String> oneGroup = new ArrayList<String>();
        	for(String article: line.split(",")){
        		oneGroup.add(article);
        	}
        	sysGroups.add(oneGroup);
        	
        }
        //construct pairs
        for (ArrayList<String> gr : sysGroups) {
            for (int i=0; i<gr.size(); i++) {
            	for (int j=i+1; j<gr.size(); j++){
            		String pair = gr.get(i)+","+gr.get(j);
            		sysPairs.add(pair);
            	}
            }
        }
        
        //check Matches
        for (String base : basePairs){
        	for(String sys : sysPairs){
        		if(base.contentEquals(sys)){
        			matchPairs++;
        		}
        	}
        }
        
        //System.out.println("");
        //System.out.println(systemGroupingPath);
        //System.out.println("Manual Groups: "+ groups);
        //System.out.println("Grouped Articles: "+ groupedArticles);
        //System.out.println("Manual Pairs: "+ basePairs.size()+", System Pairs: "+ sysPairs.size()+" ->Matches: "+ matchPairs);
        //System.out.println("System Pairs: "+ sysPairs.size());
        //System.out.println("Matches: "+ matchPairs);
        
        Fscore = (2.0*matchPairs)/(basePairs.size()+sysPairs.size());
		//System.out.println("FScore : " + Fscore);
		//Fscore = /(basePairs.size()+sysPairs.size());
        
    	//System.out.println(sysPairs);
        		
		return Fscore;
	}
	
}
