<%@ page pageEncoding="UTF-8"%>
<%
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>查询入口</title>
<style type="text/css">
body {
	overflow: hidden;
	margin: 0;
	padding: 0;
}

html {
	overflow-y: hidden;
	background-image: url(/html/portal/ext/images/login_bg.jpg);
}

#logContainer {
	height: 460px;
	padding-top: 140px;
	margin-top: 0px;
	margin-right: auto;
	margin-bottom: 0px;
	margin-left: auto;
}

#loginFrame {
	background-image: url(/html/portal/ext/images/bottom.png);
	_margin-top: -3px;
	height: 217px;
	width: 518px;
}

#loginArea {
	width: 518px;
	margin: 0px auto;
}

#loginButtom {
	/*background-image: url(/html/portal/ext/images/login_bg_2.gif);*/
	height: 68px;
	width: 580px;
	line-height: 20px;
	padding-top: 80px;
	padding-right: 60px;
	text-align: center;
	color: #444;
	font-size: 13px;
}

#loginButtom a {
	color: #444;
}

#loginButtom a:hover {
	color: #blue;
	text-decoration: underline;
}

div label {
	color: #000;
	font-weight: bold;
	font-size: 14px;
}
</style>
</head>
<body>
	<div id="logContainer">
		<div id="loginArea">
			<div style="height:77px">
				<img src="/html/portal/ext/images/top.png" width="518" height="77" />
			</div>
			<div id="loginFrame">
				<div id="loginButtom">
					<a id="audit" target="_blank"
						href="pages/audit/audit_no_login_main.zul"
						style="text-decoration:none;cursor:pointer;">稽核查询</a>&nbsp&nbsp&nbsp|&nbsp
					<!-- <a id="staffContrast" target="_blank" href="pages/contrast/crm.zul"
						style="text-decoration:none;cursor:pointer;">员工对照查询</a>&nbsp&nbsp&nbsp|&nbsp -->
					<a id="organizationStructure" target="_blank"
						href="pages/structurePermission/organization_structure_main.zul"
						style="text-decoration:none;cursor:pointer;">组织机构查询</a> <br />
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		/* $(function() {
			$('#staffContrast').click(
					function() {
						//获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp    
						var curWwwPath = window.document.location.href;
						//获取主机地址之后的目录，如： uimcardprj/share/meun.jsp    
						var pathName = window.document.location.pathname;
						var pos = curWwwPath.indexOf(pathName);
						//获取主机地址，如： http://localhost:8083    
						var localhostPaht = curWwwPath.substring(0, pos);
						//获取带"/"的项目名，如：/uimcardprj    
						var projectName = pathName.substring(0, pathName
								.substr(1).indexOf('/') + 1);
						window.open(localhostPaht
								+ "/uom-apps/pages/contrast/crm.zul");
					});

			$('#organizationStructure')
					.click(
							function() {
								//获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp    
								var curWwwPath = window.document.location.href;
								//获取主机地址之后的目录，如： uimcardprj/share/meun.jsp    
								var pathName = window.document.location.pathname;
								var pos = curWwwPath.indexOf(pathName);
								//获取主机地址，如： http://localhost:8083    
								var localhostPaht = curWwwPath
										.substring(0, pos);
								//获取带"/"的项目名，如：/uimcardprj    
								var projectName = pathName.substring(0,
										pathName.substr(1).indexOf('/') + 1);
								window
										.open(localhostPaht
												+ "/uom-apps/pages/structurePermission/organization_structure_main.zul");
							});
		}); */
	</script>
</body>