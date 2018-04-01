package mimlclassifier.regularization;

import java.util.Arrays;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import data.Bag;
import data.MIMLInstances;
import mimlclassifier.MIMLClassifier;
import mulan.classifier.InvalidDataException;
import mulan.classifier.MultiLabelOutput;
import mulan.core.ArgumentNullException;
import weka.core.matrix.Matrix;
import weka.core.matrix.SingularValueDecomposition;

public class MIMLkNN extends MIMLClassifier {

	/** For serialization */
	private static final long serialVersionUID = 1L;
	
	/** Number of citers*/
	private int num_citers = 1;
	
	/**Number of references*/
	private int num_references = 1;
	
	/** Metric for measure the distance between bags */
	private IDistance metric;
	
	/** MIML data */
	private MIMLInstances dataset;
	
	/** Dataset size (number of bags) */
	int d_size;
	
	private double[][] distance_matrix;
	private int[][] ref_matrix;
	
	private double[][] weights_matrix;	
	private double[][] t_matrix;	
	private double[][] phi_matrix;	
	
	public MIMLkNN(int num_references, int num_citers, IDistance metric) {
		this.num_citers = num_citers;
		this.num_references = num_references;
		this.metric = metric;
	}
	
	public MIMLkNN(IDistance metric) {
		this.metric = metric;
	}

	@Override
	protected void buildInternal(MIMLInstances trainingSet) throws Exception {
		if (trainingSet == null) {
			throw new ArgumentNullException("trainingSet");
		}
		
		this.dataset = trainingSet;
		d_size = trainingSet.getNumBags();
		
		// Change num_references if its necessary
		if(d_size <= num_references)
			num_references = d_size-1;		
		
		// Initialize matrices
		t_matrix = new double[d_size][numLabels];
		phi_matrix = new double[d_size][numLabels];
		
		long startTime = System.currentTimeMillis();
		
		System.out.println("Calculando matriz de distancias...");
		calculateDatasetDistances();
		System.out.println("Calculando referencias y citas...");
		calculateReferenceMatrix();
		
		for(int i = 0; i < d_size; ++i) {
		    Integer[] neighbors = getUnionNeighbors(i);		    
		    // Update matrices
		    phi_matrix[i] = calculateRecordLabel(neighbors).clone();
		    t_matrix[i] = getBagLabels(i).clone(); 
		}
		
		weights_matrix = getWeightsMatrix();
		
		System.out.println("+Matriz de pesos:");
		for(int i = 0; i < numLabels; ++i)
			System.out.println(Arrays.toString(weights_matrix[i]));
		
		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		System.out.println("\n-Tiempo transcurrido en crear el modelo: " + elapsedTime);
	}

	@Override
	protected MultiLabelOutput makePredictionInternal(Bag instance) throws Exception, InvalidDataException {
		// Create a new distances matrix 
		double[][] distanceMatrixCopy = distance_matrix.clone();
		distance_matrix = new double[d_size+1][d_size+1];
		double[] distances = new double[d_size];
		
		for(int i = 0; i < d_size; ++i) {
			for(int j = i; j < d_size; ++j) {
				// Fill distance matrix
				double distance = distanceMatrixCopy[i][j];
				distance_matrix[i][j] = distance;
				distance_matrix[j][i] = distance;
			}
		}
		
		for(int i = 0; i < d_size; ++i) {
			double distance = metric.distance(instance, dataset.getBag(i));
			distances[i] = distance;
			// Upgrade distance matrix;
			distance_matrix[i][d_size] = distance;
			distance_matrix[d_size][i] = distance;
		}
		//Update d_size to calculate references matrix
		d_size++;
		//System.out.println("Calculando referencias y citas...");
		calculateReferenceMatrix();
		//Restore d_size value
		d_size--;
		
		Integer[] neighbors = getUnionNeighbors(d_size);
		double[] recordLabel =  calculateRecordLabel(neighbors);
		
		int[] predictedLabel = new int[numLabels];
		
		for(int i = 0; i < numLabels; ++i) {
			double[] column = new double[numLabels];
			
			for(int j = 0; j < numLabels; ++j)
				column[i] = weights_matrix[j][i];
			
			double decision = linearClassifier(column, recordLabel);
			if (decision > 0)
				predictedLabel[i] = 1;
		}
		/*		
		System.out.print("Clases predichas: [");
		for(int i = 0; i < numLabels; ++i)
			System.out.print(predictedLabel[i] + ",");
		System.out.println("]");*/
				
		
		MultiLabelOutput finalDecision = new MultiLabelOutput(predictedLabel);
		distance_matrix = distanceMatrixCopy.clone();
				
		return finalDecision;
	}
	
	private void calculateDatasetDistances() throws Exception {

		distance_matrix = new double[d_size][d_size];
		double distance;
		
		for(int i = 0; i < d_size; ++i) {
			
			Bag first = dataset.getBag(i);
			for(int j = i; j < d_size; ++j) {
				Bag second = dataset.getBag(j);
				distance = metric.distance(first, second);
				distance_matrix[i][j] = distance;
				distance_matrix[j][i] = distance;
				//System.out.println("Distancia ("+i+","+j+")" + distance);
			}
		}
	}
	
