package mimlclassifier.inprogress;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.stream.DoubleStream;

import data.Bag;
import weka.core.DistanceFunction;
import weka.core.EuclideanDistance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.neighboursearch.PerformanceStats;

public class AverageHausdorffDistance implements Serializable, DistanceFunction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String[] getOptions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration listOptions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setOptions(String[] arg0) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public double distance(Instance arg0, Instance arg1){		
		return distance(arg0, arg1, Double.POSITIVE_INFINITY);
	}

	@Override
	public double distance(Instance arg0, Instance arg1, PerformanceStats arg2) throws Exception {
		return distance(arg0, arg1, Double.POSITIVE_INFINITY, arg2);
	}

	@Override
	public double distance(Instance arg0, Instance arg1, double arg2) {
		return distance(arg0, arg1, arg2, null);
	}

	@Override
	public double distance(Instance arg0, Instance arg1, double arg2, PerformanceStats arg3) {
		
		double finalDistance = 0.0;
		
		try {
			
			Bag first = new Bag(arg0);
			Bag second = new Bag(arg1);
	
		
			EuclideanDistance euclideanDistance = new EuclideanDistance(first.getBagAsInstances());
			
			int nInstances = second.getBagAsInstances().size();
			
			int idx = 0;
			double sumU = 0.0;
			double[] minDistancesV = new double[nInstances];
			Arrays.fill(minDistancesV, Double.MAX_VALUE);
			
			for(Instance u : first.getBagAsInstances()) {
							
				double minDistance = Double.MAX_VALUE;
				
				for(Instance v : second.getBagAsInstances()) {
					
					double distance = euclideanDistance.distance(u, v, arg2, arg3);
	
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
			
			finalDistance =  (sumU + sumV) / (first.getNumInstances() + second.getNumInstances());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return finalDistance;
	}

	@Override
	public String getAttributeIndices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Instances getInstances() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getInvertSelection() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void postProcessDistances(double[] arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setAttributeIndices(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setInstances(Instances arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setInvertSelection(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Instance arg0) {
		// TODO Auto-generated method stub

	}

}
