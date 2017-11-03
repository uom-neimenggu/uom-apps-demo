package cn.ffcs.uom.rolePermission.model;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.rolePermission.dao.RbacResourceDao;
import cn.ffcs.uom.systemconfig.model.AttrValue;

public class RbacResource extends UomEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	public Long getRbacResourceId() {
		return super.getId();
	}

	public void setRbacResourceId(Long rbacResourceId) {
		super.setId(rbacResourceId);
	}

	@Getter
	@Setter
	private String rbacResourceCode;

	@Getter
	@Setter
	private String rbacResourceName;

	@Getter
	@Setter
	private String rbacResourceUrl;

	@Getter
	@Setter
	private String rbacResourceLeaf;

	@Getter
	@Setter
	private String rbacResourceDesc;

	/**
	 * 获取叶子节点名
	 */
	public String getRbacResourceLeafName() {
		List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
				"RbacResource", "rbacResourceLeaf", this.getRbacResourceLeaf(),
				BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (list != null && list.size() > 0) {
			return list.get(0).getAttrValueName();
		}
		return "";
	}

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static RbacResourceDao repository() {
		return (RbacResourceDao) ApplicationContextUtil
				.getBean("rbacResourceDao");
	}

}