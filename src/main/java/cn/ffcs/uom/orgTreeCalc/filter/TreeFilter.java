package cn.ffcs.uom.orgTreeCalc.filter;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.ffcs.uom.orgTreeCalc.TreeNode;

public interface TreeFilter {
	HashMap<String, List<TreeNode>> nodesVisible(HashMap<String, List<TreeNode>> nodes);
	void setFilter(TreeFilter filter);
}
