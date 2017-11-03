package cn.ffcs.uom.common.zkplus.zul.tree.model;

import org.zkoss.zul.AbstractTreeModel;

import cn.ffcs.uom.common.zkplus.zul.tree.node.TreeNode;

/**
 * BaseTreeModel.
 * 
 * @author chenmh
 * @version Revision 1.0.0
 * 
 */
public class BaseTreeModel extends AbstractTreeModel {
	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 构造函数.
	 * 
	 * @param root
	 *            TreeNode
	 */
	public BaseTreeModel(final TreeNode root) {
		super(root);
	}

	// @Override
	/**
	 * getChild.
	 * 
	 * @param arg0
	 *            Object
	 * @param arg1
	 *            int
	 * @return Object
	 * @see org.zkoss.zul.TreeModel#getChild(java.lang.Object, int)
	 */
	public Object getChild(final Object arg0, final int arg1) {
		return ((TreeNode) arg0).getChild(arg1);
	}

	// @Override
	/**
	 * getChildCount.
	 * 
	 * @param arg0
	 *            Object
	 * @return int
	 * @see org.zkoss.zul.TreeModel#getChildCount(java.lang.Object)
	 */
	public int getChildCount(final Object arg0) {
		return ((TreeNode) arg0).getChildCount();
	}

	// @Override
	/**
	 * isLeaf.
	 * 
	 * @param arg0
	 *            Object
	 * @return boolean
	 * @see org.zkoss.zul.TreeModel#isLeaf(java.lang.Object)
	 */
	public boolean isLeaf(final Object arg0) {
		if (arg0 == null) {
			return true;
		}

		return ((TreeNode) arg0).isLeaf();
	}
}
