/*
 * MasterDegreeCandidateOJB.java
 * 
 * Created on 17 de Outubro de 2002, 11:30
 */

/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package ServidorPersistente.OJB;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.ICursoExecucao;
import Dominio.IExecutionYear;
import Dominio.IMasterDegreeCandidate;
import Dominio.IPessoa;
import Dominio.MasterDegreeCandidate;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentMasterDegreeCandidate;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.SituationName;
import Util.Specialization;
import Util.State;

public class MasterDegreeCandidateOJB extends PersistentObjectOJB implements
        IPersistentMasterDegreeCandidate {

    /** Creates a new instance of MasterDegreeCandidateOJB */
    public MasterDegreeCandidateOJB() {
    }

    public List readMasterDegreeCandidatesByUsername(String username) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("person.username", username);
        return queryList(MasterDegreeCandidate.class, crit);

    }

    public IMasterDegreeCandidate readCandidateByNumberAndApplicationYearAndDegreeCodeAndSpecialization(
            Integer candidateNumber, String applicationYear, String degreeCode,
            Specialization specialization) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("candidateNumber", candidateNumber);
        crit.addEqualTo("executionDegree.executionYear.year", applicationYear);
        crit.addEqualTo("executionDegree.curricularPlan.degree.sigla", degreeCode);
        crit.addEqualTo("specialization", specialization.getSpecialization());
        return (IMasterDegreeCandidate) queryObject(MasterDegreeCandidate.class, crit);

    }

    public void writeMasterDegreeCandidate(IMasterDegreeCandidate masterDegreeCandidateToWrite)
            throws ExcepcaoPersistencia {
        if (masterDegreeCandidateToWrite == null)
            return;

        // Write the Person first to see if there's no clash

        try {
            SuportePersistenteOJB.getInstance().getIPessoaPersistente().escreverPessoa(
                    masterDegreeCandidateToWrite.getPerson());
        } catch (ExistingPersistentException e) {
            throw new ExistingPersistentException("Existing Person !");
        } catch (ExcepcaoPersistencia e) {
            throw e;
        } catch (IllegalAccessException e) {
            throw new ExcepcaoPersistencia();
        } catch (InvocationTargetException e) {
            throw new ExcepcaoPersistencia();
        }

        IMasterDegreeCandidate masterDegreeCandidateBD1 = this
                .readCandidateByNumberAndApplicationYearAndDegreeCodeAndSpecialization(
                        masterDegreeCandidateToWrite.getCandidateNumber(), masterDegreeCandidateToWrite
                                .getExecutionDegree().getExecutionYear().getYear(),
                        masterDegreeCandidateToWrite.getExecutionDegree().getCurricularPlan()
                                .getDegree().getSigla(), masterDegreeCandidateToWrite
                                .getSpecialization());

        IMasterDegreeCandidate masterDegreeCandidateBD2 = this
                .readByUsernameAndExecutionDegreeAndSpecialization(masterDegreeCandidateToWrite
                        .getPerson().getUsername(), masterDegreeCandidateToWrite.getExecutionDegree(),
                        masterDegreeCandidateToWrite.getSpecialization());

        if (masterDegreeCandidateBD1 == null && masterDegreeCandidateBD2 == null) {
            super.lockWrite(masterDegreeCandidateToWrite);
            return;
        }

        if (masterDegreeCandidateBD1 != null
                && (masterDegreeCandidateToWrite instanceof MasterDegreeCandidate)
                && ((MasterDegreeCandidate) masterDegreeCandidateBD1).getIdInternal().equals(
                        ((MasterDegreeCandidate) masterDegreeCandidateToWrite).getIdInternal())) {
            super.lockWrite(masterDegreeCandidateToWrite);
            return;
        }
        if (masterDegreeCandidateBD2 != null
                && (masterDegreeCandidateToWrite instanceof MasterDegreeCandidate)
                && ((MasterDegreeCandidate) masterDegreeCandidateBD2).getIdInternal().equals(
                        ((MasterDegreeCandidate) masterDegreeCandidateToWrite).getIdInternal())) {
            super.lockWrite(masterDegreeCandidateToWrite);
            return;
        }

        throw new ExistingPersistentException();
    }

    public Integer generateCandidateNumber(String executionYear, String degreeCode,
            Specialization specialization) throws ExcepcaoPersistencia {
        int number = 0;

        Criteria crit = new Criteria();
        crit.addEqualTo("executionDegree.executionYear.year", executionYear);
        crit.addEqualTo("executionDegree.curricularPlan.degree.sigla", degreeCode);
        crit.addEqualTo("specialization", specialization.getSpecialization());

        List list = queryList(MasterDegreeCandidate.class, crit, "candidateNumber", false);
        if (list != null && list.size() > 0) {
            Object result = queryList(MasterDegreeCandidate.class, crit, "candidateNumber", false)
                    .get(0);
            if (result != null)
                number = ((IMasterDegreeCandidate) result).getCandidateNumber().intValue();
        }

        return new Integer(number + 1);

    }

    public void delete(IMasterDegreeCandidate masterDegreeCandidate) throws ExcepcaoPersistencia {
        super.delete(masterDegreeCandidate);
    }

    public IMasterDegreeCandidate readByIdentificationDocNumberAndTypeAndExecutionDegreeAndSpecialization(
            String idDocumentNumber, Integer idDocumentType, ICursoExecucao executionDegree,
            Specialization specialization) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("specialization", specialization);
        crit.addEqualTo("executionDegree.executionYear.year", executionDegree.getExecutionYear()
                .getYear());
        crit.addEqualTo("executionDegree.curricularPlan.name", executionDegree.getCurricularPlan()
                .getName());
        crit.addEqualTo("executionDegree.curricularPlan.degree.nome", executionDegree
                .getCurricularPlan().getDegree().getNome());
        crit.addEqualTo("person.numeroDocumentoIdentificacao", idDocumentNumber);
        crit.addEqualTo("person.tipoDocumentoIdentificacao", idDocumentType);
        return (IMasterDegreeCandidate) queryObject(MasterDegreeCandidate.class, crit);

    }

    public IMasterDegreeCandidate readByUsernameAndExecutionDegreeAndSpecialization(String username,
            ICursoExecucao executionDegree, Specialization specialization) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("specialization", specialization);
        crit.addEqualTo("executionDegree.executionYear.year", executionDegree.getExecutionYear()
                .getYear());
        crit.addEqualTo("executionDegree.curricularPlan.name", executionDegree.getCurricularPlan()
                .getName());
        crit.addEqualTo("executionDegree.curricularPlan.degree.nome", executionDegree
                .getCurricularPlan().getDegree().getNome());
        crit.addEqualTo("person.username", username);
        return (IMasterDegreeCandidate) queryObject(MasterDegreeCandidate.class, crit);

    }

    /**
     * Reads all candidates that with certains properties. The properties are
     * specified by the arguments of this method. If an argument is null, then
     * the candidate can have any value concerning that argument.
     * 
     * @return a list with all candidates that satisfy the conditions specified
     *         by the non-null arguments.
     */
    public List readCandidateList(String degreeName, Specialization specialization,
            SituationName candidateSituation, Integer candidateNumber, IExecutionYear executionYear)
            throws ExcepcaoPersistencia {

        if (degreeName == null && specialization == null && candidateSituation == null
                && candidateNumber == null)
            return readByExecutionYear(executionYear);

        Criteria criteria = new Criteria();

        if (degreeName != null) {

            criteria.addEqualTo("executionDegree.idInternal", degreeName);
        }

        if (specialization != null) {
            criteria.addEqualTo("specialization", specialization);
        }

        if (candidateNumber != null) {
            criteria.addEqualTo("candidateNumber", candidateNumber);
        }

        if (candidateSituation != null) {
            criteria.addEqualTo("situations.situation", candidateSituation.getSituationName());
            criteria.addEqualTo("situations.validation", new Integer(State.ACTIVE));
        }
        return queryList(MasterDegreeCandidate.class, criteria);
    }

    public List readByExecutionYear(IExecutionYear executionYear) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("executionDegree.academicYear", executionYear.getIdInternal());
        return queryList(MasterDegreeCandidate.class, criteria);
    }

    public IMasterDegreeCandidate readByNumberAndExecutionDegreeAndSpecialization(Integer number,
            ICursoExecucao executionDegree, Specialization specialization) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("specialization", specialization);
        crit.addEqualTo("executionDegree.executionYear.year", executionDegree.getExecutionYear()
                .getYear());
        crit.addEqualTo("executionDegree.curricularPlan.name", executionDegree.getCurricularPlan()
                .getName());
        crit.addEqualTo("executionDegree.curricularPlan.degree.nome", executionDegree
                .getCurricularPlan().getDegree().getNome());
        crit.addEqualTo("candidateNumber", number);
        return (IMasterDegreeCandidate) queryObject(MasterDegreeCandidate.class, crit);

    }

    public IMasterDegreeCandidate readByExecutionDegreeAndPerson(ICursoExecucao executionDegree,
            IPessoa person) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("person.username", person.getUsername());
        crit.addEqualTo("executionDegree.executionYear.year", executionDegree.getExecutionYear()
                .getYear());
        crit.addEqualTo("executionDegree.curricularPlan.name", executionDegree.getCurricularPlan()
                .getName());
        crit.addEqualTo("executionDegree.curricularPlan.degree.nome", executionDegree
                .getCurricularPlan().getDegree().getNome());

        return (IMasterDegreeCandidate) queryObject(MasterDegreeCandidate.class, crit);

    }

    public IMasterDegreeCandidate readByExecutionDegreeAndPersonAndNumber(
            ICursoExecucao executionDegree, IPessoa person, Integer number) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("person.username", person.getUsername());
        crit.addEqualTo("executionDegree.executionYear.year", executionDegree.getExecutionYear()
                .getYear());
        crit.addEqualTo("executionDegree.curricularPlan.name", executionDegree.getCurricularPlan()
                .getName());
        crit.addEqualTo("executionDegree.curricularPlan.degree.nome", executionDegree
                .getCurricularPlan().getDegree().getNome());
        crit.addEqualTo("candidateNumber", number);
        return (IMasterDegreeCandidate) queryObject(MasterDegreeCandidate.class, crit);

    }

    public List readByExecutionDegree(ICursoExecucao executionDegree) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        /*
         * crit.addEqualTo( "executionDegree.executionYear.year",
         * executionDegree.getExecutionYear().getYear());
         */
        crit.addEqualTo("executionDegree.curricularPlan.name", executionDegree.getCurricularPlan()
                .getName());
        crit.addEqualTo("executionDegree.curricularPlan.degree.nome", executionDegree
                .getCurricularPlan().getDegree().getNome());
        return queryList(MasterDegreeCandidate.class, crit);

    }

    public List readByPersonID(Integer personID) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("person.idInternal", personID);
        return queryList(MasterDegreeCandidate.class, criteria);
    }

} // End of class definition
