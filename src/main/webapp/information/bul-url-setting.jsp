<%@ include file="/public/jsp/common.jsp"%>
<%@ page pageEncoding="UTF-8"%>
<cj:override name="bodyContent">
	<jsp:useBean id="portletPrefBean"
		class="cn.ffcs.uom.information.action.PortletPreferencesBean" />
	<cj:call bean="${portletPrefBean}" method="setPageContext">
		<cj:param paramClass="javax.servlet.jsp.PageContext" value="${pageContext}" />
	</cj:call>
	<cj:call var="infoCenterUrlValue" bean="${portletPrefBean}" method="portletPreferencesValue">
		<cj:param paramClass="java.lang.String" value="infoCenterUrl" />
	</cj:call>
	<raptornuke-portlet:actionURL name="doSaveInfoCenterUrl" var="doSaveURL">
		<raptornuke-portlet:param
			name="<%=cn.ffcs.raptornuke.plugin.common.web.JSPPortlet.PROCESS_ACTION_BEAN_TYPE%>"
			value="cn.ffcs.uom.information.action.PortletPreferencesBean" />
	</raptornuke-portlet:actionURL>

	<form method="post" action="${doSaveURL}">
		<table>
			<tr>
				<td>信息中心的友好URL</td>
				<td><input name="<cj:pn />infoCenterUrl" value="${infoCenterUrlValue}" /></td>
			</tr>
		</table>
		<input type='submit' value='提交' />
	</form>

	<hr />
</cj:override>
<%-- extends templates --%>
<%@ include file="/templates/portlet_tpl.jspf"%>