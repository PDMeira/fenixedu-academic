/*
 * Created on 08/Set/2003
 *
 */
package ServidorApresentacao.Action.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoSiteShiftsAndGroups;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixContextAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author asnr and scpo
 *  
 */
public class ViewShiftsAndGroupsAction extends FenixContextAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");

        Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);

        InfoSiteShiftsAndGroups infoSiteShiftsAndGroups;
        Object[] args = { groupPropertiesCode };
        try {
            infoSiteShiftsAndGroups = (InfoSiteShiftsAndGroups) ServiceUtils.executeService(userView,
                    "ReadShiftsAndGroups", args);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute("infoSiteShiftsAndGroups", infoSiteShiftsAndGroups);

        return mapping.findForward("sucess");
    }
}