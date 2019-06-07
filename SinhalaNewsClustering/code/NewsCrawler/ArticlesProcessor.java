package NewsCrawler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;

import edu.uci.ics.crawler4j.parser.HtmlParseData;

class startEndPositions{
	int headingStartPos;
	int headingEndPos;
	int bodyStartPos;
	int bodyEndPos;
}

public class ArticlesProcessor {

	public static int newsID=0;
	public static int maxPagesPerSite = 20;
	public static int maxArticles = 150;
	private static int[] articlesCount = new int[NewsCrawlController.seedData.length];
	private static boolean[] maxCountExceed = new boolean[NewsCrawlController.seedData.length];
	private String fileName = "\\NewsData.txt";
	private static ArrayList<String> processedUrls = new ArrayList<String>();
	private static ArrayList<String> lakbimaTitles = new ArrayList<String>();
	private static ArrayList<String> dinaminaTitles = new ArrayList<String>();
	private static ArrayList<String> lankadeepaTitles = new ArrayList<String>();
	private String fullName = NewsCrawlController.crawlStorageFolder.concat(fileName);
	
	ArticlesProcessor(){
		File dataFile = new File(fullName);
		if(dataFile.exists()){
			dataFile.delete();
		}
		/*
		for(int i= 0; i<maxCountExceed.length; i++){
			maxCountExceed[i] = true;
		}
		maxCountExceed[7] = false;
		*/
	}
	
