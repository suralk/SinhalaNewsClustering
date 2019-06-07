package sinhalaNewsCluster;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

//import NewsCrawler.NewsCrawlController;

public class ArticlesCluster {

	public static double SIMILARITY_TRESHOLD = 0.18;//0.18-cosine, 0.195-TW 0.07-dwCosine, 0.075-dw-TW;
	public double BETA = 0.1;
	//private static String ArticlesFile = "F:\\MSc\\NewsData\\NewsData.txt";
	//private static String HtmlFile = "F:\\MSc\\NewsData\\SinhalaNews.html";
	//public static ArrayList<ArrayList<Double>> tfidfDocsVector = new ArrayList<ArrayList<Double>>();
	private ArrayList<ArrayList<Double>> centroidVector = new ArrayList<ArrayList<Double>>();
	private ArrayList<ArrayList<Double>> PNcentroidVector = new ArrayList<ArrayList<Double>>();
	private ArrayList<ArrayList<Integer>> clusters = new ArrayList<ArrayList<Integer>>();
	//private double[][] cosineValues;
	private static PreProcessor pp = new PreProcessor();
	private static PreProcessor pp2 = new PreProcessor();
	private static PreProcessor pp3 = new PreProcessor();
	private static PreProcessor pp4 = new PreProcessor();
	private static PreProcessor pp5 = new PreProcessor();
	private static PreProcessor pp6 = new PreProcessor();
	private static PreProcessor pp7 = new PreProcessor();
	private static PreProcessor pp8 = new PreProcessor();
	private static PreProcessor pp9 = new PreProcessor();
	
