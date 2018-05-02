package model;

import java.io.IOException;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class ObjectIdMapping {
    public static class ObjectIdSerializer extends JsonSerializer<ObjectId>{
        @Override
        public void serialize(ObjectId id, JsonGenerator json, SerializerProvider provider) throws IOException, JsonProcessingException {
            json.writeString(id.toString());

        }
    }
    public static class ObjectIdDeserializer extends JsonDeserializer<ObjectId>{
        @Override
        public ObjectId deserialize(JsonParser jp, DeserializationContext context) throws IOException, JsonProcessingException {
            if (!ObjectId.isValid(jp.getText())) throw context.mappingException("invalid ObjectId " + jp.getText());
            return new ObjectId(jp.getText());
        }
    }
}   