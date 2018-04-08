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

import org.apache.commons.configuration.Configuration;

import data.Bag;
import data.MIMLInstances;
import mulan.classifier.InvalidDataException;
import mulan.classifier.MultiLabelOutput;
import mulan.classifier.transformation.LabelPowerset;
import mulan.data.MultiLabelInstances;
import weka.classifiers.Classifier;

/**
 * Class inheriting the LP algorithm for MIML data. Each label set in the
 * dataset is considered as a new class and then single-label classification is
 * performed.
 * 
 * @author Ana I. Reyes
 * @author Eva Gibaja
 * @author Amelia Zafra
 * @version 20170507
 */
public class MIMLLabelPowerset extends MIMLClassifier {
	/** For serialization */
	private static final long serialVersionUID = 1L;

	/** An LP classifier */
	private LabelPowerset LP;

	/**
	 * Constructor.
	 * 
	 * @param baseClassifier
	 *            Classifier
	 * @throws Exception
	 *             To be handled in an upper level.
	 */
	public MIMLLabelPowerset(Classifier baseClassifier) throws Exception {
		super();
		LP = new LabelPowerset(baseClassifier);
	}

	@Override
	protected void buildInternal(MIMLInstances dataSet) throws Exception {
		MultiLabelInstances mlData = new MultiLabelInstances(dataSet.getDataSet(), dataSet.getLabelsMetaData());
		LP.setDebug(getDebug());
		LP.build(mlData);
	}

	@Override
	protected MultiLabelOutput makePredictionInternal(Bag bag) throws Exception, InvalidDataException {
		return LP.makePrediction(bag);
	}

	@Override
	public void configure(Configuration configuration) {
		// TODO Auto-generated method stub
		
	}

}
