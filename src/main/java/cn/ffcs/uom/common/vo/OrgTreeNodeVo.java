package cn.ffcs.uom.common.vo;

/**
 * 
 */

import java.util.List;

/**
 * @author 曾臻
 * @date 2012-12-19
 *
 */
public class OrgTreeNodeVo {
	private long id;
	private String label;
	private List<OrgTreeNodeVo> children;
	
	/**
	 * 父节点标识，方便检索
	 */
	private long parentId;
	/**
	 * 是否包含子节点
	 */
	private boolean openable;
	/**
	 * 是否排除在外
	 */
	private boolean exclusion;

	
	public boolean isExclusion() {
		return exclusion;
	}
	public void setExclusion(boolean exclusion) {
		this.exclusion = exclusion;
	}
	public long getParentId() {
		return parentId;
	}
	public void setParentId(long parentId) {
		this.parentId = parentId;
	}
	public List<OrgTreeNodeVo> getChildren() {
		return children;
	}
	public void setChildren(List<OrgTreeNodeVo> children) {
		this.children = children;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public boolean isOpenable() {
		return openable;
	}
	public void setOpenable(boolean openable) {
		this.openable = openable;
	}
}
