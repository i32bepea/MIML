package mimlclassifier.inprogress;

import java.util.Enumeration;

import data.Bag;
import mimlclassifier.regularization.AverageHausdorff;
import weka.core.DistanceFunction;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.neighboursearch.PerformanceStats;

public class AverageHausdorffDistance implements DistanceFunction {

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
		
		double distance = 0.0;
		
		try {
			Bag bag1 = new Bag(arg0);
			Bag bag2 = new Bag(arg1);
			
			AverageHausdorffDistance metric = new AverageHausdorffDistance();
			
			distance = metric.distance(bag1, bag2);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return distance;
	}

	@Override
	public double distance(Instance arg0, Instance arg1, PerformanceStats arg2) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double distance(Instance arg0, Instance arg1, double arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double distance(Instance arg0, Instance arg1, double arg2, PerformanceStats arg3) {
		// TODO Auto-generated method stub
		return 0;
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
