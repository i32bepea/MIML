package tutorial;

import java.io.File;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.XMLConfiguration;

import data.MIMLInstances;
import mimlclassifier.MIMLClassifier;
import mulan.data.InvalidDataFormatException;
import mulan.evaluation.Evaluation;
import mulan.evaluation.Evaluator;
import utils.IConfiguration;
import weka.core.Utils;

public class exampleMLkNNadapted {

	@SuppressWarnings("unchecked")
	static MIMLClassifier loadClassifier(Configuration jobConf) {
		
		MIMLClassifier classifier = null;
		
		try {

			String clsName = jobConf.getString("classifier[@name]");
			
			//Instantiate the classifier class used in the experiment
			Class<? extends MIMLClassifier> clsClass = 
					(Class <? extends MIMLClassifier>) Class.forName(clsName);
			
			classifier = clsClass.newInstance();
			//Configure the classifier
			if(classifier instanceof MIMLClassifier)
				((IConfiguration) classifier).configure(jobConf.subset("classifier"));
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		
		return classifier;	
	}
	

	/** Shows the help on command line. */
	public static void showUse() {
		System.out.println("Program parameters:");
		System.out.println("\t-f arffPathFile Name -> path of arff file.");
		System.out.println("\t-x xmlPathFileName -> path of arff file.");
		System.out.println("Example:");
		System.out.println("\tjava -jar example MIMLInstances -f data" + File.separator + "toy.arff -x data"
				+ File.separator + "toy.xml");
		System.exit(-1);
	}

	public static void main(String[] args) {

		try {

			// String arffFileName = Utils.getOption("f", args);
			// String xmlFileName = Utils.getOption("x", args);
			// String arffFileName = "data+File.separator+miml_text_data.arff";
			// String xmlFileName = "data+File.separator+miml_text_data.xml";
	
			String configFile = Utils.getOption("c", args);
			Configuration jobConf = null;
			
			//Try open job file
			File jobFile = new File(configFile);
			try {
				
				jobConf = new XMLConfiguration(jobFile);
			}
			catch (Exception e) {
				e.printStackTrace();
				System.exit(-1);
			}
			
			String arffFileTrain = jobConf.subset("data").getString("trainFile");
			String arffFileTest = jobConf.subset("data").getString("testFile");
			String xmlFileName = jobConf.subset("data").getString("xmlFile");
			
			// Parameter checking
			if (arffFileTrain.isEmpty()) {
				System.out.println("Data tain must be specified.");
				showUse();
			}
			// Parameter checking
			if (arffFileTest.isEmpty()) {
				System.out.println("Data test must be specified.");
				showUse();
			}
			if (xmlFileName.isEmpty()) {
				System.out.println("Xml pathName must be specified.");
				showUse();
			}
			

			// Loads the dataset
			System.out.println("Loading the dataset....");
			MIMLInstances mimlTrain = new MIMLInstances(arffFileTrain, xmlFileName);
			MIMLInstances mimlTest = new MIMLInstances(arffFileTest, xmlFileName);
					
			MIMLClassifier clasificador = loadClassifier(jobConf);
			
			clasificador.build(mimlTrain);
			/*
			Evaluator evalCV = new Evaluator();
			MultipleEvaluation resultsCV;
			int numFolds = 5;
			System.out.println("\nPerforming " + numFolds + "-fold cross-validation:\n");
			resultsCV = evalCV.crossValidate(clasificador, mimlTrain, numFolds);
			System.out.println("\nResults on cross validation evaluation:\n" + resultsCV);
			*/
			// Performs a train-test evaluation
			
			Evaluator evalTT = new Evaluator();
			System.out.println("\nPerforming train-test evaluation:\n");
			Evaluation resultsTT = evalTT.evaluate(clasificador, mimlTrain, mimlTrain);
			System.out.println("\nResults on train test evaluation:\n" + resultsTT);
			
			/*
			Bag bag = mimlTrain.getBag(1266);
			MultiLabelOutput prediction = clasificador.makePrediction(bag);
			System.out.println("\nPrediction on a single instance:\n\t" + prediction.toString());
			bag = mimlTrain.getBag(1267);
			prediction = clasificador.makePrediction(bag);
			System.out.println("\nPrediction on a single instance:\n\t" + prediction.toString());
			bag = mimlTrain.getBag(1261);
			prediction = clasificador.makePrediction(bag);
			System.out.println("\nPrediction on a single instance:\n\t" + prediction.toString());			
			*/
			
			
		} catch (InvalidDataFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {

			e.printStackTrace();
		}
		

	}
	
}
