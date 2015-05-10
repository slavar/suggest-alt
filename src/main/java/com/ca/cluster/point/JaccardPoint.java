package com.ca.cluster.point;

import java.util.Map;

/**
 * Computes Jacard's distance between centroid and given point
 * D = 1 - (P/(P+Q+R))
 * where:
 * P is number of variables that positive on both points
 * Q is the number of variables that positive on centroid but negative on compared point
 * R is the number of variables that negative on centroid but positive on compared point
 */
public class JaccardPoint extends BasicPoint {

	public void calculateDistance() {
		Map<String,String> centroidAttributes = centroid.getAttributes();
		Map<String,String> pointAttributes = getAttributes(); 
		Double P = 0.0, Q = 0.0, R = 0.0;
		for(String attrName:centroidAttributes.keySet()){
			//check if such attribute exists in point
			if(pointAttributes.containsKey(attrName)){
				String pointAttributeValue = pointAttributes.get(attrName);
				String centroidAttributeValue = centroidAttributes.get(attrName);
				if(centroidAttributeValue == null){
					continue;
				}
				
				if(centroidAttributeValue.equals(pointAttributeValue)){
					P++; 
				}else{
					Q++;
				}
			}else{
				R++;
			}
		}
		distance = 1-(P/(P+Q+R));
	}

}
