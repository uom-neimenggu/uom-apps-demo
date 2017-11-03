package cn.ffcs.uom.common.zkplus.zul.tree.node;

import java.util.ArrayList;

/**
 * .
 * 
 * @author wuyx
 * @version Revision 1.0.0
 * 
 */
public interface TreeNodeEntity {

	/**
	 * 是否获取跟节点.
	 * 
	 * @return boolean
	 */
	boolean isGetRoot();

	/**
	 * 获取跟节点.
	 * 
	 * @return ArrayList<TreeNodeEntity>
	 */
	ArrayList<TreeNodeEntity> getRoot();

	/**
	 * 获取子节点.
	 * 
	 * @return ArrayList<TreeNodeEntity>
	 */
	ArrayList<TreeNodeEntity> getChildren();

	/**
	 * 获取名称.
	 * 
	 * @return String
	 */
	String getLabel();

}
