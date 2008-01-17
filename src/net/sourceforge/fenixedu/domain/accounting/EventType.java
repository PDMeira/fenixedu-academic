package net.sourceforge.fenixedu.domain.accounting;

public enum EventType {

    CANDIDACY_ENROLMENT,

    SCHOOL_REGISTRATION_CERTIFICATE_REQUEST,

    ENROLMENT_CERTIFICATE_REQUEST,

    APPROVEMENT_CERTIFICATE_REQUEST,

    DEGREE_FINALIZATION_CERTIFICATE_REQUEST,

    SCHOOL_REGISTRATION_DECLARATION_REQUEST,
    
    ENROLMENT_DECLARATION_REQUEST, 
    
    BOLONHA_DEGREE_DIPLOMA_REQUEST,
    
    BOLONHA_MASTER_DEGREE_DIPLOMA_REQUEST,
    
    BOLONHA_ADVANCED_FORMATION_DIPLOMA_REQUEST,
    
    BOLONHA_PHD_PROGRAM_DIPLOMA_REQUEST,
    
    GRATUITY,

    INSURANCE,

    DFA_REGISTRATION,
    
    PHD_REGISTRATION,

    ADMINISTRATIVE_OFFICE_FEE,
    
    ADMINISTRATIVE_OFFICE_FEE_INSURANCE, 
    
    IMPROVEMENT_OF_APPROVED_ENROLMENT,
    
    STUDENT_REINGRESSION_REQUEST,
    
    EQUIVALENCE_PLAN_REQUEST,
    
    PHOTOCOPY_REQUEST,
    
    COURSE_LOAD_REQUEST;

    public String getName() {
	return name();
    }

    public String getQualifiedName() {
	return EventType.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
	return EventType.class.getName() + "." + name();
    }

}
