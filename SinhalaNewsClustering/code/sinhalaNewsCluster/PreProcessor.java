package sinhalaNewsCluster;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class articleData{
	int index;
	String heading = new String();
	String link = new String();
	ArrayList<String> termsDocsArray;
	ArrayList<Double> tfidfArray;
	ArrayList<String> PNtermsDocsArray;
	ArrayList<Double> PNtfidfArray;
	
	void addTermsArray(ArrayList<String> terms){
		termsDocsArray = new ArrayList<String>();
		for(String term:terms){
			termsDocsArray.add(term);
		}
	}
	
	void addTfidfArray(ArrayList<Double> tfidfValues){
		tfidfArray = new ArrayList<Double>();
		for(Double tfIdf:tfidfValues){
			tfidfArray.add(tfIdf);
		}		
	}
	
	void addPPTermsArray(ArrayList<String> terms){
		PNtermsDocsArray = new ArrayList<String>();
		for(String term:terms){
			PNtermsDocsArray.add(term);
		}
	}
	
	void addPPTfidfArray(ArrayList<Double> tfidfValues){
		PNtfidfArray = new ArrayList<Double>();
		for(Double tfIdf:tfidfValues){
			PNtfidfArray.add(tfIdf);
		}		
	}
}

public class PreProcessor {
	//private static String stopWordFile = "F:\\MSc\\NewsData\\stopWordsList-UCSC.txt";
	private static String stopWordFile = "stopWordsList.txt";
	private static String stopWordFileNipatha = "F:\\MSc\\NewsData\\stopWordsList-onlyNipatha.txt";	
	final int maxWordsPerArticle = 100;
	private static int PNIndex = 0;
	static boolean newArticle = false;
	
	//public ArrayList<ArrayList<String>> termsDocsArray = new ArrayList<ArrayList<String>>();
	public ArrayList<String> allTerms = new ArrayList<String>();
	public ArrayList<String> tempTerms = new ArrayList<String>();
	public ArrayList<String> PNallTerms = new ArrayList<String>();
	public ArrayList<String> PNtempTerms = new ArrayList<String>();
	public ArrayList<articleData> articleCollection = new ArrayList<articleData>();	
	public ArrayList<articleData> PParticleCollection = new ArrayList<articleData>();	

	/*
    public void calcTfIdf() throws IOException {
        double tf; //term frequency
        double idf; //inverse document frequency
        double tfidf; //term frequency-inverse document frequency        
        for (articleData article : articleCollection) {
        	ArrayList<Double> tfidfvectors = new ArrayList<Double>();
            for (String terms : allTerms) {
                tf = calcTf(article.termsDocsArray, terms);
                idf = calcIdf(articleCollection, terms);
                tfidf = tf * idf;
                tfidfvectors.add(tfidf);
            }
            article.addTfidfArray(tfidfvectors);
            
        	ArrayList<Double> PNtfidfvectors = new ArrayList<Double>();
            for (String terms : PNallTerms) {
                tf = calcTf(article.PNtermsDocsArray, terms);
                idf = calcPPIdf(articleCollection, terms);
                tfidf = tf * idf;
                PNtfidfvectors.add(tfidf);
            }
            article.addPPTfidfArray(PNtfidfvectors);    
         
        }
        //printArrays();
    }
  */
   public void calcTfIdf() throws IOException {
        double tf; //term frequency
        double idf; //inverse document frequency
        double tfidf; //term frequency-inverse document frequency        
        for (articleData article : articleCollection) {
        	ArrayList<Double> tfidfvectors_RAW = new ArrayList<Double>();
        	ArrayList<Double> tfidfvectors = new ArrayList<Double>();
            for (String terms : allTerms) {
                tf = calcTf(article.termsDocsArray, terms);
                idf = calcIdf(articleCollection, terms);
                tfidf = tf * idf;
                tfidfvectors_RAW.add(tfidf);
            }
            normalize(tfidfvectors_RAW, tfidfvectors);
            article.addTfidfArray(tfidfvectors);
            
        	ArrayList<Double> PNtfidfvectors_RAW = new ArrayList<Double>();
        	ArrayList<Double> PNtfidfvectors = new ArrayList<Double>();
            for (String terms : PNallTerms) {
                tf = calcTf(article.PNtermsDocsArray, terms);
                idf = calcPPIdf(articleCollection, terms);
                tfidf = tf * idf;
                PNtfidfvectors_RAW.add(tfidf);
            }
            normalize(PNtfidfvectors_RAW, PNtfidfvectors);
            article.addPPTfidfArray(PNtfidfvectors);             
        }
        //printArrays();
    }
	
