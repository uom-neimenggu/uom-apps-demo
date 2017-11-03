(function(jQuery, undefined) {
	registerInitFunc("list", function() {
		initUi();
	});
    registerOnActiveFunc("list",function(){
    	jQuery("#imal-query").click();
    });
	
	function initUi() {
		//buttons
    	jQuery("button").button();
    	jQuery("#imal-query").click(function() {
    		var t=jQuery("#imal-table");
    		var page=t.getGridParam("page");
    		var size=t.getGridParam("rowNum");
    		queryPage(page,size);
    	});
    	jQuery("#imal-reset").click(function() {
    		reset();
    	});
    	jQuery("#imal-add").click(function() {
    		onAdd();
    	});
    	jQuery("#imal-batch-remove-release").click(function() {
    		onBatchRemoveRelease();
    	});
    	jQuery("#imal-batch-remove").click(function() {
    		onBatchRemove();
    	});
    	jQuery("#imal-batch-release").click(function() {
    		onBatchRelease();
    	});
    	
    	//date inputs
    	jQuery("#imal-begin-date").click(function() {
    		setDate(this);
    
    		//TODO temp resolve css conflict
    		jQuery("#ui-datepicker-div").addClass("css-fix")
    	});
    	jQuery("#imal-end-date").click(function() {
    		setDate(this);
    
    		//TODO temp resolve css conflict
    		jQuery("#ui-datepicker-div").addClass("css-fix")
    	});
    	
    	//selects
    	jQuery("#imal-category").append("<option value='' selected>不限</option>");
    	initCategorySel("#imal-category",function(){
    		selectOption("#imal-category","");
    	});
    	
    	//table
    	jQuery("#imal-table").jqGrid({
    		loadui:"enable",
    		height:1200,//310,
    		multiselect:true,
			jsonReader : {
			  repeatitems: false
			},
			datatype : onData,
			autowidth:true,
			//sortname:'creationDate',
			colNames : ['标识', '发布人', '标题', '分类','展现方式', '状态','创建日期', '生效日期','失效日期','操作',''],
			colModel : [ {
				name:'articleId',
				hidden:true
			},{
				name : 'releaserText',
				index : 'releaser',
				width:35,
				align:'center'
			}, {
				name : 'title',
				index : 'title',
				width:100
			}, {
				name : 'categoryText',
				index : 'category_code',
				width:30,
				align:'center'
			}, {
				name : 'displayTypeText',
				index : 'display_type',
				width:30,
				align:'center'
			}, {
				name : 'stateText',
				index : 'state',
				width:25,
				align:'center'
			}, {
				name : 'creationDate',
				index : 'creation_date',
				width:43,
				align:'center'
			}, {
				name : 'effectiveDate',
				index : 'effective_date',
				width:43,
				align:'center'
			}, {
				name : 'expiredDate',
				index : 'expired_date',
				width:43,
				align:'center'
			}, {
				name : 'op',
				index : 'op',
				width:70,
				align:'center',
				sortable:false
			} , {
				name : 'state',
				index : 'state',
				hidden:true
			} ],
			rowNum : 50,
			rowList : [ 15,30 ,50,100],
			pager : '#imal-pager',
			viewrecords : true,
			sortorder : "desc",
			caption : "文章列表"
		});
	};
	function onData(d){
		var t=jQuery("#imal-table");
		queryPage(d.page,d.rows,d.sidx,d.sord);
	}
	function queryPage(curPage,pageSize,sort,order){
		var cond={
				title:jQuery("#imal-title").val(),
				categoryCode:jQuery("#imal-category").val(),
				beginDate:datestr2timestamp(jQuery("#imal-begin-date").val()),
				endDate:datestr2timestamp(jQuery("#imal-end-date").val()),
				displayType:jQuery("#imal-disp-type").val()
		};
		informationAction.queryArticle(cond,curPage,pageSize,sort,order,function(r){
			if(r[0]=="ok"){
				var t=jQuery("#imal-table");
				var records=r[2];
				var maxPage=Math.ceil(r[2]/pageSize);
				var curPage2=curPage>maxPage?maxPage:curPage;
				var list=r[1];
				var rows=[];
				for(var i=0;i<list.length;i++){
					rows[i]=list[i];
					rows[i].id=list[i].articleId;
					rows[i].creationDate=formatDate(new Date(list[i].creationDate));
					rows[i].effectiveDate=formatDate(new Date(list[i].effectiveDate));
					rows[i].expiredDate=formatDate(new Date(list[i].expiredDate));
					rows[i].title=jQuery('<div/>').text(list[i].title).html();
				}
				t.clearGridData();
				t[0].addJSONData({
					"page":curPage2,
					"total":maxPage,
					"records":records, 
					"rows":rows
				});
				makeOperationButtons(t);
			}
		});
	}
	function reset(){
		jQuery("#imal-title").val("");
		jQuery("#imal-category").val(""),
		jQuery("#imal-begin-date").val(""),
		jQuery("#imal-end-date").val("")
	}
	function onRemove(id){
		showConfirmDlg(jQuery(document),"确认要删除这条信息吗？","删除确认",function(){
			informationAction.updateArticleState(id,"deleted",function(r){
				if(r[0]=="ok"){
					refresh();
				}
			})	
		});
	}
	function onRemoveRelease(id){
		informationAction.updateArticleState(id,"draft",function(r){
			if(r[0]=="ok"){
				refresh();
			}
		})
	}
	function onEdit(id){
		jQuery("#im-tabs a[href='#im-tab-release']").click();
		informationAction.loadArticle(id,function(r){
			var a=r[1];
			imad.editArticle(a);
		});
	}
	function onRelease(id){
		informationAction.updateArticleState(id,"released",function(r){
			if(r[0]=="ok"){
				refresh();
			}
		})
	}
	function onAdd(){
		jQuery("#im-tabs a[href='#im-tab-release']").click();
		imad.newArticle();
	}
	function onBatchRemoveRelease(){
		_batchUpdateState("released","draft");
	}
	function onBatchRemove(){
		_batchUpdateState("draft","deleted","确认要删除发布已选中的信息吗？","删除确认");
	}
	function onBatchRelease(){
		_batchUpdateState("draft","released");
	}
	function _batchUpdateState(expectedState,newState,message,title){
		var func=function(expectedState,newState){
    		var table=jQuery("#imal-table");
    		var ids=getCheckedId();
    		var map={};
    		jQuery.each(ids,function(i,n){
    			map[n]=true;
    		});
    		var arr=table.getRowData();
    		var filter=[];
    		jQuery.each(arr,function(i,n){
    			if(map[n.articleId]!=true)
    				return true;
    			if(n.state!=expectedState)
    				return true;
    			filter.push(n.articleId);
    		});
    		informationAction.updateArticleStateBatch(filter,newState,function(r){
    			if(r[0]=="ok"){
    				refresh();
    			}
    		});
		};
		if(message){
			showConfirmDlg(jQuery(document),message,title,function(){
	    		func(expectedState,newState);
			});
		}else{
			func(expectedState,newState);
		}
	}
	function refresh(){
		jQuery("#imal-table").trigger("reloadGrid");
	}
	function makeOperationButtons(table){
		var arr=table.getRowData();
		var ids=table.getDataIDs();
		for(var i=0;i<arr.length;i++){
			var row=arr[i];
			var id=ids[i];
			var removeRelease="<a class='row-btn-remove-release' article-id='"+id+"'>删除发布</a>";
			var edit="<a class='row-btn-edit' article-id='"+id+"'>编辑</a>";
			var release="<a class='row-btn-release' article-id='"+id+"'>正式发布</a>";
			var remove="<a class='row-btn-remove' article-id='"+id+"'>删除</a>";
			//var recovery="<a class='row-btn-recovery' article-id='"+id+"'>恢复</a>";
			var html="";
			if(row.state=="draft"){
				html=release+"&nbsp;"+edit+"&nbsp;"+remove;
			}else if(row.state="released"){
				html=removeRelease;
			}/*else if(row.state="deleted"){
				html=recovery;
			}*/
			table.setRowData(id,{op:html});
		}
		table.find(".row-btn-remove-release").click(function(){
			var aid=jQuery(this).attr("article-id");
			onRemoveRelease(aid);
		});
		table.find(".row-btn-edit").click(function(){
			var aid=jQuery(this).attr("article-id");
			onEdit(aid);
		});
		table.find(".row-btn-release").click(function(){
			var aid=jQuery(this).attr("article-id");
			onRelease(aid);
		});
		table.find(".row-btn-remove").click(function(){
			var aid=jQuery(this).attr("article-id");
			onRemove(aid);
		});
		/*table.find(".row-btn-recovery").click(function(){
			var aid=jQuery(this).attr("article-id");
			onRecovery(aid);
		});*/
	}
	function getCheckedId(){
		var ids=[];
		jQuery("#imal-table tr input:checked[type='checkbox']").each(function(){
			var e=jQuery(this).closest("tr");
			ids.push(e[0].id);
		});
		return ids;
	}
})(jQuery);