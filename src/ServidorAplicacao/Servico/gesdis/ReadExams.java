/*
 * Created on 13/Mai/2003
 *
 * 
 */
package ServidorAplicacao.Servico.gesdis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.IExam;
import Dominio.IExecutionCourse;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Jo�o Mota
 * @author Fernanda Quit�rio
 *  
 */
public class ReadExams implements IService {

    /**
     * Executes the service. Returns the current collection of exams
     * 
     *  
     */

    public List run(Integer executionCourseCode) throws FenixServiceException {
        try {
            ISuportePersistente sp;
            IExecutionCourse executionCourse;

            sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();

            executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                    ExecutionCourse.class, executionCourseCode);
            if (executionCourse == null) {
                throw new NonExistingServiceException();
            }
            List infoExams = new ArrayList();
            List exams = null;

            if (executionCourse != null) {
                exams = executionCourse.getAssociatedExams();

                Iterator iter = exams.iterator();
                while (iter.hasNext()) {
                    infoExams.add(Cloner.copyIExam2InfoExam((IExam) iter.next()));
                }

            }
            return infoExams;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

    }

}