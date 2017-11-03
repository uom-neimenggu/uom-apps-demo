package cn.ffcs.uom.rolePermission.model;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.rolePermission.dao.RbacBusinessSystemDao;
import cn.ffcs.uom.systemconfig.model.AttrValue;

public class RbacBusinessSystem extends UomEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	public Long getRbacBusinessSystemId() {
		return super.getId();
	}

	public void setRbacBusinessSystemId(Long rbacBusinessSystemId) {
		super.setId(rbacBusinessSystemId);
	}

	@Getter
	@Setter
	private String rbacBusinessSystemCode;

	@Getter
	@Setter
	private String rbacBusinessSystemName;

	@Getter
	@Setter
	private String rbacBusinessSystemUrl;

	@Getter
	@Setter
	private String rbacBusinessSystemDomain;

	@Getter
	@Setter
	private String rbacBusinessSystemDesc;

	/**
	 * 获取业务系统域名
	 */
	public String getRbacBusinessSystemDomainName() {
		List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
				"RbacBusinessSystem", "rbacBusinessSystemDomain",
				this.getRbacBusinessSystemDomain(),
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
	public static RbacBusinessSystemDao repository() {
		return (RbacBusinessSystemDao) ApplicationContextUtil
				.getBean("rbacBusinessSystemDao");
	}

}