package us.fishhelp.lambda;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import us.fishhelp.lambda.Species.Fish;

public class StreamLength {
	
	public static class StreamLengthPredictionInput {
	    String fishname; // scientific name
	    String velocity; // m/s
	    String meanAverageWaterTemperature; // celcius
		public String getFishname() {
			return fishname;
		}
		public void setFishname(String fishname) {
			this.fishname = fishname;
		}
		public String getVelocity() {
			return velocity;
		}
		public void setVelocity(String velocity) {
			this.velocity = velocity;
		}
		public String getMeanAverageWaterTemperature() {
			return meanAverageWaterTemperature;
		}
		public void setMeanAverageWaterTemperature(String meanAverageWaterTemperature) {
			this.meanAverageWaterTemperature = meanAverageWaterTemperature;
		}
	}
	
	// String, double in kilometers
	public String predictstreamlength(StreamLengthPredictionInput input) {
		Fish fish = Fish.valueOfScientificName(input.getFishname());
		double velocity = Double.valueOf(input.getVelocity());
		double estimatedIncubationTime = estimateIncubationTime(fish, Double.valueOf(input.getMeanAverageWaterTemperature()));
		// D=3600*V*I/1000=3.6*V*I
		double streamLength = 3.6 * velocity * estimatedIncubationTime; // km
		return Double.toString(streamLength);
	}
	
	public static class StreamLengthGraphData {
		private String location;
		private String species;
		private List<StreamLengthDataPoint> streamLengthGraphData;
		public String getLocation() {
			return location;
		}
		public void setLocation(String location) {
			this.location = location;
		}
		public String getSpecies() {
			return species;
		}
		public void setSpecies(String species) {
			this.species = species;
		}
		public List<StreamLengthDataPoint> getStreamLengthGraphData() {
			return streamLengthGraphData;
		}
		public void setStreamLengthGraphData(List<StreamLengthDataPoint> streamLengthGraphData) {
			this.streamLengthGraphData = streamLengthGraphData;
		}
	}
	
	public static class StreamLengthDataPoint {
		private String streamLength;
		private String date;
		public String getStreamLength() {
			return streamLength;
		}
		public void setStreamLength(String streamLength) {
			this.streamLength = streamLength;
		}
		public String getDate() {
			return date;
		}
		public void setDate(String date) {
			this.date = date;
		}
	}
	
	public static class StreamLengthGraphRequest {
		private String species;
		private String location;
		public String getSpecies() {
			return species;
		}
		public void setSpecies(String species) {
			this.species = species;
		}
		public String getLocation() {
			return location;
		}
		public void setLocation(String location) {
			this.location = location;
		}
	}
	
	public StreamLengthGraphData streamlengthgraphdata(StreamLengthGraphRequest request) {
		StreamLengthGraphData graphData = new StreamLengthGraphData();
		graphData.setLocation(request.location);
		graphData.setSpecies(request.species);
				
		List<StreamLengthPredictionInput> history = gethistory(request.location, request.species);
		
		LocalDate date = LocalDate.now();
		date = date.minusDays(history.size());
		
		List<StreamLengthDataPoint> series = new ArrayList<StreamLengthDataPoint>();
		for (StreamLengthPredictionInput input : history) {
			String streamLengthPrediction = predictstreamlength(input);
			StreamLengthDataPoint p = new StreamLengthDataPoint();
			p.setStreamLength(streamLengthPrediction);
			p.setDate(date.toString());
			date = date.plusDays(1);
			series.add(p);
		}
		
		graphData.setStreamLengthGraphData(series);
		return graphData;
	}
	
	private List<StreamLengthPredictionInput> gethistory(String location, String species) {
		double[] meanTemperature = null;
		double[] velocity = null;
		
		// get mean average temperature data series for location
		switch (location)
		{
		default:
			double[] d = {14.0, 14.2, 14.6, 14.6, 14.9};
			meanTemperature = d;
		}
		
		// get velocity data series for location
		switch (location)
		{
		default:
			double[] d = {0.5, 0.4, 0.3, 0.4, 0.35};
			velocity = d;
		}
		
		// build a list
		List<StreamLengthPredictionInput> l = new ArrayList<StreamLengthPredictionInput>();
		int length = meanTemperature.length;
		System.out.println(meanTemperature.length + " : " + velocity.length);
		for (int i = 0; i < length; i++) {
			StreamLengthPredictionInput input = new StreamLengthPredictionInput();
			input.setFishname(species);
			input.setMeanAverageWaterTemperature(Double.toString(meanTemperature[i]));
			input.setVelocity(Double.toString(velocity[i]));
			l.add(input);
		}
		return l;
	}

	// 🇨🇦
	private double estimateIncubationTime(Fish fish, double meanAverageWaterTemperature) {
		double estimatedIncubationTime = 0;
		switch (fish) {
			case C_IDELLA :  
		    	// y=233855x-2.4915
				estimatedIncubationTime = 233855 * Math.pow(meanAverageWaterTemperature, -2.4915);
		    	break;
			case H_MOLITRIX :
				// y=22456x-2.0989
				estimatedIncubationTime = 22456 * Math.pow(meanAverageWaterTemperature, -2.0989);
			case M_PICEUS :
				// y=233855x-2.822
				estimatedIncubationTime = 233855 * Math.pow(meanAverageWaterTemperature, -2.822);
			default:
				estimatedIncubationTime = -1;
				break;
		}
		return estimatedIncubationTime;
	}
}
