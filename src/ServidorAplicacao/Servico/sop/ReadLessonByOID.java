/*
 * Created on 2003/07/30
 *
 *
 */
package ServidorAplicacao.Servico.sop;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoLesson;
import DataBeans.util.Cloner;
import Dominio.Aula;
import Dominio.IAula;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IAulaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 *  
 */
public class ReadLessonByOID implements IService {

    public InfoLesson run(Integer oid) throws FenixServiceException {

        InfoLesson result = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IAulaPersistente lessonDAO = sp.getIAulaPersistente();
            IAula lesson = (IAula) lessonDAO.readByOID(Aula.class, oid);
            if (lesson != null) {
                InfoLesson infoLesson = Cloner.copyILesson2InfoLesson(lesson);
                //				ITurno shift = lesson.getShift();
                //				InfoShift infoShift = Cloner.copyShift2InfoShift(shift);
                //				infoLesson.setInfoShift(infoShift);

                result = infoLesson;
            }
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }

        return result;
    }
}