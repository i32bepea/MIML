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

package tutorial;

import java.io.File;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.XMLConfiguration;

import data.MIMLInstances;
import mimlclassifier.MIMLClassifier;
import mimlclassifier.regularization.MIMLkNN;
import utils.IConfiguration;
import weka.core.Utils;
import mulan.data.InvalidDataFormatException;
import mulan.evaluation.Evaluation;
import mulan.evaluation.Evaluator;

/**
 * 
 * Class implementing basic handling of MIML datasets.
 * 
 * @author Ana I.Reyes Melero
 * @author Eva Gibaja
 * @author Amelia Zafra
 * @version 20170507
 *
 */

public class exampleMIMLkNN {
	
	@SuppressWarnings("unchecked")
	static MIMLClassifier loadClassifier(Configuration jobConf) {
		
		MIMLClassifier classifier = null;
		
		try {

			String clsName = jobConf.getString("classifier[@name]");
			
			//Insantiate the classifier class used in the experiment
			Class<? extends MIMLClassifier> clsClass = 
					(Class <? extends MIMLClassifier>) Class.forName(clsName);
			
			classifier = clsClass.newInstance();
			//Configure de classifier
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
			System.out.println(configFile);
			
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
			System.out.println(((MIMLkNN) clasificador).getNumReferences());
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
			Evaluation resultsTT = evalTT.evaluate(clasificador, mimlTest, mimlTrain);
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
