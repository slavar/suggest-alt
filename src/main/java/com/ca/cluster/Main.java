package com.ca.cluster;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ca.cluster.point.CosinePoint;
import com.ca.cluster.point.JaccardPoint;
import com.ca.cluster.point.Point;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {

	//private static Double THRESHOLD = 0.7;
	private static Double THRESHOLD = 0.15D;

	public static void main(String[] args) {
		List<Point> data = null;
		try{
			data = setupClusterPoints();
		}catch(Exception e){
			e.printStackTrace();
		}

		WeightsCalculator weightsCalculator = new WeightsCalculator(data, THRESHOLD);
		Map<String, Double> attributeWeights = weightsCalculator.calculateWeights();

//		attributeWeights = new HashMap<String, Double>();
//		attributeWeights.put("Title", 0.5);
//		attributeWeights.put("First Name", 0.0);
//		attributeWeights.put("Second Name", 0.0);
//		attributeWeights.put("Organization", 0.15);
//		attributeWeights.put("Office", 0.15);
//		attributeWeights.put("Sex", 0.2);

		Cluster cluster = new Cluster(data.get(0), data, attributeWeights);
		cluster.setThreshold(THRESHOLD);
		List<Point> similarPoints = cluster.computeSimilarItems();
		for (Point point : similarPoints) {
			Map<String, String> attributes = point.getAttributes();
			String output = String.format("Name:%s, eye color:%s, age:%d, gender:%s, distance:%f", attributes.get("name"), attributes.get("eyeColor"), attributes.get("age"), attributes.get("gender"), point.getDistance());
			//System.out.println("Name:" + attributes.get("name") + ", eye color:" + attributes.get("eyeColor") + ", age:" + attributes.get("age") + ", gender:" + attributes.get("gender") +" distance: " + point.getDistance());
			System.out.println(output);
		}

	}

	private static Point setupCentroid() {
		Point centroid = new JaccardPoint();
		Map<String, String> centroidAttributes = centroid.getAttributes();
		centroidAttributes.put("Organization", "CA");
		centroidAttributes.put("Title", "Programmer");
		centroidAttributes.put("Office", "IL");
		centroidAttributes.put("Gender", "Male");
		centroidAttributes.put("First Name", "Slava");
		centroidAttributes.put("Second Name", "Risenberg");

		return centroid;
	}

	private static Point getNewPoint() {
		//return new JaccardPoint();
		return new CosinePoint();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<Point> setupClusterPoints() throws Exception{
		JsonFactory jfactory = new JsonFactory();
		Reader fileReader = new BufferedReader(new FileReader("people.json"));
		JsonParser jParser = jfactory.createParser(fileReader);
		ObjectMapper mapper = new ObjectMapper();
		List<Point> retVal = new ArrayList<Point>();
		Iterator<Map<?,?>> ds = mapper.readValue(jParser, List.class).iterator();
		while(ds.hasNext()){
			Point point = getNewPoint();
			Map map = ds.next();
			Iterator<Map.Entry<?,?>> entries = map.entrySet().iterator();
			Map centroidAttributes = point.getAttributes();
			while(entries.hasNext()){
				Map.Entry<?,?> entry = entries.next();
				centroidAttributes.put(entry.getKey(), entry.getValue());
			}
			
			point.setAttributes(centroidAttributes);
			retVal.add(point);
		}
		
/*
		Point point1 = getNewPoint();
		Map<String, String> centroidAttributes = point1.getAttributes();
		centroidAttributes.put("Organization", "CA");
		centroidAttributes.put("Title", "Programmer");
		centroidAttributes.put("Office", "IL");
		centroidAttributes.put("Sex", "Male");
		centroidAttributes.put("First Name", "Yaron");
		centroidAttributes.put("Second Name", "Holland");
		retVal.add(point1);

		Point point2 = getNewPoint();
		centroidAttributes = point2.getAttributes();
		centroidAttributes.put("Organization", "CA");
		centroidAttributes.put("Title", "Team Leader");
		centroidAttributes.put("Office", "IL");
		centroidAttributes.put("Sex", "Male");
		centroidAttributes.put("First Name", "Ido");
		centroidAttributes.put("Second Name", "Priel");
		retVal.add(point2);

		Point point3 = getNewPoint();
		centroidAttributes = point3.getAttributes();
		centroidAttributes.put("Organization", "CA");
		centroidAttributes.put("Title", "Programmer");
		centroidAttributes.put("Office", "IL");
		centroidAttributes.put("Sex", "Female");
		centroidAttributes.put("First Name", "Liat");
		centroidAttributes.put("Second Name", "Dremer");
		retVal.add(point3);

		Point point4 = getNewPoint();
		centroidAttributes = point4.getAttributes();
		centroidAttributes.put("Organization", "CA");
		centroidAttributes.put("Title", "QA");
		centroidAttributes.put("Office", "IL");
		centroidAttributes.put("Sex", "Male");
		centroidAttributes.put("First Name", "Rami");
		centroidAttributes.put("Second Name", "Dahmush");
		retVal.add(point4);

		Point point5 = getNewPoint();
		centroidAttributes = point5.getAttributes();
		centroidAttributes.put("Organization", "CA");
		centroidAttributes.put("Title", "Team Leader");
		centroidAttributes.put("Office", "IL");
		centroidAttributes.put("Sex", "Female");
		centroidAttributes.put("First Name", "Mira");
		centroidAttributes.put("Second Name", "Gil-Or");
		retVal.add(point5);

		Point point6 = getNewPoint();
		centroidAttributes = point6.getAttributes();
		centroidAttributes.put("Organization", "CA");
		centroidAttributes.put("Title", "QA");
		centroidAttributes.put("Office", "IL");
		centroidAttributes.put("Sex", "Female");
		centroidAttributes.put("First Name", "Lilya");
		centroidAttributes.put("Second Name", "Hudyak");
		retVal.add(point6);

		Point point7 = getNewPoint();
		centroidAttributes = point7.getAttributes();
		centroidAttributes.put("Organization", "CA");
		centroidAttributes.put("Title", "Architect");
		centroidAttributes.put("Office", "IL");
		centroidAttributes.put("Sex", "Male");
		centroidAttributes.put("First Name", "Avraham");
		centroidAttributes.put("Second Name", "Rosenzweig");
		retVal.add(point7);

		Point point8 = getNewPoint();
		centroidAttributes = point8.getAttributes();
		centroidAttributes.put("Organization", "CA");
		centroidAttributes.put("Title", "Architect");
		centroidAttributes.put("Office", "IL");
		centroidAttributes.put("Sex", "Male");
		centroidAttributes.put("First Name", "Yuval");
		centroidAttributes.put("Second Name", "Nissan");
		retVal.add(point8);

		Point point9 = getNewPoint();
		centroidAttributes = point9.getAttributes();
		centroidAttributes.put("Organization", "CA");
		centroidAttributes.put("Title", "Product Manager");
		centroidAttributes.put("Office", "IL");
		centroidAttributes.put("Sex", "Female");
		centroidAttributes.put("First Name", "Sharon");
		centroidAttributes.put("Second Name", "Faber");
		retVal.add(point9);

		Point point10 = getNewPoint();
		centroidAttributes = point10.getAttributes();
		centroidAttributes.put("Organization", "CA");
		centroidAttributes.put("Title", "Programmer");
		centroidAttributes.put("Office", "IL");
		centroidAttributes.put("Sex", "Male");
		centroidAttributes.put("First Name", "Rafat");
		centroidAttributes.put("Second Name", "Balaum");
		retVal.add(point10);
*/
		return retVal;
		
	}
	
	public static class Pair implements Map.Entry<String, String>{
		
		private String key;
		private String value;
		
		public Pair(String key){
			this.key = key;
		}
		
		public String getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}

		public String setValue(String value) {
			this.value = value;
			return this.value;
		}
		
	}
	
}


