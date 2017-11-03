
/**
 * 
var dataTable = new CommonDataTable({
	tableId: 'task-management-div', 						// (*) DIV元素的Id
	headTitle: ['名称', '创建日期'], 						// (*) 表头
	headWidth: [150, 80],           						// (*) 表头宽度
	columnNames: ['id', 'name', 'createdDate'], 			// (*) 数据字段（checkbox的值：第一个字段的值）
	itemsPerPage: 10,										// (?) 每页条数
	searchParamMode: CommonDataTable.PARAM_MODE_OBJECT,		// (?) 参数提交方式（对象模式/不定参模式）
	
	// 远程调用方法：增、删、改、查
	getDataListHandler:      taskManagementAction.getTaskPlanItems,  // (*) 获取数据列表
	getRowDataDetailHandler: taskManagementAction.getTaskItemDetail, // (?) 获取详细数据
	addRowDataHandler:       taskManagementAction.addTaskItem,       // (*) 新增数据
	updateRowDataHandler:    taskManagementAction.updateTaskItem,    // (*) 更新数据
	deleteRowDataHandler:    taskManagementAction.deleteTaskItems,   // (*) 删除数据
	
	// 条件查询：设置查询条件、展示查询结果
	setSearchParamHandler:   taskItemPage.setSearchParams,           // (*) 设置查询参数的值：addSearchParam(name, value);
	rowMapper:               taskItemPage.rowMapper,                 // (?) 进行行数据的转换
	
	// 对话框：初始化、设置、清空
	initialDialogDataHandler: taskItemPage.initialDialogDataHandler, // (*) 初始对话框数据
	submitDialogDataHandler: taskItemPage.submitDialogDataHandler,   // (*) 提交对话框数据
	clearDialogDataHandler: taskItemPage.clearDialogDataHandler,     // (?) 清除对话框数据
});

 * 
 * 
 */
function CommonDataTable(settings) {
	// 样式
	this.tableId = CommonDataTable._getSettingsValue(settings, "tableId", "");
	this.headTitle = CommonDataTable._getSettingsValue(settings, "headTitle", []);
	this.headWidth = CommonDataTable._getSettingsValue(settings, "headWidth", []);
	var headLength = this.headTitle.length;
	if (this.headWidth.length == 0 && headLength > 0) {
		var headWdithPercent = 100 / headLength;
		for (var i = 0; i < headLength; i++) {
			this.headWidth.push(headWidthPercent + "%");
		}
	}
	
	// 数据
	this.columnNames = CommonDataTable._getSettingsValue(settings, "columnNames", []);
	this.searchParamMode = CommonDataTable._getSettingsValue(settings, "searchParamMode", CommonDataTable.PARAM_MODE_OBJECT);
	this._searchParamNames = [];
	this.searchParams = {
		// 当前页: pageindex
		currentPageIndex : 1,
		// 每页显示条数：pagesize
		itemsPerPage : CommonDataTable._getSettingsValue(settings, "itemsPerPage", CommonDataTable.DEFAULT_ITEM_PER_PAGE)
	}
	this.tableData = {};// 表格数据
	this._rowDataDialog = null;
	this._otherDialogs = {};
	this.dialogRowData = null; // 对话框数据
	this._dialogMode = CommonDataTable.DIALOG_MODE_CREATE;
	this.dialogButtonVisible = CommonDataTable._getSettingsValue(settings, "dialogButtonVisible", true);// 对话框按钮的显示和隐藏（一旦设置，不允许修改）
	
	// 远程调用方法：增、删、改、查
	this._getDataListHandler = CommonDataTable._getSettingsValue(settings, "getDataListHandler", CommonDataTable.getDataListHandler);
	this._getRowDataDetailHandler = settings["getRowDataDetailHandler"];
	this._addRowDataHandler = CommonDataTable._getSettingsValue(settings, "addRowDataHandler", CommonDataTable.addRowDataHandler);;
	this._updateRowDataHandler = CommonDataTable._getSettingsValue(settings, "updateRowDataHandler", CommonDataTable.updateRowDataHandler);;
	this._deleteRowDataHandler = CommonDataTable._getSettingsValue(settings, "deleteRowDataHandler", CommonDataTable.deleteRowDataHandler);
	
	// 条件查询：查询条件、查询结果
	this._setSearchParamHandler =  CommonDataTable._getSettingsValue(settings, "setSearchParamHandler", CommonDataTable.setSearchParamHandler);
	this._rowMapper = CommonDataTable._getSettingsValue(settings, "rowMapper", CommonDataTable.rowMapper);
	
	// 对话框：初始化、设置、清空
	this._initialDialogDataHandler = CommonDataTable._getSettingsValue(settings, "initialDialogDataHandler", CommonDataTable.initialDialogDataHandler);
	this._submitDialogDataHandler = CommonDataTable._getSettingsValue(settings, "submitDialogDataHandler", CommonDataTable.submitDialogDataHandler);
	this._clearDialogDataHandler = CommonDataTable._getSettingsValue(settings, "clearDialogDataHandler", CommonDataTable.clearDialogDataHandler);
}

