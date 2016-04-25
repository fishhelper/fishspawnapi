package us.fishhelp.lambda;

public class SpawnPrediction {

	private static final int GDD_655 = 655;
	private static final int GDD_900 = 900;
	private static final int WATER_TEMPERATURE_CUTOFF = 17;
    private static final double FLOW_SPIKE_CUTOFF = 0.7;
	
	public enum SpawnLikelihood {
		NOT_SUITABLE,
		MINIMALLY_SUITABLE,
		SUITABLE,
		VERY_SUITABLE,
		HIGHLY_SUITABLE
	}
	
	public static class SpawnPredictionRequest {
		private Integer gdd;  // growing degree days
		private String meanWaterTemperature;    //celcius
		private String spawnLengthPrediction;   //km
		private String unimpoundedStreamLength; //km
		private String flowSpike;  // m/s
		public Integer getGdd() {
			return gdd;
		}
		public void setGdd(Integer gdd) {
			this.gdd = gdd;
		}
		public String getMeanWaterTemperature() {
			return meanWaterTemperature;
		}
		public void setMeanWaterTemperature(String meanWaterTemperature) {
			this.meanWaterTemperature = meanWaterTemperature;
		}
		public String getSpawnLengthPrediction() {
			return spawnLengthPrediction;
		}
		public void setSpawnLengthPrediction(String spawnLengthPrediction) {
			this.spawnLengthPrediction = spawnLengthPrediction;
		}
		public String getUnimpoundedStreamLength() {
			return unimpoundedStreamLength;
		}
		public void setUnimpoundedStreamLength(String unimpoundedStreamLength) {
			this.unimpoundedStreamLength = unimpoundedStreamLength;
		}
		public String getFlowSpike() {
			return flowSpike;
		}
		public void setFlowSpike(String flowSpike) {
			this.flowSpike = flowSpike;
		}
	}
	
	public String spawnprediction(SpawnPredictionRequest request) {
		int gdd = request.getGdd();
		double waterTemperature = Double.valueOf(request.getMeanWaterTemperature());
		double predicted = Double.valueOf(request.getSpawnLengthPrediction());
		double riverLength = Double.valueOf(request.getUnimpoundedStreamLength());
		double flow = Double.valueOf(request.getFlowSpike());
		
		if (gdd > GDD_655) {
			System.out.println("Above first gdd tier");
		}
		else {
			return SpawnLikelihood.NOT_SUITABLE.toString();
		}
		
		if (waterTemperature > WATER_TEMPERATURE_CUTOFF) {
			System.out.println("Above water temperature cutoff");
		}
		else {
			return SpawnLikelihood.NOT_SUITABLE.toString();
		}
		
		if (riverLength > predicted) {
			System.out.println("Actual is above predicted");			
		}
		else {
			return SpawnLikelihood.NOT_SUITABLE.toString();
		}

		SpawnLikelihood likelihood = SpawnLikelihood.NOT_SUITABLE;
		boolean abovegdd900 = gdd > GDD_900;
		boolean aboveFlowSpike = flow > FLOW_SPIKE_CUTOFF;
		
		System.out.println("Above gdd900 " + abovegdd900);			
		System.out.println("Above flowSpike " + aboveFlowSpike);			

		if (abovegdd900 && aboveFlowSpike) {
			likelihood = SpawnLikelihood.HIGHLY_SUITABLE;
		}
		if (abovegdd900 && !aboveFlowSpike) {
			likelihood = SpawnLikelihood.VERY_SUITABLE;
		}
		if (!abovegdd900 && aboveFlowSpike) {
			likelihood = SpawnLikelihood.SUITABLE;
		}
		if (!abovegdd900 && !aboveFlowSpike) {
			likelihood = SpawnLikelihood.MINIMALLY_SUITABLE;
		}
		
		return likelihood.toString();
	}
}
