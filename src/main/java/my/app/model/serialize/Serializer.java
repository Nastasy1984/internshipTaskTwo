package my.app.model.serialize;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;


@SuppressWarnings("serial")
public class Serializer extends StdSerializer<LocalDateTime> {
    private static final Logger LOG = LoggerFactory.getLogger(my.app.model.serialize.Serializer.class.getName());
	// we will write dates to JSON using this format
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	public Serializer() {
		this(null);
	}

	public Serializer(Class<LocalDateTime> t) {
		super(t);
	}

	@Override
	public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider arg2)
			throws IOException, JsonProcessingException {
		LOG.info("serialize method was invoked with LocalDateTime: {}", value);
		gen.writeString(formatter.format(value));
		LOG.info("serialize method generated string: {}", formatter.format(value));
		//gen.writeString(String.valueOf(value.atZone(ZoneId.systemDefault()).toEpochSecond()));
	}
}

