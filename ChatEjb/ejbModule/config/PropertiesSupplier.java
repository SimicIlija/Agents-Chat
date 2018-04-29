package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.ejb.Singleton;
import javax.faces.annotation.ManagedProperty;
import javax.servlet.ServletContext;

@Singleton
public class PropertiesSupplier implements PropertiesSupplierLocal{


    Properties prop = new Properties();
    InputStream input = null;
    

    public PropertiesSupplier() {
	    try {
	
	    	ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	    	InputStream input = classLoader.getResourceAsStream("config/config.properties");
	        
	        prop.load(input);
	    } catch (IOException ex) {
	        ex.printStackTrace();
	    } finally {
	        if (input != null) {
	            try {
	                input.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }
    }
    
    public String getProperty(String propr) {
    	return prop.getProperty(propr);
    }
}
