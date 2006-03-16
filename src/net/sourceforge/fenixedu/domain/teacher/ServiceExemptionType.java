/*
 * Created on 7/Mar/2004
 */
package net.sourceforge.fenixedu.domain.teacher;


public enum ServiceExemptionType {
        
    GRANT_OWNER_EQUIVALENCE_WITHOUT_SALARY,
    GRANT_OWNER_EQUIVALENCE_WITH_SALARY,    
    SABBATICAL,    
    MEDICAL_SITUATION,    
    SPECIAL_LICENSE,    
    LICENSE_WITHOUT_SALARY_YEAR,
    LICENSE_WITHOUT_SALARY_LONG,      
    MATERNAL_LICENSE,
    MATERNAL_LICENSE_WITH_SALARY_80PERCENT,
    CONTRACT_SUSPEND,
    CONTRACT_SUSPEND_ART_73_ECDU,
    SERVICE_COMMISSION_IST_OUT,
    SERVICE_COMMISSION,
    LICENSE_WITHOUT_SALARY_FOR_ACCOMPANIMENT,
    REQUESTED_FOR,
    LICENSE_WITHOUT_SALARY_FOR_INTERNATIONAL_EXERCISE,    
    LICENSE_WITHOUT_SALARY_UNTIL_NINETY_DAYS,
    DANGER_MATERNAL_LICENSE,
    CHILDBIRTH_LICENSE,
    MILITAR_SITUATION,
    GRANT_OWNER_EQUIVALENCE_WITH_SALARY_SABBATICAL,
    GRANT_OWNER_EQUIVALENCE_WITH_SALARY_WITH_DEBITS,
    TEACHER_SERVICE_EXEMPTION_DL24_84_ART51_N6_EST_DISC,
    TEACHER_SERVICE_EXEMPTION_E_C_D_U,
    FUNCTIONS_MANAGEMENT_SERVICE_EXEMPTION,
    INCAPACITY_FOR_TOGETHER_DOCTOR_OF_THE_CGA,
    PUBLIC_MANAGER,
    GOVERNMENT_MEMBER;      
        
    public String getName() {
        return name();
    }
}