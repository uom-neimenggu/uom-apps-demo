function ZTreeBox(){
	ZTreeBox.treeMap = {};
	ZTreeBox.getTree = function(treeId) {
		return ZTreeBox.treeMap[treeId];
	};
	ZTreeBox.putTree = function(treeId, tree) {
		ZTreeBox.treeMap[treeId] = tree;
	}
}

ZTreeBox.prototype.isInitiated = false;
ZTreeBox.prototype.isLoadData = false;
ZTreeBox.prototype.source = {};
ZTreeBox.prototype.treeContainerId = "_zTreeContainer";
ZTreeBox.prototype.treeContainer = {};
ZTreeBox.prototype.tree = {};
ZTreeBox.prototype.selectionIds = [];
ZTreeBox.prototype.selectionNames = [];
ZTreeBox.prototype.selectionDatas = [];
ZTreeBox.prototype.clickCallback = null;
ZTreeBox.prototype.setting = {
	view: {
		dblClickExpand: false
	},
	data: {
		simpleData: {
			enable: true,
			idKey: "id"
		}
	},
	callback: {
	    //beforeClick: beforeClick,
		//onClick: onClick
		//onExpand: onExpand
	}
};

ZTreeBox.prototype.initTreeHtml = function(sourceElementId, expandCallback, filterCallback, clickCallback) {
	this.treeContainerId = sourceElementId + "_zTreeContainer";
	var treeId = sourceElementId + "_zTree";
	var treeContainerDiv = 
	        '<div id="' + this.treeContainerId + '" class="menuContent" style="display: none; position: absolute;Z-index:10000">' + 
				'<ul id="' + treeId + '" class="ztree" style="margin-top:0; width:95%;"></ul>' +
			'</div>';
	this.source = jQuery("#" + sourceElementId);
	jQuery("body").append(treeContainerDiv);
	this.treeContainer = jQuery("#" + this.treeContainerId);
	this.tree = jQuery("#" + treeId);
	ZTreeBox.putTree(treeId, this);
	//var treeBox = this;
	this.clickCallback = clickCallback;
	this.setting.callback.onClick = function(e, zTreeId, zTreeNode) {//e, treeId, treeNode
    	var zTree = jQuery.fn.zTree.getZTreeObj(zTreeId),
    	nodes = zTree.getSelectedNodes(),
//    	datas = [],
    	ids = [],
    	names = [];
    	nodes.sort(function compare(a,b){return a.id-b.id;});
    	for (var i=0, l=nodes.length; i<l; i++) {
//    		datas.push(nodes[i]);
    		ids.push(nodes[i].id);
    		names.push(nodes[i].name);
    	}
    	var treeBox = ZTreeBox.getTree(zTreeId);
    	treeBox.selectionDatas = nodes;//datas;
    	treeBox.selectionIds = ids;
    	treeBox.selectionNames = names;
    	treeBox.source.attr("value", names.join(', '));
    	if (treeBox.clickCallback) {
    		treeBox.clickCallback(treeBox.selectionIds, treeBox.selectionNames);
    	}
    };
	this.setting.callback.beforeClick = filterCallback;
	this.setting.callback.onExpand = expandCallback;
	this.isInitiated = true;
}

//ZTreeBox.prototype.selectionNames = function(names) {
//	if (arguments.length > 0) {
//		// 设置
//		if (names == null) {
//			names = [];
//		}
//		this._selectionNames = names;
//	}
//	return this._selectionNames;
//}

ZTreeBox.prototype.clearData = function() {
	if (this.isLoadData) {
		this.tree.empty();
	}
	this.isLoadData = false;
}

ZTreeBox.prototype.loadTreeData = function(treeData) {
	if (this.isLoadData) {
		this.clearData();
	}
    jQuery.fn.zTree.init(this.tree, this.setting, treeData);
    this.isLoadData = true;
}

ZTreeBox.prototype.addTreeData = function(treeId, treeNode, treeData) {
	var treeObj = jQuery.fn.zTree.getZTreeObj(treeId);
	//var node = treeObj.getNodeByTId(treeNode.tId);
	if(treeNode.open){
		if(treeNode.children==null){
			//加载子节点数据
		    treeObj.addNodes(treeNode, treeData);
		}
	}
}

ZTreeBox.prototype.showTree = function() {
	var sourceOffset = this.source.offset();
	this.treeContainer.css({
		left:(sourceOffset.left) + "px", 
		top:(sourceOffset.top + this.source.outerHeight()) + "px",
		width: (this.source.outerWidth()) + "px"
		}).slideDown("fast");
	//this.treeContainer.css({top:this.source.outerHeight() + "px"}).slideDown("fast");
	var treeBox = this;
	this.onBodyDown = function(event){
		if (!(event.target.id == treeBox.treeContainerId || jQuery(event.target).parents("#" + treeBox.treeContainerId).length>0)) {
			treeBox.hideTree();
		}
	};
	jQuery("body").bind("mousedown", this.onBodyDown);
}

ZTreeBox.prototype.onBodyDown = function() {};

ZTreeBox.prototype.hideTree = function() {
	this.treeContainer.fadeOut("fast");
	jQuery("body").unbind("mousedown", this.onBodyDown);
}
