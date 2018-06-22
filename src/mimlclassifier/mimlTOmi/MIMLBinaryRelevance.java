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

package mimlclassifier.mimlTOmi;

import org.apache.commons.configuration.Configuration;




import data.Bag;
import data.MIMLInstances;
import mimlclassifier.MIMLClassifier;
import mulan.classifier.InvalidDataException;
import mulan.classifier.MultiLabelOutput;
import mulan.classifier.transformation.BinaryRelevance;
import mulan.data.MultiLabelInstances;
import weka.classifiers.Classifier;
import weka.classifiers.AbstractClassifier;

/**
 * 
 * Class implementing the BR algorithm for MIML data. A classifier is built for
 * each label and predictions are also gathered for each label.
 * 
 * @author Ana I. Reyes Melero
 * @author Eva Gibaja
 * @author Amelia Zafra
 * @author Álvaro A. Belmonte
 * @version 20180619
 *
 */
public class MIMLBinaryRelevance extends MIMLClassifier {

	/** For serialization */
	private static final long serialVersionUID = 1L;

	/** A BinaryRelevance classifier */
	private BinaryRelevance BR;

	/**
	 * Constructor.
	 * 
	 * @param baseClassifier
	 *            Classifier
	 * @throws Exception
	 *             To be handled in an upper level.
	 */
	public MIMLBinaryRelevance(Classifier baseClassifier) throws Exception {
		super();
		BR = new BinaryRelevance(baseClassifier);
	}

	@Override
	public void buildInternal(MIMLInstances dataSet) throws Exception {
		MultiLabelInstances mlData = new MultiLabelInstances(dataSet.getDataSet(), dataSet.getLabelsMetaData());
		BR.setDebug(getDebug());
		BR.build(mlData);
	}

	@Override
	protected MultiLabelOutput makePredictionInternal(Bag bag) throws Exception, InvalidDataException {
		return BR.makePrediction(bag);
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public void configure(Configuration configuration) {
		
		try {
			//Get the name of the base classifier class
			String baseName = configuration.getString("multiInstanceClassifier[@name]");
			//Instance class
			Class<? extends Classifier> baseClassifier = 
					(Class <? extends Classifier>) Class.forName(baseName);
				
			String optionsAux = configuration.getString("multiInstanceClassifier[@listOptions]");
			
			if(optionsAux !=null){
				String []  options = optionsAux.split(" ");
			
				Classifier classifier = baseClassifier.newInstance();
			
				((AbstractClassifier) classifier).setOptions(options);
				
				BR = new BinaryRelevance(classifier);
			}
			else
				BR = new BinaryRelevance(baseClassifier.newInstance());
	       
			
			
		}
		catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}			
	}
}