	public boolean RecordData(String url, HtmlParseData parseData) throws IOException{
		
		startEndPositions positions = new startEndPositions();
		boolean addArticle = false;
		int newsSiteId = 0;

		
		if(newsID > maxArticles){
			System.out.println("Exceeded maximum articles count");
			System.exit(0);
			//return false;
			
		}
				
		//have to process differently for different sites
			
		if(url.startsWith(NewsCrawlController.seedData[0][1]) == true){
			newsSiteId = 0;
			if(maxCountExceed[newsSiteId] == false){
				addArticle = processAdaDerana(parseData, positions);
			}
		}
		else if(url.startsWith(NewsCrawlController.seedData[1][1]) == true){
			newsSiteId = 1;
			if(maxCountExceed[newsSiteId] == false){				
				if(url.contains("cartoon")){
					return true;
				}
				addArticle = processLakbima(parseData, positions);
			}				
		}
		else if(url.startsWith(NewsCrawlController.seedData[2][1]) == true){
			newsSiteId = 2;
			if(maxCountExceed[newsSiteId] == false){
				if(url.endsWith("http://www.lankadeepa.lk/latest_news/1")){
					return true;
				}
				addArticle = processLankadeepa(parseData, positions);
			}
		}			
		else if(url.startsWith(NewsCrawlController.seedData[3][1]) == true){
			newsSiteId = 3;
			if(maxCountExceed[newsSiteId] == false){
				addArticle = processDinamina(parseData, positions);
			}
		}
		else if(url.startsWith(NewsCrawlController.seedData[4][1]) == true){
			newsSiteId = 4;
			if(maxCountExceed[newsSiteId] == false){
				addArticle = processHiruNews(parseData, positions);
			}
		}
		else if(url.startsWith(NewsCrawlController.seedData[5][1]) == true){
			newsSiteId = 5;
			if(maxCountExceed[newsSiteId] == false){
				addArticle = processMawbima(parseData, positions);
			}
		}
		else if(url.startsWith(NewsCrawlController.seedData[6][1]) == true){
			newsSiteId = 6;
			if(url.indexOf("tmpl=component") > 0){
				System.out.println("Skipped");
				return true;
			}
			if(maxCountExceed[newsSiteId] == false){
				addArticle = processTheindependent(parseData, positions);
			}
		}
		else if(url.startsWith(NewsCrawlController.seedData[7][1]) == true){
			newsSiteId = 7;
			if(maxCountExceed[newsSiteId] == false){
				for(String link:processedUrls){
					if(url.compareToIgnoreCase(link) == 0){
						System.out.println("Skipped: "+link);
						return true;
					}
				}
				addArticle = processNewsFirst(parseData, positions);
				if(addArticle){
					processedUrls.add(url);
				}
			}
		}
		
		else if(url.startsWith(NewsCrawlController.seedData[8][1]) == true){
			newsSiteId = 8;
			if(maxCountExceed[newsSiteId] == false){
				addArticle = processRivira(parseData, positions);
			}
		}

		else if(url.startsWith(NewsCrawlController.seedData[9][1]) == true){
			newsSiteId = 9;
			if(maxCountExceed[newsSiteId] == false){
				if(url.endsWith("news.html")){
					return true;
				}
				addArticle = processDivaina(parseData, positions);
			}
		}
		
		else if(url.startsWith(NewsCrawlController.seedData[10][1]) == true){
			newsSiteId = 10;
			if(maxCountExceed[newsSiteId] == false){
				addArticle = processAda(parseData, positions);
			}
		}
		
		if(addArticle){
			if(parseData.getText().substring(positions.bodyStartPos, positions.bodyEndPos).replaceAll("\\s+", "").length() <= 0){
				System.out.println("Skipped");
				return true;
			}
			
			if( articlesCount[newsSiteId]++ > maxPagesPerSite){
				maxCountExceed[newsSiteId] = true;
				System.out.println("Exeeded Maximum count for "+ NewsCrawlController.seedData[newsSiteId][0]);
				return true;
			}
		
			Writer writer;
			try {
				writer = new OutputStreamWriter(
				        new FileOutputStream(fullName, true), "UTF-8");			
	
				writer.append(System.lineSeparator());
				writer.append("<ARTICLE>").append(System.lineSeparator());
				writer.append(" <INDEX> ").append(String.valueOf(newsID++)).append(" </INDEX>").append(System.lineSeparator());
				writer.append(" <LINK> ").append(url).append(" </LINK>").append(System.lineSeparator());	
				writer.append(" <TITLE>  ").append(parseData.getTitle().substring(positions.headingStartPos, positions.headingEndPos)).append(" </TITLE>").append(System.lineSeparator());
				writer.append(" <BODY> ").append(parseData.getText().substring(positions.bodyStartPos, positions.bodyEndPos).replaceAll("[\\t\\n\\r]+"," ").replaceAll("\\s+", " ")).append(" </BODY> ").append(System.lineSeparator());
				writer.append("</ARTICLE>").append(System.lineSeparator());
				writer.close();
			} catch ( IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		return true;
	}

	
	private boolean processAdaDerana(HtmlParseData parseData, startEndPositions positions) throws IOException 
	{
		positions.headingStartPos = 0;
		positions.headingEndPos = parseData.getTitle().length();
		
		if(parseData.getText().indexOf(parseData.getTitle()) <= 0){
			return false;
		}else{
			positions.bodyStartPos = parseData.getText().indexOf(parseData.getTitle()) + parseData.getTitle().length();
		}
		
		if(parseData.getText().indexOf(" am", positions.bodyStartPos ) >= 0 ){
			positions.bodyStartPos = parseData.getText().indexOf(" am", positions.bodyStartPos) + 10;
		}
		else if(parseData.getText().indexOf(" pm", positions.bodyStartPos) >= 0 ){
			positions.bodyStartPos = parseData.getText().indexOf(" pm", positions.bodyStartPos) + 10;
		}
		else{
			return false;
		}
		positions.bodyEndPos = parseData.getText().length();
		if(parseData.getText().indexOf("Most Viewed") > 0){
			positions.bodyEndPos = parseData.getText().indexOf("Most Viewed") ;
		}
		return true;
	}
	
	private boolean processLakbima(HtmlParseData parseData, startEndPositions positions) throws IOException 
	{
		positions.headingStartPos = 0;
		positions.headingEndPos = parseData.getTitle().length();
		
		for(String title:lakbimaTitles){
			if(parseData.getTitle().compareToIgnoreCase(title) == 0){
				System.out.println("Skipped: "+ title);
				return false;
			}
		}
		//positions.bodyStartPos = 0;
		//positions.bodyEndPos = parseData.getText().length();
		
		
		if(parseData.getText().indexOf(parseData.getTitle()) <= 0){
			System.out.println("Skipped");
			return false;
		}
		else{
			positions.bodyStartPos = parseData.getText().indexOf(parseData.getTitle()) + parseData.getTitle().length();
		}
		positions.bodyEndPos = parseData.getText().length();
		if(parseData.getText().indexOf("සටහන -") > 0){
			positions.bodyEndPos = parseData.getText().indexOf("සටහන -") ;
		}
		if(parseData.getText().indexOf("Hits:") > 0){
			positions.bodyEndPos = parseData.getText().indexOf("Hits:") ;
		}
		if(parseData.getText().substring(positions.bodyStartPos, positions.bodyEndPos) .indexOf("October") > 0){
			positions.bodyEndPos = positions.bodyStartPos + parseData.getText().substring(positions.bodyStartPos, positions.bodyEndPos) .indexOf("October") - 3 ;
		}
		
		lakbimaTitles.add(parseData.getTitle());
		
		return true;
	}
	
	private boolean processLankadeepa(HtmlParseData parseData, startEndPositions positions) throws IOException 
	{
		int tempPosition = 0;
		positions.headingStartPos = 0;
		positions.headingEndPos = parseData.getTitle().length();

		positions.headingStartPos = ("Lankadeepa Online||  ").length(); 
		
		String title = parseData.getTitle().substring(positions.headingStartPos);

		for(String t:lankadeepaTitles){
			if(parseData.getTitle().compareToIgnoreCase(t) == 0){
				System.out.println("Skipped: "+ t);
				return false;
			}
		}
		
		positions.bodyStartPos =0;
		positions.bodyEndPos = parseData.getText().length();
		
		tempPosition = parseData.getText().indexOf(title);
		if(tempPosition <= 0){
			return false;
		}
		else{		
			positions.bodyStartPos = tempPosition + title.length();
			tempPosition = parseData.getText().substring(positions.bodyStartPos).indexOf("(");
			if( tempPosition < 100 && tempPosition > 0){
				tempPosition = parseData.getText().substring(positions.bodyStartPos).indexOf(")");
				if(tempPosition > 0){
					positions.bodyStartPos = positions.bodyStartPos + tempPosition + 2;
				}
			}
			tempPosition = parseData.getText().indexOf("Recommended Articles");
			if(tempPosition > 0){
				positions.bodyEndPos = tempPosition;
			}			
		}

		lankadeepaTitles.add(title);
		return true;
	}
	
	private boolean processDinamina(HtmlParseData parseData, startEndPositions positions) throws IOException 
	{
		positions.headingStartPos = 0;
		positions.headingEndPos = parseData.getTitle().indexOf(" | දිනමිණ");
		String title = parseData.getTitle().substring(0, positions.headingEndPos);
		
		for(String tit:dinaminaTitles){
			if(parseData.getTitle().compareToIgnoreCase(tit) == 0){
				System.out.println("Skipped: "+ tit);
				return false;
			}
		}
		
		if(parseData.getText().indexOf(title) <= 0){
			return false;
		}
		else{
			positions.bodyStartPos = parseData.getText().indexOf(title) + title.length();
		}
		//get the second one
		if(parseData.getText().substring(positions.bodyStartPos).indexOf(title) <= 0){
			return false;
		}
		else{
			positions.bodyStartPos += parseData.getText().substring(positions.bodyStartPos).indexOf(title) + title.length();
			if(parseData.getText().substring(positions.bodyStartPos).indexOf("2017") > 0){
				positions.bodyStartPos += parseData.getText().substring(positions.bodyStartPos).indexOf("2017") + 12;
			}			
		}		
		
		positions.bodyEndPos = parseData.getText().length();
		if(parseData.getText().indexOf("නව අදහස දක්වන්න") > 0){
			positions.bodyEndPos = parseData.getText().indexOf("නව අදහස දක්වන්න");
		}
	
		//positions.bodyStartPos = 0;
		//positions.bodyEndPos = parseData.getText().length();
		
		dinaminaTitles.add(parseData.getTitle());
		return true;
	}
	
	private boolean processHiruNews(HtmlParseData parseData, startEndPositions positions) throws IOException 
	{ 
		positions.headingStartPos = 0;
		if(parseData.getTitle().indexOf("Update") > 0){
			positions.headingStartPos += "Update".length() + 3; 
		}

		if(parseData.getTitle().indexOf("Hiru News") <=0){
			System.out.println("Skipped");
			return false;
		}
		else{
			positions.headingEndPos = parseData.getTitle().indexOf("Hiru News") - 3;
		}

		if(parseData.getTitle().indexOf("(ඡායාරූප)") > 0){
			positions.headingEndPos = parseData.getTitle().indexOf("(ඡායාරූප)");
		}

		//body
		if(parseData.getText().indexOf(parseData.getTitle().substring(0, positions.headingEndPos)) <= 0){
			return false;
		}
		else{
			positions.bodyStartPos = parseData.getText().indexOf(parseData.getTitle().substring(0, positions.headingEndPos)) + parseData.getTitle().substring(0, positions.headingEndPos).length();
		}

		if(parseData.getText().indexOf("Views", positions.bodyStartPos) > 0){
			positions.bodyStartPos = parseData.getText().indexOf("Views", positions.bodyStartPos) + 6;			
		}
		positions.bodyEndPos = parseData.getText().length();
		if(parseData.getText().indexOf("DOWNLOAD HIRUNEWS APP") > 0){
			positions.bodyEndPos = parseData.getText().indexOf("DOWNLOAD HIRUNEWS APP") ;
		}
		if(parseData.getText().indexOf("Update-") > 0 && parseData.getText().indexOf("Update-") < positions.bodyEndPos){
			positions.bodyEndPos = parseData.getText().indexOf("Update-") ;
		}
		if(parseData.getText().indexOf("Make a Comment") > 0 && parseData.getText().indexOf("Make a Comment") < positions.bodyEndPos){
			positions.bodyEndPos = parseData.getText().indexOf("Make a Comment") ;
		}
		return true;
	}

	private boolean processMawbima(HtmlParseData parseData, startEndPositions positions) throws IOException 
	{
		positions.headingStartPos = 0;
		if(parseData.getTitle().indexOf("Mawbima.lk") <=0){
			System.out.println("Skipped");
			return false;
		}
		else{
			positions.headingEndPos = parseData.getTitle().indexOf("Mawbima.lk") - 3;
		}
		
		if(parseData.getText().indexOf("By mawbima", positions.bodyStartPos) > 0){
			positions.bodyStartPos = parseData.getText().indexOf("By mawbima") + "By mawbima".length() + 11 ;
			/*
			//get rid of continuous \n s
			while((parseData.getText().indexOf("\n", positions.bodyStartPos) == positions.bodyStartPos) || (parseData.getText().indexOf(" ", positions.bodyStartPos) == positions.bodyStartPos)){
				positions.bodyStartPos++;
			}
			positions.bodyStartPos++;
			positions.bodyStartPos = parseData.getText().indexOf("\n", positions.bodyStartPos+1);
			*/
		}
		positions.bodyEndPos = parseData.getText().length();
		if(parseData.getText().indexOf("තීරු ලිපි", positions.bodyStartPos) > 0){
			positions.bodyEndPos = parseData.getText().indexOf("තීරු ලිපි", positions.bodyStartPos) ;
		}
		int tempIndex = parseData.getText().substring(positions.bodyStartPos, positions.bodyEndPos).lastIndexOf("ඡායාරූප");
		if( tempIndex > 0 && 50 > (positions.bodyEndPos - positions.bodyStartPos) - tempIndex){
			positions.bodyEndPos = positions.bodyStartPos + tempIndex;
		}
		//positions.bodyStartPos = 0;
		return true;
	}
	
	private boolean processTheindependent(HtmlParseData parseData, startEndPositions positions) throws IOException 
	{	

		if (parseData.getTitle().startsWith("Theindependent.lk Sinhala Ver")) {
			positions.headingStartPos = "Theindependent.lk Sinhala Ver".length() + 2;
		}
		positions.headingEndPos = parseData.getTitle().length();
		
		if(parseData.getText().indexOf("votes") < 0){
			return false;
		}
		else{
			positions.bodyStartPos = parseData.getText().indexOf("votes")+125; 
		}
		positions.bodyEndPos = parseData.getText().length();
		if(parseData.getText().indexOf("Tweet") > 0){
			positions.bodyEndPos = parseData.getText().indexOf("Tweet") ;
		}
		return true;
	}
	
	private boolean processNewsFirst(HtmlParseData parseData, startEndPositions positions) throws IOException 
	{
		positions.headingStartPos = 0;
		positions.headingEndPos = parseData.getTitle().length() - "Sri Lanka News".length() -3;
		if( parseData.getTitle().indexOf("Sri Lanka News") < positions.headingEndPos+3){
			System.out.println("Skipped");
			return false;
		}
		if( parseData.getTitle().startsWith("JUST IN:")){
			positions.headingStartPos = "JUST IN:".length() +1;
		}
		else if( parseData.getTitle().startsWith("Breaking News")){
			positions.headingStartPos = "Breaking News".length() +3;
		}
		else if( parseData.getTitle().startsWith("BREAKING")){
			positions.headingStartPos = "BREAKING".length() +3;
		}
		
		positions.bodyStartPos = 0;
		//positions.bodyEndPos = parseData.getText().length();
		
		
		if(parseData.getText().indexOf("News Ticker") > 0 ){
			positions.bodyStartPos = parseData.getText().indexOf("News Ticker") + 50; 
		}
		if(parseData.getText().indexOf("Slider") > 0 ){
			positions.bodyStartPos = parseData.getText().indexOf("Slider") + 50; 
		}		
		if(parseData.getText().indexOf("tweet") > 0 ){
			positions.bodyStartPos = parseData.getText().indexOf("tweet") + 6; 
		}
		
		positions.bodyEndPos = parseData.getText().length();
		if(parseData.getText().indexOf("For the latest news") > 0){
			positions.bodyEndPos = parseData.getText().indexOf("For the latest news") ;
		}
		
		return true;
	}
	
	private boolean processRivira(HtmlParseData parseData, startEndPositions positions) throws IOException 
	{
		positions.headingStartPos = 0;
		positions.headingEndPos = parseData.getTitle().length();
		if(parseData.getTitle().indexOf("» Rivira Online") < 0){
			System.out.println("Skipped - title error");
			return false;
		}
		else{
			positions.headingEndPos = parseData.getTitle().indexOf("» Rivira Online")-1;
		}

		String heading = parseData.getTitle().substring(positions.headingStartPos, positions.headingEndPos);
		positions.bodyStartPos = 0;
		//positions.bodyEndPos = parseData.getText().length();
		
		//first match
		if(parseData.getText().indexOf(heading) <= 0){
			System.out.println("Skipped");
			return false;
		}
		else{
			positions.bodyStartPos = parseData.getText().indexOf(heading) + heading.length();
			//second match
			if(parseData.getText().indexOf(heading, positions.bodyStartPos) <= 0){
				System.out.println("Skipped");
				return false;
			}
			else{
				positions.bodyStartPos = parseData.getText().indexOf(heading, positions.bodyStartPos)+ heading.length();
				//third match
				if(parseData.getText().indexOf(heading, positions.bodyStartPos) <= 0){
					System.out.println("Skipped");
					return false;
				}
				else{
					positions.bodyStartPos = parseData.getText().indexOf(heading, positions.bodyStartPos);
					positions.bodyStartPos += heading.length();
					positions.bodyStartPos = parseData.getText().indexOf("2017", positions.bodyStartPos)+"2017".length()+37;
				}				
			}
		}
			
		positions.bodyEndPos = parseData.getText().length();
		if(parseData.getText().indexOf("SHARE", positions.bodyStartPos) > 0){
			positions.bodyEndPos = parseData.getText().indexOf("SHARE", positions.bodyStartPos) ;
		}
		else{
			System.out.println("Skipped");
			return false;
		}
		if(parseData.getText().indexOf("TAGS", positions.bodyStartPos) > 0){
			positions.bodyEndPos = parseData.getText().indexOf("TAGS", positions.bodyStartPos) ;
		}		
		
		return true;

	}
	
	private boolean processDivaina(HtmlParseData parseData, startEndPositions positions) throws IOException 
	{
		positions.headingStartPos = 0;
		positions.headingEndPos = parseData.getTitle().length();
		positions.bodyStartPos =0;
		positions.bodyEndPos = parseData.getText().length();
		/*
		if(parseData.getText().indexOf(parseData.getTitle()) <= 0){
			System.out.println("Skipped");
			return false;
		}
		else{
			positions.bodyStartPos = parseData.getText().indexOf(parseData.getTitle())+parseData.getTitle().length(); 
		}
		
		if(parseData.getText().indexOf("Tweet",positions.bodyStartPos) <= 0){
			System.out.println("Skipped");
			return false;
		}
		else{
			positions.bodyStartPos = parseData.getText().indexOf("Tweet",positions.bodyStartPos) + 6; 
		}
		
		positions.bodyEndPos = parseData.getText().length();
		if(parseData.getText().substring(positions.bodyStartPos).indexOf("අදහස්(") > 0){
			positions.bodyEndPos = positions.bodyStartPos + parseData.getText().substring(positions.bodyStartPos).indexOf("අදහස්(") ;
		}
		*/
		return true;
	}

	private boolean processAda(HtmlParseData parseData, startEndPositions positions) throws IOException 
	{
		//int tempPosition = 0;
		positions.headingStartPos = 0;
		positions.headingEndPos = parseData.getTitle().length();
		positions.bodyStartPos =0;
		positions.bodyEndPos = parseData.getText().length();
		
		/*
		if(parseData.getText().indexOf(parseData.getTitle()) <= 0){
			System.out.println("Skipped");
			return false;
		}
		else{
			positions.bodyStartPos = parseData.getText().indexOf(parseData.getTitle())+parseData.getTitle().length(); 
		}
		
		if(parseData.getText().indexOf("Tweet",positions.bodyStartPos) <= 0){
			System.out.println("Skipped");
			return false;
		}
		else{
			positions.bodyStartPos = parseData.getText().indexOf("Tweet",positions.bodyStartPos) + 6; 
		}
		
		positions.bodyEndPos = parseData.getText().length();
		if(parseData.getText().substring(positions.bodyStartPos).indexOf("අදහස්(") > 0){
			positions.bodyEndPos = positions.bodyStartPos + parseData.getText().substring(positions.bodyStartPos).indexOf("අදහස්(") ;
		}
		*/
		return true;
	}
	
}
