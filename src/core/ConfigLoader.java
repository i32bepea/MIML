package core;

import java.lang.reflect.Method;


import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

import data.MIMLInstances;
import mimlclassifier.MIMLClassifier;
import mulan.classifier.MultiLabelLearner;
import mulan.data.MultiLabelInstances;
import mulan.evaluation.Evaluation;
import mulan.evaluation.Evaluator;
import mulan.evaluation.MultipleEvaluation;
import report.IReport;
import report.MIMLReport;

/**
 * Class used to read a xml file and configure a experiment.
 * 
 * @author Álvaro A. Belmonte
 * @author Amelia Zafra
 * @author Eva Gigaja
 * @version 20180619
 */
public class ConfigLoader {

	/**  Configuration object. */
	protected XMLConfiguration configuration;
	
	/** The params of evaluator method. */
	private Object[] params = null;
	
	/** The evaluator. */
	private Evaluator evaluator = null;
	
	/** The type of evaluation method. */
	private String evalMethod = null;
	
	/** The data. */
	private MIMLInstances data;

	/**
	 * Gets the data.
	 *
	 * @return MIML data sets for train
	 */
	public MIMLInstances getData() {
		return data;
	}

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
	 * Gets the params of evaluator method.
	 *
	 * @return The params of evaluator method
	 */
	public Object[] getParams() throws Exception {
		
		if(params == null) {
			System.err.println("Error, it's necessary load the evaluator first");
			throw new Exception();
		}
		return params;
	}
	
	/**
	 * Gets the evaluator method.
	 *
	 * @return The evaluator method
	 */
	public Evaluator getEvaluator() throws Exception {
		if(evaluator == null) {
			System.err.println("Error, it's necessary load the evaluator first");
			throw new Exception();
		}
		return evaluator;
	}
	
	/**
	 * Gets the type of evaluation method.
	 *
	 * @return The type of evaluation method
	 */
	public String getEvalMethod() throws Exception {
		if(evalMethod == null) {
			System.err.println("Error, it's necessary load the evaluator first");
			throw new Exception();
		}
		return evalMethod;
	}


	/**
	 * Instantiates a new config loader.
	 *
	 * @param filename
	 * 				the name of config file
	 */
	public ConfigLoader(String filename) throws ConfigurationException {
		configuration = new XMLConfiguration(filename);
	}
	
	/**
	 * Read current configuration to load and configure the classifier.
	 *
	 * @return A MIML classifier
	 */
	@SuppressWarnings("unchecked")
	public MIMLClassifier loadClassifier() throws Exception {

		MIMLClassifier classifier = null;

		String clsName = configuration.getString("classifier[@name]");
		//Instantiate the classifier class used in the experiment
		Class<? extends MIMLClassifier> clsClass = 
				(Class <? extends MIMLClassifier>) Class.forName(clsName);
		
		classifier = (MIMLClassifier) clsClass.newInstance();
		//Configure the classifier
		if(classifier instanceof MIMLClassifier)
			((IConfiguration) classifier).configure(configuration.subset("classifier"));

		return classifier;			
	}
	
	/**
	 * Load train data.
	 *
	 * @param configuration
	 * 				The subset of configuration with the data route
	 * 
	 * @return A MIML instances
	 */
	private MIMLInstances loadTrainData(Configuration configuration) throws Exception  {
		
		String arffFileTrain = configuration.subset("data").getString("trainFile");
		String xmlFileName = configuration.subset("data").getString("xmlFile");	
		
		return new MIMLInstances(arffFileTrain, xmlFileName);
	}
	
	/**
	 * Load test data.
	 *
	 * @param configuration
	 * 				The subset of configuration with the data route
	 * 
	 * @return A MIML instances
	 */
	private MIMLInstances loadTestData(Configuration configuration) throws Exception  {
		
		String arffFileTest = configuration.subset("data").getString("testFile");
		String xmlFileName = configuration.subset("data").getString("xmlFile");	
		
		return new MIMLInstances(arffFileTest, xmlFileName);	
	}

	/**
	 * Load name of report file to save the experiment's results.
	 *
	 * @return Filename
	 */
	public String loadReportName() {
		
		String filename = configuration.subset("report").getString("fileName", null);
		
		return filename;
	}
	
	/**
	 * Load the evaluator method used to train the classifier.
	 *
	 * @param classifier
	 * 				Classifier which is going to be evaluated
	 * 
	 * @return The method to evaluate the classifier
	 */
	@SuppressWarnings("rawtypes")
	public Method loadEvaluator(MIMLClassifier classifier) throws Exception {
		
		evaluator = (Evaluator) Class.forName("mulan.evaluation.Evaluator").newInstance();
		
		evalMethod = configuration.getString("evaluator[@method]");
		
		Configuration subConfiguration = configuration.subset("evaluator"); //getProperty("multiLable")
	
		Class[] parameterTypes = new Class[3];
		params = new Object[3];
		
		parameterTypes[0] = MultiLabelLearner.class;
		params[0] = classifier;
				
		if ("cross-validation".equals(evalMethod)) {
			
			evalMethod = "crossValidate";
			// load train data
			MIMLInstances data = loadTrainData(subConfiguration);
			parameterTypes[1] = MultiLabelInstances.class;
			params[1] = data;
			
			// load folds' number
			int numFolds = subConfiguration.getInt("numFolds");
			parameterTypes[2] = int.class;
			params[2] = numFolds;
			
			this.data = data;
				
		}
		else if ("train-test".equals(evalMethod)) {
			
			evalMethod = "evaluate";
			// load train data
			MIMLInstances train = loadTrainData(subConfiguration);
			parameterTypes[1] = MultiLabelInstances.class;
			params[1] = train;

			// load test data
			MIMLInstances test = loadTestData(subConfiguration);
			parameterTypes[2] = MultiLabelInstances.class;
			params[2] = test;
			
			this.data = train;					
		}
		
		Method method = evaluator.getClass().getMethod(evalMethod, parameterTypes);
		
		return method;
	}
	
	/**
	 * Load report for cross-validation.
	 *
	 * @param evaluator 
	 * 			the evaluator used in cross-validation
	 * @return the MIML report
	 */
	@SuppressWarnings("unchecked")
	public MIMLReport loadReportCrossValidation(MultipleEvaluation evaluator) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		
		MIMLReport report = null;

		String reportName = configuration.getString("report[@name]");
		//Instantiate the classifier class used in the experiment
		Class<? extends MIMLReport> clsClass = 
				(Class <? extends MIMLReport>) Class.forName(reportName);
		
		report = (MIMLReport) clsClass.newInstance();
		
		report.setEvaluationCrossValidation(evaluator);
		report.setData(data);

		return report;			
	}
	
	/**
	 * Load report for cross-validation.
	 *
	 * @param evaluator 
	 * 			the evaluator used in cross-validation
	 * @return the MIML report
	 */
	@SuppressWarnings("unchecked")
	public MIMLReport loadReportHoldout(Evaluation evaluator) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		
		MIMLReport report = null;

		String reportName = configuration.getString("report[@name]");
		System.out.println(reportName);
		//Instantiate the classifier class used in the experiment
		Class<? extends MIMLReport> clsClass = 
				(Class <? extends MIMLReport>) Class.forName(reportName);
		
		report = (MIMLReport) clsClass.newInstance();
		
		report.setEvaluationHoldout(evaluator);
		report.setData(data);

		return report;			
	}

	
}
