package cn.ffcs.uom.staff.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.staff.dao.TreeOrgStaffExtendAttrDao;

/**
 *树组织员工扩展属性实体.
 * 
 * @author
 * 
 **/
public class TreeOrgStaffExtendAttr extends UomEntity implements Serializable {

	/**
     * .
     */
    private static final long serialVersionUID = 1L;
    /**
	 *员工树组织属性标识.
	 **/
	public Long getTreeOrgStaffExtendAttrId() {
		return super.getId();
	}

	public void setTreeOrgStaffExtendAttrId(Long treeOrgStaffExtendAttrId) {
		super.setId(treeOrgStaffExtendAttrId);
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
	 *组织标识.
	 **/
	@Getter
	@Setter
	private Long orgId;

	/**
	 * 获取仓库
	 * 
	 * @return
	 */
	public static TreeOrgStaffExtendAttrDao repository() {
		return (TreeOrgStaffExtendAttrDao) ApplicationContextUtil
				.getBean("treeOrgStaffExtendAttrDao");
	}
}
