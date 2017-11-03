<%@ include file="/public/jsp/common.jsp"%>
<%@ page pageEncoding="UTF-8"%>
<cj:override name="heads">
	<!-- jquery ui -->
	<cj:head type="css" url="/html/juithemes/base/jquery.ui.core.css"/>
	<cj:head type="css" url="/html/juithemes/base/jquery.ui.all.css"/>
	<cj:head type="css" url="/html/juithemes/base/jquery.ui.button.css"/>
	<cj:head type="css" url="/html/juithemes/base/jquery.ui.dialog.css"/>
	<cj:head type="css" url="/html/juithemes/base/jquery.ui.datepicker.css"/>
	<cj:head type="css" url="/html/juithemes/base/jquery.ui.tabs.css"/>
	<!-- kindeditor -->
	<cj:head type="css" url="/uom-apps/public/kindeditor/themes/default/default.css"/>
	<!-- jqGrid -->
	<cj:head type="css" url="/uom-apps/public/jqGrid/css/ui.jqgrid.css"/>
	<!-- jqTree -->
	<cj:head type="css" url="/uom-apps/public/jqTree/jqtree.css"/>
	<!-- ui mod -->
	<cj:head type="css" url="/uom-apps/public/jqui-themes/mod/tabs.css"/>
	<cj:head type="css" url="/uom-apps/public/jqui-themes/mod/jqgrid.css"/>
	<cj:head type="css" url="/uom-apps/public/style-fix/style-fix.css"/>
	<!-- utils -->
	<cj:head type="css" url="/uom-apps/public/utils/org-selector.css"/>
	<!-- swfupload -->
	<cj:head type="css" url="/uom-apps/public/swfupload/upload-dlg.css"/>
	<!-- info-mgr -->
	<cj:head type="css" url="/uom-apps/information/manager.css"/>
	
	<!-- effects -->
	<cj:head type="js" url="/html/js/jquery/effects.core.js" />
	<cj:head type="js" url="/html/js/jquery/effects.fade.js" />
	<cj:head type="js" url="/html/js/jquery/effects.scale.js" />
	<!-- dwr -->
	<cj:head type="js" url="/uom-apps/dwr/engine.js" />
	<cj:head type="js" url="/uom-apps/dwr/interface/informationAction.js" />
	<cj:head type="js" url="/uom-apps/dwr/interface/orgTreeAction.js" />
	<!-- kindeditor -->
	<cj:head type="js" url="/uom-apps/public/kindeditor/kindeditor.js" />
	<cj:head type="js" url="/uom-apps/public/kindeditor/lang/zh_CN.js"/>
	<!-- jqGrid -->
	<cj:head type="js" url="/uom-apps/public/jqGrid/js/i18n/grid.locale-cn.js" />
	<cj:head type="js" url="/uom-apps/public/jqGrid/js/jquery.jqGrid.min.js"/>
	<!-- jqTree -->
	<cj:head type="js" url="/uom-apps/public/jqTree/tree.jquery.js"/>
	<!-- fullcalendar -->
	<cj:head type="js" url="/uom-apps/public/fullcalendar/fullcalendar.min.js" />
	<!-- swfupload -->
	<cj:head type="js" url="/uom-apps/public/swfupload/js/swfupload.js" />
	<cj:head type="js" url="/uom-apps/public/swfupload/js/swfupload.queue.js" />
	<cj:head type="js" url="/uom-apps/public/swfupload/js/fileprogress.js" />
	<cj:head type="js" url="/uom-apps/public/swfupload/js/handlers.js" />
	<cj:head type="js" url="/uom-apps/public/swfupload/js/jquery.swfupload.js" />
	<cj:head type="js" url="/uom-apps/public/swfupload/upload-dlg.js" />
	<!-- alfresco -->
	<!-- XXX mod for uom -->
	<!--cj:head type="js" url="/uniportal-ecms/dwr/interface/attachmentAction.js" /-->
	<!-- utils -->
	<cj:head type="js" url="/uom-apps/public/utils/base.js" />
	<cj:head type="js" url="/uom-apps/public/utils/dialog.js" />
	<cj:head type="js" url="/uom-apps/public/utils/date.js" />
	<cj:head type="js" url="/uom-apps/public/utils/org-selector.js" />
	<cj:head type="js" url="/uom-apps/public/utils/test.js" />
	<!-- info-mgr -->
	<cj:head type="js" url="/uom-apps/information/mgr-init.js" />
	<cj:head type="js" url="/uom-apps/information/mgr-article-detail.js" />
	<cj:head type="js" url="/uom-apps/information/mgr-article-list.js" />
	<cj:head type="js" url="/uom-apps/information/manager.js" />
	<!-- tab mod -->
	<cj:head type="js" url="/uom-apps/public/jqui-themes/mod/tabs.js" />
</cj:override>
<cj:override name="bodyContent">
	<div id="im-tabs" style="display:none">
	    <ul>
	        <li><a href="#im-tab-list">信息列表</a></li>
	        <li><a href="#im-tab-release">信息发布</a></li>
	    </ul>
	    <div id="im-tab-list">
	    	<%@ include file="mgr-article-list.jspf" %>
	    </div>
	    <div id="im-tab-release">
	    	<%@ include file="mgr-article-detail.jspf" %>
	    </div>
	    <div id="im-tab-pending">
	    </div>
	</div>
</cj:override>
<%-- extends templates --%>
<%@ include file="/templates/portlet_tpl.jspf" %>