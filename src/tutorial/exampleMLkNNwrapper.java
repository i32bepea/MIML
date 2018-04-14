package tutorial;

import java.io.File;

import data.Bag;
import data.MIMLInstances;
import mimlclassifier.MIMLClassifier;
import mulan.classifier.MultiLabelOutput;
import mulan.data.InvalidDataFormatException;
import mulan.evaluation.Evaluation;
import mulan.evaluation.Evaluator;
import mulan.evaluation.MultipleEvaluation;
import utils.ConfigLoader;
import utils.IConfiguration;
import weka.core.Utils;

public class exampleMLkNNwrapper {

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
			
			ConfigLoader loader = new ConfigLoader(Utils.getOption("c", args));

			// String arffFileName = Utils.getOption("f", args);
			// String xmlFileName = Utils.getOption("x", args);
			// String arffFileName = "data+File.separator+miml_text_data.arff";
			// String xmlFileName = "data+File.separator+miml_text_data.xml";
	
			// EJECUCIÓN => -c configurations/MLkNNwrapper.config 

			MIMLClassifier classifier = loader.loadClassifier();
			MIMLInstances mimlTrain = loader.loadTrainData();
			MIMLInstances mimlTest = loader.loadTestData();
			
			
			Evaluator evalCV = new Evaluator();
			MultipleEvaluation resultsCV;
			int numFolds = 5;
			System.out.println("\nPerforming " + numFolds + "-fold cross-validation:\n");
			resultsCV = evalCV.crossValidate(classifier, mimlTrain, numFolds);
			System.out.println("\nResults on cross validation evaluation:\n" + resultsCV);
			
			// Performs a train-test evaluation
			/*
			classifier.build(mimlTrain);
			Evaluator evalTT = new Evaluator();
			System.out.println("\nPerforming train-test evaluation:\n");
			Evaluation resultsTT = evalTT.evaluate(classifier, mimlTest, mimlTrain);
			System.out.println("\nResults on train test evaluation:\n" + resultsTT);
			*/
			/*
			Bag bag = mimlTrain.getBag(1266);
			MultiLabelOutput prediction = classifier.makePrediction(bag);
			System.out.println("\nPrediction on a single instance:\n\t" + prediction.toString());
			bag = mimlTrain.getBag(1267);
			prediction = classifier.makePrediction(bag);
			System.out.println("\nPrediction on a single instance:\n\t" + prediction.toString());
			bag = mimlTrain.getBag(1261);
			prediction = classifier.makePrediction(bag);
			System.out.println("\nPrediction on a single instance:\n\t" + prediction.toString());			
			*/
			
			
		} catch (InvalidDataFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {

			e.printStackTrace();
		}
		

	}
	
}
