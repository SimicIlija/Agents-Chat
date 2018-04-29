package dataAccess;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.PrePassivate;
import javax.ejb.Stateless;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;
//import com.mongodb.MongoException;
import com.mongodb.WriteConcern;

import model.BaseDO;

@Stateless
public class MongoConnection {

	private MongoClient mongo = null;
	private Datastore dataStore = null;
	private Morphia morphia = null;
	
	public MongoClient getMongo() throws RuntimeException {
		if (mongo == null) {
			//logger
			MongoClientOptions.Builder options = MongoClientOptions.builder()
													.connectionsPerHost(10)
													.maxConnectionIdleTime((60 * 1_000))
													.maxConnectionLifeTime((120 * 1_000));
													;

			MongoClientURI uri = new MongoClientURI("mongodb://localhost:27017", options);
			
			try {
				mongo = new MongoClient(uri);
				mongo.setWriteConcern(WriteConcern.ACKNOWLEDGED);
			} catch (MongoException ex) {
				System.out.println("An error occoured when connecting to MongoDB");
				ex.printStackTrace();
			} catch (Exception ex) {
				System.out.println("An error occoured when connecting to MongoDB");
				ex.printStackTrace();
			}
			
			// To be able to wait for confirmation after writing on the DB
			mongo.setWriteConcern(WriteConcern.ACKNOWLEDGED);
		}

		return mongo;
	}

	public Morphia getMorphia() {
		if (morphia == null) {
			morphia = new Morphia();
			morphia.mapPackageFromClass(BaseDO.class);
		}
		return morphia;
	}

	public Datastore getDatastore() {
		if (dataStore == null) {
			String dbName = "testdb";
			dataStore = getMorphia().createDatastore(getMongo(), dbName);
		}
		return dataStore;
	}
	
	@PostConstruct
	public void init() {
		getMongo();
		getMorphia();
		getDatastore();
	}
	
	@PreDestroy
	@PrePassivate
	public void close() {
		if (mongo != null) {
			try {
				mongo.close();
				mongo = null;
				morphia = null;
				dataStore = null;
			} catch (Exception e) {
				System.out.println("An error occurred when closing the MongoDB connection\n%s");
				e.printStackTrace();
			}
		} else {
			System.out.println("mongo object was null, wouldn't close connection");
		}
	}
	
}
