package utils;

import org.apache.commons.configuration.Configuration;

/**
 * Class used to indicate that a class can be configured
 * 
 * @author Álvaro Belmonte Pérez
 * 
 */

public interface IConfiguration {
	
	/**
	 * Configuration method
	 * 
	 * @param configuration the configuration class
	 */
	public void configure(Configuration configuration);	

}
