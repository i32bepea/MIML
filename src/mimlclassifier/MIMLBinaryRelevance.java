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

package mimlclassifier;

import data.Bag;
import data.MIMLInstances;
import mulan.classifier.InvalidDataException;
import mulan.classifier.MultiLabelOutput;
import mulan.classifier.transformation.BinaryRelevance;
import mulan.data.MultiLabelInstances;
import weka.classifiers.Classifier;

/**
 * 
 * Class implementing the BR algorithm for MIML data. A classifier is built for
 * each label and predictions are also gathered for each label.
 * 
 * @author Ana I. Reyes Melero
 * @author Eva Gibaja
 * @author Amelia Zafra
 * @version 20170507
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

}
