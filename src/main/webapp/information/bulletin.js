var ib_page_size_normal=5;//正常公告显示信息条数
var ib_page_size_image=3;//图片新闻公告显示信息条数
var ib_page_size_rolling=3;//滚动通知公告显示信息条数
var ib_rolling_counts=100;//滚动通知信息显示条数
var ib_rolling_interval=5000;//滚动通知时间间隔
var ib_refresh_interval=1000*100;//整体刷新间隔
var ib_interval_map={};
var ib_last_rolling_map={};//用于刷新判断

function isIE6(){
	var flag=jQuery.browser.msie && /msie 6\.0/i.test(navigator.userAgent);
	return flag;
}
function isLastRollingChanged(cat,list){
	var a="";
	jq.each(list,function(i,n){
		a+=n.text;
	});
	return a!=ib_last_rolling_map[cat];
}
function setLastRolling(cat,list){
	var a="";
	jq.each(list,function(i,n){
		a+=n.text;
	});
	ib_last_rolling_map[cat]=a;
}
function registerInterval(cat,type,id){
	ib_interval_map[cat+"-"+type]=id;
}
function unregisterInterval(cat,type){
	var id=ib_interval_map[cat+"-"+type];
	if(id){
		clearInterval(id);
		ib_interval_map[cat+"-"+type]=null;
	}
}
function initBulletinUi(cat){
	var div=jq("div.ib-bulletin[cat='"+cat+"']");
	//show list
	var ul=div.find("table");
	showBulletin(ul,cat);
	
	unregisterInterval(cat,"refresh");
	//自动刷新暂时禁止
	var intId=setInterval(function(){
		showBulletin(ul,cat);
	},ib_refresh_interval);
	registerInterval(cat,"refresh",intId);
	
	//show more
	var more=div.find("div.ib-more");
	more.append("<a> 更多&nbsp;»</a>");
	more.find("a").click(function(){
		var url="infocenter";
		var param="?type=list&cat="+cat;
		//XXX mod for uom
		//parent.Raptornuke.trigger(pns+'changeState', {'tabUrl_value': '/private/guest/infocenter'});
		location.href=PortalEnv.pathContext+url+param+"&jump=true";
		//XXX mod for uom
		//popupPortlet("informationCenterPortlet_WAR_uomapps","type=list&cat="+cat+"&popup=true");
		//popupPortlet("informationCenterPortlet_WAR_uniportalplugins","type=list&cat="+cat+"&popup=true");
	});
}
function _renderNormalTitles(elem,cat,dispType,callback){
	var pageSize=9;
	if(dispType=="image"){
		pageSize=ib_page_size_image;
		jq("div.ib-bulletin[cat='"+cat+"'] div.ib-list").addClass("ib-bultype-image_");
		jq("div.ib-bulletin[cat='"+cat+"'] div.ib-top").addClass("ib-bultype-image");
	}else if(!dispType||dispType=="normal"){
		pageSize=ib_page_size_normal;
		jq("div.ib-bulletin[cat='"+cat+"'] div.ib-list").removeClass("ib-bultype-image_");
		jq("div.ib-bulletin[cat='"+cat+"'] div.ib-top").removeClass("ib-bultype-image");
		jq("div.ib-bulletin[cat='"+cat+"'] div.ib-list").removeClass("ib-bultype-rolling_");
		jq("div.ib-bulletin[cat='"+cat+"'] div.ib-top").removeClass("ib-bultype-rolling");
	}else if(dispType=="rolling"){
		pageSize=ib_page_size_rolling;
		jq("div.ib-bulletin[cat='"+cat+"'] div.ib-list").addClass("ib-bultype-rolling_");
		jq("div.ib-bulletin[cat='"+cat+"'] div.ib-top").addClass("ib-bultype-rolling");
	}
	informationAction.queryBulletinArticle(cat,"normal",1,pageSize,function(r){
		if(r[0]=="ok"){
			//fill
			var maxChars=0;
			//MOD FOR UOM，加try catch解决frame index框架下当切换到其他tab报charWdith undefined的问题
			try{
				maxChars=calculateMaxChars(elem);
			}catch(e){
				return;
			}
			var list=r[1];
			elem.empty();
			
			if(list.length==0&&(!dispType||dispType=="normal")){
				//空时显示“暂无数据”
				jq("div.ib-bulletin[cat='"+cat+"'] div.ib-list").css("display","none");
				jq("div.ib-bulletin[cat='"+cat+"'] div.ib-more").css("display","none");
				var nodata=jq("div.ib-bulletin[cat='"+cat+"'] div.ib-nodata").css("display","block");
				nodata.html("<div class='ib-nodata-content'><span>提示：查无数据</span></div>");
			}else{
				jq("div.ib-bulletin[cat='"+cat+"'] div.ib-list").css("display","block");
				jq("div.ib-bulletin[cat='"+cat+"'] div.ib-more").css("display","block");
				jq("div.ib-bulletin[cat='"+cat+"'] div.ib-nodata").css("display","none");
				for(var i=0;i<list.length;i++){
					var a=list[i];
					var title=createTitle(a.title,maxChars);
					var d=new Date(a.modifDate);
					var datetime=jq.fullCalendar.formatDate(d,"MM月dd日");
					
					elem.append("<tr><td>" +
							"<a class='ib-title' cat='"+a.categoryCode+"' article-id='"+a.articleId+"'>"+title+"</a>"+
							"&nbsp;&nbsp;<span class='ib-time'>"+datetime+"</span></td></tr>");
				}
				//bind click event
				_bindTitleClickEvent(elem.find("a.ib-title"));
			}
			
			if(callback)callback();
		}
	});
}
function _bindTitleClickEvent(elem){
	elem.click(function(){
		var id=jq(this).attr("article-id");
		var cat2=jq(this).attr("cat");
		var url= "infocenter";
		var param="?type=article&cat="+cat2+"&id="+id;
		//mod for uom
		//TODO 跳转tab方式存在问题：1.无法直接传参数，2.必须写死当前社区，如要实现在custmgr搜索关键字tabUrl_value
		location.href=PortalEnv.pathContext+url+param+"&jump=true";
		//mod for uom
		//popupPortlet("informationCenterPortlet_WAR_uomapps","type=article&cat="+cat2+"&id="+id+"&popup=true");
		//popupPortlet("informationCenterPortlet_WAR_uniportalplugins","type=article&cat="+cat2+"&id="+id+"&popup=true");
	});
}
function _getBriefText(article,maxlen){
	var result="";
	var brief=article.thumbnail.brief;
	if(!brief){
		var html=jq(article.content);
		var text=html.text();
		result=text.substring(0,maxlen);
	}else{
		result=brief.substring(0,maxlen);
	}
	return result;
}
function showBulletin(elem,cat,callback){
	informationAction.loadBulletinSetting(cat,function(r){
		if(r[0]=="error")
			return;
		var dispType=r[1].displayType;
		var pageSize;
		
		if(dispType=="image"){
			//render image news
			var div=jq("div.ib-bulletin[cat='"+cat+"']");
			var topDiv=div.find("div.ib-top");
			informationAction.queryBulletinImageArticle(cat,function(r){
	    		if(r[0]=="ok"&&r[1]!="notfound"){
	    			var article=r[1];
	    			topDiv.html(
	    					"<div class='ib-img-div'>"+
	    					"<img width='115' height='77'src='/uom-apps/informationThumbnailDownload.action?id="+article.thumbnail.thumbnailId+"'/>"+
	    					"</div>"+
	    					"<div class='ib-img-title' cat='"+cat+"' article-id='"+article.articleId+"'></div>"+
	    					"<div class='ib-img-brief'></div>"
	    				);
	    			//fill title
	    			var titleDiv=topDiv.find("div.ib-img-title");
	    			var maxlen=0;
	    			//MOD FOR UOM，加try catch解决frame index框架下当切换到其他tab报charWdith undefined的问题
	    			try{
	    				maxlen=calculateMaxChars2(titleDiv);
	    			}catch(e){
	    				return;
	    			}
	    			titleDiv.text(createTitle(article.title,maxlen));
	    			
	    			//fill brief
	    			var briefDiv=topDiv.find("div.ib-img-brief");
	    			//MOD FOR UOM
	    			try{
	    				maxlen=calculateMaxChars3(briefDiv);
	    			}catch(e){
	    				return;
	    			}
	    			briefDiv.html(_getBriefText(article,maxlen)+
	    					"&nbsp;..."+
	    					"&nbsp;&nbsp;[<a class='ib-detail' cat='"+cat+"' article-id='"+article.articleId+"'>详细</a>]"
		    					);
	
		    			//bind event
		    			_bindTitleClickEvent(topDiv.find("div.ib-img-title"));
		    			_bindTitleClickEvent(topDiv.find("a.ib-detail"));
		    		}else if(r[0]=="ok"&&r[1]=="notfound"){
		    			topDiv.empty();
		    			dispType="normal";
		    		}
	    		_renderNormalTitles(elem,cat,dispType,callback);
			});
		}else if(dispType=="rolling"){
			//render rolling notifications
			var div=jq("div.ib-bulletin[cat='"+cat+"']");
			var topDiv=div.find("div.ib-top");
			var maxlen=0;
			//MOD FOR UOM
			try{
				maxlen=calculateMaxChars4(topDiv);
			}catch(e){
				return;
			}
			var px=calculateTextWidth4(topDiv);
			informationAction.queryBulletinRollingList(cat,maxlen,ib_rolling_counts,function(r){
	    		if(r[0]=="ok"&&r[1].length>0){
	    			var list=r[1];
	    			
	    			if(isLastRollingChanged(cat,list)){
    	    			topDiv.html(//"<span><img class='ib-rolling-icon' src='/uom-apps/information/images/speacker.png'/></span>"+
    	    					"<span class='ib-rolling-container' n='"+list.length+"'></span>" +
    	    					"<span><img class='ib-rolling-left' src='/uom-apps/information/images/left.png'/></span>"+
    	    					"<span><img class='ib-rolling-right' src='/uom-apps/information/images/right.png'/></span>"
    	    					);
    	    			
    	    			setLastRolling(cat, list);
    	    			//fill title
    	    			jq.each(list,function(i,n){
    	    				n.title=createTitle(n.text,maxlen);
    	    			});
    	    			var contDiv=topDiv.find("span.ib-rolling-container");
    	    			var maxWidth=0;
    	    			jq.each(list,function(i,n){
    	    				var style;
    	    				//if(n.type=="title")
    	    					cl="ib-rolling-title";
    	    				//else if(n.type=="content")
    	    					//cl="ib-rolling-content";
    	    				contDiv.append("<div seq='"+i+"' style='display:none' class='"+cl+"' " +
    	    						"cat='"+cat+"' article-id='"+n.articleId+"'>"+n.text+"</div>");
    	    				
    	    			});
    	    			contDiv.find("div.ib-rolling-title").css("width",px);
    	    			contDiv.css("width",px);
    	    			contDiv.find("div:first").show();
    	    			
    	    			//set rolling
    	    			unregisterInterval(cat,"rolling");
    	    			//自动刷新暂时禁止
    	    			var intId=setInterval(function(){
    	    				rollTitle(cat,1);
    	    			},ib_rolling_interval);
    	    			registerInterval(cat,"rolling",intId);
    	    			
    	    			//bind event
    	    			_bindTitleClickEvent(topDiv.find("div.ib-rolling-title"));
    	    			topDiv.find("img.ib-rolling-left").click(function(){
    	    				rollTitle(cat,1);
    	    			});
    	    			topDiv.find("img.ib-rolling-right").click(function(){
    	    				rollTitle(cat,-1);
    	    			});
	    			}
	    		}else if(r[0]=="ok"&&r[1].length==0){
	    			topDiv.empty();
	    			dispType="normal";
	    		}
	    		_renderNormalTitles(elem,cat,dispType,callback);
			});
		}else{
			//direct render normal titles
			_renderNormalTitles(elem,cat,dispType,callback);
		}
	});
}
/**
 * 滚动通知标题滚动
 * @param direction 1/-1
 */
