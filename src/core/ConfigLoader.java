package core;

import java.lang.reflect.Method;


import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

import data.MIMLInstances;
import mimlclassifier.MIMLClassifier;
import mulan.classifier.MultiLabelLearner;
import mulan.data.MultiLabelInstances;
import mulan.evaluation.Evaluator;

public class ConfigLoader {

	/** Configuration object */
	protected XMLConfiguration configuration;
	
	private Object[] params = null;
	private Evaluator evaluator = null;
	private String evalMethod = null;
	private MIMLInstances data;

	public MIMLInstances getData() {
		return data;
	}

	public XMLConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(XMLConfiguration configuration) {
		this.configuration = configuration;
	}

	public Object[] getParams() throws Exception {
		
		if(params == null) {
			System.err.println("Error, it's necessary load the evaluator first");
			throw new Exception();
		}
		return params;
	}
	
	public Evaluator getEvaluator() throws Exception {
		if(evaluator == null) {
			System.err.println("Error, it's necessary load the evaluator first");
			throw new Exception();
		}
		return evaluator;
	}
	
	public String getEvalMethod() throws Exception {
		if(evalMethod == null) {
			System.err.println("Error, it's necessary load the evaluator first");
			throw new Exception();
		}
		return evalMethod;
	}


	public ConfigLoader(String filename) throws ConfigurationException {
		configuration = new XMLConfiguration(filename);
	}
	
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
	
	private MIMLInstances loadTrainData(Configuration configuration) throws Exception  {
		
		String arffFileTrain = configuration.subset("data").getString("trainFile");
		String xmlFileName = configuration.subset("data").getString("xmlFile");	
		
		return new MIMLInstances(arffFileTrain, xmlFileName);
	}
	
	private MIMLInstances loadTestData(Configuration configuration) throws Exception  {
		
		String arffFileTest = configuration.subset("data").getString("testFile");
		String xmlFileName = configuration.subset("data").getString("xmlFile");	
		
		return new MIMLInstances(arffFileTest, xmlFileName);	
	}

	public String loadNameCSV() {
		
		String filename = configuration.getString("reportFileName", null);
		
		return filename;
	}
	
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

	
}
