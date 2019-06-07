 package misc;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class commonWordsFinder {

	public ArrayList<String> allTerms = new ArrayList<String>();
	public ArrayList<Integer> allTermsFrq = new ArrayList<Integer>();
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String file = args[0];
		commonWordsFinder f = new commonWordsFinder();
		f.findCommonWords(file);
		//f.checkOverlaps();
	}
	
	public void findCommonWords(String file) throws IOException{
		int linesForArticle = 6;
		Charset charset = Charset.forName("utf-8");
        List<String> lines = Files.readAllLines(Paths.get(file), charset);
		int index;
		int currentFreq;
        for (int i=0; i<lines.size(); i++) {        
        	//if(lines.get(i).startsWith("<ARTICLE>") && i+linesForArticle <= lines.size()){
	    		//for(int j=i; j<i+linesForArticle; j++){
	    			//String line = lines.get(j);
	    			//if(line.startsWith(" <TITLE>") || line.startsWith(" <BODY>")){ 
        				String line = lines.get(i);
		    			for(String word: line.split("[\\p{P} \\t\\n\\r<>]+")){
	    					//Stop word removal
		    				word = word.trim().replaceAll("\u00A0", "");
    		    				//writer.append(word).append(System.lineSeparator());
 							if(allTerms.contains(word)){
	        					index = allTerms.indexOf(word);
								currentFreq = allTermsFrq.get(index);
								allTermsFrq.set(index, ++currentFreq);
 							}else{
			    				allTerms.add(word);
			    				allTermsFrq.add(1);
 							}
		    			}
	    			//}
	    		//}
			//}
        }
		
        int totWords = 0;
        int topWords = 0;
		int freq = 0;
		for(int i=0; i<allTerms.size(); i++){
			freq = allTermsFrq.get(i);
			totWords += freq;
			if(/*freq < 370 &&*/ freq > 1500){
				topWords++;
				//System.out.println(allTerms.get(i)+" - "+ freq);
				System.out.println(allTerms.get(i));
			}
		}
		System.out.println("Top words : "+topWords);
		System.out.println("Total words : "+totWords);
	}
	
	public void checkOverlaps() throws IOException{
		Charset charset = Charset.forName("utf-8");
		
        List<String> lines1 = Files.readAllLines(Paths.get("F:\\MSc\\NewsData\\UCSC_stopWords.txt"), charset);
        List<String> lines2 = Files.readAllLines(Paths.get("F:\\MSc\\NewsData\\stopWordsList-onlyNipatha.txt"), charset);
 
        for(String line:lines1){
            for(String line2:lines2){
            	if(line.equals(line2)){
            		System.out.println(line2);
            	}
            }
	    }
        
	}
}


