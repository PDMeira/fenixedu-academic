/*
 * 
 * Created on 27 of March de 2003
 * 
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 */
package ServidorApresentacao.Action.masterDegree.administrativeOffice.student;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoCountry;
import DataBeans.InfoPerson;
import DataBeans.InfoStudent;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.Data;
import Util.EstadoCivil;
import Util.Sexo;
import Util.TipoCurso;
import Util.TipoDocumentoIdentificacao;
import framework.factory.ServiceManagerServiceFactory;

public class ReadPersonInfoOfStudentsAction extends ServidorApresentacao.Action.base.FenixAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        //		Clear the Session
        session.removeAttribute(SessionConstants.NATIONALITY_LIST_KEY);
        session.removeAttribute(SessionConstants.MARITAL_STATUS_LIST_KEY);
        session.removeAttribute(SessionConstants.IDENTIFICATION_DOCUMENT_TYPE_LIST_KEY);
        session.removeAttribute(SessionConstants.SEX_LIST_KEY);
        session.removeAttribute(SessionConstants.MONTH_DAYS_KEY);
        session.removeAttribute(SessionConstants.MONTH_LIST_KEY);
        session.removeAttribute(SessionConstants.YEARS_KEY);
        session.removeAttribute(SessionConstants.EXPIRATION_YEARS_KEY);
        session.removeAttribute(SessionConstants.CANDIDATE_SITUATION_LIST);

        if (session != null) {
            IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
            Integer studentNumber = new Integer(getFromRequest("studentNumber", request));
            //String graduationType = getFromRequest("graduationType",
            // request);

            InfoStudent infoStudent = null;
            Object args[] = { studentNumber, new TipoCurso(TipoCurso.MESTRADO) };
            try {
                infoStudent = (InfoStudent) ServiceManagerServiceFactory.executeService(userView,
                        "ReadStudentByNumberAndType", args);
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }

            if (infoStudent == null) {
                ActionErrors errors = new ActionErrors();
                errors.add("nonExisting", new ActionError("error.exception.noStudents"));
                saveErrors(request, errors);
                return mapping.findForward("NoStudent");
            }
            request.setAttribute("infoStudent", infoStudent);
            //request.setAttribute("graduationType",graduationType);
            request.setAttribute("studentNumber", studentNumber);
            request.setAttribute("infoPerson", infoStudent.getInfoPerson());

            DynaActionForm changeApplicationInfoForm = (DynaActionForm) form;
            populateForm(changeApplicationInfoForm, infoStudent.getInfoPerson());

            //			Get List of available Countries
            Object result = null;
            result = ServiceManagerServiceFactory.executeService(userView, "ReadAllCountries", null);
            List country = (ArrayList) result;

            //			Build List of Countries for the Form
            Iterator iterador = country.iterator();

            List nationalityList = new ArrayList();
            while (iterador.hasNext()) {
                InfoCountry countryTemp = (InfoCountry) iterador.next();
                nationalityList.add(new LabelValueBean(countryTemp.getNationality(), countryTemp
                        .getNationality()));
            }

            session.setAttribute(SessionConstants.NATIONALITY_LIST_KEY, nationalityList);
            session.setAttribute(SessionConstants.MARITAL_STATUS_LIST_KEY, new EstadoCivil()
                    .toArrayList());
            session.setAttribute(SessionConstants.IDENTIFICATION_DOCUMENT_TYPE_LIST_KEY,
                    TipoDocumentoIdentificacao.toArrayList());
            session.setAttribute(SessionConstants.SEX_LIST_KEY, new Sexo().toArrayList());
            session.setAttribute(SessionConstants.MONTH_DAYS_KEY, Data.getMonthDays());
            session.setAttribute(SessionConstants.MONTH_LIST_KEY, Data.getMonths());
            session.setAttribute(SessionConstants.YEARS_KEY, Data.getYears());
            session.setAttribute(SessionConstants.EXPIRATION_YEARS_KEY, Data.getExpirationYears());

            return mapping.findForward("Success");
        }
        throw new Exception();
    }

    private String getFromRequest(String parameter, HttpServletRequest request) {
        String parameterString = request.getParameter(parameter);
        if (parameterString == null) {
            parameterString = (String) request.getAttribute(parameter);
        }
        return parameterString;
    }

    private void populateForm(DynaActionForm changeApplicationInfoForm, InfoPerson infoPerson) {
        changeApplicationInfoForm.set("identificationDocumentNumber", infoPerson
                .getNumeroDocumentoIdentificacao());
        changeApplicationInfoForm.set("identificationDocumentType", infoPerson
                .getTipoDocumentoIdentificacao().toString());
        changeApplicationInfoForm.set("identificationDocumentIssuePlace", infoPerson
                .getLocalEmissaoDocumentoIdentificacao());
        changeApplicationInfoForm.set("name", infoPerson.getNome());

        Calendar birthDate = Calendar.getInstance();
        if (infoPerson.getNascimento() == null) {
            changeApplicationInfoForm.set("birthDay", Data.OPTION_DEFAULT);
            changeApplicationInfoForm.set("birthMonth", Data.OPTION_DEFAULT);
            changeApplicationInfoForm.set("birthYear", Data.OPTION_DEFAULT);
        } else {
            birthDate.setTime(infoPerson.getNascimento());
            changeApplicationInfoForm.set("birthDay", new Integer(birthDate.get(Calendar.DAY_OF_MONTH))
                    .toString());
            changeApplicationInfoForm.set("birthMonth", new Integer(birthDate.get(Calendar.MONTH))
                    .toString());
            changeApplicationInfoForm.set("birthYear", new Integer(birthDate.get(Calendar.YEAR))
                    .toString());
        }

        Calendar identificationDocumentIssueDate = Calendar.getInstance();
        if (infoPerson.getDataEmissaoDocumentoIdentificacao() == null) {
            changeApplicationInfoForm.set("idIssueDateDay", Data.OPTION_DEFAULT);
            changeApplicationInfoForm.set("idIssueDateMonth", Data.OPTION_DEFAULT);
            changeApplicationInfoForm.set("idIssueDateYear", Data.OPTION_DEFAULT);
        } else {
            identificationDocumentIssueDate.setTime(infoPerson.getDataEmissaoDocumentoIdentificacao());
            changeApplicationInfoForm.set("idIssueDateDay", new Integer(identificationDocumentIssueDate
                    .get(Calendar.DAY_OF_MONTH)).toString());
            changeApplicationInfoForm.set("idIssueDateMonth", new Integer(
                    identificationDocumentIssueDate.get(Calendar.MONTH)).toString());
            changeApplicationInfoForm.set("idIssueDateYear", new Integer(identificationDocumentIssueDate
                    .get(Calendar.YEAR)).toString());
        }

        Calendar identificationDocumentExpirationDate = Calendar.getInstance();
        if (infoPerson.getDataValidadeDocumentoIdentificacao() == null) {
            changeApplicationInfoForm.set("idExpirationDateDay", Data.OPTION_DEFAULT);
            changeApplicationInfoForm.set("idExpirationDateMonth", Data.OPTION_DEFAULT);
            changeApplicationInfoForm.set("idExpirationDateYear", Data.OPTION_DEFAULT);
        } else {
            identificationDocumentExpirationDate.setTime(infoPerson
                    .getDataValidadeDocumentoIdentificacao());
            changeApplicationInfoForm.set("idExpirationDateDay", new Integer(
                    identificationDocumentExpirationDate.get(Calendar.DAY_OF_MONTH)).toString());
            changeApplicationInfoForm.set("idExpirationDateMonth", new Integer(
                    identificationDocumentExpirationDate.get(Calendar.MONTH)).toString());
            changeApplicationInfoForm.set("idExpirationDateYear", new Integer(
                    identificationDocumentExpirationDate.get(Calendar.YEAR)).toString());
        }

        changeApplicationInfoForm.set("fatherName", infoPerson.getNomePai());
        changeApplicationInfoForm.set("motherName", infoPerson.getNomeMae());
        changeApplicationInfoForm.set("nationality", infoPerson.getInfoPais().getNationality());
        changeApplicationInfoForm.set("birthPlaceParish", infoPerson.getFreguesiaNaturalidade());
        changeApplicationInfoForm.set("birthPlaceMunicipality", infoPerson.getConcelhoNaturalidade());
        changeApplicationInfoForm.set("birthPlaceDistrict", infoPerson.getDistritoNaturalidade());
        changeApplicationInfoForm.set("address", infoPerson.getMorada());
        changeApplicationInfoForm.set("place", infoPerson.getLocalidade());
        changeApplicationInfoForm.set("postCode", infoPerson.getCodigoPostal());
        changeApplicationInfoForm.set("addressParish", infoPerson.getFreguesiaMorada());
        changeApplicationInfoForm.set("addressMunicipality", infoPerson.getConcelhoMorada());
        changeApplicationInfoForm.set("addressDistrict", infoPerson.getDistritoMorada());
        changeApplicationInfoForm.set("telephone", infoPerson.getTelefone());
        changeApplicationInfoForm.set("mobilePhone", infoPerson.getTelemovel());
        changeApplicationInfoForm.set("email", infoPerson.getEmail());
        changeApplicationInfoForm.set("webSite", infoPerson.getEnderecoWeb());
        changeApplicationInfoForm.set("contributorNumber", infoPerson.getNumContribuinte());
        changeApplicationInfoForm.set("occupation", infoPerson.getProfissao());
        changeApplicationInfoForm.set("username", infoPerson.getUsername());
        changeApplicationInfoForm.set("areaOfAreaCode", infoPerson.getLocalidadeCodigoPostal());

        if (infoPerson.getSexo() != null)
            changeApplicationInfoForm.set("sex", infoPerson.getSexo().toString());
        if (infoPerson.getEstadoCivil() != null)
            changeApplicationInfoForm.set("maritalStatus", infoPerson.getEstadoCivil().toString());
    }

}