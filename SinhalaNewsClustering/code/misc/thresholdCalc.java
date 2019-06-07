package misc;

import FMeasure.calculateFMeasure;
import sinhalaNewsCluster.ArticlesCluster;
import sinhalaNewsCluster.PreProcessor;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class thresholdCalc {
	public static void main(String[] args) throws Exception {
		thresholdCalc th = new thresholdCalc();
		th.threshold();
		System.out.println("FINISH");
	}
	
	public void threshold() throws Exception
	{
		//String folder = "F:\\MSc\\threshold\\ActualData\\DWTW";
		String folder = "F:\\MSc\\threshold\\PNTWDW";
		//String folder = "F:\\MSc\\threshold\\noStopWords";

		String file1 = "NewsData_0320";
		String file2 = "NewsData_0324";
		String file3 = "NewsData_0327";
		String file4 = "NewsData_0329";
		String file5 = "NewsData_0401";
		String file6 = "NewsData_0404";
		String file7 = "NewsData_0406";
		String file8 = "NewsData_0408";
		String file9 = "NewsData_0411";
		String file10 = "NewsData_0416";
		//String file1 = "NewsData_0425";
		//String file2 = "NewsData_0421";
		//String file3 = "NewsData_0418";
		//String file4 = "NewsData_1103";
		//String file5 = "NewsData_1110";*/
		oneFile(folder, file1);
		oneFile(folder, file2);
		oneFile(folder, file3);
		oneFile(folder, file4);
		oneFile(folder, file5);
		oneFile(folder, file6);
		oneFile(folder, file7);
		oneFile(folder, file8);
		oneFile(folder, file9);
		oneFile(folder, file10);
		
	}
	
	
	public void oneFile(String folder, String file) throws Exception{
		calculateFMeasure cf = new calculateFMeasure();

		PreProcessor pp = new PreProcessor();
		
		Writer writer = new OutputStreamWriter(
		        new FileOutputStream(folder+"\\FScoreValues.txt", true));
		String fileFname = folder+"\\"+file+"\\"+file+".txt";
		String manual = folder+"\\"+file+"\\manual.txt";
		//pp.readNewsFile(fileFname, 1, 0, false, false); //without stopwords
		//pp.readNewsFile(fileFname, 1, 2, false, false); //nipatha stopwords
		//pp.readNewsFile(fileFname, 1, 1, false, false); //All stopwords
		//pp.readNewsFile(fileFname, 1, 1, false, false); //DW-cosine
		//pp.readNewsFile(fileFname, 2, 1, false, false);//DWTW
		//pp.readNewsFile(fileFname, 1, 1, true, false);//TitleOnly
		//pp.readNewsFile(fileFname, 2, 1, false, false);//TW2
		//pp.readNewsFile(fileFname, 3, 1, false, false);//TW3
		//pp.readNewsFile(fileFname, 1, 1, false, true);//properNames
		//pp.readNewsFile(fileFname, 2, 0, false, true);//properNames, TW2 withoutstopwords
		pp.readNewsFile(fileFname, 2, 1, false, true);//properNames -with TW2
		//pp.readNewsFile(fileFname, 2, 1, false, true);//properNames -with TW2
		pp.calcTfIdf();
		
		String folderName = fileFname.substring(0, fileFname.lastIndexOf("\\"))+"\\results";
		Files.createDirectories(Paths.get(folderName));
		
		//for(int i=5; i<50; i++){
		//for(int i=0; i<600; i++){
			ArticlesCluster ac = new ArticlesCluster();
			int i = 92; 
			ac.SIMILARITY_TRESHOLD = i/1000.0;
			//ac.SIMILARITY_TRESHOLD = .179; 
			//System.out.println("TRESHOLD:"+SIMILARITY_TRESHOLD);
			String numberAsString = String.format ("%03d", i);
			String fileName = folderName+"\\"+numberAsString+".txt";
			//ac.assignToClusters(false, fileName, pp, true);	//properNames
			ac.assignToClusters(true, fileName, pp, true);	//properNames+DW
			//ac.assignToClusters(true, fileName, pp, false);	//DW
			//ac.assignToClusters(false, fileName, pp, false);//other
			//ac.outputClusterData(fileName.substring(0, fileName.indexOf(".")), pp);
		//}
		
		
		try(Stream<Path> paths = Files.walk(Paths.get(folder+"\\"+file+"\\results"))) {
		    paths.forEach(filePath -> {
		        if (Files.isRegularFile(filePath)) {
		        	try {
		        		Double Fvalue = cf.FMeasure(manual, filePath.toString());
		        		writer.append(Fvalue.toString()).append(",");
		        		//System.out.println("***********"+Fvalue);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        }
		    });
		}
		writer.append(System.lineSeparator());
		writer.close();
		
	}		
}
