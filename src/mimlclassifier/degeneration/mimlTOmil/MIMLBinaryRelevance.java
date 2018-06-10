/*    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package mimlclassifier.degeneration.mimlTOmil;

import org.apache.commons.configuration.Configuration;

import data.Bag;
import data.MIMLInstances;
import mimlclassifier.MIMLClassifier;
import mulan.classifier.InvalidDataException;
import mulan.classifier.MultiLabelOutput;
import mulan.classifier.transformation.BinaryRelevance;
import mulan.data.MultiLabelInstances;
import weka.classifiers.Classifier;

/**
 * 
 * Class implementing the BR algorithm for MIML data. A classifier is built for
 * each label and predictions are also gathered for each label.
 * 
 * @author Ana I. Reyes Melero
 * @author Eva Gibaja
 * @author Amelia Zafra
 * @version 20170507
 *
 */
public class MIMLBinaryRelevance extends MIMLClassifier {

	/** For serialization */
	private static final long serialVersionUID = 1L;

	/** A BinaryRelevance classifier */
	private BinaryRelevance BR;

	/**
	 * Constructor.
	 * 
	 * @param baseClassifier
	 *            Classifier
	 * @throws Exception
	 *             To be handled in an upper level.
	 */
	public MIMLBinaryRelevance(Classifier baseClassifier) throws Exception {
		super();
		BR = new BinaryRelevance(baseClassifier);
	}

	@Override
	public void buildInternal(MIMLInstances dataSet) throws Exception {
		MultiLabelInstances mlData = new MultiLabelInstances(dataSet.getDataSet(), dataSet.getLabelsMetaData());
		BR.setDebug(getDebug());
		BR.build(mlData);
	}

	@Override
	protected MultiLabelOutput makePredictionInternal(Bag bag) throws Exception, InvalidDataException {
		return BR.makePrediction(bag);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void configure(Configuration configuration) {
		
		try {
			//Get the name of the base classifier class
			String baseName = configuration.getString("multiInstanceClassifier[@name]");
			//Instance class
			Class<? extends Classifier> baseClassifier = 
					(Class <? extends Classifier>) Class.forName(baseName);
			
			Configuration subConfiguration = configuration.subset("multiInstanceClassifier"); //getProperty("multiLable")
			//Parameters length
			int parameterLength = subConfiguration.getList("parameters.classParameters").size();
			
			//Obtaining las clasess
			Class [] cArg = new Class[parameterLength];
			Object [] obj = new Object [parameterLength];
				
			for(int i=0; i<parameterLength; i++){
				if(configuration.getString("multiLabelClassifier.parameters.classParameters("+i+")").equals("int.class")){
					cArg[i] = int.class;
					obj[i] =  configuration.getInt("multiLabelClassifier.parameters.valueParameters("+i+")");
					
				}
				else if(configuration.getString("multiLabelClassifier.parameters.classParameters("+i+")").equals("double.class")){
					cArg[i] = double.class;
					obj[i] =  configuration.getDouble("multiLabelClassifier.parameters.valueParameters("+i+")");
					
				}
				else if(configuration.getString("multiLabelClassifier.parameters.classParameters("+i+")").equals("char.class")){
					cArg[i] = char.class;
					obj[i] =   configuration.getInt("multiLabelClassifier.parameters.valueParameters("+i+")");
					
				}
				else if(configuration.getString("multiLabelClassifier.parameters.classParameters("+i+")").equals("byte.class")){
					cArg[i] = byte.class;
					obj[i] =   configuration.getByte("multiLabelClassifier.parameters.valueParameters("+i+")");
					
				}
				//Añadir el resto:long,short,boolean, ....,
				else{
					cArg[i] = Class.forName(configuration.getString("multiLabelClassifier.parameters.classParameters("+i+")")); 
					obj[i] =   configuration.getString("multiLabelClassifier.parameters.valueParameters("+i+")");
				}
					//En este caso el objeto debe ser del tipo asignado, debería recogerse como cadena, si fuere por un ejemplo un clasificador base que utilizase a su vez
			}
			
			
			
	        // valueParameters.
			// Assign 
			BR = new BinaryRelevance(baseClassifier.getConstructor(cArg).newInstance(obj));
		}
		catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}			
	}
}
