package core.distance;

import java.io.Serializable;

import data.Bag;
import weka.core.Instances;

public interface IDistance extends Serializable{

	/**
	 * Get the average Hausdorff distance between two bags
	 */
	public double distance(Bag first, Bag second) throws Exception;

	/**
	 * Get the average Hausdorff distance between two bags in the form of a set of instances
	 */
	public double distance(Instances first, Instances second) throws Exception;

}
