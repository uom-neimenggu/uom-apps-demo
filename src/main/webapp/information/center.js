(function($,undefined){
    var ic_page_size=17;
    var popupFlag=false;
    var jumpFlag=false;
    $(function() {
    	var p=parseUrlParams(location.href);
    	//query cateogries
    	informationAction.queryCategoryList(function(r){
    		if(r[0]=="ok"){
    			var list=r[1];
    			for(var i=0;i<list.length;i++)
    				$("#ic-tabs ul").append("<li id='ic-"+list[i].code+"'><a href='#ic-panel-right'>"+list[i].name+"</a></li>");

    			//make tabs
    			makeVerticalTabs("#ic-tabs");
    			    			
    			//init style
    		    $("#ic-tabs .ui-tabs-nav a").click(function(){
    		    	onCategoryClicked();
    		    });
    		    
    		    //load content by url parameters
    		    if(p.type=="list"){
    		    	$("#ic-tabs .ui-tabs-nav li[id='ic-"+p.cat+"'] a").click();
    		    }else if(p.type=="article"){
    		    	$("#ic-tabs .ui-tabs-nav li.ui-tabs-selected").removeClass("ui-tabs-selected");
    		    	var li=$("#ic-tabs .ui-tabs-nav li[id='ic-"+p.cat+"']").addClass("ui-tabs-selected");
    		    	//li.parent().find(".ui-tabs-noline").removeClass("ui-tabs-noline");
    				li.prev().addClass("ui-tabs-noline");
    		    	showArticle(p.id);
    		    }else{
    		    	$("#ic-tabs .ui-tabs-nav li a").get(0).click();
    		    }
    		    
    		    //is popup portlet
    		    if(p.popup=="true")
    		    	popupFlag=true;
    		    
    		    //XXX mod for uom
    		    if(p.jump=="true")
    		    	jumpFlag=true;
    		    	$("#ic-jump-back").show().click(function(){
    		    		history.go(-1);
    		    	});
    		}
    	});
    });
    
    /**
     * 分类点击事件
     */
    function onCategoryClicked(){
    	var c=getCurrentCategory();
    	$("#ic-title-list").simpletable({
    		paginationElem:"#ic-pagination",
			header:["标题","发布时间"],
			headerWidth:["","15em"],
			pageSize:ic_page_size,
			onData : function(page,size,callback) {
				getTitleList(c[0],page,size,callback);
			}
    	});
    }
    /**
     * 分页控件事件
     */
    function onPagination(page){
    	var c=getCurrentCategory();
    	showTitleList(c[0],page+1,ic_page_size);
    	return false;
    }
    function getCurrentCategory(){
    	var div=$("#ic-tabs .ui-tabs-nav li.ui-tabs-selected");
    	var code=div.attr("id").substring(3,div.attr("id").length);
    	var name=div.text();
    	return [code,name];
    }
    /**
     * 显示文章内容
     */
    function showArticle(id){
    	informationAction.loadArticle(id,function(r){
    		if(r[0]=="ok"){
    			//fill
    			var a=r[1];
    			$("#ic-article-title").text(a.title);
    			$("#ic-article-info").text(a.releaserText+" 发表于 "+ createDateTimeStr(a.modifDate));
    			$("#ic-article-content").html(a.content);
    			$("#ic-article-attachment-list").empty();
    			if(a.attachmentList.length==0){
    				$("#ic-article-attachment").hide();
    			}else{
        			$.each(a.attachmentList,function(i,n){
        				var html="<a class='ic-attachment' attachment-id='"+n.attachmentId+"'>"+
        				n.name+"("+toReadableLength(n.length)+")</a><br/>";
        				$("#ic-article-attachment-list").append(html);
        			});
        			$("#ic-article-attachment").show();
        			$("#ic-article-attachment a.ic-attachment").click(function(){
    					var id=$(this).attr("attachment-id");
    					window.open("/uom-apps/informationAttachmentDownload.action?id="+id);
    				});
    			}
    			
    			//show
    			showOrHide("article");
    		}
    	});
    }
    
    function createDateTimeStr(ts){
    	var d=new Date(ts);
    	var r=$.fullCalendar.formatDate(d,"yyyy-MM-dd H:mm:ss");
    	return r;
    }
    /**
     * 显示文章清单
     */
    function getTitleList(cat,page,size,onUpdateTable){
    	informationAction.queryBulletinArticle(cat,null,page,size,function(r){
    		if(r[0]=="ok"){
    			var data=[];
    			var list=r[1];
    			for(var i=0;i<list.length;i++){
    				var a=list[i];
    				var title=a.title;
    				var d=new Date(a.modifDate);
    				var datetime=$.fullCalendar.formatDate(d,"yyyy年MM月dd日 HH:mm:ss");
    				var row=[];
    				row[0]={
    					attr:"style='text-align:left'",
    					tag:"<a class='ic-title' article-id='"+a.articleId+"'>"+title+"</a>"
    				};
    				row[1]={
    					attr:"style='width:15em;vertical-align:bottom;'",
    					tag:"<span class='ic-title-time'>"+datetime+"</span>"
    				};
    				data.push(row);
    			}
    			
    			onUpdateTable(data,r[2],function(){
    				showOrHide("list");
        			
    				//bind click event
        			$("#ic-title-list a.ic-title").click(function(){
    					var id=$(this).attr("article-id");
    					showArticle(id);
    				});
    			});
    		}
    	});
    }
    function showOrHide(type){
    	showNav(type);
    	if(type=="list"){
    		$("#ic-type-article").hide();
			$("#ic-type-list").show();
    	}else if(type=="article"){
    		$("#ic-type-list").hide();
			$("#ic-type-article").show();
    	}
    }
    /**
     * 导航栏
     */
    function showNav(type){
    	var nodes=[];
    	var onClicks=[];
    	var codes=[];
    	if(type=="list"){
    		nodes=["首页"];
    		onClicks=[function(e){
    			if(popupFlag||jumpFlag){
    				//Raptornuke.Popup.close(top);
    			}else
    				location.href=PortalEnv.pathContext+"home";
    		}];
    	}else if(type=="article"){
    		var cat=getCurrentCategory();
    		nodes=["首页",cat[1]];
    		codes=[null,cat[0]];
    		onClicks=[function(e){
    				if(popupFlag||jumpFlag){
    					//Raptornuke.Popup.close(top);
    				}else 
    					location.href=PortalEnv.pathContext+"home";
        		},function(e){
        			onCategoryClicked();
        		}];
    	}
    	var r=renderNav(nodes,codes,onClicks);
		$("#ic-nav").html(r[0]);
		r[1]();
    }
    /**
     * 数组对应导航节点，从左到右
     * @param nodes 文本
     * @param codes 编码，如分类编码
     * @param onClicks 点击事件响应函数 
     */
    function renderNav(nodes,codes,onClicks){
    	var html="";
    	var eventBinder;
    	for(var i=0;i<nodes.length;i++){
    		var code=codes[i]?codes[i]:"";
    		html+="<a index='"+i+"'code='"+code+"'>"+nodes[i]+"</a>";
    		if(i!=nodes.length-1)
    			html+=" &gt; ";
    	}
    	eventBinder=function(){
    		for(var i=0;i<nodes.length;i++){
    			$("#ic-nav a[index='"+i+"']").click(function(){
    				var index=$(this).attr("index");
    				if(onClicks[index])
    					onClicks[index]($(this));
    			});
    		}
    	};
    	return [html,eventBinder];
    }
})(jQuery);