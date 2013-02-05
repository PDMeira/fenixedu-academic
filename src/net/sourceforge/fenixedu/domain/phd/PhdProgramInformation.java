package net.sourceforge.fenixedu.domain.phd;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.exceptions.PhdDomainOperationException;
import pt.ist.fenixWebFramework.services.Service;

public class PhdProgramInformation extends PhdProgramInformation_Base {

    protected PhdProgramInformation() {
        super();
    }

    protected PhdProgramInformation(final PhdProgramInformationBean bean) {
        init(bean);
    }

    protected void init(final PhdProgramInformationBean bean) {
        checkParameters(bean);

        setBeginDate(bean.getBeginDate());
        setMinThesisEctsCredits(bean.getMinThesisEctsCredits());
        setMaxThesisEctsCredits(bean.getMaxThesisEctsCredits());
        setMinStudyPlanEctsCredits(bean.getMinStudyPlanEctsCredits());
        setMaxStudyPlanEctsCredits(bean.getMaxStudyPlanEctsCredits());
        setNumberOfYears(bean.getNumberOfYears());
        setNumberOfSemesters(bean.getNumberOfSemesters());
        setPhdProgram(bean.getPhdProgram());

    }

    protected void checkParameters(final PhdProgramInformationBean bean) {
        if (bean.getBeginDate() == null) {
            throw new DomainException("error.phd.PhdProgramInformation.beginDate.required");
        }

        if (bean.getMinThesisEctsCredits() == null) {
            throw new DomainException("error.phd.PhdProgramInformation.MinThesisEctsCredits.required");
        }

        if (bean.getMaxThesisEctsCredits() == null) {
            throw new DomainException("error.phd.PhdProgramInformation.maxThesisEctsCredits.required");
        }

        if (bean.getMinStudyPlanEctsCredits() == null) {
            throw new DomainException("error.phd.PhdProgramInformation.minStudyPlanEctsCredits.required");
        }

        if (bean.getMaxStudyPlanEctsCredits() == null) {
            throw new DomainException("error.phd.PhdProgramInformation.maxStudyPlanEctsCredits.required");
        }

        if (bean.getNumberOfYears() == null) {
            throw new DomainException("error.phd.PhdProgramInformation.numberOfYears");
        }

        if (bean.getNumberOfSemesters() == null) {
            throw new DomainException("error.phd.PhdProgramInformation.numberOfSemesters");
        }

        if (hasSomePhdProgramInformationBeanWithSameBeginDate(bean)) {
            throw new PhdDomainOperationException("error.phd.PhdProgramInformation.other.information.with.same.beginDate");
        }
    }

    private boolean hasSomePhdProgramInformationBeanWithSameBeginDate(final PhdProgramInformationBean bean) {
        for (PhdProgramInformation information : bean.getPhdProgram().getPhdProgramInformations()) {
            if (this == information) {
                continue;
            }

            if (information.getBeginDate().equals(bean.getBeginDate())) {
                return true;
            }
        }

        return false;
    }

    @Service
    public void edit(final PhdProgramInformationBean bean) {
        checkParameters(bean);

        setBeginDate(bean.getBeginDate());
        setMinThesisEctsCredits(bean.getMinThesisEctsCredits());
        setMaxThesisEctsCredits(bean.getMaxThesisEctsCredits());
        setMinStudyPlanEctsCredits(bean.getMinStudyPlanEctsCredits());
        setMaxStudyPlanEctsCredits(bean.getMaxStudyPlanEctsCredits());
        setNumberOfYears(bean.getNumberOfYears());
        setNumberOfSemesters(bean.getNumberOfSemesters());
    }

    @Service
    public static PhdProgramInformation createInformation(final PhdProgramInformationBean bean) {
        return new PhdProgramInformation(bean);
    }

}
