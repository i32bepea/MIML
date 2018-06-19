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

