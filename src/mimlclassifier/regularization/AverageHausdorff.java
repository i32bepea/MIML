package mimlclassifier.regularization;

import java.util.Arrays;
import java.util.stream.DoubleStream;
import data.Bag;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.neighboursearch.PerformanceStats;
import weka.core.EuclideanDistance;

public class AverageHausdorff implements IDistance {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2002702276955682922L;

	/**
	 * Get the average Hausdorff distance between two bags
	 */
	public double distance(Bag first, Bag second) throws Exception {
		
		EuclideanDistance euclideanDistance = new EuclideanDistance(first.getBagAsInstances());
		
		int nInstances = second.getBagAsInstances().size();
		
		int idx = 0;
		double sumU = 0.0;
		double[] minDistancesV = new double[nInstances];
		Arrays.fill(minDistancesV, Double.MAX_VALUE);
		
		for(Instance u : first.getBagAsInstances()) {
						
			double minDistance = Double.MAX_VALUE;
			
			for(Instance v : second.getBagAsInstances()) {
				
				double distance = euclideanDistance.distance(u, v);

				if ( distance < minDistance)
					minDistance = distance;
				
				if (distance < minDistancesV[idx])
					minDistancesV[idx] = distance;
				
				idx++;
			}
			
			idx = 0;
			sumU += minDistance;
		}
		
		double sumV = DoubleStream.of(minDistancesV).sum();

		return (sumU + sumV) / (first.getNumInstances() + second.getNumInstances());
	}

	/**
	 * Get the average Hausdorff distance between two bags in the form of a set of instances
	 */
	public double distance(Instances first, Instances second) throws Exception {
		
		EuclideanDistance euclideanDistance = new EuclideanDistance(first);
		euclideanDistance.setDontNormalize(true);
		
		int nInstances = second.size();
		
		int idx = 0;
		double sumU = 0.0;
		double[] minDistancesV = new double[nInstances];
		Arrays.fill(minDistancesV, Double.MAX_VALUE);
		
		for(int i = 0; i < first.size(); ++i) {
						
			Instance u = first.instance(i);
			
			double minDistance = Double.MAX_VALUE;
			
			for(int j = 0; j < nInstances; ++j) {
				
				double distance = euclideanDistance.distance(u, second.instance(j));
				
				if ( distance < minDistance)
					minDistance = distance;
				
				if (distance < minDistancesV[idx])
					minDistancesV[idx] = distance;
				
				idx++;
			}
			
			idx=0;
			sumU += minDistance;
		}
		
		double sumV = DoubleStream.of(minDistancesV).sum();

		return (sumU + sumV) / (first.size() + second.size());
	}
	
}
