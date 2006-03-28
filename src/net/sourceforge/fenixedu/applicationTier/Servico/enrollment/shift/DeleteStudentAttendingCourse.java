package net.sourceforge.fenixedu.applicationTier.Servico.enrollment.shift;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;

/*
 * 
 * @author Fernanda Quit�rio 11/Fev/2004
 *  
 */
public class DeleteStudentAttendingCourse extends Service {

    public class AlreadyEnrolledInGroupServiceException extends FenixServiceException {
    }

    public class AlreadyEnrolledServiceException extends FenixServiceException {
    }

    public class AlreadyEnrolledInShiftServiceException extends FenixServiceException {
    }

    public Boolean run(InfoStudent infoStudent, Integer executionCourseID) throws FenixServiceException,
            ExcepcaoPersistencia {

        if (infoStudent == null) {
            return Boolean.FALSE;
        }
        final Student student = readStudent(persistentSupport, infoStudent);
        final ExecutionCourse executionCourse = readExecutionCourse(persistentSupport,
                executionCourseID);

        deleteAttend(persistentSupport, student, executionCourse);

        return Boolean.TRUE;
    }

    private ExecutionCourse readExecutionCourse(final ISuportePersistente persistentSupport,
            final Integer executionCourseID) throws ExcepcaoPersistencia, FenixServiceException {
        final ExecutionCourse executionCourse = (ExecutionCourse) persistentObject.readByOID(
                ExecutionCourse.class, executionCourseID);
        if (executionCourse == null) {
            throw new FenixServiceException("error.noExecutionCourse");
        }
        return executionCourse;
    }

    private Student readStudent(final ISuportePersistente persistentSupport,
            final InfoStudent infoStudent) throws FenixServiceException, ExcepcaoPersistencia {
        final Student student = (Student) persistentObject.readByOID(Student.class, infoStudent
                .getIdInternal());
        if (student == null) {
            throw new FenixServiceException("error.exception.noStudents");
        }
        return student;
    }

    private void deleteAttend(final ISuportePersistente persistentSupport, final Student student,
            final ExecutionCourse executionCourse) throws FenixServiceException, ExcepcaoPersistencia {
        
        final Attends attend = student.readAttendByExecutionCourse(executionCourse);

        if (attend != null) {
            checkIfHasStudentGroups(attend);
            checkIfIsEnrolled(attend);
            checkStudentShifts(student, executionCourse);
            attend.delete();
        }
    }

    private void checkStudentShifts(final Student student, final ExecutionCourse executionCourse)
            throws AlreadyEnrolledInShiftServiceException {
        for (final Shift shift : student.getShifts()) {
            if (shift.getDisciplinaExecucao() == executionCourse) {
                throw new AlreadyEnrolledInShiftServiceException();
            }
        }
    }

    private void checkIfIsEnrolled(final Attends attend) throws AlreadyEnrolledInGroupServiceException {
        if (attend.getEnrolment() != null) {
            throw new AlreadyEnrolledInGroupServiceException();
        }
    }

    private void checkIfHasStudentGroups(final Attends attend)
            throws AlreadyEnrolledInGroupServiceException {
        if (attend.getStudentGroupsCount() > 0) {
            throw new AlreadyEnrolledInGroupServiceException();
        }
    }
}