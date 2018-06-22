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

package miml;

import java.io.PrintWriter;
import java.lang.reflect.Method;

import core.ConfigLoader;
import mimlclassifier.MIMLClassifier;
import mulan.data.InvalidDataFormatException;
import mulan.evaluation.Evaluation;
import mulan.evaluation.MultipleEvaluation;
import report.BaseMIMLReport;
import report.IReport;
import report.MIMLReport;
import weka.core.Utils;

/**
 * Class that allow run any algorithm of the library configured by a
 * file configuration.
 * 
 * @author A.A Belmonte
 * @author Amelia Zafra
 * @author Eva Gigaja
 * @version 20180619
 */
public class RunAlgorithm {	
	
	/**
	 * Load the evaluation method and run the algorithm.
	 *
	 * @param loader 
	 * 			the experiment's configuration
	 * @param classifier 
	 * 			the classifier used in the experiment
	 * 
	 * @return the reports
	 */
	private static IReport run(ConfigLoader loader, MIMLClassifier classifier) throws Exception {
		
		System.out.println("Loading evaluation method");
		Method evaluation = loader.loadEvaluator(classifier);
		MIMLReport report = null;
		
		if("crossValidate".equals(loader.getEvalMethod())){
			System.out.println("initializing cross validation...");
			MultipleEvaluation results = (MultipleEvaluation) evaluation.invoke(loader.getEvaluator(), loader.getParams());
			report = loader.loadReportCrossValidation(results);
		}
		else if("evaluate".equals(loader.getEvalMethod())) {
			System.out.println("Building model...");
			classifier.build(loader.getData());
			System.out.println("Getting evaluation results...");
			Evaluation results = (Evaluation) evaluation.invoke(loader.getEvaluator(), loader.getParams());
			report = loader.loadReportHoldout(results);
		}
		
		return ((IReport) report);
	}

	/**
	 * The main method.
	 *
	 * @param args 
	 * 			the arguments(route of config file with the option -c)
	 */
	public static void main(String[] args) {

		try {
			// example execution => -c configurations/MIMLkNN.config 
			ConfigLoader loader = new ConfigLoader(Utils.getOption("c", args));
			
			System.out.println("Loading classifier");
			MIMLClassifier classifier = loader.loadClassifier();
			
			IReport report = run(loader, classifier);
			
			String filename = loader.loadReportName();
			
			if(filename != null) {
				try (PrintWriter out = new PrintWriter(filename)) {
										
				    out.println(report.toCSV());
				    out.close();
				    System.out.println("Results saved in " + filename);
				}
			}
			
			System.out.print("Results:");
			System.out.println(report.toString());
			
		} catch (InvalidDataFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {

			e.printStackTrace();
		}
		
	}

}
