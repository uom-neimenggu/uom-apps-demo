package cn.ffcs.uom.rolePermission.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.rolePermission.dao.RoleAttrValueDao;

/**
 * 角色属性取值实体.
 * 
 * @author
 * 
 **/
public class RoleAttrValue extends UomEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 属性值标识.
	 **/
	public Long getRoleAttrValueId() {
		return super.getId();
	}

	public void setRoleAttrValueId(Long roleAttrValueId) {
		super.setId(roleAttrValueId);
	}

	/**
	 * 属性规格标识.
	 **/
	@Getter
	@Setter
	private Long roleAttrSpecId;

	/**
	 * 属性值.
	 **/
	@Getter
	@Setter
	private String roleAttrValue;

	/**
	 * 属性名称.
	 **/
	@Getter
	@Setter
	private String roleAttrValueName;

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static RoleAttrValueDao repository() {
		return (RoleAttrValueDao) ApplicationContextUtil
				.getBean("roleAttrValueDao");
	}
}
