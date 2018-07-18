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

import core.ConfigLoader;
import evaluation.IEvaluator;
import mimlclassifier.IMIMLClassifier;
import mulan.data.InvalidDataFormatException;
import report.IReport;
import weka.core.Utils;

/**
 * Class that allow run any algorithm of the library configured by a
 * file configuration.
 * 
 * @author Alvaro A. Belmonte
 * @author Amelia Zafra
 * @author Eva Gigaja
 * @version 20180619
 */
public class RunAlgorithm {	

	/**
	 * The main method to configure and run an algorithm.
	 *
	 * @param args 
	 * 			the arguments(route of config file with the option -c)
	 */
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) {

		try {
			// example execution => -c configurations/MIMLkNN.config 
			ConfigLoader loader = new ConfigLoader(Utils.getOption("c", args));
			
			System.out.println("Loading classifier");
			IMIMLClassifier classifier = loader.loadClassifier();
			
			System.out.println("Loading evaluation method");
			IEvaluator evaluator = loader.loadEvaluator();
			evaluator.runExperiment(classifier);
			
			IReport report = loader.loadReport();
			
			report.saveReport(report.toCSV(evaluator));
			
			System.out.println("Results:");
			System.out.println(report.toString(evaluator));
			
		} catch (InvalidDataFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
