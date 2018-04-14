package utils;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

import data.MIMLInstances;
import mimlclassifier.MIMLClassifier;

public class ConfigLoader {

	/** Configuration object */
	protected XMLConfiguration configuration;
	
	public ConfigLoader(String filename) throws ConfigurationException {
		configuration = new XMLConfiguration(filename);
	}
	
	public MIMLClassifier loadClassifier() throws Exception {
		
		MIMLClassifier classifier = null;

		String clsName = configuration.getString("classifier[@name]");
		
		//Instantiate the classifier class used in the experiment
		Class<? extends MIMLClassifier> clsClass = 
				(Class <? extends MIMLClassifier>) Class.forName(clsName);
		
		classifier = clsClass.newInstance();
		//Configure the classifier
		if(classifier instanceof MIMLClassifier)
			((IConfiguration) classifier).configure(configuration.subset("classifier"));

		return classifier;			
	}
	
	public MIMLInstances loadTrainData() throws Exception  {
		
		String arffFileTrain = configuration.subset("data").getString("trainFile");
		String xmlFileName = configuration.subset("data").getString("xmlFile");	
		
		return new MIMLInstances(arffFileTrain, xmlFileName);
	}
	
	public MIMLInstances loadTestData() throws Exception  {
		
		String arffFileTest = configuration.subset("data").getString("testFile");
		String xmlFileName = configuration.subset("data").getString("xmlFile");	
		
		return new MIMLInstances(arffFileTest, xmlFileName);	
	}

	
}
