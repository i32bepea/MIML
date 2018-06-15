package mimlclassifier;

import java.io.Serializable;



import data.MIMLInstances;
import mulan.classifier.MultiLabelOutput;
import weka.core.Instance;

public interface IMIMLClassifier extends Serializable{

	
	public MultiLabelOutput makePrediction(Instance instance) throws Exception;
	
	
	public void build(MIMLInstances trainingSet) throws Exception; 
	
	 /**
     * Sets whether debugging information should be output by the model
     * 
     * @param debug True to show debug information, False not to
     */
    public void setDebug(boolean debug);
}