/**
 * 
 * 静态成员
 * 
 */
CommonDataTable.DEFAULT_ITEM_PER_PAGE = 10;
CommonDataTable.PARAM_MODE_OBJECT = "PARAM_MODE_OBJECT"; // 参数以“整个对象”提交
CommonDataTable.PARAM_MODE_ARRAY = "PARAM_MODE_ARRAY";   // 参数以“不定参”提交
CommonDataTable.DIALOG_MODE_CREATE = "DIALOG_MODE_CREATE";  // 创建对话框
CommonDataTable.DIALOG_MODE_EDIT = "DIALOG_MODE_EDIT";      // 编辑对话框

CommonDataTable._getSettingsValue = function (settings, settingName, defaultValue) {
	if (settings[settingName] == undefined) {
		return defaultValue;
	} else {
		return settings[settingName];
	}
};


// 远程调用方法：增、删、改、查
CommonDataTable.getDataListHandler = function(params, callback) {
	throw "请配置数据获取函数，格式：getDataListHandler: dwrAction.getXXXList";
}
CommonDataTable.addRowDataHandler = function() {
	throw "请配置新增数据函数，格式：addRowDataHandler: dwrAction.addXXX";
}
CommonDataTable.updateRowDataHandler = function() {
	throw "请配置更新数据函数，格式：addRowDataHandler: dwrAction.updateXXX";
}
CommonDataTable.deleteRowDataHandler = function() {
	throw "请配置删除数据函数，格式：deleteRowDataHandler: dwrAction.deleteXXX";
}

// 条件查询：查询条件、查询结果
// 设置查询参数
CommonDataTable.setSearchParamHandler = function() {
	// 普通字段：
	// dataTableInstance.addSearchParam("name", jQuery("#name").val());
	
	// 日期字段：
	// var value = jQuery("#createdDateStart").val();
	// value = (value == "") ? null : new Date(Date.parse(value.replace(/-/g, "/")));
	// dataTableInstance.addSearchParam("createdDateStart", value);
}
// 行数据的处理（可以添加属性，最好不修改原始属性的值）
CommonDataTable.rowMapper = function(rowData) {
	// 未做任何处理
	return rowData;
}

// 对话框：初始化、设置、清空
CommonDataTable.initialDialogDataHandler = function(rowData) {
	throw "请配置数据初始化函数，格式：initialDialogDataHandler: function() {...}";
}
CommonDataTable.submitDialogDataHandler = function() {
	throw "请配置数据提交函数，格式：submitDialogDataHandler: function() {...}";
}
CommonDataTable.clearDialogDataHandler = function() {
	// throw "请配置对话框清理函数，格式：clearDialogDataHandler: function() {...}";
}



/**
 * 
 * 主题处理部分
 * 
 */
// 添加查询参数（日期类型）
CommonDataTable.prototype.addDateSearchParam = function(name, value) {
	value = dateStr2Date(value);
	this.addSearchParam(name, value);
}

