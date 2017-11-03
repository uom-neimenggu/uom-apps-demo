package cn.ffcs.uom.organization.zkUtil;

import org.zkoss.zk.ui.Component;
import org.zkoss.zkplus.databind.TypeConverter;

import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.organization.manager.OrganizationManager;
import cn.ffcs.uom.organization.model.Organization;

public class organizationInfoConvert implements TypeConverter {

	public Object coerceToBean(Object arg0, Component arg1) {
		return null;
	}

	public Object coerceToUi(Object arg0, Component arg1) {
		Long id = (Long) arg0;
		OrganizationManager organizationManager = (OrganizationManager) ApplicationContextUtil
				.getBean("organizationManager");
		Organization organization = organizationManager.getById(id);
		if(organization != null) {
			return organization.getOrgName();
		}
		return "";
	}
}
