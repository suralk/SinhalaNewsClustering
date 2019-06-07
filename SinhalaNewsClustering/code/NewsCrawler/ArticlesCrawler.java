package NewsCrawler;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.http.Header;

public class ArticlesCrawler extends WebCrawler {

	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g" + "|png|tiff?|mid|mp2|mp3|mp4"
			+ "|wav|avi|mov|mpeg|ram|m4v|pdf" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

	private ArticlesProcessor dataSaver = new ArticlesProcessor();
	
	private static boolean continueFetching = true;
	
	//private String[] myCrawlDomains;

	//@Override
	//public void onStart() {
	//	myCrawlDomains = (String[]) myController.getCustomData(); 
	//}
	
	/**
	 * specify whether the given url should be crawled or not 
	 */
	@Override
	public boolean shouldVisit(WebURL url) {
		if(!continueFetching ){
			return false;
		}
		String href = url.getURL().toLowerCase();
		if (FILTERS.matcher(href).matches()) {
			return false;
		}
		//System.out.println("checked: "+href);
		//Date today = new Date( );
	    //SimpleDateFormat formattedDate = new SimpleDateFormat ("yyyy/MM/dd");
	    
		for(int i=0; i<NewsCrawlController.seedData.length; i++){
		//	String filter = NewsCrawlController.seedData[i][1].replaceAll("<DATE>", formattedDate.format(today));
			
			if (href.startsWith(NewsCrawlController.seedData[i][1])) {			
				//System.out.println("l1: "+href.length()+"l2: "+NewsCrawlController.seedData[i][1].length()+ " > "+ href);
				return true;
			}
		}
		return false;
	}

	/**
	 * This function is called when a page is fetched and ready to be processed
	 * by your program.
	 */
	@Override
	public void visit(Page page) {
		int docid = page.getWebURL().getDocid();
		String url = page.getWebURL().getURL();
		String domain = page.getWebURL().getDomain();
		String path = page.getWebURL().getPath();
		String subDomain = page.getWebURL().getSubDomain();
		String parentUrl = page.getWebURL().getParentUrl();
		String anchor = page.getWebURL().getAnchor();


		//System.out.println("Docid: " + docid);
		//System.out.println("URL: " + url);
		//System.out.println("Domain: '" + domain + "'");
		//System.out.println("Sub-domain: '" + subDomain + "'");
		//System.out.println("Path: '" + path + "'");
		//System.out.println("Parent page: " + parentUrl);
		//System.out.println("Anchor text: " + anchor);
		
		if(!continueFetching ){
			return;
		}
		
		if (page.getParseData() instanceof HtmlParseData) {
					
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			//String text = htmlParseData.getText();
			String html = htmlParseData.getHtml();
		
			for(int i=0; i<NewsCrawlController.seedData.length; i++){
				//	String filter = NewsCrawlController.seedData[i][1].replaceAll("<DATE>", formattedDate.format(today));
					
				if (url.startsWith(NewsCrawlController.seedData[i][1])) {
				//if ((url.length()>= NewsCrawlController.seedData[i][1].length())) {	//to cut the seed page		
					//System.out.println("Visited: "+url);
					try {
						continueFetching = dataSaver.RecordData(url, htmlParseData);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			List<WebURL> links = htmlParseData.getOutgoingUrls();
			String title = htmlParseData.getTitle();

			
			//System.out.println(title);
			//System.out.println("Text length: " + text.length());
			//System.out.println("Html length: " + html.length());
			
			//System.out.println("Number of outgoing links: " + links.size());
			
			//for(WebURL l: links){
			//	System.out.println(l.getURL());
			//}
			
		}

		Header[] responseHeaders = page.getFetchResponseHeaders();
		if (responseHeaders != null) {
			//System.out.println("Response headers:");
			//for (Header header : responseHeaders) {
			//	System.out.println("\t" + header.getName() + ": " + header.getValue());
			//}
		}
		
		//System.out.println("=============");
	}
}

	

