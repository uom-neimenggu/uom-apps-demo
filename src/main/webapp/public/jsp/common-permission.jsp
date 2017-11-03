<%@ page import="cn.ffcs.raptornuke.portal.security.permission.PermissionChecker"%>
<%@ page import="cn.ffcs.uom.common.key.ActionKeys"%>
<%
	long groupId = themeDisplay.getScopeGroupId();
	String rootPortletId = themeDisplay.getPortletDisplay()
			.getRootPortletId();
	String primKey = themeDisplay.getPortletDisplay().getResourcePK();
	PermissionChecker permissionChecker = themeDisplay
			.getPermissionChecker();
%>