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

import java.io.PrintWriter;

import data.MIMLInstances;
import mimlclassifier.MIMLClassifier;
import mulan.data.InvalidDataFormatException;
import mulan.evaluation.Evaluation;
import mulan.evaluation.Evaluator;
import mulan.evaluation.MultipleEvaluation;
import utils.ConfigLoader;
import utils.Reports;
import weka.core.Utils;

public class RunAlgorithm {	

	public static void main(String[] args) {

		try {

			ConfigLoader loader = new ConfigLoader(Utils.getOption("c", args));

			// String arffFileName = Utils.getOption("f", args);
			// String xmlFileName = Utils.getOption("x", args);
			// String arffFileName = "data+File.separator+miml_text_data.arff";
			// String xmlFileName = "data+File.separator+miml_text_data.xml";
	
			// EJECUCIÓN => -c configurations/MIMLkNN.config 

			MIMLClassifier classifier = loader.loadClassifier();
			MIMLInstances mimlTrain = loader.loadTrainData();
			MIMLInstances mimlTest = loader.loadTestData();
			Evaluator evalCV = new Evaluator();
			MultipleEvaluation resultsCV;
			
			int numFolds = 5;
			System.out.println("\nPerforming " + numFolds + "-fold cross-validation:\n");
			resultsCV = evalCV.crossValidate(classifier, mimlTrain, numFolds);
			

			//System.out.println("\nResults on cross validation evaluation:\n" + resultsCV);
			
			// Performs a train-test evaluation
			/*
			classifier.build(mimlTrain);
			Evaluator evalTT = new Evaluator();
			System.out.println("\nPerforming train-test evaluation:\n");
			Evaluation resultsTT = evalTT.evaluate(classifier, mimlTest, mimlTrain);
			System.out.println("\nResults on train test evaluation:\n" + resultsTT);*/
			
			
			String filename = loader.loadNameCSV();
			
			if(filename != null) {
				
				try (PrintWriter out = new PrintWriter(filename)) {
					
					Reports report = new Reports(resultsCV, mimlTrain);
					
					System.out.println(resultsCV.toString());
					System.out.println("Guardando resultados en " + filename + "...");
				    out.println(report.toCSV());
				    out.close();
				}
			}
			
			
		} catch (InvalidDataFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {

			e.printStackTrace();
		}
		

	}

}
