/*
 * Created on 18/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionDegree;
import Dominio.Campus;
import Dominio.CursoExecucao;
import Dominio.ExecutionYear;
import Dominio.ICampus;
import Dominio.ICursoExecucao;
import Dominio.IExecutionYear;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionDegree;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;
import ServidorPersistente.places.campus.IPersistentCampus;

/**
 * @author lmac1
 */
public class EditExecutionDegree implements IService {

    public void run(InfoExecutionDegree infoExecutionDegree) throws FenixServiceException {

        IPersistentExecutionDegree persistentExecutionDegree = null;
        IExecutionYear executionYear = null;

        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            persistentExecutionDegree = persistentSuport.getIPersistentExecutionDegree();

            ICursoExecucao oldExecutionDegree = (ICursoExecucao) persistentExecutionDegree.readByOID(
                    CursoExecucao.class, infoExecutionDegree.getIdInternal());

            IPersistentCampus campusDAO = persistentSuport.getIPersistentCampus();

            ICampus campus = (ICampus) campusDAO.readByOID(Campus.class, infoExecutionDegree
                    .getInfoCampus().getIdInternal());
            if (campus == null) {
                throw new NonExistingServiceException("message.nonExistingCampus", null);
            }

            if (oldExecutionDegree == null) {
                throw new NonExistingServiceException("message.nonExistingExecutionDegree", null);
            }
            IPersistentExecutionYear persistentExecutionYear = persistentSuport
                    .getIPersistentExecutionYear();

            executionYear = (IExecutionYear) persistentExecutionYear.readByOID(ExecutionYear.class,
                    infoExecutionDegree.getInfoExecutionYear().getIdInternal());

            if (executionYear == null) {
                throw new NonExistingServiceException("message.non.existing.execution.year", null);
            }
            persistentExecutionDegree.simpleLockWrite(oldExecutionDegree);
            oldExecutionDegree.setExecutionYear(executionYear);

            oldExecutionDegree.setTemporaryExamMap(infoExecutionDegree.getTemporaryExamMap());

            oldExecutionDegree.setCampus(campus);

        } catch (ExistingPersistentException ex) {
            throw new ExistingServiceException(ex);
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }
    }
}