// 添加查询参数（其他类型）
CommonDataTable.prototype.addSearchParam = function(name, value) {
	if (value == "") {
		value = null;
	}
	if (this.searchParamMode == CommonDataTable.PARAM_MODE_ARRAY) {
		var names = this._searchParamNames;
		var nameLength = names.length;
		var isFound = false;
		if (nameLength > 0) {
			for (var i = 0; i < nameLength; i++) {
				if (names[i] == name) {
					isFound = true;
					break;
				}
			}
		}
		if (!isFound) {
			names.push(name);
		}
	}
	this.searchParams[name] = value;
}

// 执行查询
CommonDataTable.prototype.search = function() {
	this._setSearchParamHandler();
	this._fetchDataList(1);
}

// 获取某一个分页的数据列表
CommonDataTable.prototype._fetchDataList = function(currentPage) {
	this.searchParams.currentPageIndex = currentPage;
	
	var dataTableInstance = this;
	if (this.searchParamMode == CommonDataTable.PARAM_MODE_OBJECT) {
		this._getDataListHandler(this.searchParams, function(result) {
			CommonDataTable._fetchDataCallback(dataTableInstance, result);
		});
	} else {
		var paramNames = this._searchParamNames;
		var paramNameLength = paramNames.length;
		var params = this.searchParams;
		var args = [];
		for (var i = 0; i < paramNameLength; i++) {
			var name = paramNames[i];
			args.push(params[name]);
		}
		args.push(function(result) {
			CommonDataTable._fetchDataCallback(dataTableInstance, result);
		});
		this._getDataListHandler.apply(this, args);
	}
}

// 获取某行数据
CommonDataTable.prototype.getDataById = function(id) {
	return this.tableData[id];
}

// 获取选中项
CommonDataTable.prototype.getSelectedDatas = function() {
	var checkBoxName = CommonDataTable._getCheckBoxName(this.tableId);
	var checkBoxList = jQuery("#" + this.tableId + " input[type=checkbox][name='" + checkBoxName + "']:checked");
	var checkBoxLength = checkBoxList.length;
	var result = [];
	
	for (var i = 0; i < checkBoxLength; i++) {
		var checkBox = checkBoxList[i];
		var value = this.tableData[checkBox.value];
		result.push(value);
	}
	return result;
}

// 获取选中项Id
CommonDataTable.prototype.getSelectedIds = function() {
	var checkBoxName = CommonDataTable._getCheckBoxName(this.tableId);
	var checkBoxList = jQuery("#" + this.tableId + " input[type=checkbox][name='" + checkBoxName + "']:checked");
	var result = [];
	var iLength = checkBoxList.length;
	if (iLength == 0) {
		return result;
	}
	
	for (var i = 0; i < iLength; i++) {
		var checkBox = checkBoxList[i];
		var value = checkBox.value;
		result.push(value);
	}
	return result;
}


// 得到数据列表的回调处理函数
CommonDataTable._fetchDataCallback = function(dataTableInstance, result) {
	if (result[0] == "ok") {
		CommonDataTable._createTable(dataTableInstance, result[1], result[2]);
	} else if (result[0] == "error") {
		showErrorDlg(jQuery(document.body), "查询数据失败！");
	} else {
		alert(result);
	}
}

// 打开新建对话框
CommonDataTable.prototype.showCreateDialog = function(dialogId, iWidth, iHeight) {
	this._dialogMode = CommonDataTable.DIALOG_MODE_CREATE;
	this.dialogRowData = {};
	this._showRowDataDialog(dialogId, iWidth, iHeight);
}

// 打开编辑对话框
CommonDataTable.prototype.showEditDialog = function(dialogId, iWidth, iHeight, id) {
	this._dialogMode = CommonDataTable.DIALOG_MODE_EDIT;
	var rowData = this.getRowData(id);
	this._showRowDataDialog(dialogId, iWidth, iHeight, rowData);
}

