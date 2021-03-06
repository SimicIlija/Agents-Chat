package model;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Version;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class BaseDO implements Serializable{
	
	@Id
	@Property("id")
	@JsonSerialize(using = ObjectIdMapping.ObjectIdSerializer.class)
	@JsonDeserialize(using = ObjectIdMapping.ObjectIdDeserializer.class)
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

 