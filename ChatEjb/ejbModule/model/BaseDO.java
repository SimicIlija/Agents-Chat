package model;

import java.io.IOException;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Version;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class BaseDO {
	
	@Id
	@Property("id")
	@JsonSerialize(using=ObjectID_Serializer.class)
	private ObjectId id;

	@Version
	@Property("version")
	private Long version;

	public BaseDO() {
		super();
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

}

class ObjectID_Serializer extends JsonSerializer<ObjectId>{
	@Override
	public void serialize(ObjectId objid, JsonGenerator jsongen, SerializerProvider provider) throws IOException, JsonProcessingException {
		
		if(objid == null ){
			jsongen.writeNull();
		}else{
			jsongen.writeString(objid.toString());
		}	
	}
}