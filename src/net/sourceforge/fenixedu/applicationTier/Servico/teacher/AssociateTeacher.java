package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.exceptions.ExistingPersistentException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author Fernanda Quit�rio
 */
public class AssociateTeacher extends Service {

    /**
     * Executes the service.
     * @throws ExcepcaoPersistencia 
     */
    public boolean run(Integer infoExecutionCourseCode, Integer teacherNumber)
            throws FenixServiceException, ExcepcaoPersistencia {
        try {
            Teacher iTeacher = Teacher.readByNumber(teacherNumber);
            if (iTeacher == null) {
                throw new InvalidArgumentsServiceException();
            }

            ExecutionCourse iExecutionCourse = (ExecutionCourse) persistentObject.readByOID(
                    ExecutionCourse.class, infoExecutionCourseCode);

            if (lectures(iTeacher, iExecutionCourse.getProfessorships())) {
                throw new ExistingServiceException();
            }

            Professorship.create(false, iExecutionCourse, iTeacher, null);
            
        } catch (ExistingPersistentException ex) {
            throw new ExistingServiceException(ex);
        }
        return true;
    }

    protected boolean lectures(final Teacher teacher, final List professorships) {
        return CollectionUtils.find(professorships, new Predicate() {

            public boolean evaluate(Object arg0) {
                Professorship professorship = (Professorship) arg0;
                return professorship.getTeacher().getIdInternal().equals(teacher.getIdInternal());
            }}) != null;
    }
}