package model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Version;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class BaseDO {
	
	@Id
	@Property("id")
	@JsonSerialize(using=ObjectID_Serializer.class)
//	@JsonSerialize(using = ObjectIdMapping.ObjectIdSerializer.class)
//	@JsonDeserialize(using = ObjectIdMapping.ObjectIdDeerializer.class)
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

 