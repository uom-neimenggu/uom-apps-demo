/**
 * 文件上传对话框
 * @author Anonymous
 * @author 曾臻
 * @date 2013-01-07
 * 
 * 依赖（需导入以下文件）：
<cf:head type="css" url="/uniportal-plugins/public/swfupload/upload-dlg.css"/>

<cf:head type="css" url="/uniportal-plugins/public/jqGrid/css/ui.jqgrid.css"/>
<cf:head type="js" url="/uniportal-plugins/public/jqGrid/js/i18n/grid.locale-cn.js" />
<cf:head type="js" url="/uniportal-plugins/public/jqGrid/js/jquery.jqGrid.min.js"/>

<cf:head type="js" url="/uniportal-plugins/public/swfupload/js/swfupload.js" />
<cf:head type="js" url="/uniportal-plugins/public/swfupload/js/swfupload.queue.js" />
<cf:head type="js" url="/uniportal-plugins/public/swfupload/js/fileprogress.js" />
<cf:head type="js" url="/uniportal-plugins/public/swfupload/js/handlers.js" />
<cf:head type="js" url="/uniportal-plugins/public/swfupload/js/jquery.swfupload.js" />

<cf:head type="js" url="/uniportal-plugins/public/utils/base.js" />
<cf:head type="js" url="/uniportal-plugins/public/utils/date.js" />
<cf:head type="js" url="/uniportal-plugins/public/utils/dialog.js" />
 */


