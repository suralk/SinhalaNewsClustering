package sinhalaNewsCluster;

import java.util.ArrayList;
import java.lang.Math;

public class DWCosineSimilarity {

    /**
     * Method to calculate distance weighted cosine similarity between two documents.
     * @param docVector1 : document vector 1 (a)
     * @param docVector2 : document vector 2 (b)
     * @return 
     */
    public double dwCosineSimilarity(ArrayList<Double> docVector1, ArrayList<Double> docVector2) {
        double dwCosineSimilarity = 0.0;
    	double dotProduct = 0.0;
        double magnitude1 = 0.0;
        double magnitude2 = 0.0;
        double cosineSimilarity = 0.0;
        double hammingDistance = 0.0;

        for (int i = 0; i < docVector1.size(); i++) //docVector1 and docVector2 must be of same length
        {
            dotProduct += docVector1.get(i) * docVector2.get(i);  //a.b
            magnitude1 += Math.pow(docVector1.get(i), 2);  //(a^2)
            magnitude2 += Math.pow(docVector2.get(i), 2); //(b^2)
        }

        magnitude1 = Math.sqrt(magnitude1);//sqrt(a^2)
        magnitude2 = Math.sqrt(magnitude2);//sqrt(b^2)

        if (magnitude1 != 0.0 | magnitude2 != 0.0) {
            cosineSimilarity = dotProduct / (magnitude1 * magnitude2);
        } else {
            return 0.0;
        }
        
        hammingDistance = wightedHammingDistance(docVector1, docVector2);
        dwCosineSimilarity = cosineSimilarity/(hammingDistance*hammingDistance+cosineSimilarity);
        
        return dwCosineSimilarity;
    }
    
    public double wightedHammingDistance(ArrayList<Double> docVector1, ArrayList<Double> docVector2){
    	double wHammingDistance = 0.0;
    	double sumVector1 = 0.0;
    	double sumVector2 = 0.0;
    	double hammingVector1 = 0.0;
    	double hammingVector2 = 0.0;
    	double tempValue = 0.0;
    	
    	//calculate sum of vector1 and vector2
        for (int i = 0; i < docVector1.size(); i++) //docVector1 and docVector2 must be of same length
        {
        	sumVector1 += docVector1.get(i);
        	sumVector2 += docVector2.get(i);
        	
        	//vector1
        	tempValue = docVector1.get(i)*(1- Math.signum(docVector1.get(i)*docVector2.get(i)));
        	hammingVector1 += tempValue;
        	//vector2
        	tempValue = docVector2.get(i)*(1- Math.signum(docVector2.get(i)*docVector1.get(i)));
        	hammingVector2 += tempValue;  
        }	
    	
        
        wHammingDistance = (hammingVector1/sumVector1) + (hammingVector2/sumVector2);
        //wHammingDistance = 0.0;
    	return wHammingDistance;
    }
}