	public static void main(String[] args) throws Exception  {
		
		String ArticlesFile;
		boolean test = false;
		if(args.length == 0){
			ArticlesFile =  "NewsData.txt";
		}else{
			ArticlesFile = args[0];
		}

		if(!test){
			ArticlesCluster ac = new ArticlesCluster();
			String baseName = ArticlesFile.substring(0, ArticlesFile.lastIndexOf(".txt"));
			if(Paths.get(baseName+"_Tags.txt").toFile().exists()){
				SIMILARITY_TRESHOLD =0.092;
				pp.readNewsFile(ArticlesFile, 2, 1, false, true);//properNames -with TW2
				pp.calcTfIdf();
				ac.assignToClusters(true, "result", pp, true);	//properNames+DW
				ac.outputClusterData("result", pp);
			}
			else{
				SIMILARITY_TRESHOLD =0.132;
				pp.readNewsFile(ArticlesFile, 2, 1, false, false);//properNames -with TW2
				pp.calcTfIdf();
				ac.assignToClusters(true, "result", pp, false);	//properNames+DW
				ac.outputClusterData("result", pp);				
			}
			
		}else{
		//without title weight	
		pp.readNewsFile(ArticlesFile, 1, 1, false, false);
		pp.calcTfIdf();
		//pp.getHighFreqList();
		
		//without title weight - Cosine
		System.out.println("Base-----");
		ArticlesCluster clusterer1 = new ArticlesCluster();
		SIMILARITY_TRESHOLD =0.179;
		clusterer1.assignToClusters(false, "base", pp, false);		
		clusterer1.outputClusterData("base", pp);
	
		//Without title weight - DW Cosine
		System.out.println("DW--------");
		ArticlesCluster clusterer2 = new ArticlesCluster();
		SIMILARITY_TRESHOLD =0.138;//0.075
		clusterer2.assignToClusters(true, "DW", pp, false);		
		clusterer2.outputClusterData("DW", pp);
		
		//with title weight
		pp2.readNewsFile(ArticlesFile, 2, 1, false, false);
		pp2.calcTfIdf();
		//pp.getHighFreqList();
		
		//with title weight - Cosine
		System.out.println("TW-2-------");
		ArticlesCluster clusterer3 = new ArticlesCluster();
		SIMILARITY_TRESHOLD =0.212;
		clusterer3.assignToClusters(false,"TW2", pp2, false);		
		clusterer3.outputClusterData("TW2", pp2);
		
		//with title weight
		pp3.readNewsFile(ArticlesFile, 3, 1, false, false);
		pp3.calcTfIdf();
		//pp.getHighFreqList();
		
		//with title weight - Cosine
		System.out.println("TW-3-------");
		ArticlesCluster clusterer4 = new ArticlesCluster();
		SIMILARITY_TRESHOLD =0.190;
		clusterer4.assignToClusters(false,"TW3", pp3, false);		
		clusterer4.outputClusterData("TW3", pp3);
		
		//with title weight
		pp4.readNewsFile(ArticlesFile, 4, 1, false, false);
		pp4.calcTfIdf();
		//pp.getHighFreqList();
		
		//with title weight - Cosine
		System.out.println("TW-4-------");
		ArticlesCluster clusterer5 = new ArticlesCluster();
		SIMILARITY_TRESHOLD =0.187;
		clusterer5.assignToClusters(false,"TW4", pp4, false);		
		clusterer5.outputClusterData("TW4", pp4);
		
		//with title weight
		pp5.readNewsFile(ArticlesFile, 5, 1, false, false);
		pp5.calcTfIdf();
		//pp.getHighFreqList();
		
		//with title weight - Cosine
		System.out.println("TW-5-------");
		ArticlesCluster clusterer6 = new ArticlesCluster();
		SIMILARITY_TRESHOLD =0.178;
		clusterer6.assignToClusters(false,"TW5", pp5, false);		
		clusterer6.outputClusterData("TW5", pp5);
		
		//Without title weight - DW Cosine
		System.out.println("DWTW----------");
		ArticlesCluster clusterer7 = new ArticlesCluster();
		SIMILARITY_TRESHOLD =0.132;
		clusterer7.assignToClusters(true,"DWTW", pp2, false);		
		clusterer7.outputClusterData("DWTW", pp2);			
		
		//Without stopwords filtering
		pp6.readNewsFile(ArticlesFile, 1, 0, false, false);
		pp6.calcTfIdf();		

		//base case without stopwords
		System.out.println("Without Stopwords-----");
		ArticlesCluster clusterer8 = new ArticlesCluster();
		SIMILARITY_TRESHOLD =0.207;
		clusterer8.assignToClusters(false, "woStopwords", pp6, false);		
		clusterer8.outputClusterData("woStopwords", pp6);

		//stopwords filtering with only Nipatha
		pp7.readNewsFile(ArticlesFile, 1, 2, false, false);
		pp7.calcTfIdf();		

		//stopwords filtering with only Nipatha
		System.out.println("Nipatha only Stopwords-----");
		ArticlesCluster clusterer9 = new ArticlesCluster();
		SIMILARITY_TRESHOLD =0.221;
		clusterer9.assignToClusters(false, "NipathaStopwords", pp7, false);		
		clusterer9.outputClusterData("NipathaStopwords", pp7);
		
		//Title only
		pp8.readNewsFile(ArticlesFile, 1, 1, true, false);
		pp8.calcTfIdf();		

		//base case title only
		System.out.println("Title only-----");
		ArticlesCluster clusterer10 = new ArticlesCluster();
		SIMILARITY_TRESHOLD =0.327;
		clusterer10.assignToClusters(false, "titleOnly", pp8, false);		
		clusterer10.outputClusterData("titleOnly", pp8);
	/*
		//ProperNames
		pp9.readNewsFile(ArticlesFile, 1, 1, false, true);
		pp9.calcTfIdf();

		//base case title only
		System.out.println("ProperNoun-----");
		ArticlesCluster clusterer11 = new ArticlesCluster();
		SIMILARITY_TRESHOLD =0.1;
		clusterer11.assignToClusters(false, "properNoun", pp9, true);		
		clusterer11.outputClusterData("properNoun", pp9);
		*/		
		//clusterer.interClusterSimilarity();
		
		//getCosineSimilarity();
		
		//printCosine();
		
		//ArticlesCluster ac = new ArticlesCluster();
		//ac.getClusters(ArticlesFile);
		} 
	}
	
	public void getClusters(String articleFile) throws Exception{
		//(String file, int tWeight, int stopWordsType, boolean titleOnly, boolean ProperNoun)
		PreProcessor ppx = new PreProcessor();
		ppx.readNewsFile(articleFile, 1, 1, false, true);
		ppx.calcTfIdf();		

		//ArticlesCluster clusterer = new ArticlesCluster();
		
		String folderName = articleFile.substring(0, articleFile.lastIndexOf("\\"))+"\\results";
		Files.createDirectories(Paths.get(folderName));
		
		for(int i=5; i<50; i++){
			SIMILARITY_TRESHOLD = i/100.0;
			System.out.println("TRESHOLD:"+SIMILARITY_TRESHOLD);
			String fileName = folderName+"\\"+i+".txt";
			ArticlesCluster clusterer = new ArticlesCluster();
			clusterer.assignToClusters(false, fileName, ppx, true);		
			//clusterer.outputClusterData(fileName.substring(0, fileName.indexOf(".")), pp);
		}
	}
	
