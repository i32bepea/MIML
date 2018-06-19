package core.distance;

import data.Bag;
import weka.core.EuclideanDistance;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Class that implements Minimal Hausdorff metric to measure 
 * the distance between 2 bags of a data set
 * 
 * @author Álvaro A. Belmonte
 * @author Amelia Zafra
 * @author Eva Gigaja
 * @version 20180619
 */
public class MinimalHausdorff implements IDistance {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4225065329008023904L;


	/**
	 * Get the minimal Hausdorff distance between two bags.
	 *
	 * @param first
	 * 			first Bag
	 * @param seconf
	 * 			second Bag
	 * 
	 * @return distance between two bags
	 */
	@Override
	public double distance(Bag first, Bag second) throws Exception {
		
		EuclideanDistance euclideanDistance = new EuclideanDistance(first.getBagAsInstances());
		double finalDistance = Double.MAX_VALUE;
		
		for(Instance u : first.getBagAsInstances()) {
			
			for(Instance v : second.getBagAsInstances()) {
				
				double distance = euclideanDistance.distance(u, v);

				if ( distance < finalDistance)
					finalDistance = distance;
			}
		}	

		return finalDistance;
	}

	/**
	 * Get the minimal Hausdorff distance between two bags in the form of a set of instances.
	 *
	 * @param first
	 * 			first Bag as Instances
	 * @param second
	 * 			second Bag as Instances
	 * 
	 * @return distance between two bags
	 */
	@Override
	public double distance(Instances first, Instances second) throws Exception {
		
		EuclideanDistance euclideanDistance = new EuclideanDistance(first);
		euclideanDistance.setDontNormalize(true);
		
		int nInstances = second.size();
		double finalDistance = Double.MAX_VALUE;
		
		for(int i = 0; i < first.size(); ++i) {
						
			Instance u = first.instance(i);
			
			for(int j = 0; j < nInstances; ++j) {
				
				double distance = euclideanDistance.distance(u, second.instance(j));
				
				if ( distance < finalDistance)
					finalDistance = distance;
			}
		}
		
		return finalDistance;
	}

}
