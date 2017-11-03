/**
 * jQuery support added by zengzhen 
 * 依赖： 
 * <cf:head type="css" url="/uniportal-plugins/public/pagination/pagination.css"/> 
 * <cf:head type="css" url="/uniportal-plugins/public/simple-table/css/simple-table.css"/>
 * <cf:head type="js" url="/uniportal-plugins/public/pagination/jquery.pagination.js" />
 * <cf:head type="js" url="/uniportal-plugins/public/simple-table/js/jquery.simple-table.src.js" />
 * 
 */
(function($) {
	var options = {};
	var methods = {
		init : function(opts) {
			
			return this.each(function() {
				var id=$(this).attr("id");
				options[id]={};
				options[id] = $.extend({
					paginationElem : "",
					header : [],
					headerWidth : [],
					pageSize : 15,
					onData : function() {
					},
					showNoData:false
				}, opts || {});
				
				var updateTable=function(data,total,onDone){
					var html = createTable(
							options[id].header,
							options[id].headerWidth, data,options[id].showNoData);
					table.html(html);

					if (onDone)
						onDone();
				};
				var table = $(this);
				
				//第一次加载
				options[id].onData(1,options[id].pageSize,
					function(data,total,onDone){
						updateTable(data,total,onDone);
						$(options[id].paginationElem).pagination(
							total,{
								items_per_page : options[id].pageSize,
								num_display_entries : 10,
								num_edge_entries : 2,
								prev_text : "上一页",
								next_text : "下一页",
								load_at_once:false,
								callback : function(page) {
									//点击页码事件
									options[id].onData(page + 1, options[id].pageSize,
										function(data, total, onDone) {
											updateTable(data,total,onDone);
										});
									return false;
								}
							});
					}
				);
			});
		},
		gotoPage:function(page){
			//TODO multi widget support
			$(options.paginationElem).pagination("gotoPage",page);
		},
		refresh : function() {
			//TODO multi widget support
			$(options.paginationElem).pagination("refresh");
		}
	};

	$.fn.simpletable = function(method) {

		if (methods[method]) {
			return methods[method].apply(this, Array.prototype.slice.call(
					arguments, 1));
		} else if (typeof method === 'object' || !method) {
			return methods.init.apply(this, arguments);
		} else {
			$.error('Method ' + method + ' does not exist on jQuery.tooltip');
		}

	};

	/**
	 * 生成表格内容
	 * 
	 * @param {Object}
	 *            tableHeader 表格头部内容
	 * @param {Object}
	 *            tableHeaderWitdh 头部每项的宽度
	 * @param {Object}
	 *            tableData
	 * @return {TypeName}
	 */
	function createTable(tableHeader, tableHeaderWitdh, tableData,showNoData) {
		if (tableHeader == null) {
			alert("表格头部未定义");
			return "";
		} else {
			var tableHTML = '<table class="tbData">';
			tableHTML += '<tr>';
			for ( var i = 0; i < tableHeader.length; i++) {
				if (tableHeaderWitdh[i] != "")
					tableHTML += '<th style="width:' + tableHeaderWitdh[i]
							+ '">' + tableHeader[i] + '</th>';
				else
					tableHTML += '<th>' + tableHeader[i] + '</th>';
			}
			tableHTML += '</tr>';
			if (tableData != null && tableData.length > 0) {
				for ( var i = 0; i < tableData.length; i++) {
					tableHTML += '<tr>';
					for ( var j = 0; j < tableData[i].length; j++) {
						var attr = tableData[i][j].attr ? tableData[i][j].attr
								: "";
						tableHTML += "<td " + attr + ">" + tableData[i][j].tag
								+ "</td>";
					}
					tableHTML += '</tr>';
				}
			} else if(showNoData){
								tableHTML += '<tr><td colspan=' + tableHeader.length
										+ '>暂无数据，请重新查询</td></tr>';
			}
			tableHTML += '</table>';
			return tableHTML;
		}
	}
})(jQuery);