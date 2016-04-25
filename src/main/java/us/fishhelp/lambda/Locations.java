package us.fishhelp.lambda;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Locations {

	public enum Station {
		SPRINGCREEKNORTH("Spring Creek North"),
		DONATGLENSHIELDS("Don at Glenshields"),
		KROSNOCREEK("Krosno Creek");
		
		private final String name;
		
		private Station(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	public static List<Map<String, String>> getlocations() {		
		List<Map<String,String>> locations = new ArrayList<Map<String,String>>();
		
		for (Station s : Station.values()) {
			Map<String, String> m = new TreeMap<String, String>();
			m.put("name", s.getName());
			m.put("id", Integer.toString(s.ordinal()));
			locations.add(m);
		}
		
		return locations;
	}
}