	public void assignToClusters(boolean DW, String fileName, PreProcessor ppx, boolean properNoun) throws Exception{
		int clusterIndex = 0;
		double lastCosineValue = 0.0, ALLcosineValue = 0.0, PNcosineValue = 0.0, cosineValue = 0.0;
		boolean assignedToCluster = false;
		CosineSimilarity cs = new CosineSimilarity();
		DWCosineSimilarity dcs = new DWCosineSimilarity();
		for(int articleIndex=0; articleIndex<ppx.articleCollection.size(); articleIndex++){
			ArrayList<Double> article = ppx.articleCollection.get(articleIndex).tfidfArray;
			ArrayList<Double> properNouns = ppx.articleCollection.get(articleIndex).PNtfidfArray;
			lastCosineValue = 0.0;
			assignedToCluster = false;
			for(int j=0; j<centroidVector.size(); j++){
				ArrayList<Double> centroid = centroidVector.get(j);
				ArrayList<Double> PNcentroid = PNcentroidVector.get(j);
				
				if(!DW){
					//Cosine Similarity
					ALLcosineValue = cs.cosineSimilarity(article, centroid);
					PNcosineValue = cs.cosineSimilarity(properNouns, PNcentroid);
				}else{
				//Distance-weighted cosine similarity
					ALLcosineValue = dcs.dwCosineSimilarity(article, centroid);
					PNcosineValue = dcs.dwCosineSimilarity(properNouns, PNcentroid);				
				}
				cosineValue = ALLcosineValue;
				if(properNoun){
					cosineValue += BETA * PNcosineValue;
				}
				//System.out.println(cosineValue);
				if(cosineValue >= SIMILARITY_TRESHOLD && cosineValue > lastCosineValue){
					clusterIndex = j;
					lastCosineValue = cosineValue;
					assignedToCluster = true;
				}
			}
			if(assignedToCluster){
					clusters.get(clusterIndex).add(articleIndex);
					//System.out.println("AD article:" + articleIndex +" cluster:"+ clusterIndex + "with Cosine:"+cosineValue); 
					updateCentroid(article, properNouns, clusterIndex, ppx);
					updateClusters(clusterIndex, DW);
			}else{
			//if(assignedToCluster == false){
				//centroidVector.add(article);
				ArrayList<Integer> newCluster = new ArrayList<Integer>();
				newCluster.add(articleIndex);
				clusters.add(newCluster);
				//ArrayList<Double> centroid = new ArrayList<Double>();
				//System.out.println("NW article:" + articleIndex +" cluster:"+ centroidVector.size()+ "with Cosine:"+cosineValue); 
				updateCentroid(article, properNouns, centroidVector.size(),ppx);                           
			}
		}
		printClusters(fileName);
	}
	
	private void updateCentroid(ArrayList<Double> tfidfValues, ArrayList<Double> PNtfidfValues, int centroidVectorIndex, PreProcessor ppx){

		ArrayList<Double> centroidvalues;
		ArrayList<Double> PNcentroidvalues;
		if(centroidVector.isEmpty() || centroidVector.size() <= centroidVectorIndex){
			centroidvalues = new ArrayList<Double>();
			for(Double tfidf:tfidfValues){
				centroidvalues.add(0.0);
			}
			centroidVector.add(centroidvalues);
		}       
		else{
			centroidvalues = centroidVector.get(centroidVectorIndex);
		}
		
		if(PNcentroidVector.isEmpty() || PNcentroidVector.size() <= centroidVectorIndex){
			PNcentroidvalues = new ArrayList<Double>();
			for(Double PNtfidf:PNtfidfValues){
				PNcentroidvalues.add(0.0);
			}
			PNcentroidVector.add(PNcentroidvalues);
		}       
		else{
			PNcentroidvalues = PNcentroidVector.get(centroidVectorIndex);
		}
		
		double totalTfidf , newTfidf, PNtotalTfidf, PNnewTfidf;
		int totalCount;
		for (int i = 0; i < tfidfValues.size(); i++){
			totalTfidf = 0.0; 
			newTfidf = 0.0;
			totalCount = 0;                     
			for(int article: clusters.get(centroidVectorIndex)){
				//totalTfidf += (tfidfDocsVector.get(article)).get(i);
				totalTfidf += (ppx.articleCollection.get(article).tfidfArray).get(i);
				totalCount++;
			}
			if(totalCount > 0){
				newTfidf = totalTfidf/totalCount;
			}
			//System.out.println("i: "+ i+" - tfidf:" + newTfidf);
			centroidvalues.set(i, newTfidf);
		}
		//For Proper Noun
		for (int i = 0; i < PNtfidfValues.size(); i++){
			PNtotalTfidf = 0.0; 
			PNnewTfidf = 0.0;
			totalCount = 0;                     
			for(int article: clusters.get(centroidVectorIndex)){
				//totalTfidf += (tfidfDocsVector.get(article)).get(i);
				PNtotalTfidf += (ppx.articleCollection.get(article).PNtfidfArray).get(i);
				totalCount++;
			}
			if(totalCount > 0){
				PNnewTfidf = PNtotalTfidf/totalCount;
			}
			//System.out.println("i: "+ i+" - tfidf:" + newTfidf);
			PNcentroidvalues.set(i, PNnewTfidf);
		}
	}
	
