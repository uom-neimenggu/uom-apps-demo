/**
 * @author 曾臻
 * 依赖：
 * OrgTreeAction.java
 * OrgTreeNodeVo.java
 * org-selector.css
 * tree.jquery.js
 * jqtree.css
 * jquery ui dialog
 * jquery ui button
 */

function OrgSelector(){
}
var orgSelector=new OrgSelector();
OrgSelector.prototype.showDialog=function(container,selected,onOk,onCancel){
	var html='<div id="os-dlg" style="display:none;padding-bottom:60px;padding-left:10px">'+
	'<table height="100%">'+
		'<tr>'+
		'<td width="50%" style="vertical-align:top;">'+
			'<label>可选择：</label>'+
			'<div style="overflow:auto;height:270px;width:230px;border:2px inset #aaa;_position:relative">'+
			'<div id="os-tree"></div>'+
			'</div>'+
		'</td>'+
		'<td id="os-btns" style="padding:0 1em">'+
			'<button id="os-add" class="w8em">添&#160;&#160;&#160;加&#160;&#160;&gt;</button>'+
			'<div class="h1em"/>'+
			'<button id="os-remove" class="w8em">&lt;&#160;&#160;删&#160;&#160;&#160;除</button>'+
			'<div class="h2em"/>'+
			'<button id="os-add-all" class="w8em">全部添加&#160;&gt;</button>'+
			'<div class="h1em"/>'+
			'<button id="os-remove-all" class="w8em">&lt;&#160;全部删除</button>'+
		'</td>'+
		'<td width="50%" style="vertical-align:top;">'+
			'<label>已选中：</label>'+
			'<div style="overflow:auto;height:270px;width:230px;border:2px inset #aaa;_position:relative">'+
			'<div id="os-added"></div>'+
			'</div>'+
		'</td>'+
		'</tr>'+
		'<tr>'+
		'<td>'+
			'<div style="margin:0.5em 0;">'+
				'<input id="os-include-sub-add" type="checkbox" style="vertical-align:middle;"/>'+
				'<label for="os-include-sub-add">添加时自动包含下级组织</label>'+
			'</div>'+
		'</td>'+
		'<td></td>'+
		'<td>'+
			'<div style="margin:0.5em 0;">'+
				'<input id="os-include-sub-del" type="checkbox" style="vertical-align:middle;"/>'+
				'<label for="os-include-sub-del">删除时自动包含下级组织</label>'+
			'</div>'+
		'</td>'+
		'</tr>'+
	'</table>'+
	'</div>';
	if(jQuery("#os-dlg").length==0){
		container.append(html);
		
	}
	
	var self=this;
	var dlg=jQuery("#os-dlg");
	dlg.dialog({
		title:"请选择目标组织",
		modal:true,
		resizable:false,//TODO style problem
		height:420,
		width:630,
		create:function(){
			self.onCreate();
		},
		open:function(){
			self.onOpen(selected);
		},
		selectable:false,
		buttons:{
			"确定": function() {
				jQuery( this ).dialog( "close" );
				if(onOk){
					var addedNodes=self.getAddedNodes();
					onOk(addedNodes);
				}
            	
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
        } /*,
        position:{
        	of:parent,
        	collision:"fit"
        }*/
	});
	
};
OrgSelector.prototype.tree=null;
OrgSelector.prototype.added=null;
OrgSelector.prototype.onCreate=function(){
	var self=this;
	//create tree
	//在这里控制只做一次初始化，否则onTreeOpen事件将每次叠加触发
	if(!this.added){
		this.added=jQuery("#os-added").tree({data:[],autoOpen:true,checkbox:true});
	}
	if(!this.tree){
		this.tree=jQuery("#os-tree").tree({data:[],checkbox:true});
	}
	var tree=self.tree;

	//bind button events
	jQuery("#os-add").unbind().click(function(){
		var checkedNodes=self.getCheckedNodes(self.tree);
		var addedNodes=self.getAddedNodes();
		
		var make=function(checkedNodes,addedNodes){
			var ids=[];
			jQuery.each(addedNodes,function(i,n){
				ids.push(n.id);
			});
			jQuery.each(checkedNodes,function(i,n){
				ids.push(n.id);
    		});
			
			//clear checked
			self._connectAddedTree(ids,function(){
				jQuery.each(checkedNodes,function(i,n){
	    			self.uncheckNode(self.tree,n.id);
	    		});
			});
		}
		
		if(!jQuery("#os-include-sub-add").prop("checked")){
			//不包含下级组织的情况
			make(checkedNodes,addedNodes);
		}else{
			//包含下级组织的情况
			var ids=[];
			jQuery.each(checkedNodes,function(i,n){
				ids.push(n.id);
			});
			orgTreeAction.listDescendantNodes(ids,function(r){
				if(r[0]=="ok"){
					var list=eval(r[1]);
					make(list,addedNodes);
				}
			});
		}
	});
	jQuery("#os-remove").unbind().click(function(){
		var hash=[];
		
		var proc=function(){
			var nodes;
			var ids=[];
			if(jQuery("#os-include-sub-del").prop("checked")){
	    		nodes=self._getNodes(self.added,function(node){
	    			return !hash[node.id]&&!node.exclusion;
	    		});
			}else{
				nodes=self._getNodes(self.added,function(node){
					return !node.checked&&!node.exclusion;
	    		});
			}
			jQuery.each(nodes,function(i,n){
				ids.push(n.id);
			});
			self._connectAddedTree(ids);
		}
		
		if(jQuery("#os-include-sub-del").prop("checked")){
			var ids=[];
			var checkedNodes=self.getCheckedNodes(self.added);
			jQuery.each(checkedNodes,function(i,n){
				ids.push(n.id);
			});
			orgTreeAction.listDescendantNodes(ids,function(r){
				if(r[0]=="ok"){
					var list=eval(r[1]);
					jQuery.each(list,function(i,n){
						hash[n.id]=true;
					});
					proc();
				}
			});
		}else{
			proc();
		}
	});
	jQuery("#os-add-all").unbind().click(function(){
		var ids=[];
		//d=beginTimeCost()
		self.iterateRootNodes(self.tree,function(node){
			ids.push(node.id);
		});
		//endTimeCostPrompt(d,"iterateRootNodes");
		//d=beginTimeCost()
		orgTreeAction.listDescendantNodes(ids,function(r){
			if(r[0]=="ok"){
				//endTimeCostPrompt(d,"listDescendantNodes");
				var list=eval(r[1]);
				var ids=[];
				//d=beginTimeCost()
				jQuery.each(list,function(i,n){
					ids.push(n.id);
				});
				//endTimeCostPrompt(d,"each listDescendantNodes");//2ms
				self._connectAddedTree(ids,function(){
					//endTimeCostPrompt(d,"add-all");
				});
			}
		});
		
	});
	jQuery("#os-remove-all").unbind().click(function(){
		self.added.tree("loadData",[]);
	});
	
	//bind tree events
	tree.unbind("tree.open");
	tree.bind("tree.open",onTreeOpen);
	
	function onTreeOpen(e){
		var tree=self.tree;
		if(!e.node.id)
			return;
		orgTreeAction.querySubNodes(e.node.id,function(r){
			if(r[0]=="ok"){
				var list=self._filterList(r[1]);
				var parent=tree.tree('getNodeById',e.node.id);
				var loading=tree.tree("getNodeById","loading"+e.node.id);
				if(!loading)
					return;
				tree.tree("removeNode",loading);
				tree.tree('loadData',list,parent);
				//self.makeMultiSelectable(tree);
			}
		});
	}
}
OrgSelector.prototype.onOpen=function(selected){
	var self=this;
	var tree=self.tree;
	
	//
	jQuery("#os-dlg button").button();
	
	//clear previous informatinon
	self.added.tree("loadData",[]);
	jQuery("#os-include-sub-add").prop("checked",false);
	jQuery("#os-include-sub-del").prop("checked",false)
	
	
	//fill root's sub nodes
	orgTreeAction.queryRootSubNodes(function(r){
		if(r[0]=="error"){
			showErrorDlg(jQuery(document.body),r[1]);
		}else if(r[0]=="ok"){
			var list=self._filterList(r[1]);
			list.unshift({label:"默认",id:-1});
			tree.tree("loadData",list);
			//self.makeMultiSelectable(tree);
		}
	});
	
	//init added nodes
	this._connectAddedTree(selected);
};
/**
 * 使jqTree变成可多选
 * @date 2012-12-22
 */
/*已植入jqtree，以提高性能
OrgSelector.prototype.makeMultiSelectable=function(tree){
	var make=function(n){
		var span=jQuery(n.element).children("div").children("span");
		if(span.find("input").length)
			return true;
		if(n.id.toString().substr(0,7)=="loading")
			return true;
		
		//text
		span.wrapInner("<span></span>");
		span.find("span").click(function(){
			jQuery(this).prev()[0].click();
		});
		if(n.exclusion)
			span.find("span").css("color","#d0d0d0");
		
		
		//input
		var input=jQuery("<input nid='"+n.id+"' type='checkbox'></input>");
		input.prependTo(span);
		input.click(function(){
			var c=jQuery(this).prop("checked");
			n.checked=c;
		});
		input.prop("checked",n.checked);
		return true;
	}
	this.iterateRootNodes(tree,function(node){
		make(node);
		node.iterate(make);
	});
};*/
OrgSelector.prototype.uncheckNode=function(tree,nodeId){
	var node=tree.tree("getNodeById",nodeId);
	if(node){
		node.checked=false;
		tree.find("input[nid='"+node.id+"']").prop("checked",false);
	}
}
OrgSelector.prototype.iterateRootNodes=function(tree,callback){
	var nodeMap=tree.tree("getTree").id_mapping;
	jQuery.each(nodeMap,function(key){
		if(!nodeMap[key].parent.parent)
			callback(nodeMap[key]);
	});
};
OrgSelector.prototype.getCheckedNodes=function(tree){
	var list=this._getNodes(tree,function(node){
		return node.checked;
	});
	return list;
};
/**
 * @param tree 目标树
 * @param condition 判断函数
 */
OrgSelector.prototype._getNodes=function(tree,condition){
	var list=[];
	this.iterateRootNodes(tree,function(node){
		if(condition(node))
			list[list.length]={id:node.id,name:node.name};
		node.iterate(function(node){
			if(condition(node)){
				list[list.length]={id:node.id,name:node.name};
			}
			return true;
		});
	})
	return list;
};
OrgSelector.prototype.getAddedNodes=function(){
	var list=this._getNodes(this.added,function(node){
		return !node.exclusion;
	});
	return list;
};
//让有孩子的节点能够打开（jqTree目前不能识别hasChildren属性）
OrgSelector.prototype._filterList=function(list){
	for(var i=0;i<list.length;i++){
		if(list[i].openable)
			list[i].children=[{id:"loading"+list[i].id,label:"加载中...",loading:true}];
	}
	return list;
};
OrgSelector.prototype._connectAddedTree=function(ids,onAfter){
	var _this=this;
	//var d=beginTimeCost();
	orgTreeAction.connectTree(ids,function(r){
		if(r[0]=="error"){
			showErrorDlg(jQuery(document.body),r[1]);
		}else if(r[0]=="ok"){
			//endTimeCostPrompt(d,"connectTree");
			var tree=eval(r[1]);
			//d=beginTimeCost()//10ms
			_this.added.tree("loadData",tree);
			//endTimeCostPrompt(d,"loadData");
			//d=beginTimeCost()
			//_this.makeMultiSelectable(_this.added);
			//endTimeCostPrompt(d,"makeMultiSelectable");
			if(onAfter)
				onAfter();
		}
	});
};