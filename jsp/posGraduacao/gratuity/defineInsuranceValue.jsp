<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<h2 align="center"><bean:message key="link.masterDegree.administrativeOffice.gratuity.defineInsuranceValue"/></h2>
<center>
<span class="error"><html:errors/></span>

<br/>
<br/>
<html:form action="/editInsuranceValue.do" focus="insuranceValue">
	<html:hidden property="method" value="defineInsuranceValue"/>
	<html:hidden property="executionYear" />
	<html:hidden property="page" value="2"/>
	<table border="0">
		<tr align="left">
			<td><bean:message key="label.masterDegree.gratuity.insuranceValue"/>:&nbsp;</td>
			<td>
				<html:text property="insuranceValue" size="8" />	
			</td>
		</tr>
		<tr align="left">
			<td>
				<bean:message key="label.masterDegree.gratuity.insuranceValueEndDate" />:&nbsp;
      		</td>
      		<td>				
				<html:select property="endDateDay">
					<html:options collection="<%= SessionConstants.MONTH_DAYS_KEY %>" property="value" labelProperty="label"/>
				</html:select>
				<html:select property="endDateMonth">
					<html:options collection="<%= SessionConstants.MONTH_LIST_KEY %>" property="value" labelProperty="label"/>
				</html:select>
				<html:select property="endDateYear">
					<html:options collection="<%= SessionConstants.YEARS_KEY %>" property="value" labelProperty="label"/>
				</html:select>				
			</td>          
		</tr>
	</table>
	<br/>
	<br/>
	<html:submit styleClass="inputbutton">
		<bean:message key="button.define"/>
	</html:submit>
</html:form>

</center>
