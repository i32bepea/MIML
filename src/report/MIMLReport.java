package report;

import data.MIMLInstances;
import mulan.evaluation.Evaluation;
import mulan.evaluation.MultipleEvaluation;

public abstract class MIMLReport {

	/** Cross-validation evaluator type . */
	protected MultipleEvaluation evaluationCrossValidation;
	
	/** Holdout evaluator type. */
	protected Evaluation evaluationHoldout;
	
	/** The data. */
	protected MIMLInstances data;

	/**
	 * Instantiates a new reports with a cross-validation evaluator.
	 *
	 * @param evaluation 
	 * 			cross-validation evaluator type
	 * 
	 * @param data 
	 * 			the data used by the evaluator
	 */
	public MIMLReport(MultipleEvaluation evaluation, MIMLInstances data) {
		this.evaluationCrossValidation = evaluation;
		this.data = data;
	}
	
	/**
	 * Instantiates a new reports with a holdout evaluator.
	 *
	 * @param evaluation 
	 * 			holdout evaluator type
	 * 
	 * @param data 
	 * 			the data used by the evaluator
	 */
	public MIMLReport(Evaluation evaluation, MIMLInstances data) {
		this.evaluationHoldout = evaluation;
		this.data = data;
	};

	public MultipleEvaluation getEvaluationCrossValidation() {
		return evaluationCrossValidation;
	}

	public void setEvaluationCrossValidation(MultipleEvaluation evaluationCrossValidation) {
		this.evaluationCrossValidation = evaluationCrossValidation;
	}

	public Evaluation getEvaluationHoldout() {
		return evaluationHoldout;
	}

	public void setEvaluationHoldout(Evaluation evaluationHoldout) {
		this.evaluationHoldout = evaluationHoldout;
	}

	public MIMLInstances getData() {
		return data;
	}

	public void setData(MIMLInstances data) {
		this.data = data;
	}
	
}
