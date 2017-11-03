package cn.ffcs.uom.orgTreeCalc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.ffcs.uom.common.zkplus.zul.tree.node.TreeNodeEntity;

import lombok.Getter;
import lombok.Setter;

public class TreeNode {
	@Getter
	@Setter
	private HashMap<String, List<TreeNode>> childNodes = new HashMap<String, List<TreeNode>>();
	@Getter
	@Setter
	private String value;
	@Getter
	@Setter
	private AccessFlag accessFlag = AccessFlag.unAccess;
	
	public enum AccessFlag{
		unAccess,
		underAccess,
		Accessed
	}
}
