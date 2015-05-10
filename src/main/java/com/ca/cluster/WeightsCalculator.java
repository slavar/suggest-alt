package com.ca.cluster;

import com.ca.cluster.point.Point;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class WeightsCalculator {
	
	private List<Point> data;
	private Double threshold;
	
	private static int weightProbeEval = 3;
	
	int clusterSizeForWeightsComputation = 10;//10%
	
	public WeightsCalculator(List<Point> data, Double threshold){
		this.data = data;
		this.threshold = threshold;		
				
		if(data.size() < 1000){// if data size is lesser than 1000 items, then we increase the temporary cluster size to include more objects
			clusterSizeForWeightsComputation = 50;
			weightProbeEval = 5;
		}else
		if(data.size() > 100000){
			clusterSizeForWeightsComputation = 5;//5% of probing should be enough for large data sets
		}
	}
	
	public Map<String, Double> calculateWeights(){		
		Map<String, Integer> singleRoundAttrWeights = new HashMap<String, Integer>();
		for(int i = 0; i < weightProbeEval; i++){
			computeAttributeWeights(singleRoundAttrWeights);		
		}

		Collection<Integer> dispersionValues = singleRoundAttrWeights.values();
		Integer total = 0;
		for(Integer val:dispersionValues){
			total += val;
		}
		
		Map<String, Double> retVal = new HashMap<String, Double>(dispersionValues.size());
		for(String dispersionAttrName:singleRoundAttrWeights.keySet()){
			Double dispersion = (singleRoundAttrWeights.get(dispersionAttrName)*100)/new Double(total);
			
			BigDecimal bd = BigDecimal.valueOf(dispersion/100);
			bd = bd.setScale(2, BigDecimal.ROUND_FLOOR);
			
			retVal.put(dispersionAttrName, bd.doubleValue());						
			System.out.println("Weight of " + dispersionAttrName + ":" + bd);
		}
		return retVal;
	}
	
	/*
	 * Creates a new temporary cluster with random centroid.
	 * Size of new cluster will be 10% of original data set (parameterized).
	 * Compute distance of each cluster member to centroid and sort cluster members according
	 * to distance to centroid. 
	 */
	public Map<String, Integer> computeAttributeWeights(Map<String, Integer> statDispersion){
		// randomly select a new cluster centroid
		Random rand = new Random(); 
		int size = data.size();
		int value = rand.nextInt(size); 
		Point probeCentroid = data.get(value);
		
		List<Point> partialClusterPoints = getPartialClusterForWeightComputation(rand);		
		Cluster probeCluster = new Cluster(probeCentroid);
		for(Point point:partialClusterPoints){
			probeCluster.addPoint(point);
		}
		probeCluster.setThreshold(threshold);
		List<Point> probeSimilarities = probeCluster.computeSimilarItems();
		//compute dispersion		
		Map<String,String> centroidAttributes = probeCentroid.getAttributes();		
		for(Point point:probeSimilarities){
			Map<String,String> pointAttrs = point.getAttributes();
			for(String pointAttrName:pointAttrs.keySet()){				
				if(centroidAttributes.containsKey(pointAttrName)){
					if(!(centroidAttributes.get(pointAttrName) instanceof String)){
						continue;
					}
					String centroidValue = centroidAttributes.get(pointAttrName);
					String attrValue = pointAttrs.get(pointAttrName);
					if(centroidValue != null && centroidValue.equalsIgnoreCase(attrValue)){
						if(! statDispersion.containsKey(pointAttrName)){
							statDispersion.put(pointAttrName, new Integer(0));
						}
						Integer dispersionValue = statDispersion.get(pointAttrName);
						statDispersion.put(pointAttrName, ++dispersionValue);
					}
				}
			}
		}
		
		return statDispersion;
	}
	
	private List<Point> getPartialClusterForWeightComputation(Random rand){		
		int clusterMemberSize = data.size();
		int partialClusterSize = (clusterMemberSize * clusterSizeForWeightsComputation)/100;
		List<Point> partialClusterPoints = new ArrayList<Point>(partialClusterSize);
		for(int i = 0; i < partialClusterSize; i++){
			int index = rand.nextInt(clusterMemberSize);
			partialClusterPoints.add(data.get(index));
		}
		
		return partialClusterPoints;
	}
}
