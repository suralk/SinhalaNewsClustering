package misc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class UCSCCommonWords {

	public static void main(String[] args) throws IOException {
		createCorpusFile();

	}

	static void createCorpusFile() throws IOException{
		//Charset charset = Charset.forName("utf-8");
		Writer writerOUT = new OutputStreamWriter(
		        new FileOutputStream("WordsListUCSC.txt", true), "UTF-8");
		try(Stream<Path> paths = Files.walk(Paths.get("F:\\MSc\\CourpusForCommonWords\\All"))) {
		    paths.forEach(filePath -> {
		        if (Files.isRegularFile(filePath)) {
					try {
						
					    List<String> lines = Files.readAllLines(filePath);
					    
					    for(String line:lines){
					    	writerOUT.append(line).append(System.lineSeparator());
					    }
						
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        }
		    });
			writerOUT.close();
		}
	}
}
