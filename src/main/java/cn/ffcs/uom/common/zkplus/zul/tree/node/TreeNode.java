package cn.ffcs.uom.common.zkplus.zul.tree.node;

import java.util.ArrayList;

/**
 * TreeNode.
 * 
 * @author wuyx
 * @version Revision 1.0.0
 * 
 */
public interface TreeNode {

	/**
	 * 查询子节点.
	 * 
	 */
	void readChildren();

	/**
	 * 获取子节点.
	 * 
	 * @return ArrayList
	 */
	ArrayList getChildren();

	/**
	 * 是否叶子.
	 * 
	 * @return boolean
	 */
	boolean isLeaf();

	/**
	 * 获取显示名称.
	 * 
	 * @return String
	 */
	String getLable();

	/**
	 * getChildCount.
	 * 
	 * @return int
	 */
	int getChildCount();

	/**
	 * getChild.
	 * 
	 * @param arg1
	 *            序号
	 * @return TreeNode
	 */
	TreeNode getChild(int arg1);

	/**
	 * getAttr .
	 * 
	 * @param name
	 *            String
	 * @return Object
	 */
	Object getAttr(String name);

	/**
	 * 显示提示信息.
	 * 
	 * @return String
	 * @author wuyx 2011-5-4 wuyx
	 */
	String getHint();

	/**
	 * 
	 * 用于获取树节点的id.
	 * 
	 * @return String 测试id
	 * @author zhoupc 2011-5-9 zhoupc
	 */
	String getTestId();

	/**
	 * .
	 * 
	 * @return boolean
	 * @author wuyx 2012-2-3 wuyx
	 */
	boolean isOpen();
}
