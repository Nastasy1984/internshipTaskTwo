package my.app.model.serialize;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class Deserializer extends StdDeserializer<LocalDateTime>{ 

//extends StdDeserializer<LocalDateTime>{
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	public Deserializer() {
	        this(null);
	    }

	    public Deserializer(Class<LocalDateTime> vc) {
	        super(vc);
	    }
	    
	    @Override
	    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
	    	String dateString = p.getText();   	    	
	    	return LocalDateTime.parse(dateString,formatter);
	        //return LocalDateTime.ofInstant(Instant.ofEpochSecond(Long.parseLong(p.getValueAsString())), ZoneId.systemDefault());
	    }
}