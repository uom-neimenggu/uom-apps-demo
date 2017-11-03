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
	<cj:head type="css" url="/uom-apps/information/center.css"/>
	<!-- tab mod -->
	<cj:head type="css" url="/uom-apps/public/jqui-themes/mod/tabs.css"/>
	<!-- simple-table -->
	<cj:head type="css" url="/uom-apps/public/simple-table/css/simple-table.css"/>
	<!-- pagination -->
	<cj:head type="css" url="/uom-apps/public/pagination/pagination.css"/>
	<!-- dwr -->
	<cj:head type="js" url="/uom-apps/dwr/engine.js" />
	<cj:head type="js" url="/uom-apps/dwr/interface/informationAction.js" />
	<!-- utils -->
	<cj:head type="js" url="/uom-apps/public/utils/base.js" />
	<cj:head type="js" url="/uom-apps/public/fullcalendar/fullcalendar.min.js" />
	<cj:head type="js" url="/uom-apps/public/pagination/jquery.pagination.js" />
	<cj:head type="js" url="/uom-apps/public/simple-table/js/jquery.simple-table.src.js" />
	<cj:head type="js" url="/uom-apps/public/utils/dialog.js" />
	<cj:head type="js" url="/uom-apps/public/utils/date.js" />
	<cj:head type="js" url="/uom-apps/public/utils/misc.js" />
	<!-- info-center -->
	<cj:head type="js" url="/uom-apps/information/center.js" />
	<!-- tab mod -->
	<cj:head type="js" url="/uom-apps/public/jqui-themes/mod/tabs.js" />
	<%@include file="../public/utils/portal-env.jspf"%>
</cj:override>
<cj:override name="bodyContent">
	<!-- XXX mod for uom -->
	<div id="ic-jump-back" style="display:none"><a>&lt;&lt;返回公告</a></div>
	<div id="ic-tabs">
		<ul>
    	</ul>
		<div id="ic-panel-right">
			<!-- 位置 -->
			<div id="ic-nav"></div>
			<!-- 文章清单 -->
	    	<div name="ic-type" id="ic-type-list">
				<div id="ic-category-title"></div>
				<div id="ic-title-list-wrapper">
					<div width="100%" height="" id="ic-title-list">
					</div>
				</div>
				<div id="ic-pagination" class="pagination"></div>
				</div>
				<!-- 文章内容 -->
				<div name="ic-type" id="ic-type-article">
					<div id="ic-article-title"></div>
					<div id="ic-article-info"></div>
					<div id="ic-article-content"></div>
					<div id="ic-article-attachment">
						<div id="ic-article-attachment-title">附件：</div>
					<div id="ic-article-attachment-list">
					</div>
				</div>
			</div>
    	</div>
	</div>
</cj:override>
<%-- extends templates --%>
<%@ include file="/templates/portlet_tpl.jspf" %>