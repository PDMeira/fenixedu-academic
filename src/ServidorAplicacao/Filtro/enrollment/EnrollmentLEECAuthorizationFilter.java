/*
 * Created on 6/Fev/2004
 *  
 */
package ServidorAplicacao.Filtro.enrollment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import DataBeans.InfoRole;
import Dominio.ICoordinator;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.ITeacher;
import Dominio.ITutor;
import ServidorAplicacao.IUserView;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCoordinator;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;

/**
 * @author T�nia Pous�o
 *  
 */
public class EnrollmentLEECAuthorizationFilter extends EnrollmentAuthorizationFilter {
    private static String DEGREE_LEEC_CODE = new String("LEEC");

    protected Collection getNeededRoles() {
        List roles = new ArrayList();

        InfoRole infoRole = new InfoRole();
        infoRole.setRoleType(RoleType.COORDINATOR);
        roles.add(infoRole);

        infoRole = new InfoRole();
        infoRole.setRoleType(RoleType.TEACHER);
        roles.add(infoRole);

        infoRole = new InfoRole();
        infoRole.setRoleType(RoleType.STUDENT);
        roles.add(infoRole);

        infoRole = new InfoRole();
        infoRole.setRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE);
        roles.add(infoRole);

        infoRole = new InfoRole();
        infoRole.setRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER);
        roles.add(infoRole);
        return roles;
    }

    protected String hasPrevilege(IUserView id, Object[] arguments) {
        try {
            List roles = getRoleList((List) id.getRoles());

            ISuportePersistente sp = null;
            sp = SuportePersistenteOJB.getInstance();

            //verify if the student making the enrollment is a LEEC degree
            // student
            if (roles.contains(RoleType.STUDENT)) {
                IStudent student = readStudent(id, sp);
                if (student == null) {
                    return "noAuthorization";
                }

                if (!verifyStudentLEEC(arguments, sp)) {
                    return new String("error.student.degreeCurricularPlan.LEEC");
                }
                if (!curriculumOwner(student, id)) {
                    return "noAuthorization";

                }
                ITutor tutor = verifyStudentWithTutor(student, sp);
                if (tutor != null) {
                    return new String("error.enrollment.student.withTutor+"
                            + tutor.getTeacher().getTeacherNumber().toString() + "+"
                            + tutor.getTeacher().getPerson().getNome());
                }
            } else {
                //verify if the student to enroll is a LEEC degree student
                if (!verifyStudentLEEC(arguments, sp)) {
                    return new String("error.student.degreeCurricularPlan.LEEC");
                }

                //verify if the coodinator is of the LEEC degree
                if (roles.contains(RoleType.COORDINATOR) && arguments[0] != null) {
                    ITeacher teacher = readTeacher(id, sp);
                    if (teacher == null) {
                        return "noAuthorization";
                    }

                    if (!verifyCoordinatorLEEC(teacher, arguments, sp)) {
                        return "noAuthorization";
                    }
                } else if (roles.contains(RoleType.TEACHER)) {
                    ITeacher teacher = readTeacher(id, sp);
                    if (teacher == null) {
                        return "noAuthorization";
                    }

                    IStudent student = readStudent(arguments, sp);
                    if (student == null) {
                        return "noAuthorization";
                    }

                    if (!verifyStudentTutor(teacher, student, sp)) {
                        return new String("error.enrollment.notStudentTutor+"
                                + student.getNumber().toString());
                    }

                } else if (roles.contains(RoleType.DEGREE_ADMINISTRATIVE_OFFICE)
                        || roles.contains(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER)) {
                    IStudentCurricularPlan studentCurricularPlan = readStudentCurricularPlan(arguments,
                            sp);

                    if (studentCurricularPlan.getStudent() == null) {
                        return "noAuthorization";
                    }
                    if (insideEnrollmentPeriod(studentCurricularPlan, sp)) {
                        ITutor tutor = verifyStudentWithTutor(studentCurricularPlan.getStudent(), sp);
                        if (tutor != null) {
                            return new String("error.enrollment.student.withTutor+"
                                    + tutor.getTeacher().getTeacherNumber().toString() + "+"
                                    + tutor.getTeacher().getPerson().getNome());
                        }
                    }
                    return null;
                } else {
                    return "noAuthorization";
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return "noAuthorization";
        }
        return null;
    }

    private boolean verifyStudentLEEC(Object[] arguments, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        IStudentCurricularPlan studentCurricularPlan = readStudentCurricularPlan(arguments, sp);
        if (studentCurricularPlan == null) {
            return false;
        }

        String degreeCode = null;
        if (studentCurricularPlan.getDegreeCurricularPlan() != null
                && studentCurricularPlan.getDegreeCurricularPlan().getDegree() != null) {
            degreeCode = studentCurricularPlan.getDegreeCurricularPlan().getDegree().getSigla();
        }

        return DEGREE_LEEC_CODE.equals(degreeCode);
    }

    private boolean verifyCoordinatorLEEC(ITeacher teacher, Object[] arguments, ISuportePersistente sp)
            throws ExcepcaoPersistencia {

        IPersistentCoordinator persistentCoordinator = sp.getIPersistentCoordinator();
        ICoordinator coordinator = persistentCoordinator.readCoordinatorByTeacherAndExecutionDegreeId(
                teacher, (Integer) arguments[0]);
        if (coordinator == null) {
            return false;
        }

        String degreeCode = null;
        if (coordinator.getExecutionDegree() != null
                && coordinator.getExecutionDegree().getCurricularPlan() != null
                && coordinator.getExecutionDegree().getCurricularPlan().getDegree() != null) {
            degreeCode = coordinator.getExecutionDegree().getCurricularPlan().getDegree().getSigla();
        }

        return DEGREE_LEEC_CODE.equals(degreeCode);
    }

}