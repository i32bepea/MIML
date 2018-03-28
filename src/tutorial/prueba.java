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
import java.util.Arrays;
import java.util.Set;

import data.Bag;
import data.MIMLInstances;
import data.statistics.MILStatistics;
import data.statistics.MLStatistics;
import mimlclassifier.regularization.MIMLkNN;
import mimlclassifier.regularization.averageHausdorff;
import mulan.classifier.MultiLabelOutput;
import mulan.data.InvalidDataFormatException;
import mulan.data.MultiLabelInstances;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

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

public class prueba {

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
			
			String nameTrain = "miml_04_data";
			String nameTest = "miml_text_data_random_20test";
			
			String arffFileTrain = "data" + File.separator + nameTrain + ".arff";
			String xmlFileTrain = "data" + File.separator + nameTrain + ".xml";
			//String arffFileTest = "data" + File.separator + nameTest + ".arff";
			//String xmlFileTest = "data" + File.separator + nameTest + ".xml";
			
			// Parameter checking
			if (arffFileTrain.isEmpty()) {
				System.out.println("Arff pathName must be specified.");
				showUse();
			}
			if (xmlFileTrain.isEmpty()) {
				System.out.println("Xml pathName must be specified.");
				showUse();
			}

			// Loads the dataset
			System.out.println("Loading the dataset....");
			MIMLInstances mimlDataSetTrain = new MIMLInstances(arffFileTrain, xmlFileTrain);
			//MIMLInstances mimlDataSetTest = new MIMLInstances(arffFileTrain, xmlFileTrain);
					
			averageHausdorff metric = new averageHausdorff();
			MIMLkNN clasificador = new MIMLkNN(3,3, metric);
			
			clasificador.setDebug(true);
			clasificador.build(mimlDataSetTrain);
			
			for(int i = 0; i < mimlDataSetTrain.getNumBags(); ++i) {
				MultiLabelOutput pru = clasificador.makePredictionFinal(mimlDataSetTrain.getBag(i));	
			}
			
			
		} catch (InvalidDataFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {

			e.printStackTrace();
		}
		

	}

}