function SwfUpload(){
}
var swfUpload=new SwfUpload();
SwfUpload.prototype.items=[];
//SwfUpload.prototype.canceledFileIds={};
SwfUpload.prototype.actionUrl="";
SwfUpload.prototype.showDialog=function(container,actionUrl,items,onOk,onCancel,onClose){
	var html='<div id="swfupload-dlg" style="display:none; margin-bottom:55px !important;_margin-bottom:55px;padding-left:10px;padding-top:5px;overflow-y:auto;">'+
        	'<table height="100%" width="100%">'+
        		'<tr height="1%">'+
        			'<td  style="vertical-align:top">'+
        				'<table id="swfupload-table" ></table>'+
        			'</td>'+
        		'</tr>'+
        		'<tr>'+
        			'<td  style="vertical-align:top">'+
        				'<div id="swfupload-control" style="padding-top:1em">'+
        				    '<input type="button" id="swfupload-button"/>'+
        				    '<ul id="swfupload-log" style="padding-top:1em;"></ul>'+
        				'</div>'+
        			'</td>'+
        		'</tr>'+
        	'</table>'+
    	'</div>';

    if(jQuery("#swfupload-dlg").length==0){
    	container.append(html);
    }
    	
	var _this=this;
	_this.actionUrl=actionUrl;
	_this.items=jQuery.extend(true,[],items);
	var dlg=jQuery("#swfupload-dlg");
	dlg.dialog({
		title:"上传文件",
		modal:true,
		resizable:false,//TODO style problem
		height:440,
		width:530,
		create:function(){
			_this.onCreate();
		},
		open:function(){
			_this.onOpen(_this.items);
		},
		close:function(){
			if(onClose)
				onClose(_this.items);
		},
		selectable:false,
		buttons:{
			"确定": function() {
				jQuery( this ).dialog( "close" );
				if(onOk){
					onOk(_this.items);
				}
			},
			"取消": function() {
				jQuery( this ).dialog( "close" );
				if(onCancel)
					onCancel(_this.items);
			}
		},
		show:{
        	effect:"scale",
        	duration:200
        },
        hide:{
        	effect:"fade",
        	duration:300
        }
	});
};
SwfUpload.prototype.toReadableLength=function(size){
	if(size>1024*1024){
		return Math.floor(size/1024/1024)+" MB";
	}else if(size>1024){
		return Math.floor(size/1024)+" KB";
	}else{
		return size+" Bytes";
	}
	return "";
};
SwfUpload.prototype._refresh=function(){
	var _this=this;
	jQuery("#swfupload-table").clearGridData();
	var items2=this._filterItems(this.items);
	jQuery.each(items2,function(i,n){
		jQuery("#swfupload-table").jqGrid('addRowData',n.id,n);
	});
	this._makeOpButtons(jQuery("#swfupload-table"));
};
SwfUpload.prototype._filterItems=function(items){
	var _this=this;
	var result=[];
	jQuery.each(items,function(i,n){
		//目前暂不考虑“还原”功能，将其隐藏
		if(n.operation=="add-remove")
			return true;
		else if(n.operation=="remove")
			return true;
		
		var n2=jQuery.extend({
	        	lengthText:_this.toReadableLength(n.length),
	        	creationDateText:n.creationDate?formatDate(new Date(n.creationDate)):""	},
	        n);
		
		if(n.operation=="add")
			n2.state="新增";
		else if(!n.operation)
			n2.state="已有";
		else if(n.operation=="add-remove")
			n2.state="已删除";
		else if(n.operation=="remove")
			n2.state="已删除";
		
		result.push(n2);
	});
	return result;
};
SwfUpload.prototype._makeOpButtons=function(table){
	var _this=this;
	var arr=table.getRowData();
	var ids=table.getDataIDs();
	for(var i=0;i<arr.length;i++){
		var row=arr[i];
		var id=ids[i];
		var remove="<a class='row-btn-remove' item-id='"+id+"'>删除</a>";
		var html="";
		if(!row.operation){
			html=remove;
		}else if(row.operation="add"){
			html=remove;
		}/*else if(row.operation="remove"){
			html=recovery;
		}else if(row.operation="add-remove"){
			html=recovery;
		}*/
		table.setRowData(id,{op:html});
	}
	table.find(".row-btn-remove").click(function(){
		var id=jQuery(this).attr("item-id");
		jQuery.each(_this.items,function(i,n){
			if(n.id==id){
				if(!n.operation)
					n.operation="remove";
				else if(n.operation=="add")
					n.operation="add-remove";
				return false;
			}
		});
		_this._refresh();
	});
}
SwfUpload.prototype._initSwf=function(){
	var _this=this;
	jQuery('#swfupload-control').swfupload({
        upload_url:_this.actionUrl,
        file_post_name:'uploadfile',
        file_size_limit:"1000 MB",
        file_types:"*.*",
        file_types_description:"All files",  
        //XXX mod for uom
        flash_url:"/uom-apps/public/swfupload/js/swfupload.swf",
        //flash_url:"/uniportal-plugins/public/swfupload/js/swfupload.swf",
        button_image_url:'/uom-apps/public/swfupload/js/swfupload/upload_114x29.png',
        //button_image_url:'/uniportal-plugins/public/swfupload/js/swfupload/upload_114x29.png',
        button_width:114,
        button_height:29,
        button_placeholder:jQuery('#swfupload-button')[0],
        button_cursor:SWFUpload.CURSOR.HAND
    })
            .bind('fileQueued', function (event, file) {
                var listitem = '<li id="swfupload-' + file.id + '" >' +
                        '文件: <em>' + file.name + '</em> (' + _this.toReadableLength(file.size) + ') <span class="progressvalue" ></span>' +
                        '<div class="progressbar" ><div class="progress" ></div></div>' +
                        '<p class="status" >排队中</p>' +
                        '<span class="stopUpload" >&nbsp;</span>' +
                        '</li>';
                jQuery('#swfupload-log').append(listitem);
                jQuery('#swfupload-log li#swfupload-' + file.id).hide();
                jQuery('li#swfupload-' + file.id + ' .stopUpload').bind('click', function () { //Remove from queue on cancel click
                    var swfu = jQuery.swfupload.getInstance('#swfupload-control');
                    swfu.cancelUpload(file.id);
                    //_this.canceledFileIds[file.id]=true;
                    jQuery('#swfupload-log li#swfupload-' + file.id).find('p.status').text('已取消');
                    jQuery('#swfupload-log li#swfupload-' + file.id).find('span.stopUpload').css('background-image','none')
                    jQuery('#swfupload-log li#swfupload-' + file.id).find('span.stopUpload').css('cursor','default')
                    //jQuery('#swfupload-log li#swfupload-' + file.id).slideUp('fast');
                    jQuery('#swfupload-log li#swfupload-' + file.id).remove();
                });
                //jQuery('#swfupload-log-div').scrollTop(jQuery('#swfupload-log-div')[0].scrollHeight);
                // start the upload since it's queued
                jQuery(this).swfupload('startUpload');
            })
            .bind('fileQueueError', function (event, file, errorCode, message) {
                //alert('Size of the file '+file.name+' is greater than limit');
                alert(errorCode+":"+message)
            })
            .bind('fileDialogComplete', function (event, numFilesSelected, numFilesQueued) {
                //jQuery('#swfupload-queuestatus').text('已选中文件: ' + numFilesSelected + ' / 队列中文件: ' + numFilesQueued);
            })
            .bind('uploadStart', function (event, file) {
            	jQuery('#swfupload-log li#swfupload-' + file.id).show();
                jQuery('#swfupload-log li#swfupload-' + file.id).find('p.status').text('上传中...');
                jQuery('#swfupload-log li#swfupload-' + file.id).find('span.progressvalue').text('0%');
                jQuery('#swfupload-log li#swfupload-' + file.id).find('span.cancel').hide();
            })
            .bind('uploadProgress', function (event, file, bytesLoaded) {
                //Show Progress
                var percentage = Math.round((bytesLoaded / file.size) * 100);
                jQuery('#swfupload-log li#swfupload-' + file.id).find('div.progress').css('width', percentage + '%');
                jQuery('#swfupload-log li#swfupload-' + file.id).find('span.progressvalue').text(percentage + '%');
            })
            .bind('uploadSuccess', function (event, file, serverData) {
                var item = jQuery('#swfupload-log li#swfupload-' + file.id);
                jQuery('#swfupload-log li#swfupload-' + file.id).find('span.stopUpload').css('background-image','none') ;
                jQuery('#swfupload-log li#swfupload-' + file.id).find('span.stopUpload').css('cursor','default') ;
                
                if(serverData.substring(0,6)=="error:"){
                	//失败
                	var message=serverData.substr(6);
                	item.find('div.progress').css('width', '0%');
                    item.find('span.progressvalue').text('0%');
                	item.find('p.status').html('错误:'+message);
                }else{
                	//成功
                	item.find('div.progress').css('width', '100%');
                    item.find('span.progressvalue').text('100%');
                    item.addClass('success').find('p.status').html('完成!!! ');
                	jQuery('#swfupload-log li#swfupload-' + file.id).remove();
                	//refresh table
                    var arr=eval(serverData);
                    if(!arr)
                    	return;
                    
                    _this.items.unshift({
                    	id:arr[0],
                    	name:file.name,
                    	length:file.size,
                    	operation:"add"
                    });
    //                if(_this.canceledFileIds[arr[0]])
    //                	_this.items[0].operation="interrupt";
                    _this._refresh();
                }
            })
            .bind('uploadComplete', function (event, file) {
                // upload has completed, try the next one in the queue
                jQuery(this).swfupload('startUpload');
            });
};
SwfUpload.prototype.onCreate=function(selected){
	jQuery("#swfupload-table").jqGrid({
		height:150,
		width:500,
		datatype : "local",
		//autowidth:true,
		colNames : [ '文件名', '大小', '状态','上传日期', '操作'],
		colModel : [ {
			name : 'name',
			index : 'name',
			width:200
		}, {
			name : 'lengthText',
			index : 'lengthText',
			width:30
		}, {
			name : 'state',
			index : 'state',
			width:30,
			align:'center'
		}, {
			name : 'creationDateText',
			index : 'creationDateText',
			width:70,
			align:'center'
		}, {
			name : 'op',
			index : 'op',
			width:70,
			align:'center',
			sortable:false
		} ],
		viewrecords : true,
		sortorder : "desc",
		caption : "文件列表"
	});
	
	setTimeout(function(){swfUpload._initSwf()},200);
};
SwfUpload.prototype.onOpen=function(){
	jQuery("#SWFUpload_0").css({width:"114px",height:"29px"});
	
	jQuery("#swfupload-log").empty();
	this._refresh();
};