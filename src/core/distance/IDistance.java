package core.distance;

import java.io.Serializable;

import data.Bag;
import weka.core.Instances;

/**
 * Interface to implements the metrics used to measure the
 * distance between bags of a data sets.
 *
 * @author Álvaro A. Belmonte
 * @author Amelia Zafra
 * @author Eva Gigaja
 * @version 20180619
 */
public interface IDistance extends Serializable{

	/**
	 * Get the distance between two bags.
	 *
	 * @param first
	 * 			first Bag
	 * @param second
	 * 			second Bag
	 * 
	 * @return distance between two bags
	 */
	public double distance(Bag first, Bag second) throws Exception;

	/**
	 * Get the distance between two bags in the form of a set of instances.
	 *
	 * @param first
	 * 			first Bag as Instances
	 * @param second
	 * 			second Bag as Instances
	 * 
	 * @return distance between two bags
	 */
	public double distance(Instances first, Instances second) throws Exception;

}
