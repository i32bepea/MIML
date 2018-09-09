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

package mimlclassifier.mimlTOml;


import org.apache.commons.configuration.Configuration;

import data.Bag;
import data.MIMLInstances;
import mimlclassifier.MIMLClassifier;
import mulan.classifier.InvalidDataException;
import mulan.classifier.MultiLabelLearner;
import mulan.classifier.MultiLabelOutput;
import mulan.data.MultiLabelInstances;
import transformation.mimlTOml.MIMLtoML;

/**
 * 
 * Class implementing the degenerative algorithm for MIML data to solve it with ML learning. 
 * 
 * @author Alvaro A. Belmonte
 * @author Eva Gibaja
 * @author Amelia Zafra
 * @version 20180608
 *
 */
public class MIMLClassifierML extends MIMLClassifier{

	/**  For serialization. */
	private static final long serialVersionUID = 1L;

	/**  A Generic MultiLabel classifier. */
	MultiLabelLearner baseClassifier;
	
	/** The transform method. */
	MIMLtoML transformMethod;
	
	/** The miml dataset. */
	MIMLInstances mimlDataset;

	/**
	 * Constructor.
	 *
	 * @param baseClassifier            Classifier
	 * @param transformMethod the transform method
	 * @throws Exception             To be handled in an upper level.
	 */
	public MIMLClassifierML(MultiLabelLearner baseClassifier, MIMLtoML transformMethod) throws Exception {
		super();
		this.baseClassifier = baseClassifier;
		this.transformMethod = transformMethod;
	}

	/**
	 *  No-argument constructor for xml configuration.
	 */
	public MIMLClassifierML() {
	}
	
	
	/* (non-Javadoc)
	 * @see mimlclassifier.MIMLClassifier#buildInternal(data.MIMLInstances)
	 */
	@Override
	public void buildInternal(MIMLInstances mimlDataSet) throws Exception {
		
		// Transforms a dataset
		MultiLabelInstances mlDataSet = transformMethod.transformDataset(mimlDataSet);
		baseClassifier.build(mlDataSet);
	}

	/* (non-Javadoc)
	 * @see mimlclassifier.MIMLClassifier#makePredictionInternal(data.Bag)
	 */
	@Override
	protected MultiLabelOutput makePredictionInternal(Bag bag) throws Exception, InvalidDataException {
		return baseClassifier.makePrediction(bag);
	}

	/* (non-Javadoc)
	 * @see core.IConfiguration#configure(org.apache.commons.configuration.Configuration)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void configure(Configuration configuration) {
		

		
		try {
			//Get the string with the base classifier class
			String classifierName = configuration.getString("multiLabelClassifier[@name]");
			//Instance class
			Class<? extends MultiLabelLearner> classifierClass = 
					(Class <? extends MultiLabelLearner>) Class.forName(classifierName);
			Configuration subConfiguration = configuration.subset("multiLabelClassifier"); //getProperty("multiLable")
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
				//A�adir el resto:long,short,boolean, ....,
				else{
//					cArg[i] = Class.forName(configuration.getString("multiLabelClassifier.parameters.classParameters("+i+")")); 
//					obj[i] =  configuration.getString("multiLabelClassifier.parameters.valueParameters("+i+")");
					
					cArg[i] = Class.forName(configuration.getString("multiLabelClassifier.parameters.classParameters("+i+")"));
					obj[i] =  Class.forName(configuration.getString("multiLabelClassifier.parameters.valueParameters("+i+")")).newInstance(); 
				}
				
					

			}
			
			this.baseClassifier = (MultiLabelLearner) classifierClass.getConstructor(cArg).newInstance(obj);
			
			//Get the string with the base classifier class
			String transformerName = configuration.getString("transformMethod[@name]");
			//Instance class
			Class<? extends MIMLtoML> transformerClass = 
					(Class <? extends MIMLtoML>) Class.forName(transformerName);
			this.transformMethod = transformerClass.newInstance();
			
			
		}
		catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}	
		
	}

}
