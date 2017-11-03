package cn.ffcs.uom.staff.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.staff.dao.TreeStaffExtendAttrDao;

/**
 *树员工扩展属性实体.
 * 
 * @author
 * 
 **/
public class TreeStaffExtendAttr extends UomEntity implements Serializable {
	/**
     * .
     */
    private static final long serialVersionUID = 1L;
    /**
	 *员工树属性标识.
	 **/
	public Long getTreeStaffExtendAttrId() {
		return super.getId();
	}

	public void setTreeStaffExtendAttrId(Long treeStaffExtendAttrId) {
		super.setId(treeStaffExtendAttrId);
	}
	/**
	 *员工属性规格分类标识.
	 **/
	@Getter
	@Setter
	private Long staffAttrSpecSortId;
	/**
	 *组织树类型.
	 **/
	@Getter
	@Setter
	private String treeType;

	/**
	 * 获取仓库
	 * 
	 * @return
	 */
	public static TreeStaffExtendAttrDao repository() {
		return (TreeStaffExtendAttrDao) ApplicationContextUtil
				.getBean("treeStaffExtendAttrDao");
	}
}
