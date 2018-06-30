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

import java.io.Serializable;



import data.MIMLInstances;
import mulan.classifier.MultiLabelOutput;
import weka.core.Instance;


/**
 * The Interface for MIML classifiers.
 *
 * @author Álvaro A. Belmonte
 * @author Amelia Zafra
 * @author Eva Gigaja
 * @version 20180619
 */
public interface IMIMLClassifier extends Serializable{

	
	/**
	 * Make a prediction.
	 *
	 * @param instance 
	 * 			the instance to be predicted
	 * 
	 * @return the multi label output
	 */
	public MultiLabelOutput makePrediction(Instance instance) throws Exception;
	
	
	/**
	 * Builds the classifier.
	 *
	 * @param trainingSet 
	 * 				the training set
	 */
	public void build(MIMLInstances trainingSet) throws Exception; 
	
	 /**
 	 * Sets whether debugging information should be output by the model.
 	 *
 	 * @param debug 
 	 * 			True to show debug information, False not to.
 	 */
    public void setDebug(boolean debug);
}

