/*
 * Created on Nov 23, 2003 by jpvl
 *  
 */
package ServidorPersistente.OJB.teacher.professorship;

import java.util.Date;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IExecutionPeriod;
import Dominio.IProfessorship;
import Dominio.ISupportLesson;
import Dominio.ITeacher;
import Dominio.SupportLesson;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.PersistentObjectOJB;
import ServidorPersistente.teacher.professorship.IPersistentSupportLesson;
import Util.DiaSemana;

/**
 * @author jpvl
 */
public class SupportLessonOJB extends PersistentObjectOJB implements IPersistentSupportLesson {

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.teacher.professorship.IPersistentSupportLesson#readByProfessorship(Dominio.IProfessorship)
     */
    public List readByProfessorship(IProfessorship professorship) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyProfessorship", professorship.getIdInternal());
        return queryList(SupportLesson.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.teacher.professorship.IPersistentSupportLesson#readByUnique(Dominio.ISupportLesson)
     */
    public ISupportLesson readByUnique(ISupportLesson supportLesson) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyProfessorship", supportLesson.getProfessorship().getIdInternal());
        criteria.addEqualTo("weekDay", supportLesson.getWeekDay());
        criteria.addEqualTo("startTime", supportLesson.getStartTime());
        criteria.addEqualTo("endTime", supportLesson.getEndTime());
        return (ISupportLesson) queryObject(SupportLesson.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.teacher.professorship.IPersistentSupportLesson#readOverlappingPeriod(Dominio.ITeacher,
     *      Dominio.IExecutionPeriod, Util.DiaSemana, java.util.Date,
     *      java.util.Date)
     */
    public List readOverlappingPeriod(ITeacher teacher, IExecutionPeriod executionPeriod,
            DiaSemana weekDay, Date startTime, Date endTime) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("professorship.executionCourse.keyExecutionPeriod", executionPeriod
                .getIdInternal());
        criteria.addEqualTo("professorship.keyTeacher", teacher.getIdInternal());
        criteria.addEqualTo("weekDay", weekDay);

        Criteria startCriteria = new Criteria();
        startCriteria.addGreaterThan("startTime", startTime);
        startCriteria.addLessThan("startTime", endTime);

        Criteria endCriteria = new Criteria();
        endCriteria.addGreaterThan("endTime", startTime);
        endCriteria.addLessThan("startTime", endTime);

        Criteria equalCriteria = new Criteria();
        equalCriteria.addEqualTo("startTime", startTime);
        equalCriteria.addEqualTo("endTime", endTime);

        Criteria timeCriteria = new Criteria();
        timeCriteria.addOrCriteria(startCriteria);
        timeCriteria.addOrCriteria(endCriteria);
        timeCriteria.addOrCriteria(equalCriteria);

        criteria.addAndCriteria(timeCriteria);

        return queryList(SupportLesson.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.teacher.professorship.IPersistentSupportLesson#readByTeacherAndExecutionPeriod(Dominio.ITeacher,
     *      Dominio.IExecutionPeriod)
     */
    public List readByTeacherAndExecutionPeriod(ITeacher teacher, IExecutionPeriod executionPeriod)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("professorship.keyTeacher", teacher.getIdInternal());
        criteria.addEqualTo("professorship.executionCourse.keyExecutionPeriod", executionPeriod
                .getIdInternal());
        return queryList(SupportLesson.class, criteria);
    }

    public void delete(ISupportLesson supportLesson) throws ExcepcaoPersistencia {
        super.delete(supportLesson);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.teacher.professorship.IPersistentSupportLesson#readByExecutionPeriod(Dominio.IExecutionPeriod)
     */
    public List readByExecutionPeriod(IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("professorship.executionCourse.executionPeriod.idInternal", executionPeriod
                .getIdInternal());
        return queryList(SupportLesson.class, criteria);
    }
}