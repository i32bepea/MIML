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

package transformation.mimlTOmil;

import data.Bag;
import data.MIMLInstances;
import mulan.transformations.LabelPowersetTransformation;
import weka.core.Instance;
import weka.core.Instances;

/**
 * 
 * Class that uses LabelPowerset transformation to convert MIMLInstances to MIL
 * Instances with relational attribute.
 * 
 * @author Ana I. Reyes Melero
 * @author Eva Gibaja
 * @author Amelia Zafra
 * @version 20170507
 *
 */
public class LPTransformation {

	/** LabelPowerSetTransformation */
	protected LabelPowersetTransformation LPT;

	/**
	 * Constructor
	 */
	public LPTransformation() {
		this.LPT = new LabelPowersetTransformation();
	}

	/**
	 * Returns the format of the transformed instances
	 * 
	 * @return the format of the transformed instances
	 */
	public LabelPowersetTransformation getLPT() {
		return LPT;
	}

	/**
	 * 
	 * @param bag
	 *            The bag to be transformed.
	 * @param labelIndices
	 *            The labels to remove.
	 * @return Instance
	 * @throws Exception
	 *             To be handled in an upper level.
	 */
	public Instance transformBag(Bag bag, int[] labelIndices) throws Exception {
		return LPT.transformInstance(bag, labelIndices);
	}

	/**
	 * 
	 * @param dataSet
	 *            MIMLInstances dataSet.
	 * @return Instances
	 * @throws Exception
	 *             To be handled in an upper level.
	 */
	public Instances transformBags(MIMLInstances dataSet) throws Exception {
		return LPT.transformInstances(dataSet);
	}

}
