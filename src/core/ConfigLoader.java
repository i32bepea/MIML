/*    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package core;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

import evaluation.IEvaluator;
import mimlclassifier.IMIMLClassifier;
import mimlclassifier.MIMLClassifier;
import report.IReport;

/**
 * Class used to read a xml file and configure a experiment.
 * 
 * @author Alvaro A. Belmonte
 * @author Amelia Zafra
 * @author Eva Gigaja
 * @version 20180619
 */
public class ConfigLoader {

	/**  Configuration object. */
	private XMLConfiguration configuration;
	
	/**
	 * Gets the configuration.
	 *
	 * @return The configuration
	 */
	public XMLConfiguration getConfiguration() {
		return configuration;
	}

	/**
	 * Sets the configuration.
	 *
	 * @param configuration
	 * 				A new configuration
	 */
	public void setConfiguration(XMLConfiguration configuration) {
		this.configuration = configuration;
	}

	/**
	 * Instantiates a new config loader.
	 *
	 * @param filename
	 *  				the name of config file
	 * @throws ConfigurationException the configuration exception
	 */
	public ConfigLoader(String filename) throws ConfigurationException {
		configuration = new XMLConfiguration(filename);
	}
	
	/**
	 * Read current configuration to load and configure the classifier.
	 *
	 * @return A MIML classifier
	 * @throws Exception the exception
	 */
	@SuppressWarnings("unchecked")
	public IMIMLClassifier loadClassifier() throws Exception {

		IMIMLClassifier classifier = null;

		String clsName = configuration.getString("classifier[@name]");
		//Instantiate the classifier class used in the experiment
		Class<? extends IMIMLClassifier> clsClass = 
				(Class <? extends IMIMLClassifier>) Class.forName(clsName);
		
		classifier = clsClass.newInstance();
		//Configure the classifier
		if(classifier instanceof IMIMLClassifier)
			((IConfiguration) classifier).configure(configuration.subset("classifier"));

		return classifier;			
	}
	
	/**
	 * Read current configuration to load and configure the evaluator.
	 *
	 * @return A evaluator for MIML Classifiers
	 * @throws ClassNotFoundException the class not found exception
	 * @throws InstantiationException the instantiation exception
	 * @throws IllegalAccessException the illegal access exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public IEvaluator loadEvaluator() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		
		IEvaluator evaluator = null;

		String evalName = configuration.getString("evaluator[@name]");
		//Instantiate the evaluator class used in the experiment
		Class<? extends IEvaluator> evalClass = 
				(Class <? extends IEvaluator>) Class.forName(evalName);
		
		evaluator = evalClass.newInstance();
		//Configure the evaluator
		if(evaluator instanceof IEvaluator)
			((IConfiguration) evaluator).configure(configuration.subset("evaluator"));

		return evaluator;			
	}
	
	/**
	 * Read current configuration to load and configure the report.
	 *
	 * @return the MIML report
	 * @throws ClassNotFoundException the class not found exception
	 * @throws InstantiationException the instantiation exception
	 * @throws IllegalAccessException the illegal access exception
	 */
	@SuppressWarnings("unchecked")
	public IReport loadReport() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		
		IReport report = null;

		String reportName = configuration.getString("report[@name]");
		
		//Instantiate the report class used in the experiment
		Class<? extends IReport> clsClass = 
				(Class <? extends IReport>) Class.forName(reportName);
		
		report = clsClass.newInstance();
		
		//Configure the report
		if(report instanceof IReport)
			((IConfiguration) report).configure(configuration.subset("report"));

		return report;			
	}

	
}
