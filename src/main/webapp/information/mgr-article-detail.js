/* @author 曾臻
 * @date 2012-11-27
 * 
 * 改动日志：
 * 2013-02-01：新增一种特殊分类“图片新闻”，显示大图片。
 * 切换后“展现方式”、“内容编辑”等字段将被隐藏，也不需要校验。
 * 考虑将来是否将分类作为一个水平tabs，下面的页面作为内嵌jsp，图片新闻的单独做个页面，独立维护。
 * 
 * 2013-04-09：附件尝试改用Alfresco方式，修改之处标记为ALF
 * 2013-06-09：ALF已回滚，注释保留
 */
(function($,undefined){
    var editor;
    
    registerInitFunc("release", function() {
    	initUi();
    });

    /**
     * 界面初始化，只运行一次
     * 
     * @author 曾臻
     * @date 2012-11-26
     */
    function initUi() {
    	//editor
    	imad.editor=editor = KindEditor.create('#imad-content', {
    		width : 'auto',
    		height : '380px',
    		resizeType : 0,
    		uploadJson : '/uom-apps/informationImageUpload.action'
    	});
    
    	//buttons
    	$("button").button();
    	$("#imad-reset").click(function() {
    		reset();
    	});
    	$("#imad-saveAsDraft").click(function() {
    		saveAsDraft();
    	});
    	$("#imad-release").click(function() {
    		release();
    	});
    	$("#imad-add").click(function() {
    		newArticle();
    	});
    
    	//date inputs
    	$("#imad-effectiveDate").click(function() {
    		setDate(this);
    
    		//TODO temp resolve css conflict
    		$("#ui-datepicker-div").addClass("css-fix")
    	});
    	$("#imad-expiredDate").click(function() {
    		setDate(this);
    
    		//TODO temp resolve css conflict
    		$("#ui-datepicker-div").addClass("css-fix")
    	});
    	
    	//target orgs
    	$("#imad-target-orgs-select").click(function(){
    		var orgs=imad._readTargetOrgs();
    		var ids=imad._filterTargetOrgId(orgs);
    		orgSelector.showDialog($("#imad-org-selector-container"),ids,function(r){
    			imad._writeTargetOrgsText(r);
    			imad._writeTargetOrgsId(r);
    		});
    	});
    
    	//attachments
    	$("#imad-attachments-select").click(function(){
    		var alist=imad._readAttachments();
    		jQuery.each(alist,function(i,n){
    			n.id=n.attachmentId;
    		});
    		swfUpload.showDialog(
    				$("#imad-attachments-container"),
    				//ALF
    				"/uom-apps/informationAttachmentUpload.action",
    				//"/uniportal-ecms/attachmentUpload.action?module=information",
    				alist,
    				function(r){
            			jQuery.each(r,function(i,n){
            				//ALF
            				n.attachmentId=n.id;
            				//n.alfId=n.id;
            			});
        			
            			//delete temp attachments "add-remove" on ok
            			imad._deleteTempAttachments(r,"add-remove");
            			
            			imad._writeAttachments(r);
            		},
            		function(r){
            			jQuery.each(r,function(i,n){
            				//ALF
            				n.attachmentId=n.id;
            				//n.alfId=n.id;
            			});
            			//delete temp attachments "add" on cancel
            			imad._deleteTempAttachments(r,"add");
            		}
            	);
    	});
    	
    	//display type select
    	$("#imad-display-type").click(function(){
    		showOrHide();
    	});
    	
    	//thumbnail img upload
    	$('#imad-thumbnail-su').swfupload({
            upload_url:"/uom-apps/informationThumbnailUpload.action?jsessionid="+$("#imad-session-id").val(),
            file_post_name:'uploadfile',
            file_size_limit:"10 MB",
            file_types:"*.jpg;*.jpeg;*.gif;*.png;*.bmp",
            file_types_description:"All files",  
            flash_url:"/uom-apps/public/swfupload/js/swfupload.swf",
            button_image_url:'/uom-apps/public/swfupload/js/swfupload/choose_pic_114x29.png',
            button_width:114,
            button_height:29,
            button_placeholder:$('#imad-thumbnail-su-btn')[0],
            button_cursor:SWFUpload.CURSOR.HAND
        }).bind('fileQueued', function (event, file) {
                $(this).swfupload('startUpload');
        }).bind('fileQueueError', function (event, file, errorCode, message) {
            alert(errorCode+":"+message)
        }).bind('uploadSuccess', function (event, file, serverData) {
        	var result=eval("("+serverData+")");
        	if(result.error==1){
        		showErrorDlg($(document.body), result.message);
        	}else{
        		imad._writeThumbnailImg(result.id);
        	}
        });
    	
    	
    	//init select options
    	initCategorySel("#imad-category",function(){
    		//初始化默认为“新增”模式
			newArticle();
    	});
    	
    	//图片新闻定制
    	$("#imad-category").change(function(){
    		showOrHide();
    	});
    	
    }
    function showOrHide(){
    	imad._showOrHide();
    }
    function newArticle() {
    	imad.newArticle();
    }
    /**
     * 点击重置按钮后
     */
    function reset(){
    	//delete temp attachments "add" on reset
    	var alist=imad._readAttachments();
		imad._deleteTempAttachments(alist,"add");
    	
    	var d=new Date();
    	d.setYear(d.getFullYear()+5);
//    	var detail = {
//    		articleId : parseInt($("#imad-articleId").val()),
//    		title : "",
//    		categoryCode : null,
//    		effectiveDate : new Date().getTime(),
//    		expiredDate : d.getTime(),
//    		content : "",
//    		targetOrgList:imad._readTargetOrgs(),
//    		attachmentList:imad.articleInit.attachmentList
//    	};
//    	writeArticleDetail(detail);
    	writeArticleDetail(imad.articleInit);
    }
    /**
     * 发布成功后
     */
    function reset2(){
    	var d=new Date();
    	d.setMonth(d.getMonth()+1);
    	var detail = {
    		articleId : 0,
    		title : "",
    		categoryCode : null,
    		effectiveDate : new Date().getTime(),
    		expiredDate : d.getTime(),
    		content : "",
    		targetOrgList:imad._readTargetOrgs(),
    		attachmentList:[]
    	};
    	writeArticleDetail(detail);
    }
    function release() {
    	var self=this;
    	var id = parseInt($("#imad-articleId").val());
    	var detail = readArticleDetail();
    	detail.state = "released";
    	doValidate(detail, function() {
    		if (!id){
    			informationAction.addArticle(detail, function(r) { 
    				if (r[0] == "ok") {
    					showMessageDlg($(document.body), "发布成功。");
    					reset2();
    				} else if (r[0] == "error") {
    					showErrorDlg($(document.body), "发布失败。");
    				}
    			});
    		}
    		else
    			informationAction.updateArticle(detail, function(r) {
    				if (r[0] == "ok") {
    					showMessageDlg($(document.body), "发布成功。");
    				} else if (r[0] == "error") {
    					showErrorDlg($(document.body), "发布失败。");
    				}
    			});
    	});
    }
    function saveAsDraft() {
    	var self=this;
    	var id = parseInt($("#imad-articleId").val());
    	var detail = readArticleDetail();
    	detail.state = "draft";
    	doValidate(detail, function() {
    		if (!id){
    			informationAction.addArticle(detail, function(r) {
    				if (r[0] == "ok") {
    					showMessageDlg($(document.body), "保存成功。");
    					//保存成功，转换成编辑模式
    					$("#imad-articleId").val(r[1]);
    					imad.setMode("edit");
    					
    				} else if (r[0] == "error") {
    					showErrorDlg($(document.body), "保存失败。");
    				}
    			});
    		}else
    			informationAction.updateArticle(detail, function(r) {
    				if (r[0] == "ok") {
    					showMessageDlg($(document.body), "发布成功。");
    				} else if (r[0] == "error") {
    					showErrorDlg($(document.body), "发布失败。");
    				}
    			});
    	});
    }
    function writeArticleDetail(detail) {
    	imad.writeArticleDetail(detail);
    }
    function readArticleDetail() {
    	var detail = {
    		articleId : parseInt($("#imad-articleId").val()),
    		title : $("#imad-title").val(),
    		categoryCode : $("#imad-category").val(),
    		effectiveDate : datestr2timestamp($("#imad-effectiveDate").val()),
    		expiredDate : datestr2timestamp($("#imad-expiredDate").val()),
    		content : editor.html(),
    		targetOrgList:imad._readTargetOrgs(),
    		attachmentList:imad._readAttachments(),
    		displayType:$("#imad-display-type").val(),
    		thumbnail:imad._readThumbnail()
    	};
    	return detail;
    }
    function doValidate(detail, onPass) {
    	var r = validate(detail);
    	if (r) {
    		showErrorDlg($(document.body), r);
    		return;
    	} else {
    		if (onPass)
    			onPass();
    	}
    }
    function validate(detail) {
    	var cat=$("#imad-category").val();
    	if (!detail.title) {
    		return "标题不能为空！";
    	} else if (!detail.effectiveDate) {
    		return "生效日期不能为空！";
    	} else if (!detail.expiredDate) {
    		return "失效日期不能为空！";
    	}else if(detail.expiredDate<=detail.effectiveDate){
    		return "失效日期不能小于生效日期！";
    	}else if (!detail.content) {
    		if(cat!="image")
    			return "内容不能为空！";
    	}else if(!detail.targetOrgList||detail.targetOrgList.length==0){
    		return "目标组织必须选择！";
    	}else if(detail.displayType=="image"||cat=="image"){
    		if(!detail.thumbnail){
    			return "请上传缩略图！";
    		}else if(detail.thumbnail.brief.length>500){
    			return "简介长度不能大于500！";
    		}
    	}
    		
    	return "";
    }
})(jQuery);

