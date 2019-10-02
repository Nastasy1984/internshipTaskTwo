package my.app.model.serialize;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

@SuppressWarnings("serial")
public class Deserializer extends StdDeserializer<LocalDateTime>{ 
    private static final Logger LOG = LoggerFactory.getLogger(my.app.model.serialize.Deserializer.class.getName());
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	public Deserializer() {
	        this(null);
	    }

	    public Deserializer(Class<LocalDateTime> vc) {
	        super(vc);
	    }
	    
	    @Override
	    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
			LOG.info("deserialize method was invoked");
	    	String dateString = p.getText();
	    	LOG.info("dateString is: {}", dateString);
	    	return LocalDateTime.parse(dateString,formatter);
	        //return LocalDateTime.ofInstant(Instant.ofEpochSecond(Long.parseLong(p.getValueAsString())), ZoneId.systemDefault());
	    }
}