   	private void normalize(ArrayList<Double> rawVector, ArrayList<Double> normalized)
   	{
   		Double L2 = 0.0;
   		Double normValue = 0.0;
   		for(Double value: rawVector){
   			L2 += value*value;
   		}
   		L2 = Math.sqrt(L2);
   		
   		for(Double value: rawVector){
   			normValue = value/L2;
   			normalized.add(normValue);
   		}
   	}
    
    private double calcTf(ArrayList<String> docTermsArray, String termToCheck) {
        double count = 0;  //to count the overall occurrence of the term termToCheck
        for (String s : docTermsArray) {
            if (s.equalsIgnoreCase(termToCheck)) {
                count++;
            }
        }
        //System.out.println("TF>"+termToCheck+":"+count / docTermsArray.size());
        return count / docTermsArray.size();
    }
    
    private double calcIdf(ArrayList<articleData> articles, String termToCheck) {
    	double count = 0;
        for(articleData article: articles){
        	for(String term: article.termsDocsArray){
                if (term.equalsIgnoreCase(termToCheck)) {
                    count++;
                    break;
                }
            }
        }
        //System.out.println("IDF>"+termToCheck+":"+Math.log(allTerms.size() / count)+" >AllTerms:"+allTerms.size()+" >count:"+count);
        //return 1 + Math.log(allTerms.size() / count);
        return Math.log10((articles.size() / count));
    }

    private double calcPPIdf(ArrayList<articleData> articles, String termToCheck) {
    	double count = 0;
        for(articleData article: articles){
        	for(String term: article.PNtermsDocsArray){
                if (term.equalsIgnoreCase(termToCheck)) {
                    count++;
                    break;
                }
            }
        }
        //System.out.println("IDF>"+termToCheck+":"+Math.log(allTerms.size() / count)+" >AllTerms:"+allTerms.size()+" >count:"+count);
        //return 1 + Math.log(allTerms.size() / count);
        return Math.log10((articles.size() / count));
    }
    
