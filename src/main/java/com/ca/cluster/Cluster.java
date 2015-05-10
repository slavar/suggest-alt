package com.ca.cluster;

import com.ca.cluster.point.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
	This class represents a Cluster - a space of objects with Centroid (an object at the center of Cluster).
	All objects, participating in cluster are sorted by distance to centroid
 */
public class Cluster {
	
	private Point centroid;
	private Double threshold;
	private List<Point> clusterMembers;
	private Map<String, Double> attributeWeights = new HashMap<String, Double>();
	
	public Cluster(Point centroid){
		this.centroid = centroid;
		clusterMembers = new ArrayList<Point>();
	}
	
	public Cluster(Point centroid, List<Point> data, Map<String, Double> attributeWeights){
		this(centroid);
		
		if(attributeWeights != null){
			this.attributeWeights.putAll(attributeWeights);
		}
		
		if(data != null){
			for(Point point:data){
				addPoint(point);
			}
		}
	}
	
	public void setThreshold(Double threshold){
		this.threshold = threshold;
	}
	
	protected List<Point> computeSimilarItems(){
		for(Point point:clusterMembers){
			point.calculateDistance();
		}
		
		Collections.sort(clusterMembers, new PointComparator());
		
		List<Point> retVal = new ArrayList<Point>();
		for(Point point:clusterMembers){
			if(point.getDistance() != 0.0D && point.getDistance() > threshold){
				retVal.add(point);
			}
		}
		return retVal;
	}
	
	public void addPoint(Point point){
		point.setCentroid(centroid);
		point.setWeights(attributeWeights);
		clusterMembers.add(point);
	}

	/*
		Cluster comparator, sorts points by similarity (a distance between two objects)
	 */
	class PointComparator implements Comparator<Point>{

		public int compare(Point o1, Point o2) {
			int retVal = 0;
			if(o1.getDistance() < o2.getDistance()) retVal = -1;
			else
			if(o1.getDistance() == o2.getDistance()) retVal = 0;
			else
			if(o1.getDistance() > o2.getDistance()) retVal = 1;
			
			return retVal;
		}		
	}
}
