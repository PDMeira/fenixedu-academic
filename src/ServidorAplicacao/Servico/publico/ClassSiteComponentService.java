/*
 * Created on 6/Mai/2003
 * 
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorAplicacao.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.ISiteComponent;
import DataBeans.SiteView;
import Dominio.ICursoExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.ITurma;
import Dominio.Turma;
import ServidorAplicacao.Factory.PublicSiteComponentBuilder;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionDegree;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Jo�o Mota
 * 
 *  
 */
public class ClassSiteComponentService implements IService {

    public ClassSiteComponentService() {

    }

    public Object run(ISiteComponent bodyComponent, String executionYearName,
            String executionPeriodName, String degreeInitials, String nameDegreeCurricularPlan,
            String className, Integer curricularYear, Integer classId) throws FenixServiceException {

        SiteView siteView = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
            IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();

            IPersistentExecutionDegree executionDegreeDAO = sp.getIPersistentExecutionDegree();
            ITurmaPersistente persistentSchoolClass = sp.getITurmaPersistente();

            IExecutionYear executionYear = persistentExecutionYear
                    .readExecutionYearByName(executionYearName);

            IExecutionPeriod executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear(
                    executionPeriodName, executionYear);

            ICursoExecucao executionDegree = executionDegreeDAO
                    .readByDegreeInitialsAndNameDegreeCurricularPlanAndExecutionYear(degreeInitials,
                            nameDegreeCurricularPlan, executionYear);
            PublicSiteComponentBuilder componentBuilder = PublicSiteComponentBuilder.getInstance();
            ITurma domainClass;
            if (classId == null) {
                domainClass = getDomainClass(className, curricularYear, executionPeriod,
                        executionDegree, sp);
                if (domainClass == null) {
                    throw new NonExistingServiceException();
                }
            } else {

                domainClass = (ITurma) persistentSchoolClass.readByOID(Turma.class, classId);
            }
            bodyComponent = componentBuilder.getComponent(bodyComponent, domainClass);
            siteView = new SiteView(bodyComponent);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        return siteView;
    }

    private ITurma getDomainClass(String className, Integer curricularYear,
            IExecutionPeriod executionPeriod, ICursoExecucao executionDegree, ISuportePersistente sp)
            throws ExcepcaoPersistencia {

        ITurmaPersistente persistentClass = sp.getITurmaPersistente();
        ITurma domainClass = null;
        List domainList = new ArrayList();
        if (curricularYear == null) {
            domainClass = persistentClass.readByNameAndExecutionDegreeAndExecutionPeriod(className,
                    executionDegree, executionPeriod);

        } else {
            if (className == null && curricularYear == null) {

                domainList = persistentClass.readByExecutionDegreeAndDegreeAndExecutionPeriod(
                        executionDegree, executionDegree.getCurricularPlan().getDegree(),
                        executionPeriod);

                if (domainList.size() != 0) {
                    domainClass = (ITurma) domainList.get(0);
                }
            } else {
                domainClass = new Turma();
                domainClass.setAnoCurricular(curricularYear);
                domainClass.setExecutionDegree(executionDegree);
                domainClass.setExecutionPeriod(executionPeriod);

            }
        }
        return domainClass;
    }
}