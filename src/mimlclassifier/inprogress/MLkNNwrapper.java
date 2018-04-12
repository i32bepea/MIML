package mimlclassifier.inprogress;

import org.apache.commons.configuration.Configuration;

import data.Bag;
import data.MIMLInstances;
import mimlclassifier.MIMLClassifier;
import mulan.classifier.InvalidDataException;
import mulan.classifier.MultiLabelOutput;
import mulan.classifier.lazy.MLkNN;
import mulan.data.MultiLabelInstances;
import weka.core.DistanceFunction;
import weka.core.Instance;

public class MLkNNwrapper extends MIMLClassifier{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Smooth factor */
	private double smooth = 1.0;
	
	/**Number of references*/
	private int num_references = 1;
	
	/** Metric for measure the distance between bags */
	private DistanceFunction metric;
	
	/** MIML data */
	private MIMLInstances dataset;
	
	/** ML data */
	private MultiLabelInstances datasetConverted;
	
	/** Classifier MLkNN */
	private MLkNN classifier;

	public MLkNNwrapper(int num_references, double smooth, DistanceFunction metric) {
		this.num_references = num_references;
		this.smooth = smooth;
		this.metric = metric;
		this.classifier = new MLkNN(num_references, smooth);
	}
	
	public MLkNNwrapper(DistanceFunction metric) {
		this.metric = metric;
		this.classifier = new MLkNN(num_references, smooth);
	}

	/**
	 *  No-arg constructor for xml configuration   
	*/
	public MLkNNwrapper() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void buildInternal(MIMLInstances trainingSet) throws Exception {
		
		dataset = trainingSet;
		datasetConverted = dataset.getMLDataSet();
		classifier.setDfunc(metric);		
		classifier.build(datasetConverted);
	}

	@Override
	protected MultiLabelOutput makePredictionInternal(Bag instance) throws Exception, InvalidDataException {
		
		Instance bagAsInstance = (Instance) instance;
		MultiLabelOutput predictions = classifier.makePrediction(bagAsInstance);
		
		return predictions;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void configure(Configuration configuration) {
		
		this.num_references = configuration.getInt("nReferences");
		this.smooth = configuration.getDouble("smooth");
		
		try {
			//Get the name of the metric class
			String metricName = configuration.getString("metric[@name]");
			//Instance class
			Class<? extends DistanceFunction> metricClass = 
					(Class <? extends DistanceFunction>) Class.forName(metricName);
			
			this.metric = metricClass.newInstance();
		}
		catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		this.classifier = new MLkNN(num_references, smooth);
	}
	
	public double getSmooth() {
		return smooth;
	}

	public void setSmooth(double smooth) {
		this.smooth = smooth;
	}

	public int getNum_references() {
		return num_references;
	}

	public void setNum_references(int num_references) {
		this.num_references = num_references;
	}

	public DistanceFunction getMetric() {
		return metric;
	}

	public void setMetric(DistanceFunction metric) {
		this.metric = metric;
	}



}
