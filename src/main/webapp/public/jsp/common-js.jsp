<%@ page language="java" contentType="text/javascript; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://raptornuke.ffcs.cn/ui/commonjsptld" prefix="cj" %>
<fmt:requestEncoding value="UTF-8" />
<%
	String ctx = request.getContextPath();
	boolean isPortlet = cn.ffcs.raptornuke.plugin.common.kernel.Config.getInstance().isPortlet();
%>