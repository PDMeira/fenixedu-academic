package ServidorPersistente.OJB.transactions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IExecutionYear;
import Dominio.IGuideEntry;
import Dominio.IStudent;
import Dominio.reimbursementGuide.IReimbursementGuideEntry;
import Dominio.reimbursementGuide.IReimbursementGuideSituation;
import Dominio.reimbursementGuide.ReimbursementGuideEntry;
import Dominio.reimbursementGuide.ReimbursementGuideSituation;
import Dominio.transactions.IInsuranceTransaction;
import Dominio.transactions.InsuranceTransaction;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.transactions.IPersistentInsuranceTransaction;
import Util.ReimbursementGuideState;
import Util.State;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 *  
 */
public class InsuranceTransactionOJB extends ObjectFenixOJB implements IPersistentInsuranceTransaction {

    public InsuranceTransactionOJB() {
    }

    /*
     * public IInsuranceTransaction readByExecutionYearAndStudent(
     * IExecutionYear executionYear, IStudent student) throws
     * ExcepcaoPersistencia {
     * 
     * Criteria crit = new Criteria();
     * crit.addEqualTo("executionYear.idInternal", executionYear
     * .getIdInternal()); crit.addEqualTo("student.idInternal",
     * student.getIdInternal());
     * 
     * return (IInsuranceTransaction) queryObject(InsuranceTransaction.class,
     * crit); }
     */

    public List readAllNonReimbursedByExecutionYearAndStudent(IExecutionYear executionYear,
            IStudent student) throws ExcepcaoPersistencia {

        List nonReimbursedInsuranceTransactions = new ArrayList();

        Criteria crit = new Criteria();
        crit.addEqualTo("executionYear.idInternal", executionYear.getIdInternal());
        crit.addEqualTo("student.idInternal", student.getIdInternal());

        List insuranceTransactions = queryList(InsuranceTransaction.class, crit);

        for (Iterator iter = insuranceTransactions.iterator(); iter.hasNext();) {
            IInsuranceTransaction insuranceTransaction = (IInsuranceTransaction) iter.next();

            IGuideEntry guideEntry = insuranceTransaction.getGuideEntry();
            if (guideEntry == null) {

                nonReimbursedInsuranceTransactions.add(insuranceTransaction);
            } else {
                if (guideEntry.getReimbursementGuideEntries().isEmpty()) {
                    nonReimbursedInsuranceTransactions.add(insuranceTransaction);
                } else {

                    //because of an OJB bug with caching, we have to read
                    // the reimbursement guide entry again
                    // the following code should be removed when in the next
                    // release of OJB
                    Criteria reimbursementGuideEntriesCriteria = new Criteria();
                    reimbursementGuideEntriesCriteria.addEqualTo("guideEntry.idInternal", guideEntry
                            .getIdInternal());
                    List reimbursementGuideEntries = queryList(ReimbursementGuideEntry.class,
                            reimbursementGuideEntriesCriteria);

                    Criteria activeSituationCriteria = new Criteria();

                    activeSituationCriteria.addEqualTo("reimbursementGuide.idInternal",
                            ((IReimbursementGuideEntry) reimbursementGuideEntries.get(0))
                                    .getReimbursementGuide().getIdInternal());
                    activeSituationCriteria.addEqualTo("state", new State(State.ACTIVE));

                    IReimbursementGuideSituation activeReimbursementGuideSituation = (IReimbursementGuideSituation) queryObject(
                            ReimbursementGuideSituation.class, activeSituationCriteria);

                    if (activeReimbursementGuideSituation.getReimbursementGuideState().equals(
                            ReimbursementGuideState.PAYED) == false) {

                        nonReimbursedInsuranceTransactions.add(insuranceTransaction);

                    }

                }
            }

        }

        return nonReimbursedInsuranceTransactions;
    }

    public List readAllByExecutionYearAndStudent(IExecutionYear executionYear, IStudent student)
            throws ExcepcaoPersistencia {

        Criteria crit = new Criteria();
        crit.addEqualTo("executionYear.idInternal", executionYear.getIdInternal());
        crit.addEqualTo("student.idInternal", student.getIdInternal());

        return queryList(InsuranceTransaction.class, crit);
    }

}