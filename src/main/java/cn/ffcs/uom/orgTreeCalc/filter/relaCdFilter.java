package cn.ffcs.uom.orgTreeCalc.filter;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import cn.ffcs.uom.orgTreeCalc.TreeNode;

public class relaCdFilter extends BaseFilter {
	private List<String> relaCds;
	private Logger logger = Logger.getLogger(this.getClass());

	public relaCdFilter(List<String> relaCds) {
		this.relaCds = relaCds;
		logger.debug("relaCdFilter : relaCds " + relaCds.toString());
	}

	@Override
	public HashMap<String, List<TreeNode>> nodesVisible(
			HashMap<String, List<TreeNode>> nodes) {
		HashMap<String, List<TreeNode>> vibibleNodes = new HashMap<String, List<TreeNode>>();
		if (relaCds != null && nodes != null) {
			for (int i = 0; i < relaCds.size(); i++) {
				String relaCd = relaCds.get(i);
				List<TreeNode> list = nodes.get(relaCd);
				if (list != null) {
					vibibleNodes.put(relaCd, list);
				}
			}
		}
		return super.nodesVisible(vibibleNodes);
	}
}
