package report;

import data.MIMLInstances;
import mulan.evaluation.Evaluation;
import mulan.evaluation.MultipleEvaluation;

/**
 * The Class MIMLReport.
 */
public abstract class MIMLReport implements IReport{

	/** Cross-validation evaluator type . */
	protected MultipleEvaluation evaluationCrossValidation;
	
	/** Holdout evaluator type. */
	protected Evaluation evaluationHoldout;
	
	/** The data. */
	protected MIMLInstances data;

	/**
	 * Instantiates a new report with cross-validation evaluator.
	 *
	 * @param evaluation 
	 * 				cross-validation evaluator
	 * @param data 
	 * 			the data used in the evaluation
	 */
	public MIMLReport(MultipleEvaluation evaluation, MIMLInstances data) {
		this.evaluationCrossValidation = evaluation;
		this.data = data;
	}
	
	/**
	 * Instantiates a new report with a holdout evaluator.
	 *
	 * @param evaluation 
	 * 				holdout evaluator
	 * @param data 
	 * 			the data used in the evaluation
	 */
	public MIMLReport(Evaluation evaluation, MIMLInstances data) {
		this.evaluationHoldout = evaluation;
		this.data = data;
	}
	
	/**
	 *  No-argument constructor for xml configuration.
	 */
	public MIMLReport() {
	}

	/**
	 * Gets the cross-validation evaluator.
	 *
	 * @return the cross validation evaluator
	 */
	public MultipleEvaluation getEvaluationCrossValidation() {
		return evaluationCrossValidation;
	}

	/**
	 * Sets a cross-validation evaluator.
	 *
	 * @param evaluationCrossValidation 
	 * 						the new cross validation evaluator
	 */
	public void setEvaluationCrossValidation(MultipleEvaluation evaluationCrossValidation) {
		this.evaluationCrossValidation = evaluationCrossValidation;
	}

	/**
	 * Gets the holdout evaluator.
	 *
	 * @return the holdout evaluator
	 */
	public Evaluation getEvaluationHoldout() {
		return evaluationHoldout;
	}

	/**
	 * Sets a holdout evaluator.
	 *
	 * @param evaluationHoldout
	 * 					 the new holdout evaluator
	 */
	public void setEvaluationHoldout(Evaluation evaluationHoldout) {
		this.evaluationHoldout = evaluationHoldout;
	}

	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public MIMLInstances getData() {
		return data;
	}

	/**
	 * Sets the data.
	 *
	 * @param data the new data
	 */
	public void setData(MIMLInstances data) {
		this.data = data;
	}
	
}
