package NewsCrawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class NewsCrawlController {

	//public static String crawlStorageFolder = ".";
	public static String crawlStorageFolder = "C:\\DATA\\CrawlData";
	public static int numberOfCrawlers = 10;
	
	public static String seedData[][] = {
		{"http://sinhala.adaderana.lk/","http://sinhala.adaderana.lk/news."},
		{"http://www.lakbima.lk/","http://www.lakbima.lk/index.php/2016-"},
		{"http://www.lankadeepa.lk/latest_news/1","http://www.lankadeepa.lk/latest_news/"},
		//use below temp{"http://www.dinamina.lk/?q=news","http://www.dinamina.lk/2017/05/29/"},                            //<-YYYY/MM/DD
		{"http://www.dinamina.lk/?q=news","http://www.dinamina.lk/2017/06/"},                            //<-YYYY/MM/DD
		{"http://www.hirunews.lk/sinhala/","http://www.hirunews.lk/sinhala/15"},
		{"http://www.mawbima.lk/printedition.php","http://www.mawbima.lk/print2017"},
		{"http://theindependent.lk/","http://theindependent.lk/index.php/2015-02-04-20-18-00/item/"}, 
		{"http://newsfirst.lk/sinhala/category/local/","http://newsfirst.lk/sinhala/2017/06"},				//<-YYYY/MM
		{"http://www.rivira.lk/online/pages/news/breakingnews/","http://www.rivira.lk/online/2017/06"},	//<-YYYY/MM/DD
	};
	
	
	public static void main(String[] args) throws Exception {
		NewsCrawlController crawler = new NewsCrawlController();
		crawler.loadParams();
		crawler.runCrawler();
	}
	
	
	public void loadParams() throws FileNotFoundException, IOException{
		File f = new File("CONFIG.txt");
		if(f.exists() && !f.isDirectory()) {
			try(BufferedReader br = new BufferedReader(new FileReader(f))) {
			    for(String line; (line = br.readLine()) != null; ) {
			        if(line.startsWith("TOTAL")){
			        	int num = Integer.parseInt(line.substring("TOTAL".length()+1));
			        	//		;
			        	ArticlesProcessor.maxArticles = num;
			        }else if(line.startsWith("PERSITE")){
				        	int num = Integer.parseInt(line.substring("PERSITE".length()+1));
				        	//		;
				        	ArticlesProcessor.maxPagesPerSite = num;
			        }
			        
			    }
			}
			crawlStorageFolder = ".";
			
		}
	}
	
	public void runCrawler() throws Exception{
		double startTime = System.currentTimeMillis();
		System.out.println("---NEWS FETCHER START---");

		CrawlConfig config = new CrawlConfig();

		config.setCrawlStorageFolder(crawlStorageFolder);

		config.setPolitenessDelay(1000);

		config.setMaxDepthOfCrawling(2);

		config.setMaxPagesToFetch(500);

		config.setResumableCrawling(false);
		
		config.setFollowRedirects(false);
		
		/*
		 * Instantiate the controller for this crawl.
		 */
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

		/*
		 * For each crawl, you need to add some seed urls. These are the first
		 * URLs that are fetched and then the crawler starts following links
		 * which are found in these pages
		 */

		for(int i=0; i<seedData.length; i++){
			controller.addSeed(seedData[i][0]);
			//System.out.println("Added: "+seedData[i][0]);
		}
		
		/*
		 * Start the crawl. This is a blocking operation, meaning that your code
		 * will reach the line after this only when crawling is finished.
		 */
		controller.start(ArticlesCrawler.class, numberOfCrawlers);
		System.out.println("---NEWS FETCHER FINISHED--in-"+ ((System.currentTimeMillis()-startTime)/1000)+"s");
	}
}

