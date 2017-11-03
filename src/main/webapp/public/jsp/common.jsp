<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%--@ page trimDirectiveWhitespaces="true" --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://raptornuke.ffcs.cn/ui/commonjsptld" prefix="cj" %>
<fmt:requestEncoding value="UTF-8" />
<%
	String ctx = request.getContextPath();
	boolean isPortlet = cn.ffcs.raptornuke.plugin.common.kernel.Config.getInstance().isPortlet();
%>
<%--portlet begin--%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://raptornuke.ffcs.cn/tld/portlet" prefix="raptornuke-portlet" %>
<%@ taglib uri="http://raptornuke.ffcs.cn/tld/theme" prefix="raptornuke-theme" %>
<%@ taglib uri="http://raptornuke.ffcs.cn/tld/ui" prefix="raptornuke-ui" %>
<%@ page import="javax.portlet.PortletConfig"%>
<%@ page import="javax.portlet.PortletContext"%>
<%@ page import="javax.portlet.PortletException"%>
<%@ page import="javax.portlet.PortletMode"%>
<%@ page import="javax.portlet.PortletPreferences"%>
<%@ page import="javax.portlet.PortletSession"%>
<%@ page import="javax.portlet.PortletURL"%>
<%@ page import="javax.portlet.RenderRequest"%>
<%@ page import="javax.portlet.RenderResponse"%>
<%@ page import="javax.portlet.UnavailableException"%>
<%@ page import="javax.portlet.ValidatorException"%>
<%@ page import="javax.portlet.WindowState"%>
<%@ page import="cn.ffcs.raptornuke.plugin.common.util.StringPool"%>
<%@ page import="cn.ffcs.raptornuke.plugin.common.util.StringUtil"%>
<%@ page import="cn.ffcs.raptornuke.plugin.common.util.Validator"%>
<%@ page import="cn.ffcs.raptornuke.plugin.common.util.GetterUtil" %>
<%@ page import="cn.ffcs.raptornuke.plugin.common.util.ParamUtil" %>
<%@ page import="cn.ffcs.raptornuke.portal.kernel.util.JavaConstants"%>
<%@ page import="cn.ffcs.raptornuke.portal.kernel.language.LanguageUtil" %>
<%@ page import="cn.ffcs.raptornuke.portal.service.persistence.LayoutUtil" %>
<%@ page import="cn.ffcs.raptornuke.portal.theme.ThemeDisplay"%>
<%@ page import="cn.ffcs.raptornuke.portlet.PortletPreferencesFactoryUtil" %>
<%@ page import="cn.ffcs.uom.common.key.WebKeys"%>
<%@ page import="cn.ffcs.raptornuke.portal.util.PortalUtil" %>
<portlet:defineObjects />
<%
ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
String pns = ((RenderResponse)request.getAttribute(JavaConstants.JAVAX_PORTLET_RESPONSE)).getNamespace();

WindowState windowState = null;
PortletMode portletMode = null;
if (renderRequest != null) {
	windowState = renderRequest.getWindowState();
	portletMode = renderRequest.getPortletMode();
}
else if (resourceRequest != null) {
	windowState = resourceRequest.getWindowState();
	portletMode = resourceRequest.getPortletMode();
}

String pathContext=PortalUtil.getPathContext();
%>
<c:set var="portletNamespace"><portlet:namespace /></c:set>
<fmt:setLocale value="<%=themeDisplay.getLocale()%>" />
<%--portlet end--%>