// 获取当前行数据
CommonDataTable.prototype.getRowData = function(id) {
	var rowData;
	if (!id) {
		rowData = this.getSelectedDatas()[0];
	} else {
		rowData = this.getDataById(id);
	}
	this.dialogRowData = rowData;
	return rowData;
}

// 打开对话框
CommonDataTable.prototype._showRowDataDialog = function(dialogId, iWidth, iHeight, rowData) {
	var dialogDiv = jQuery("#" + dialogId);
	if (!this._rowDataDialog) {
		var dataTableInstance = this;
		//var submitHandler = this._submitDialogDataHandler;
		//var clearHandler = this._clearDialogDataHandler;
		var buttonArray = [];
		if (dataTableInstance.dialogButtonVisible) {
			buttonArray = [
				{
					text: "提交", 
					click: function(){
						var submitData = dataTableInstance.dialogRowData;
						var isSubmit = dataTableInstance._submitDialogDataHandler(submitData);//submitHandler.apply(this);
						var message;
						if (isSubmit instanceof Array) {
							message = isSubmit[1];
							isSubmit = isSubmit[0];
						}
						if (isSubmit) {
							var dialogDiv = jQuery(this);
							var handler;
							if (dataTableInstance._dialogMode == CommonDataTable.DIALOG_MODE_CREATE) {
								handler = dataTableInstance._addRowDataHandler;
							} else {
								handler = dataTableInstance._updateRowDataHandler;
							}
							handler(submitData, function(result) {
								if (result[0] == "ok") {
									dataTableInstance.search();
									dialogDiv.dialog("close");
								} else if (result[0] == "error") {
									var message = "保存数据失败！";
									if (result[1]) {
										message = result[1];
									}
									showErrorDlg(jQuery(document.body), message);
								}
							});
						} else {
							if (!message) {
								message = "数据填写不完整失败！";
							}
							showErrorDlg(jQuery(document.body), message);
						}
					}
				},
				{
					text: "取消", 
					click: function(){
						jQuery(this).dialog("close");
					}
				}
			];
		}
		this._rowDataDialog = dialogDiv.dialog({
			autoOpen : false,
			height : iHeight,
			width : iWidth,
			modal : true,
			resizable : false,
			position : {
				of : parent,
				collision : "fit"
			},
			buttons : buttonArray,
			close : function() {
				dataTableInstance._clearDialogDataHandler();//clearHandler();
			}
		});
	}
	
	if (!rowData) {
		// 不用请求服务端，直接初始化数据
		this._initialDialogDataHandler(rowData);
	} else {
		// 初始化数据
		if (!this._getRowDataDetailHandler) {
			this._initialDialogDataHandler(rowData);
		} else {
			var dataTableInstance = this;
			this._getRowDataDetailHandler(rowData.id, function(r) {
			    if (r[0] == "ok") {
			    	jQuery.extend(rowData, r[1]);
			    	dataTableInstance._initialDialogDataHandler(rowData);//r[1]
			    } else if (r[0] == "error") {
					showErrorDlg(jQuery(document.body), "获取详细信息失败！");
				}
			});
		}
	}
	dialogDiv.dialog("open");
}

// 删除选中项
CommonDataTable.prototype.deleteSelectionRows = function() {
	var ids = this.getSelectedIds();
	var dataTableInstance = this;
	dataTableInstance._deleteRowDataHandler(ids, function(r) {
		if (r[0] == "ok") {
			dataTableInstance.search();
			var allCheckBoxName = CommonDataTable._getAllCheckBoxName(dataTableInstance.tableId);
			jQuery("#" + allCheckBoxName).attr('checked', false);
		} else if (r[0] == "error") {
			var message = "删除选中项失败。";
			if (r[1]) {
				message = r[1];
			}
			showErrorDlg(jQuery(document.body), message);
		}
	});
}

