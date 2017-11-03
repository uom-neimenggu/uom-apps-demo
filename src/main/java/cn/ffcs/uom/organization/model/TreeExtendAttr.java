package cn.ffcs.uom.organization.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.organization.dao.TreeExtendAttrDao;

/**
 *树扩展属性实体.
 * 
 * @author
 * 
 **/
public class TreeExtendAttr extends UomEntity implements Serializable {
	/**
     * .
     */
    private static final long serialVersionUID = 1L;
    /**
	 *树扩展属性标识.
	 **/
	public Long getTreeExtendAttrId() {
		return super.getId();
	}

	public void setTreeExtendAttrId(Long treeExtendAttrId) {
		super.setId(treeExtendAttrId);
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
	 * 获取dao
	 * 
	 * @return
	 */
	public static TreeExtendAttrDao repository() {
		return (TreeExtendAttrDao) ApplicationContextUtil
				.getBean("treeExtendAttrDao");
	}

}
