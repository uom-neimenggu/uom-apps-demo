package cn.ffcs.uom.rolePermission.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.rolePermission.dao.PermissionAttrValueDao;

/**
 * 权限属性取值实体.
 * 
 * @author
 * 
 **/
public class PermissionAttrValue extends UomEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 属性值标识.
	 **/
	public Long getPermissionAttrValueId() {
		return super.getId();
	}

	public void setPermissionAttrValueId(Long permissionAttrValueId) {
		super.setId(permissionAttrValueId);
	}

	/**
	 * 属性规格标识.
	 **/
	@Getter
	@Setter
	private Long permissionAttrSpecId;

	/**
	 * 属性值.
	 **/
	@Getter
	@Setter
	private String permissionAttrValue;

	/**
	 * 属性名称.
	 **/
	@Getter
	@Setter
	private String permissionAttrValueName;

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static PermissionAttrValueDao repository() {
		return (PermissionAttrValueDao) ApplicationContextUtil
				.getBean("permissionAttrValueDao");
	}
}