	private void printClusters(String fileName) throws Exception{
		Writer writer;
		writer = new OutputStreamWriter(
		        new FileOutputStream(fileName+".txt", false), "UTF-8");	
		int i = 0;
		for(ArrayList<Integer> cluster: clusters){
			//System.out.print("Cluster "+i++ +":");
			for(Integer item: cluster){
				//System.out.print(item+",");
				writer.append(item.toString()).append(",");
			}
			writer.append(System.lineSeparator());
			//System.out.println("");
		}
		writer.close();
	}
	
	public void outputClusterData(String fileName, PreProcessor ppx) throws IOException{
		String htmlName = fileName+".html";
		Writer writer;
		writer = new OutputStreamWriter(
		        new FileOutputStream(htmlName, false), "UTF-8");			

		writer.append(System.lineSeparator());
		writer.append("<!DOCTYPE html>").append(System.lineSeparator());
		writer.append("<html>").append(System.lineSeparator());
		writer.append("<head>").append(System.lineSeparator());
		writer.append("<meta charset=\"utf-8\">").append(System.lineSeparator());
		writer.append("<title>Sinhala News</title>").append(System.lineSeparator());
		writer.append("</head>").append(System.lineSeparator());
		writer.append("<body>").append(System.lineSeparator());
		//writer.append("<ul>").append(System.lineSeparator());
		for(ArrayList<Integer> cluster: clusters){
			if(cluster.size() >= 2){
				writer.append("<ul>").append(System.lineSeparator());
				for(Integer item: cluster){
					String link = ppx.articleCollection.get(item).link;
					if(link.length() > 80){
						link =link.substring(0, 80);
						link = link +"...";
					}
					writer.append("<li>")
						.append("[").append(Integer.toString(ppx.articleCollection.get(item).index)).append("]")
						.append(ppx.articleCollection.get(item).heading)
						.append("[ <a href=\"").append(ppx.articleCollection.get(item).link).append("\">").append(link).append("</a> ]</li>").append(System.lineSeparator());
					//writer.append("<li> <p>").append(pp.articleCollection.get(item).heading).append("</p> <a href=\"").append(pp.articleCollection.get(item).link).append("\">Read</a>").append("</li>").append(System.lineSeparator());
				}
				writer.append("</ul>").append(System.lineSeparator());
				//writer.append("<p> </p>").append(System.lineSeparator());
				writer.append("<br>").append(System.lineSeparator());
			}
		}
		//writer.append("</ul>").append(System.lineSeparator());
		writer.append("<p> ------ Single Articles ------</p>").append(System.lineSeparator());
		writer.append("<ul>").append(System.lineSeparator());
		for(ArrayList<Integer> cluster: clusters){
			if(cluster.size() < 2){
				writer.append("<li> [").append(Integer.toString(ppx.articleCollection.get(cluster.get(0)).index)).append("] <a href=\"").append(ppx.articleCollection.get(cluster.get(0)).link).append("\">").append(ppx.articleCollection.get(cluster.get(0)).heading).append("</a> </li>").append(System.lineSeparator());
					//writer.append("<li> <p>").append(pp.articleCollection.get(item).heading).append("</p> <a href=\"").append(pp.articleCollection.get(item).link).append("\">Read</a>").append("</li>").append(System.lineSeparator());
			}
		}
		writer.append("</ul>").append(System.lineSeparator());
		writer.append("</body>").append(System.lineSeparator());
		writer.append("</html>").append(System.lineSeparator());
		writer.close();
	}
	
	public void interClusterSimilarity(){
		double cosine = 0.0;
		CosineSimilarity cs = new CosineSimilarity();
		for(int i=0; i<centroidVector.size(); i++){
			ArrayList<Double> centroid1 = centroidVector.get(i);
			for(int j=i+1; j<centroidVector.size(); j++){
				ArrayList<Double> centroid2 = centroidVector.get(j);
					cosine = cs.cosineSimilarity(centroid1, centroid2);
					//if(cosine > SIMILARITY_TRESHOLD){
						System.out.println("similarity : "+i+"-"+j+"->"+cosine);
					//}
			}
		}
	}
	