// 单独创建表头
CommonDataTable.prototype.createTableHeader = function() {
	if (this.headTitle == null) {
		// 未定义表头信息
		showErrorDlg(jQuery(document.body), "表格头部未定义。");
		return "";
	}
	
	var tablDiv = jQuery("#" + this.tableId);
	tablDiv.empty();
	var tableHtml = '<table class="ui-common-data-table">';
	tableHtml += CommonDataTable._createTableHead(this);
	tableHtml +=    '</table>';
	tablDiv.append(tableHtml);
}


/**
 * 
 * 获取数据和构造表格。
 * 
 */

CommonDataTable._createTable = function(dataTableInstance, dataList, totalCount) {
	if (dataTableInstance.headTitle == null) {
		// 未定义表头信息
		showErrorDlg(jQuery(document.body), "表格头部未定义。");
		return "";
	}
	
	var tablDiv = jQuery("#" + dataTableInstance.tableId);
	tablDiv.empty();
	var tableHtml = '<table class="ui-common-data-table">';
	// 生成表头
	tableHtml += CommonDataTable._createTableHead(dataTableInstance);
	// 生成表格内容
	tableHtml += CommonDataTable._createTableBody(dataTableInstance, dataList);
	tableHtml +=    '</table>';
	tablDiv.append(tableHtml);
	
	//生产分页内容
	var paginationDiv = CommonDataTable._createTablePagination(dataTableInstance, dataList, totalCount);
	tablDiv.append(paginationDiv);
}

//表头
CommonDataTable._createTableHead = function(dataTableInstance) {
	var allCheckBoxName = CommonDataTable._getAllCheckBoxName(dataTableInstance.tableId);
	var headHtml = '<tr>' +
						'<th width="20"><input id="' + allCheckBoxName + '" type="checkbox" onclick="CommonDataTable._selectAllRows(this, \'' + checkBoxName + '\')"></input></th>';
	
	var checkBoxName = CommonDataTable._getCheckBoxName(dataTableInstance.tableId);
	var headTitle = dataTableInstance.headTitle;
	var headWidth = dataTableInstance.headWidth;
	var length = headTitle.length;
	for (var i = 0; i < length; i++) {
		if (headWidth[i] != null && headWidth[i] != "")
			headHtml += '<th width="' + headWidth[i] + '">' + headTitle[i] + '</th>';
		else
			headHtml += '<th>' + headTitle[i] + '</th>';
	}
	headHtml +=   '</tr>';
	return headHtml;
}

// 表数据
CommonDataTable._createTableBody = function(dataTableInstance, dataList) {
	var columnNames = dataTableInstance.columnNames;
	var columnLength = columnNames.length;
	var dataLength = dataList.length;
	if (dataList == null || dataLength == 0) {
		// 未找到数据
		return '<tr><td colspan=' + columnLength + '>没有你查询的数据，请重新查询</td></tr>';
	}
	
	var checkBoxName = CommonDataTable._getCheckBoxName(dataTableInstance.tableId);
	var contentHtml = '';
	var tableDataMap = {};
	
	for (var i = 0; i < dataLength; i++) {
		var rowData = dataTableInstance._rowMapper(dataList[i]);
		var column = columnNames[0];
		var key = rowData[column];
		tableDataMap[key] = rowData;
		contentHtml += '<tr>' +
							'<td><input type="checkbox" name="' + checkBoxName + '" value="' + key + '"></td>';
		for (var j = 1; j < columnLength; j++) {
			column = columnNames[j];
			contentHtml +=  '<td>' + rowData[column] + '</td>';
		}
		contentHtml += '</tr>';
	}
	dataTableInstance.tableData = tableDataMap;
	return contentHtml;
}

