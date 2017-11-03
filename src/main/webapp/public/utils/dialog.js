/**
 *需要导入的脚本：
 *ui.dialog.js
 */

/**
 * 显示消息对话框
 * @author 曾臻
 * @date 2012-10-26
 * @param msg
 */
function showMessageDlg(parent,msg,title){
	var dlg=jQuery("<div style='padding:1em'>"+msg+"</div>").appendTo(parent);
	dlg.dialog({
		title:title?title:"消息",
		modal:true,
		resizable:false,//TODO style problem
		buttons:{
		 "确定": function() {
                jQuery( this ).dialog( "close" );
            }
		},
		show:{
        	effect:"scale",
        	duration:200
        },
        hide:{
        	effect:"fade",
        	duration:300
        } ,
        position:{
        	of:parent,
        	collision:"fit"
        }
	});
}
function showMessageDlg2(msg,title){
	showMessageDlg(jQuery(document.body),msg,title);
}
function showErrorDlg(parent,msg,title){
	var dlg=jQuery("<div style='padding:1em'>"+msg+"</div>").appendTo(parent);
	dlg.dialog({
		title:title?title:"错误",
		modal:true,
		resizable:false,//TODO style problem
		buttons:{
		 "确定": function() {
                jQuery( this ).dialog( "close" );
            }
		},
		show:{
        	effect:"scale",
        	duration:200
        },
        hide:{
        	effect:"fade",
        	duration:300
        } ,
        position:{
        	of:parent,
        	collision:"fit"
        }
	});
}
function showErrorDlg2(msg,title){
	showErrorDlg(jQuery(document.body),msg,title);
}
function showConfirmDlg(parent,msg,title,onOk,onCancel){
	var dlg=jQuery("<div style='padding:1em'>"+msg+"</div>").appendTo(parent);
	dlg.dialog({
		title:title?title:"请确认",
		modal:true,
		resizable:false,//TODO style problem
		buttons:{
		 "确定": function() {
            jQuery( this ).dialog( "close" );
            if(onOk)
            	onOk();
		 },
		 "取消": function() {
             jQuery( this ).dialog( "close" );
             if(onCancel)
            	 onCancel();
		 }
		},
		show:{
        	effect:"scale",
        	duration:200
        },
        hide:{
        	effect:"fade",
        	duration:300
        } ,
        position:{
        	of:parent,
        	collision:"fit"
        }
	});
}
function showConfirmDlg2(msg,title,onOk,onCancel){
	showConfirmDlg(jQuery(document.body),msg,title,onOk,onCancel);
}
/**
 * 
 */
function popupPortlet(targetPortletId,urlParam){
	var instance = this;
	var portletId = targetPortletId;//jQuery(portlet).attr("id").substr(3);
	var standalone = 1;//jQuery(portlet).attr("standalone");
	var _title = '';
	var _draggable = true;
	var _resizable = false;
	var _modal = false;
	var _width = 600;
	var _height = 500;
	var _scrolling = "no";
	var _autoHeight = false;
	var _intervalTime = 200;
	jQuery.ajax({
		type: 'POST',
		async: false,
		url: themeDisplay.getPathMain() + '/scpanel/scpanel_core',
		data: {
			p_l_id: themeDisplay.getPlid(),
			p_p_id: portletId,
			cmd: "getInfos",
			doAsUserId: themeDisplay.getDoAsUserIdEncoded()
		},
		dataType: 'json',
		success: function(message) {
			_title = message.title;
			_draggable = message.draggable;
			_resizable = message.resizable;
			_modal = message.modal;
			_width = parseInt(message.width);
			_height = parseInt(message.height);
			_scrolling = message.scrolling;
			_autoHeight = message.autoHeight;
			_intervalTime = parseInt(message.intervalTime);
		}
	});
	var _url;
	if (standalone == 1) 
		_url = themeDisplay.getPathMain() + '/common/open_portlet?p_p_id=' + portletId + '&p_l_id=' + themeDisplay.getPlid() + '&doAsUserId=' + themeDisplay.getDoAsUserIdEncoded() + '&resizable=' + _resizable + '&scrolling=' + _scrolling + '&autoHeight=' + _autoHeight + '&intervalTime=' + _intervalTime + '&height=' + _height;
	else if (standalone == 2) 
		_url = themeDisplay.getPathMain() + '/portal/standalone?s_p_p_id=' + portletId + '&s_p_l_id=' + themeDisplay.getPlid() + '&s_doAsUserId=' + themeDisplay.getDoAsUserIdEncoded();
	if(urlParam){
		urlParam=urlParam.replace(/\&/g,"%26");
		urlParam=urlParam.replace(/\=/g,"%3d");
		_url+="&userParameters="+urlParam;
	}
	if (_url) {
		Raptornuke.Popup({
			draggable: _draggable,
			resizable: _resizable,
			modal: _modal,
			url: _url,
			width: _width,
			height: _height,
			message: '<div class="loading-animation" />',
			position: ['center', 'center'],
			title: _title,
			blockDragging: true,
			blockResizing: true
		});
	}
}