function rollTitle(cat,direction){
	var contDiv=jq("div.ib-bulletin[cat='"+cat+"'] span.ib-rolling-container");
	if(contDiv.prop("flag"))
		return;
	contDiv.prop("flag",1);
	var length=contDiv.attr("n");
	var curDiv=contDiv.find("div:visible");
	var seq=curDiv.attr("seq");
	//seq=(length+seq+direction)%length;//FIXME not work
	if(direction>0){
		if(++seq==length)
			seq=0;
	}else{
		if(--seq==-1)
			seq=length-1;
	}
	var nextDiv=contDiv.find("div[seq='"+seq+"']");
	curDiv.hide("slide",{direction:direction>0?"left":"right"},500,function(){
		contDiv.prop("flag",0);
	});
	nextDiv.show("slide",{direction:direction>0?"right":"left"},500,function(){
		contDiv.prop("flag",0);
	});
}
/*
 * 计算标题的最大字符数（标题本身）
 */
function calculateMaxChars(ul){
	var width=ul[0].parentNode.offsetWidth;
	var charWidth=ul.css("font-size");
	charWidth=charWidth.substring(0,charWidth.length-2);//2 is 'px'
	charWidth=parseFloat(charWidth);
	var n=Math.floor(parseFloat(width)/charWidth)-6;//6 is length of "... xx月xx日 xx:xx"
	return n;
}
/*
 * 计算图片新闻标题的最大字符数（标题本身）
 */