	private void calculateReferenceMatrix() throws Exception {
		
		ref_matrix = new int[d_size][d_size];
		
		for(int i = 0; i < d_size; ++i) {
			int[] references = calculateBagReferences(i);
			for(int j = 0; j < references.length; ++j) 
				ref_matrix[i][references[j]] = 1;		
		}
	}
	
	private int[] calculateBagReferences(int indexBag) throws Exception {
		// Nearest neighbors of the selected bag
		int[] nearestNeighbors = new int[num_references];
		// Store indices in priority queue, sorted by distance to selected bag
		PriorityQueue<Integer> pq = new PriorityQueue<Integer>(d_size, 
				(a, b) -> Double.compare(distance_matrix[indexBag][a], distance_matrix[indexBag][b]));
		
		for(int i = 0; i < d_size; ++i) {
			if(i != indexBag)	
				pq.add(i);
		}
		// Get the  R (num_references) nearest neighbors
    	for (int i = 0; i < num_references; ++i) 
    		nearestNeighbors[i] = pq.poll();
   
    	return nearestNeighbors;
	}
	
	private int[] getReferences(int indexBag) {
		
		int[] references = new int[num_references];
		int idx = 0;
		
		for(int i = 0; i < d_size; ++i) {
			if(ref_matrix[indexBag][i] == 1) {
				references[idx] = i;
				idx++;	
			}
		}	
		return references;
	}
	
	private int[] getCiters(int indexBag) {
		
		PriorityQueue<Integer> pq = new PriorityQueue<Integer>(num_references,
				(a, b) -> Double.compare(distance_matrix[indexBag][a], distance_matrix[indexBag][b]));
	
		for(int i = 0; i < d_size; ++i) 
			if(ref_matrix[i][indexBag] == 1)
				pq.add(i);
		
		int citers = (num_citers < pq.size()) ? num_citers : pq.size();
		// Nearest citers of the selected bag
		int[] nearestCiters = new int[citers];
		// Get the C (num_citers or pq.size()) nearest citers
		for(int i = 0; i < citers; ++i)
			nearestCiters[i] = pq.poll();
		
		return nearestCiters;
	}
	
	private Integer[] getUnionNeighbors(int indexBag) {
		
		int[] references = getReferences(indexBag);
		int[] citers = getCiters(indexBag);

		// Union references and citers sets
		Set<Integer> set = new HashSet<Integer>();
		
	    for (int j = 0; j < references.length; j++)
	        set.add(references[j]);
	    for (int j = 0; j < citers.length; j++)
	        set.add(citers[j]);
	    
	    Integer[] union = set.toArray(new Integer[set.size()]);	
	    return union;
	}
	
	private double[] calculateRecordLabel(Integer[] indices) {
		
		double[] labelCount = new double[numLabels];
		
		for(int i = 0; i < indices.length; ++i) {
			for(int j = 0; j < numLabels; ++j) {
				if (dataset.getDataSet().instance(indices[i]).stringValue(labelIndices[j]).equals("1"))
					labelCount[j]++;			
			}
		}
		return labelCount;
	}

	private double[] getBagLabels(int bagIndex) {
		
		double[] labels = new double[numLabels];
		
		for(int i = 0; i< numLabels; ++i) {
			if (dataset.getDataSet().instance(bagIndex).stringValue(labelIndices[i]).equals("1"))
				labels[i] = 1;			
		}	
		return labels;
	}
	
	private double[][] getWeightsMatrix(){
		
		Matrix tMatrix = new Matrix(t_matrix);
		Matrix phiMatrix = new Matrix(phi_matrix);
		Matrix phiMatrixT = phiMatrix.transpose();
		
		Matrix A = phiMatrixT.times(phiMatrix);
		Matrix B = phiMatrixT.times(tMatrix);
		Matrix inverseA;
		
		if(A.det() == 0) {
			SingularValueDecomposition svd = A.svd();
			Matrix S = svd.getS();
			Matrix U = svd.getU();
			Matrix V = svd.getV();
			
			double[][] sDouble = S.getArray();
			double value;
			double threshold = 10e-12;
			
			for(int i = 0; i < sDouble[0].length; ++i) {
				value = sDouble[i][i];
				if ( value < threshold)
					sDouble[i][i] = 0;
				else
					sDouble[i][i] = 1.0/value;
			}
			
			S = new Matrix(sDouble);
			inverseA = V.times(S);
			inverseA = inverseA.times(U.transpose());		
		}
		else
			inverseA = A.inverse();

		Matrix solution = inverseA.times(B);
		
		return solution.getArrayCopy();
	}
	
	private double linearClassifier(double[] weights, double[] record) {
		
		double decision = 0.0;
		
		for(int i = 0; i < numLabels; ++i)
			decision += weights[i]*record[i];
		
		return decision;
	}
	
	/** Returns the number of citers considered to estimate the class prediction of tests bags*/
	public int getNumCiters() {
		return num_citers;
	}

	/**Sets the number of citers considered to estimate the class prediction of tests bags*/
	public void setNumCiters(int numCiters) {
		this.num_citers = numCiters;
	}

	/**Returns the number of references considered to estimate the class prediction of tests bags*/
	public int getNumReferences() {
		return num_references;
	}

	/**Sets the number of references considered to estimate the class prediction of tests bags*/
	public void setNumReferences(int numReferences) {
		this.num_references = numReferences;
	}	
	
}