function ArticleDetail() {
}
var imad = new ArticleDetail();
/**
 * 信息初始状态，用于重置
 */
ArticleDetail.prototype.articleInit={}
ArticleDetail.prototype.editor={};
ArticleDetail.prototype.writeArticleDetail=function(detail){
	var _this=this;
	if (!detail.articleId) {
		jQuery("#imad-release").show();
		jQuery("#imad-saveAsDraft").show();
	} else {
		jQuery("#imad-release").show();
		jQuery("#imad-saveAsDraft").show();
	}

	jQuery("#imad-articleId").val(detail.articleId);
	jQuery("#imad-title").val(detail.title);
	if(detail.categoryCode){
		//jQuery("#imad-category").val(detail.categoryCode);//ie6 has problem
		selectOption("#imad-category",detail.categoryCode,function(){
			_this._showOrHide();
			jQuery("#imad-all").show();//workround the ff blanking problem
		});//ie6 no problem but firefox will blank screen
	}
	jQuery("#imad-effectiveDate").val(timestamp2datestr(detail.effectiveDate));
	if(detail.expiredDate)
		jQuery("#imad-expiredDate").val(timestamp2datestr(detail.expiredDate));
	this.editor.html(detail.content);
	
	this._writeTargetOrgs(detail.targetOrgList);
	var ids=this._filterTargetOrgId(detail.targetOrgList);
	orgTreeAction.queryNodesBatch(ids,function(r){
		if(r[0]=="ok"){
			_this._writeTargetOrgsText(r[1]);
		}
	});
	
	this._writeAttachments(detail.attachmentList);
	
	jQuery("#imad-display-type").val(detail.displayType);
	jQuery("#imad-display-type").click();
	
	this._writeThumbnail(detail.thumbnail);
}
ArticleDetail.prototype._showOrHide=function(){
	var cat=jQuery("#imad-category").val();
	if(cat=="image"){
		jQuery("#imad-field-content").hide();
		jQuery("#imad-field-attachment").hide();
		jQuery("#imad-field-display-type").hide();
		jQuery("div[name='imad-display-type-image']").show();
	}else{
		jQuery("#imad-field-content").show();
		jQuery("#imad-field-attachment").show();
		jQuery("#imad-field-display-type").show();
		var v=jQuery("#imad-display-type").val();
		if(v=="image"){
			jQuery("div[name='imad-display-type-image']").show();
		}else{
			jQuery("div[name='imad-display-type-image']").hide();
		}
	}
}
ArticleDetail.prototype.newArticle=function() {
	var d=new Date();
	d.setMonth(d.getMonth()+1);
	//初始内容在这里配置
	var detail = {
		articleId : 0,
		title : "",
		categoryCode : "bulletin",//临时需求
		//categoryCode:"image",//临时需求
		effectiveDate : new Date().getTime(),
		expiredDate: d.getTime(),
		content : "",
		targetOrgList:[],
		attachmentList:[],
		diaplayType:"normal",
		thumbnail:null
	};
	//临时需求begin  抽奖临时需求，直属分公司及其下属
//	var orgs=[16882,16710,16722,16726,16730,16734,16714,16718,16738,16934,16622,16646,16642,16634,16638,16626,16630,16890,16802,16830,16826,16822,16818,16814,16810,16806,16894,17290,17298,17294,16910,16914];
//	jQuery.each(orgs,function(i,n){
//		detail.targetOrgList.push({
//			orgId:n,
//			type:3
//		});
//	})
	//临时需求end
	
	
	this.writeArticleDetail(detail);
	this.setMode("add");
	this.articleInit=jQuery.extend(true,{},detail);
}
ArticleDetail.prototype.editArticle=function(a){
	this.writeArticleDetail(a);
	this.setMode("edit");
	this.articleInit=jQuery.extend(true,{},a);
}
ArticleDetail.prototype.setMode=function(mode){
	if(mode=="edit"){
		jQuery("#imad-mode-text").text("编辑信息");
	}else if(mode=="add"){
		jQuery("#imad-mode-text").text("发布信息");
	}
}
ArticleDetail.prototype._readTargetOrgs=function(){
	/*
	 * TODO 目前默认所有目标组织的类型为3，即同时发布到公开和私有社区（企业门户除外）
	 */
	var isPublic=jQuery("#imad-target-orgs-public").prop("checked");
	var text=jQuery("#imad-target-orgs-id").val();
	//var text2=jQuery("#imad-target-orgs-type").val();
	var ids=text?text.split(","):[];
	//var types=text2?text2.split(","):[];
	var result=[];
	jQuery.each(ids,function(i){
		result.push({orgId:ids[i],type:ids[i]==-1?1:3});//企业门户id=-1
	});
	
	//“公开”打勾表示发布到登录页面，即guest的公开社区
	var node;
	jQuery.each(result,function(i,n){
		if(n.orgId==-1){
			node=n;
			flag=true;
			return false;
		}
	});
	if(node){
		if(isPublic)
			node.type=node.type|2;
		else
			node.type=node.type&0xfd;
	}else if(isPublic){
		result.push({orgId:-1,type:2});
	}
	
	return result;
}
ArticleDetail.prototype._writeTargetOrgs=function(nodes){
	/*
	 *TODO 同上，默认目标组织类型为3 
	 */
	var isPublic=false;
	var ids="";
	//var types="";
	jQuery.each(nodes,function(i,n){
		if(n.orgId==-1&&(n.type&2)>0)
			isPublic=true;
		if(n.orgId!=-1||(n.type&1)>0){
			ids+=n.orgId+",";
		}
		//type+=n.type+",";
	});
	
	jQuery("#imad-target-orgs-public").prop("checked",isPublic);
	ids=ids.substring(0,ids.length-1);
	//types=types.substring(0,types.length-1);
	jQuery("#imad-target-orgs-id").val(ids);
	//jQuery("#imad-target-orgs-type").val(types);
}
ArticleDetail.prototype._writeTargetOrgsId=function(nodes){
	var ids="";
	jQuery.each(nodes,function(i,n){
		ids+=n.id+",";
	});
	ids=ids.substring(0,ids.length-1);
	jQuery("#imad-target-orgs-id").val(ids);
}
ArticleDetail.prototype._writeTargetOrgsText=function(nodes){
	var text="";
	for(var i=0;i<nodes.length;i++){
		//支持类型为{id,name}和{id,label}
		text+=(nodes[i].name?nodes[i].name:nodes[i].label)+"; ";
	}
	jQuery("#imad-target-orgs").val(text);
	jQuery("#imad-target-orgs").attr("title",text);
}
/**
 * 过滤出目标组织id（排除“公开打勾”而“福建不打勾”的情况）
 */
