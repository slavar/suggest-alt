package com.ca.cluster.point;

import java.util.HashMap;
import java.util.Map;

public abstract class BasicPoint implements Point {

	protected Point centroid;
	protected Double distance;
	
	protected Map<String,String> attributes;
	private Map<String, Double> weights = new HashMap<String, Double>();
	
	private static Double DEFAULT_ATTRIBUTE_WEIGHT = 1D;
	
	public BasicPoint(){
		attributes = new HashMap<String, String>();
	}
	
	public void setCentroid(Point centroid){
		this.centroid = centroid;
	}

	public Double getDistance() {
		return distance;
	}

	public void addAttribute(String name, String value){
		attributes.put(name, value);
	}
	
	public Map<String,String> getAttributes(){
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}
	
	public void setWeights(Map<String, Double> weights){
		if(weights != null){
			this.weights.putAll(weights);
		}
	}
	
	protected Double getWeightForAttribute(String attribute){
		if(! weights.containsKey(attribute)){
			return DEFAULT_ATTRIBUTE_WEIGHT;
		}
		
		return weights.get(attribute);
	}
	
}
