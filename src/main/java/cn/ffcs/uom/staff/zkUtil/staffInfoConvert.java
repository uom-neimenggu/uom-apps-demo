package cn.ffcs.uom.staff.zkUtil;

import org.zkoss.zk.ui.Component;
import org.zkoss.zkplus.databind.TypeConverter;

import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.staff.manager.StaffManager;
import cn.ffcs.uom.staff.model.Staff;

public class staffInfoConvert implements TypeConverter {

	public Object coerceToBean(Object arg0, Component arg1) {
		return null;
	}

	public Object coerceToUi(Object arg0, Component arg1) {
		Long id = (Long) arg0;
		StaffManager staffManager = (StaffManager) ApplicationContextUtil
				.getBean("staffManager");
		Staff staff = staffManager.queryStaff(id);
		if(staff != null) {
			return staff.getStaffName();
		}
		return "";
	}
}
