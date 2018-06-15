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
import report.Reports;
import weka.core.Utils;

public class RunAlgorithm {	
	
	private static Reports run(ConfigLoader loader, MIMLClassifier classifier) throws Exception {
		
		System.out.println("Loading evaluation method");
		Method evaluation = loader.loadEvaluator(classifier);
		Reports report = null;
		
		if("crossValidate".equals(loader.getEvalMethod())){
			System.out.println("initializing cross validation...");
			MultipleEvaluation results = (MultipleEvaluation) evaluation.invoke(loader.getEvaluator(), loader.getParams());
			report = new Reports(results, loader.getData());
		}
		else if("evaluate".equals(loader.getEvalMethod())) {
			System.out.println("Building model...");
			classifier.build(loader.getData());
			System.out.println("Getting evaluation results...");
			Evaluation results = (Evaluation) evaluation.invoke(loader.getEvaluator(), loader.getParams());
			report = new Reports(results, loader.getData());
		}
		
		
		return report;
	}

	public static void main(String[] args) {

		try {
			// EJECUCI�N => -c configurations/MIMLkNN.config 
			ConfigLoader loader = new ConfigLoader(Utils.getOption("c", args));
			
			System.out.println("Loading classifier");
			MIMLClassifier classifier = loader.loadClassifier();
			
			Reports report = run(loader, classifier);
			
			String filename = loader.loadNameCSV();
			
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
