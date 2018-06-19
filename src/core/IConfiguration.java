package core;

import org.apache.commons.configuration.Configuration;

/**
 * Interface used to indicate that a class can be configured
 * 
 * @author Álvaro A. Belmonte
 * @author Amelia Zafra
 * @author Eva Gigaja
 * @version 20180619
 */

public interface IConfiguration {
	
	/**
	 * Configuration method
	 * 
	 * @param configuration
	 * 				Configuration the configuration class
	 */
	public void configure(Configuration configuration);	

}
