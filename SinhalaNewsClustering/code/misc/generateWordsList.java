package misc;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;

public class generateWordsList {

	public static void main(String[] args) throws IOException, Exception {
		Pattern p1 = Pattern.compile("[(?![@'‘’”“\",&])\\p{Punct}]");
		String file = "F:\\MSc\\NewsData\\NewsData_0416.txt";
		Charset charset = Charset.forName("utf-8");
		
		Writer writer = new OutputStreamWriter(
        new FileOutputStream("WordsList.txt", true), "UTF-8");			
		//Writer writer2 = new OutputStreamWriter(
		//        new FileOutputStream("Sentences.txt", true), "UTF-8");
		
        List<String> lines = Files.readAllLines(Paths.get(file), charset);
        for (int i=0; i<lines.size(); i++) {
    		String line = lines.get(i);
    		if(lines.get(i).startsWith(" <TITLE>") || lines.get(i).startsWith(" <BODY>")){
	        	if(lines.get(i).startsWith(" <TITLE>") ){
	        		int start = (" <TITLE>").length();
	        		int end = line.indexOf("</TITLE>");
					//writer2.append(line.substring(start, end)).append(System.lineSeparator());
	        		line = line.substring(start, end);
	        		line = line.replaceAll("\\s+", " ").trim();
	        		writer.append("<START>").append(System.lineSeparator());
        		/*for(int j=0; j<line.length(); j++){
        			a = line.substring(j, j+1);
        			if(a.contentEquals(" ")){
        				writer.append(System.lineSeparator());
        				System.out.println();
        				lastSpace = true;
        			}else{
        				if(p1.matcher(a).matches() ){
        					if(!lastSpace){
        						writer.append(System.lineSeparator());
            					System.out.println();
        					}
            				writer.append(a);
            				System.out.print(a);
        					lastSpace = false;
        				}else{
        					lastSpace = false;
            				writer.append(a);
            				System.out.print(a);
        				}

        			}        			
        		}*/
    			//for(String word: lines.get(i).split("\\s")){
    			//	word = word.trim().replaceAll("\u00A0", "");
    			//	if(!word.contentEquals("<TITLE>") && !word.contentEquals("</TITLE>")){
    			//		writer.append(word).append(System.lineSeparator());
    			//	}
    			//}
        	

	        	}else if(lines.get(i).startsWith(" <BODY>") ){
	            	writer.append(System.lineSeparator());
	        		int start = (" <BODY>").length();
	        		int end = line.indexOf("</BODY>");
	        		line = line.substring(start, end);
	        		line = line.replaceAll("\\s+", " ").trim();
	        	}
        		String a;
        		boolean lastSpace =false;
        		for(int j=0; j<line.length(); j++){
        			a = line.substring(j, j+1);
        			if(a.contentEquals(" ") ){
        				if(!lastSpace){
        				writer.append(System.lineSeparator());
        				//System.out.println();
        				lastSpace = true;
        				}
        			}else{
        				if(p1.matcher(a).matches() ){
        					if(!lastSpace){
        						writer.append(System.lineSeparator());
            					//System.out.println();
        					}
            				writer.append(a);
            				//System.out.print(a);
            				writer.append(System.lineSeparator());
        					lastSpace = true;
        				}else{
        					lastSpace = false;
            				writer.append(a);
            				//System.out.print(a);
        				}

        			}
        		}
        		writer.append(System.lineSeparator());
        	}
    	}
        writer.close();
        //writer2.close();
	}
}
