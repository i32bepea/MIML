package core;

import org.apache.commons.configuration.Configuration;

/**
 * Class used to indicate that a class can be configured
 * 
 * @author �lvaro Belmonte P�rez
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
