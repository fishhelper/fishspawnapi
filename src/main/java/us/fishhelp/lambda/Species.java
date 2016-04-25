package us.fishhelp.lambda;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;

import javax.json.Json;
import javax.json.stream.JsonGenerator;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

public class Species implements RequestStreamHandler {

	public enum Fish {
		C_IDELLA("C. idella"),
		H_MOLITRIX("H. molitrix"),
		M_PICEUS("M. piceus");
		
		private String speciesName;
		private Fish(String speciesName) {
			this.speciesName = speciesName;
		}
		public String getSpeciesName() {
			return speciesName;
		}
		public void setSpeciesName(String speciesName) {
			this.speciesName = speciesName;
		}
		
		public static Fish valueOfScientificName(String name) {
			String s = scientificNameToEnumName(name);
			return Fish.valueOf(s);
		}
		
		public static String scientificNameToEnumName(String scientificName) {
			return scientificName.replace('.', '_').replaceAll("\\s+","");
		}
	}
	
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		StringWriter writer = new StringWriter();
		JsonGenerator gen = Json.createGenerator(writer);
		gen.writeStartObject()
		  .writeStartArray("species");
		
		for(Fish fish : Fish.values()) {
			gen.writeStartObject()
			   .write("name", fish.getSpeciesName())
			   .write("id", fish.ordinal())
			.writeEnd();
		}
		
  		gen.writeEnd()
	  	.writeEnd()
	    .close(); 
  		
		output.write(writer.toString().getBytes());
	}
	

}
