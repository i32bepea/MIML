package report;

import java.util.ArrayList;
import java.util.List;

import data.MIMLInstances;
import mulan.core.MulanException;
import mulan.evaluation.Evaluation;
import mulan.evaluation.MultipleEvaluation;
import mulan.evaluation.measure.MacroAverageMeasure;
import mulan.evaluation.measure.Measure;

// TODO: Auto-generated Javadoc
/**
 * Class used to generate reports with the format specified.
 * 
 * @author Álvaro A. Belmonte
 * @author Eva Gibaja
 * @author Amelia Zafra
 * @version 20180608
 */
public class BaseMIMLReport extends MIMLReport {
	
	/**
	 * Instantiates a new report with a holdout evaluator.
	 *
	 * @param evaluation 
	 * 				holdout evaluator
	 * @param data 
	 * 			the data used in the evaluation
	 */
	public BaseMIMLReport(Evaluation evaluation, MIMLInstances data) {
		super(evaluation, data);
	}
	
	/**
	 * Instantiates a new report with cross-validation evaluator.
	 *
	 * @param evaluation 
	 * 				cross-validation evaluator
	 * @param data 
	 * 			the data used in the evaluation
	 */
	public BaseMIMLReport(MultipleEvaluation evaluation, MIMLInstances data) {
		super(evaluation, data);
	}
	
	/**
	 *  No-argument constructor for xml configuration.
	 */
	public BaseMIMLReport() {
	}
	
	/**
	 * Read the cross-validation results and transform to CSV format.
	 *
	 * @return the string with CSV content
	 * @throws MulanException the mulan exception
	 */
	private String crossValidationToCSV() throws MulanException {
		
		ArrayList<Evaluation> evaluations = evaluationCrossValidation.getEvaluations();
        StringBuilder sb = new StringBuilder();
        String measureName;
        
        for (Measure m : evaluations.get(0).getMeasures()) {
        	measureName = m.getName();
            if (!(m instanceof MacroAverageMeasure)) {
            	sb.append(measureName + ",,");
            }
        }
        
        sb.append("\n");
        
        for (Measure m : evaluations.get(0).getMeasures()) {
            measureName = m.getName();
            if (!(m instanceof MacroAverageMeasure)) {
            	sb.append("mean,std,");
            }
        }
        
        sb.append("\n");

        
        for (Measure m : evaluations.get(0).getMeasures()) {
            measureName = m.getName();
            if (!(m instanceof MacroAverageMeasure)) {
                sb.append(evaluationCrossValidation.getMean(measureName) + ",");
                sb.append(evaluationCrossValidation.getStd(measureName) + ",");
            }
        }
        
        sb.append("\n\n");
        sb.append("Labels,");
        for (int i = 0; i < data.getNumLabels(); i++) {
            sb.append(data.getDataSet().attribute(data.getLabelIndices()[i]).name());
            sb.append(",,");
        }
        
        sb.append("\n");
        sb.append(",");
        for (int i = 0; i < data.getNumLabels(); i++) {
            sb.append("mean,std,");
        }
        sb.append("\n");
        
        for (Measure m : evaluations.get(0).getMeasures()) {
        	measureName = m.getName();
            if (m instanceof MacroAverageMeasure) {
            	sb.append(measureName + ",");
                for (int i = 0; i < data.getNumLabels(); i++) {
                    sb.append(evaluationCrossValidation.getMean(measureName,i) + ",");
                    sb.append(evaluationCrossValidation.getStd(measureName,i) + ",");
                }
                sb.append("\n");
            }
        }
        
        return sb.toString();		
	}
	
	/**
	 * Read the holdout results and transform to CSV format.
	 *
	 * @return the string with CSV content
	 */
	private String holdoutToCSV() {
		
        StringBuilder sb = new StringBuilder();
        String measureName;
        
        List<Measure> measures = evaluationHoldout.getMeasures();
        
        for (Measure m : measures) {
        	measureName = m.getName();
            if (!(m instanceof MacroAverageMeasure)) {
            	sb.append(measureName + ",");
            }
        }
        
        sb.append("\n");
        
        for (Measure m : measures) {
            measureName = m.getName();
            if (!(m instanceof MacroAverageMeasure)) {
                sb.append(m.getValue() + ",");
            }
        }
        
        sb.append("\n\n");
        sb.append("Labels,");
        for (int i = 0; i < data.getNumLabels(); i++) {
            sb.append(data.getDataSet().attribute(data.getLabelIndices()[i]).name());
            sb.append(",");
        }

        sb.append("\n");
        
        for (Measure m : measures) {
        	measureName = m.getName();
            if (m instanceof MacroAverageMeasure) {
            	sb.append(measureName + ",");
                for (int i = 0; i < data.getNumLabels(); i++) {
                    sb.append(((MacroAverageMeasure) m).getValue(i) + ",");
                }
                sb.append("\n");
            }
        }
        
        return sb.toString();
	}
	
	/**
	 * Convert to CSV the evaluator results.
	 *
	 * @return the string with CSV content
	 * @throws MulanException the mulan exception
	 */
	public String toCSV() throws MulanException {
		
		if (evaluationCrossValidation != null) {
			return crossValidationToCSV();
		}
		else {
			return holdoutToCSV();
		}
	}
	
	/**
	 * Convert to plain text the evaluator results.
	 *
	 * @return the string with the content
	 */
	public String toString() {
		
		if (evaluationCrossValidation != null) {
			return evaluationCrossValidation.toString();
		}
		else {
			return evaluationHoldout.toString();
		}

	}


}