	public void updateClusters(int clusterIndex, boolean DW){
		double cosine = 0.0;
		double maxCosine = 0.0;
		int mergeCluster = 0;
		boolean merge = false;
		CosineSimilarity cs = new CosineSimilarity();
		DWCosineSimilarity dwcs = new DWCosineSimilarity();
		ArrayList<Double> centroid1 = centroidVector.get(clusterIndex);
		
		for(int i=0; i<centroidVector.size(); i++){
			if(i != clusterIndex){
				ArrayList<Double> centroid2 = centroidVector.get(i);
				if(!DW){
					cosine = cs.cosineSimilarity(centroid1, centroid2);
				}
				else{
					cosine = dwcs.dwCosineSimilarity(centroid1, centroid2);
				}
				if(cosine > SIMILARITY_TRESHOLD*1.2 && cosine > maxCosine){
					maxCosine = cosine;
					mergeCluster = i;
					merge = true;
				}
			}	
		}
		
		if(merge){
			//printClusters();
			mergeClusters(clusterIndex, mergeCluster);
			//printClusters();
			
		}
	}
	
	public void mergeClusters(int cluster1, int cluster2){
		
		//update centroids
		ArrayList<Double> centroid1 = centroidVector.get(cluster1);
		ArrayList<Double> centroid2 = centroidVector.get(cluster2);
		ArrayList<Double> PNcentroid1 = PNcentroidVector.get(cluster1);
		ArrayList<Double> PNcentroid2 = PNcentroidVector.get(cluster2);	
		
		double temp = 0.0;
		double newtfidf = 0.0;
		double newPNtfidf = 0.0;
		for (int i = 0; i < centroid1.size(); i++){
			temp = centroid1.get(i)*clusters.get(cluster1).size() + centroid2.get(i)*clusters.get(cluster2).size();
			newtfidf = temp/(clusters.get(cluster1).size() + clusters.get(cluster2).size());
			centroidVector.get(cluster1).set(i, newtfidf);
		}
			
		for (int i = 0; i < PNcentroid1.size(); i++){
			temp = PNcentroid1.get(i)*clusters.get(cluster1).size();
			temp += PNcentroid2.get(i)*clusters.get(cluster2).size();
			newPNtfidf = temp/(clusters.get(cluster1).size() + clusters.get(cluster2).size());
			PNcentroidVector.get(cluster1).set(i, newPNtfidf);
		} 
		centroidVector.remove(cluster2);
		PNcentroidVector.remove(cluster2);		
		//PNcentroidvalues.set(i, PNnewTfidf);
		
//	//	System.out.println("Merged : "+clusters.get(cluster1)+" & "+clusters.get(cluster2));
		//update cluster list
		ArrayList<Integer> cluster2List = clusters.get(cluster2);
		cluster2List.addAll(clusters.get(cluster1));
		clusters.set(cluster1, cluster2List);
		//printClusters();
		clusters.remove(cluster2);		
		//printClusters();
		
	}
	 /*public void getCosineSimilarity() {
		CosineSimilarity cs = new CosineSimilarity();
		cosineValues = new double [tfidfDocsVector.size()][tfidfDocsVector.size()];
        for (int i = 0; i < tfidfDocsVector.size(); i++) {
            for (int j = 0; j < tfidfDocsVector.size(); j++) {
            	cosineValues[i][j] = cs.cosineSimilarity(tfidfDocsVector.get(i), tfidfDocsVector.get(j));
            	//cosineValues[i][j] = 0.0;
            }
        }
    }*/
	
	/*private void printCosine(){
        for (int i = 0; i < tfidfDocsVector.size(); i++) {
            for (int j = 0; j < tfidfDocsVector.size(); j++) {
            	System.out.print(String.format( "%.2f", cosineValues[i][j])+", ");
            }
            System.out.println();
        }
	}*/

	static void createNNPList() throws IOException{
		Writer writer = new OutputStreamWriter(
		        new FileOutputStream("NNPs.txt", true), "UTF-8");			
		writer.append(System.lineSeparator());

		Charset charset = Charset.forName("utf-8");
        List<String> lines = Files.readAllLines(Paths.get("C:\\DATA\\CrawlData\\Tagged_WordList.txt"), charset);
        for (int i=0; i<lines.size(); i++) {        
        	int tagStart = lines.get(i).indexOf(' ') + 1;
        	String tag = lines.get(i).substring(tagStart);
        	if(tag.equalsIgnoreCase("NNP")){
        		writer.append(lines.get(i).substring(0, tagStart)).append(System.lineSeparator());
        	}
        }
        writer.close();
	}
	
}