// 分页
CommonDataTable._createTablePagination = function(dataTableInstance, dataList, totalCount) {
	var currentPageIndex = dataTableInstance.searchParams.currentPageIndex;
	var itemsPerPage = dataTableInstance.searchParams.itemsPerPage;
	
	var createLink = function(index, pageNum, text) {
		var linkStr = '<a href="javascript:void(0)" '; //onclick="' + fnGo + '(' + index + ');"
		if (index == pageNum) {
			linkStr += 'style="background-color:eee;color:red"  ';
		}
		text = text || index;
		linkStr += '>' + text + '</a> ';
		var anchor = jQuery(linkStr);
		anchor.click(function() {
			dataTableInstance._fetchDataList(index);
		});
		return anchor;
	}
	
	// 分页块
	var paginationDiv = jQuery('<div class="pagination" align="right"></div>');
	
	// 总页数
	var pageCount = Math.ceil(totalCount / itemsPerPage);
	var displayCount = 5; //当前页左右两边显示个数
	
	// 确保当前页在有效范围内
	currentPageIndex = Math.max(currentPageIndex, 1);
	currentPageIndex = Math.min(currentPageIndex, pageCount);
	
	// 上一页
	var link;
	if (currentPageIndex > 1) {
		link = createLink(currentPageIndex - 1, currentPageIndex, '上一页');
	} else {
		link = jQuery('<span class="current prev">上一页</span> ');
	}
	paginationDiv.append(link);
	
	// 第一个页码
	var begin = 1;
	if (currentPageIndex - displayCount > 1) {
		link = createLink(1, currentPageIndex);
		paginationDiv.append(link);
		paginationDiv.append('... ');
		begin = currentPageIndex - displayCount;
	}
	
	// 创建之间的页码
	var end = Math.min(pageCount, begin + displayCount * 2);
	if (end == pageCount - 1) {
		end = pageCount;
	}
	for ( var i = begin; i <= end; i++) {
		link = createLink(i, currentPageIndex);
		paginationDiv.append(link);
	}
	
	// 最后一个页码
	if (end < pageCount) {
		paginationDiv.append('... ');
		link = createLink(pageCount, currentPageIndex);
		paginationDiv.append(link);
	}
	
	// 下一页
	if (currentPageIndex < pageCount) {
		link = createLink(currentPageIndex + 1, currentPageIndex, '下一页');
	} else {
		link = jQuery('<span class="current prev">下一页</span> ');
	}
	paginationDiv.append(link);
	return paginationDiv;
}

// 构造checkbox名称
CommonDataTable._getCheckBoxName = function(tableId) {
	return tableId + "_checkBox";
}

// 构造全选checkbox名称
CommonDataTable._getAllCheckBoxName = function(tableId) {
	return tableId + "_allCheckBox";
}

// 全部选中/全部取消
CommonDataTable._selectAllRows = function(selectAllCheckBox, checkBoxName) {
	var isChecked = selectAllCheckBox.checked;
	jQuery("input[type=checkbox][name='" + checkBoxName + "']").each(function(){
		 jQuery(this).attr("checked", isChecked);
	});
}

// 打开行数据对话框：通过“this.dialogRowData”获取当前行数据
CommonDataTable.prototype.showOtherRowDataDialog = function(dialogId, iWidth, iHeight, id, 
		initialHandler) {
	var rowData = this.getRowData(id);
	var dialogDiv = jQuery("#" + dialogId);
	var otherDialog = this._otherDialogs[dialogId];
	if (!otherDialog) {
		var dataTableInstance = this;
		otherDialog = dialogDiv.dialog({
			autoOpen : false,
			height : iHeight,
			width : iWidth,
			modal : true,
			resizable : false,
			position : {
				of : parent,
				collision : "fit"
			},
			close : function() {
//				if (clearHandler) {
//					clearHandler();
//				}
			}
		});
		this._otherDialogs[dialogId] = otherDialog;
	}
	
	if (!this._getRowDataDetailHandler) {
		// 直接初始化
		initialHandler(rowData);
	} else {
		var dataTableInstance = this;
		this._getRowDataDetailHandler(rowData.id, function(r) {
		    if (r[0] == "ok") {
		    	jQuery.extend(rowData, r[1]);
				initialHandler(rowData);
		    } else if (r[0] == "error") {
				showErrorDlg(jQuery(document.body), "获取详细信息失败！");
			}
		});
	}
	dialogDiv.dialog("open");
}