ArticleDetail.prototype._filterTargetOrgId=function(orgs){
	var ids=[];
	jQuery.each(orgs,function(i,n){
		if(n.orgId==-1&&(n.type&1)>0){
			ids.push(n.orgId);
		}else if(n.orgId!=-1){
			ids.push(n.orgId);
		}
	});
	return ids;
}
ArticleDetail.prototype._readAttachments=function(){
	var data=jQuery("#imad-attachments-ops").data("array");
	return data;
}
ArticleDetail.prototype._writeAttachments=function(data){
	var _this=this;
	jQuery("#imad-attachments-ops").data("array",data);
	var t="";
	jQuery.each(data,function(i,n){
		if(!n.operation||n.operation=="add")
			t+=n.name+"("+swfUpload.toReadableLength(n.length)+"); ";
	});
	jQuery("#imad-attachments-text").val(t);
	jQuery("#imad-attachments-text").attr("title",t);
}
ArticleDetail.prototype._readThumbnail=function(){
	var id=jQuery("#imad-thumbnail-id").val();
	if(!id)
		return null
	var t={
		thumbnailId:id,
		brief:jQuery("#imad-brief").val()
	};
	return t;
}
ArticleDetail.prototype._writeThumbnail=function(t){
	if(!t){
		jQuery("#imad-thumbnail-id").val("");
		jQuery("#imad-thumbnail-img").removeAttr("src");
		jQuery("#imad-brief").val("");
	}else{
		this._writeThumbnailImg(t.thumbnailId);
		jQuery("#imad-brief").val(t.brief);
	}
}
ArticleDetail.prototype._writeThumbnailImg=function(id){
	jQuery("#imad-thumbnail-id").val(id);
	jQuery("#imad-thumbnail-img").attr("src",
			"/uom-apps/informationThumbnailDownload.action?id="+id
		);
}
ArticleDetail.prototype._deleteTempAttachments=function(alist,onOperation){
	var ids=[];
	jQuery.each(alist,function(i,n){
		if(n.operation==onOperation)
			ids.push(n.attachmentId);
	});
	//ALF
	informationAction.deleteTempAttachments(ids,function(r){
		if (r[0] == "error") {
			showErrorDlg($(document.body), "清除临时附件时遇到错误。");
		}
	});
//	attachmentAction.deleteFiles(ids,function(r){
//		if (r[0] == "error") {
//			showErrorDlg2("清除临时附件时遇到错误。");
//		}
//	});
}