function calculateMaxChars2(elem){
	var width=elem[0].offsetWidth-145;//145 is left , include image and spaces
	var charWidth=elem.css("font-size");
	charWidth=charWidth.substring(0,charWidth.length-2);//2 is 'px'
	charWidth=parseFloat(charWidth);
	var n=Math.floor(parseFloat(width)/charWidth)-2;//2 is length of "..."
	return n;
}
/*
 * 计算图片新闻简介的最大字符数（简介本身）
 */
function calculateMaxChars3(elem){
	var width=elem[0].offsetWidth-145;//145 is left , include image and spaces
	var charWidth=elem.css("font-size");
	charWidth=charWidth.substring(0,charWidth.length-2);//2 is 'px'
	charWidth=parseFloat(charWidth);
	var n=Math.floor(parseFloat(width)/charWidth)*2-5;//2 is line counts,5 is length of "...  [详细]"
	return n;
}
/*
 *计算滚动通知标题最大字符数 
 */
function calculateMaxChars4(elem){
	var width=calculateTextWidth4(elem);
	var charWidth=elem.css("font-size");//TODO 得到14.4值会比实际字宽大，因此标题字数会偏少而出现空白
	charWidth=charWidth.substring(0,charWidth.length-2);//2 is 'px'
	charWidth=parseFloat(charWidth);
	var n=Math.floor(parseFloat(width)/charWidth)-2;//2 is length of "..."
	return n;
}
function calculateTextWidth4(elem){
	var width=elem[0].offsetWidth-100;//100 is include image and spaces
	return width;
}
function createTitle(title,maxchars){
	if(title.length<=maxchars+1)//1 is length of "..." , maxchars do not include ...
		return title;
	var t=title.substring(0,maxchars);
	return t+"...";
}
