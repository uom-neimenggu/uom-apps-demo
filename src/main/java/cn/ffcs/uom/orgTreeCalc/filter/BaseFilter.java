package cn.ffcs.uom.orgTreeCalc.filter;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.ffcs.uom.orgTreeCalc.TreeNode;

public class BaseFilter implements TreeFilter {

	private TreeFilter _filter;

	public HashMap<String, List<TreeNode>> nodesVisible(
			HashMap<String, List<TreeNode>> nodes) {
		if (_filter != null)
			return _filter.nodesVisible(nodes);
		return nodes;
	}

	public void setFilter(TreeFilter filter) {
		if (_filter == null) {
			_filter = filter;
		} else {
			_filter.setFilter(filter);
		}
	}
}
