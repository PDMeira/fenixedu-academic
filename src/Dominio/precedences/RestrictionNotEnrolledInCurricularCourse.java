package Dominio.precedences;

import Dominio.ICurricularCourse;
import Util.enrollment.CurricularCourseEnrollmentType;

/**
 * @author David Santos in Jun 9, 2004
 */

public class RestrictionNotEnrolledInCurricularCourse extends RestrictionNotDoneCurricularCourse
        implements IRestrictionByCurricularCourse {
    public RestrictionNotEnrolledInCurricularCourse() {
        super();
    }

    public CurricularCourseEnrollmentType evaluate(PrecedenceContext precedenceContext) {
        ICurricularCourse curricularCourse = this.getPrecedentCurricularCourse();
        CurricularCourseEnrollmentType result1 = null;
        CurricularCourseEnrollmentType result2 = null;

        if (!precedenceContext.getStudentCurricularPlan().isCurricularCourseEnrolled(curricularCourse)) {
            result1 = CurricularCourseEnrollmentType.DEFINITIVE;
        } else {
            result1 = CurricularCourseEnrollmentType.NOT_ALLOWED;
        }

        result2 = super.evaluate(precedenceContext);

        return result1.or(result2);
    }
}