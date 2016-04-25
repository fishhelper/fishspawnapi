package us.fishhelp.lambda;

import java.util.List;

public class Gdd {
    private static double GDD_TBASE = (float) 15.0;
	
	public static class GddPoint
	{
		private String mint; // minimum temp
		private String maxt; // maximum temp

		public String getMint() {
			return mint;
		}

		public void setMint(String mint) {
			this.mint = mint;
		}

		public String getMaxt() {
			return maxt;
		}

		public void setMaxt(String maxt) {
			this.maxt = maxt;
		}

		public GddPoint(String mint, String maxt) {
			this.mint = mint;
			this.maxt = maxt;
		}
		
		public GddPoint() {
			
		}
		
		public double meanDailyWaterTemperature() {
			return (Double.valueOf(maxt)  + Double.valueOf(mint))/2;
		}
		
		public double gdd() {
			double mdwt = meanDailyWaterTemperature();
			return (mdwt < GDD_TBASE) ? 0 : mdwt;
		}
		
		public void debug() {
			System.out.println(mint + " mint");
			System.out.println(maxt + " maxt");
		}
	}
	
	public static class Request {
		private List<GddPoint> history;

		public List<GddPoint> getHistory() {
			return history;
		}

		public void setHistory(List<GddPoint> history) {
			this.history = history;
		}
	}
	
	public int getgdd(Request request) {
		int gddsum = 0;
		List<GddPoint> history = request.getHistory();
		System.out.println(history.size() + " data points");
		for (GddPoint p : history) {
			p.debug();
			double gdd = p.gdd();
			System.out.println(gdd + " degrees");
			gddsum += (int) p.gdd();
		}
		return gddsum;
	}
}