	public void readNewsFile(String file, int tWeight, int stopWordsType, boolean titleOnly, boolean ProperNoun) throws IOException 
	{
		
		/*Writer writer = new OutputStreamWriter(
		        new FileOutputStream("WordsList.txt", true), "UTF-8");			
		writer.append(System.lineSeparator());*/
		
		int linesForArticle = 6;
		Charset charset = Charset.forName("utf-8");
        List<String> lines = Files.readAllLines(Paths.get(file), charset);
        int weight = 1;
		int totalWords = 0;
		PNIndex =0;
		newArticle = false;

        for (int i=0; i<lines.size(); i++) {        
        	if(lines.get(i).startsWith("<ARTICLE>") && i+linesForArticle <= lines.size()){
        		ArrayList<String> words = new ArrayList<String>();
        		articleData article = new articleData();
        		ArrayList<String> PPwords = new ArrayList<String>();
	    		for(int j=i; j<i+linesForArticle; j++){
	    			String line = lines.get(j);
	    			if(line.startsWith(" <INDEX>")){
	    				article.index = Integer.parseInt(line.substring(" <INDEX> ".length(), line.indexOf(" </INDEX>")));
	    			}
	    			if(line.startsWith(" <LINK>")){
	    				article.link = line.substring(" <LINK>".length(), line.indexOf("</LINK>"));
	    			}

	    			if(line.startsWith(" <TITLE>") || (!titleOnly && line.startsWith(" <BODY>"))){
	    				weight = 1;
	    				//properNounCheck = true;
		    			if(line.startsWith(" <TITLE>")){
		    				newArticle = true;
		    				weight = tWeight; //weighted heading
		    				article.heading = line.substring(" <TITLE>".length(), line.indexOf("</TITLE>"));
		    			}
		    			for(String word: line.split("[\\p{P} \\t\\n\\r<>]+")){
	    					//Stop word removal
		    				word = word.trim().replaceAll("\u00A0", "");
    						if(word.length()> 1 ){
    							if((stopWordsType == 0) || !checkStopWords(stopWordsType, word)){
    		    				//writer.append(word).append(System.lineSeparator());
    							if(words.size() < maxWordsPerArticle){  								
    								for(int k=0; k<weight; k++){
        								words.add(word);
        								totalWords++;
    								}
    								if(ProperNoun && isProperNoun(file, word)){
    									PPwords.add(word);
    									if (!PNallTerms.contains(word)) {  //avoid duplicate entry
    										PNallTerms.add(word);
    									}
    								}
    								if (!allTerms.contains(word)) {  //avoid duplicate entry
    									if(tempTerms.contains(word)){ //to set the minimum word frequency to 2–words
    										allTerms.add(word);
    										tempTerms.remove(word);
    										
    									}
    									else{
    										tempTerms.add(word);
    									}
    								}
    							}}
    						}
	    				}	        				      			
	    			}
	    		}
	    		//ignore small articles
	    		//if(words.size() > 35){
	    			article.addTermsArray(words);
	    			article.addPPTermsArray(PPwords);
	    			articleCollection.add(article);
	    		//}
	    		i += linesForArticle; 
        	}
        }
        System.out.println("Total Words: "+allTerms.size()+" PNames: "+PNallTerms.size());
        //writer.close();
	}
	
	private boolean isProperNoun(String fileName, String checkWord) throws IOException 
	{
		Charset charset = Charset.forName("utf-8");
		String PfileName = fileName.substring(0, fileName.indexOf(".txt"))+"_Tags.txt";
        List<String> words = Files.readAllLines(Paths.get(PfileName), charset);
        String entry, word, tag;
        
        if(newArticle){
        	for(int i=PNIndex; i<words.size();i++){
        		if(words.get(i).startsWith("<START>")){
        			PNIndex = i;
					newArticle = false;
        			break;
        		}
        	}
        }
        
        for(int i=PNIndex+1; i<words.size(); i++){
        	entry = words.get(i);
        	int endIndex = entry.indexOf(" ");
        	if(endIndex > 0){
        		word = entry.substring(0, endIndex);
        		word = word.trim().replaceAll("\u00A0", "");
        	}
        	else{
        		//System.out.println("Error:"+entry);
        		continue;
        	}
        	tag = entry.substring(entry.indexOf(" ")+1);
        	
        	if(Objects.equals(word.trim(), checkWord.trim())){
        		if(tag.equalsIgnoreCase("NNP")){
        			//System.out.println(" "+ word +" @ "+i);
        			PNIndex = i;
        			return true;
        		}
        		else{
        			return false;
        		}
        	}
        }
        
        //for (String word : words) {
        //	if( Objects.equals(word.trim(), checkWord.trim())){
        		//System.out.println("removed "+word);
        //		return true;
        //	}
        //}
		return false;
	}

	private static boolean checkStopWords(int stopWordList, String checkWord) throws IOException 
	{	
		Charset charset = Charset.forName("utf-8");
		List<String> words;
		if(stopWordList == 1){
			words = Files.readAllLines(Paths.get(stopWordFile), charset);
		}else{
			words = Files.readAllLines(Paths.get(stopWordFileNipatha), charset);
		}
		
        for (String word : words) {
        	if( Objects.equals(word.trim(), checkWord.trim())){
        		//System.out.println("removed "+word);
        		return true;
        	}
        }
		return false;
	}
}
