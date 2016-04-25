package us.fishhelp.lambda;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import us.fishhelp.lambda.Locations.Station;

public class Velocity {
	public double dischargeToVelocity(double discharge, Station station) {
		double velocity = 0;
		switch (station) {
		case SPRINGCREEKNORTH:
			//-0.0288x^2 + 0.4097x + 0.1393
			velocity = -0.0288 * discharge * discharge + 0.4097 * discharge + 0.1393; 
			break;
		case KROSNOCREEK:
			//y = 0.2692x + 0.0984
			velocity = 0.2692*discharge + 0.0984;
			break;
		case DONATGLENSHIELDS:
			//y = 0.2717x + 0.148
			velocity = 0.2717 * discharge + 0.148;
		default:
			velocity = -1;
			break;
		}
		return velocity;
	}
	
	public static class VelocityRequest {
		private String locationName;
		private String startDate;
		private String[] dischargeData;
		public String getLocationName() {
			return locationName;
		}
		public void setLocationName(String locationName) {
			this.locationName = locationName;
		}
		public String getStartDate() {
			return startDate;
		}
		public void setStartDate(String startDate) {
			this.startDate = startDate;
		}
		public String[] getDischargeData() {
			return dischargeData;
		}
		public void setDischargeData(String[] dischargeData) {
			this.dischargeData = dischargeData;
		}		
	}
	
	public static class VelocityGraphData {
		private List<VelocityGraphPoint> velocityData;
		private String startDate;
		private String endDate;
		private String locationName;
		public List<VelocityGraphPoint> getVelocityData() {
			return velocityData;
		}
		public void setVelocityData(List<VelocityGraphPoint> velocityData) {
			this.velocityData = velocityData;
		}
		public String getStartDate() {
			return startDate;
		}
		public void setStartDate(String startDate) {
			this.startDate = startDate;
		}
		public String getEndDate() {
			return endDate;
		}
		public void setEndDate(String endDate) {
			this.endDate = endDate;
		}
		public String getLocationName() {
			return locationName;
		}
		public void setLocationName(String locationName) {
			this.locationName = locationName;
		}
	}
	
	public static class VelocityGraphPoint {
		private String velocity;
		private String date;
		public String getVelocity() {
			return velocity;
		}
		public void setVelocity(String velocity) {
			this.velocity = velocity;
		}
		public String getDate() {
			return date;
		}
		public void setDate(String date) {
			this.date = date;
		}
	}
	
	public VelocityGraphData velocitygraphdata(VelocityRequest vr) {
		VelocityGraphData graphData = new VelocityGraphData();
		graphData.setLocationName(vr.getLocationName());
		Station station = Station.valueOf(vr.getLocationName().toUpperCase().replaceAll("\\s+",""));
		Date dateOfVelocity = null;
		try {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			dateOfVelocity = formatter.parse(vr.startDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar cl = Calendar.getInstance();
		List<VelocityGraphPoint> graphDataSeries = new ArrayList<VelocityGraphPoint>();
		System.out.println("discharge points: " + vr.getDischargeData().length);
		for (String d : vr.dischargeData) {
			double discharge = Double.valueOf(d);
			double velocity = dischargeToVelocity(discharge, station);
			VelocityGraphPoint p = new VelocityGraphPoint();
			p.setVelocity(Double.toString(velocity));
			p.setDate(dateOfVelocity.toString());
			graphDataSeries.add(p);
			
			// roll date forward
			cl.setTime(dateOfVelocity);
			cl.add(Calendar.DATE, 1);
			dateOfVelocity = cl.getTime();
		}
		graphData.setVelocityData(graphDataSeries);
		graphData.setStartDate(graphDataSeries.get(0).getDate());
		graphData.setEndDate(graphDataSeries.get(graphDataSeries.size() - 1).getDate());		
		return graphData;
	}
}
