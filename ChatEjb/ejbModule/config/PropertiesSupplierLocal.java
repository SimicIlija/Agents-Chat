package config;

import javax.ejb.Local;

@Local
public interface PropertiesSupplierLocal {

	public String getProperty(String propr);
}
