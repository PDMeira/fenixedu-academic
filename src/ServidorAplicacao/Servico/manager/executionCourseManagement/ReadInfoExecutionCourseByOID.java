package ServidorAplicacao.Servico.manager.executionCourseManagement;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.ICurricularCourse;
import Dominio.IExecutionCourse;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionDegree;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/*
 * 
 * @author Fernanda Quit�rio 22/Dez/2003
 *  
 */
public class ReadInfoExecutionCourseByOID implements IService {

    public InfoExecutionCourse run(Integer executionCourseOID) throws FenixServiceException {

        InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionDegree persistentExecutionCourse = sp.getIPersistentExecutionDegree();
            if (executionCourseOID == null) {
                throw new FenixServiceException("nullId");
            }

            IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                    ExecutionCourse.class, executionCourseOID);

            List curricularCourses = executionCourse.getAssociatedCurricularCourses();

            List infoCurricularCourses = new ArrayList();

            CollectionUtils.collect(curricularCourses, new Transformer() {
                public Object transform(Object input) {
                    ICurricularCourse curricularCourse = (ICurricularCourse) input;

                    return Cloner.copyCurricularCourse2InfoCurricularCourse(curricularCourse);
                }
            }, infoCurricularCourses);

            infoExecutionCourse = (InfoExecutionCourse) Cloner.get(executionCourse);
            infoExecutionCourse.setAssociatedInfoCurricularCourses(infoCurricularCourses);
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }
        return infoExecutionCourse;
    }
}