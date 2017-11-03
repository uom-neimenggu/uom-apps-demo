package cn.ffcs.uom.organization.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.organization.dao.TreeOrgExtendAttrDao;

/**
 *树组织扩展属性实体.
 * 
 * @author
 * 
 **/
public class TreeOrgExtendAttr extends UomEntity implements Serializable {
	/**
     * .
     */
    private static final long serialVersionUID = 1L;
    /**
	 *树组织扩展属性标识.
	 **/
	public Long getTreeOrgExtendAttrId() {
		return super.getId();
	}

	public void setTreeOrgExtendAttrId(Long treeOrgExtendAttrId) {
		super.setId(treeOrgExtendAttrId);
	}
	/**
	 *组织属性规格分类标识.
	 **/
	@Getter
	@Setter
	private Long orgAttrSpecSortId;
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
	 * 获取dao
	 * 
	 * @return
	 */
	public static TreeOrgExtendAttrDao repository() {
		return (TreeOrgExtendAttrDao) ApplicationContextUtil
				.getBean("treeOrgExtendAttrDao");
	}
}
