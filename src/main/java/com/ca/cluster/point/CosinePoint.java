package com.ca.cluster.point;

import java.util.Map;

/*
	Represents a similarity measured by Cosinal distance between two objects
	sim = cosine(theta) = A*B/(|A|*|B|)

	Modified weighted K-Means
 */
public class CosinePoint extends BasicPoint {

	public void calculateDistance(){
		Map<String,String> centroidAttributes = centroid.getAttributes();
		Map<String,String> pointAttributes = getAttributes(); 
		
		Double dotProduct = 0D;
		Double normA = 0D,normB = 0D;
		for(String centroidAttributeName:centroidAttributes.keySet()){
			Object attrValue = centroidAttributes.get(centroidAttributeName);
			if(!(attrValue instanceof String)){
				continue;
			}
			String centroidAttrValue = centroidAttributes.get(centroidAttributeName);
			String pointAttrValue = pointAttributes.get(centroidAttributeName);
			if(pointAttrValue != null && centroidAttrValue.equalsIgnoreCase(pointAttrValue)){
				dotProduct += getWeightForAttribute(centroidAttributeName);
				normB+= getWeightForAttribute(centroidAttributeName);
			}		
			
			normA+= getWeightForAttribute(centroidAttributeName);//always 1
		}
		
		distance = dotProduct/ Math.sqrt(normA) * Math.sqrt(normB);
	}
}
