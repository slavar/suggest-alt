package com.ca.cluster.point;

import java.util.Map;

public interface Point {
	public Double getDistance();
	public void calculateDistance();
	public Map<String, String> getAttributes();
	public void setAttributes(Map<String, String> attributes);
	public void setCentroid(Point centroid);
	public void setWeights(Map<String, Double> weights);
}
