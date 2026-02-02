package com.app.common.system;

import org.springframework.boot.jackson.JacksonComponent;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import tools.jackson.core.JsonGenerator;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.ValueSerializer;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.DeserializationContext;
import java.util.Map;

/**
 * Spring Boot 4 uses jackson3 and Hibernate uses jackson2,
 * and jackson2 JsonNode is serialized incorrectly in responses.
 * This is a converter for jackson2 JsonNode to JSON response.
 */
@JacksonComponent
public class JsonNodeJacksonComponent {

	public static class Serializer extends ValueSerializer<JsonNode> {

		@Override
		public void serialize(JsonNode value, JsonGenerator jgen, SerializationContext context) {
		    if (!value.isObject()) {
		        throw new RuntimeException("JsonNode serialization is supported only for objects");
		    }

			jgen.writeStartObject();
		    for (Map.Entry<String, JsonNode> pair : value.properties()) {
		        var fieldValue = pair.getValue();

		        if (fieldValue.getNodeType() == JsonNodeType.STRING) {
                    jgen.writeStringProperty(pair.getKey(), fieldValue.asText());
                } else if (fieldValue.getNodeType() == JsonNodeType.NUMBER) {
                    jgen.writeNumberProperty(pair.getKey(), fieldValue.asInt());
                } else if (fieldValue.getNodeType() == JsonNodeType.NULL) {
                    jgen.writeNullProperty(pair.getKey());
                } else if (fieldValue.getNodeType() == JsonNodeType.OBJECT) {
                    jgen.writeName(pair.getKey());
                    serialize(fieldValue, jgen, context);
                } else {
                    throw new RuntimeException("Type not supported: " + fieldValue.getNodeType());
                }
		    }
			jgen.writeEndObject();
		}

	}

	public static class Deserializer extends ValueDeserializer<JsonNode> {

		@Override
		public JsonNode deserialize(JsonParser jsonParser, DeserializationContext ctxt) {
			JsonNode tree = jsonParser.readValueAsTree();
			return tree;
		}

	}

}
