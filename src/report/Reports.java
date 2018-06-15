package report;

import java.util.ArrayList;
import java.util.List;

import data.MIMLInstances;
import mulan.core.MulanException;
import mulan.evaluation.Evaluation;
import mulan.evaluation.MultipleEvaluation;
import mulan.evaluation.measure.MacroAverageMeasure;
import mulan.evaluation.measure.Measure;

public class Reports {
	
	private MultipleEvaluation evaluationCrossValidation = null;
	private Evaluation evaluationHoldout = null;
	
	private MIMLInstances data;
	
	public Reports(MultipleEvaluation evaluation, MIMLInstances data) {
		
		this.evaluationCrossValidation = evaluation;
		this.data = data;
	}
	
	public Reports(Evaluation evaluation, MIMLInstances data) {
		
		this.evaluationHoldout = evaluation;
		this.data = data;
	}
	
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
	
	public String toCSV() throws MulanException {
		
		if (evaluationCrossValidation != null) {
			return crossValidationToCSV();
		}
		else {
			return holdoutToCSV();
		}

	}
	
	public String toString() {
		
		if (evaluationCrossValidation != null) {
			return evaluationCrossValidation.toString();
		}
		else {
			return evaluationHoldout.toString();
		}

	}


}
