/*
 * Created on 8/Jul/2004
 *
 */
package DataBeans;

import Dominio.IGratuityValues;

/**
 * @author T�nia Pous�o
 *  
 */
public class InfoGratuityValuesWithInfoExecutionDegree extends InfoGratuityValues {

    /*
     * (non-Javadoc)
     * 
     * @see DataBeans.InfoGratuityValues#copyFromDomain(Dominio.IGratuityValues)
     */
    public void copyFromDomain(IGratuityValues gratuityValues) {
        super.copyFromDomain(gratuityValues);
        if (gratuityValues != null) {
            setInfoExecutionDegree(InfoExecutionDegree.newInfoFromDomain(gratuityValues
                    .getExecutionDegree()));
        }
    }

    public static InfoGratuityValues newInfoFromDomain(IGratuityValues gratuityValues) {
        InfoGratuityValuesWithInfoExecutionDegree infoGratuityValues = null;
        if (gratuityValues != null) {
            infoGratuityValues = new InfoGratuityValuesWithInfoExecutionDegree();
            infoGratuityValues.copyFromDomain(gratuityValues);
        }
        return infoGratuityValues;
    }
}