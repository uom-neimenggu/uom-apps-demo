package cn.ffcs.uom.staffrole.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Messagebox;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.staffrole.action.bean.StaffRoleEditBean;
import cn.ffcs.uom.staffrole.constants.StaffRoleConstants;
import cn.ffcs.uom.staffrole.manager.RoleRuleManager;
import cn.ffcs.uom.staffrole.manager.StaffRoleManager;
import cn.ffcs.uom.staffrole.model.RoleRule;
import cn.ffcs.uom.staffrole.model.StaffRole;
import cn.ffcs.uom.staffrole.model.StaffRoleRela;

@Controller
@Scope("prototype")
public class StaffRoleEditComposer extends BasePortletComposer {

	private static final long serialVersionUID = 1L;
	private StaffRoleEditBean bean = new StaffRoleEditBean();
	private Staff staff;// 员工页面传送过来的员工
	private StaffRoleRela staffRoleRela;// 角色页面选择的员工关系
	private StaffRole staffRole;
	private String opType;
	@Autowired
	private StaffRoleManager staffRoleManager;
	@Autowired
	private RoleRuleManager roleRuleManager;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
		/*
		 * 让staffListbox有多选按钮
		 */
		Events.postEvent(SffOrPtyCtants.ON_SET_STAFF_RELA,
				bean.getStaffBandboxExt(), null);
	}

	public void onCreate$staffRoleEditWindow() throws Exception {
		bindBean();
	}

	public void bindBean() throws Exception {
		opType = (String) arg.get("opType");
		if ("add".equals(opType)) {
			bean.getStaffRoleEditWindow().setTitle("新增员工角色");
			staffRoleRela = (StaffRoleRela) arg.get("staffRoleRela");
			if (null != staffRoleRela) {
				staff = staffRoleRela.getStaff();
				staffRole = staffRoleRela.getStaffRole();
				if (null != staff) {
					List<Staff> staffs = new ArrayList<Staff>();
					staffs.add(staff);
					bean.getStaffBandboxExt().setStaff(staff);
					bean.getStaffBandboxExt().setStaffs(staffs);
				}
				if (null != staffRole
						&& !StaffRoleConstants.IS_PARENT.equals(staffRole
								.getIsParent())) {
					List<StaffRole> staffRoles = new ArrayList<StaffRole>();
					staffRoles.add(staffRole);
					bean.getStaffRoleBandboxExt().setStaffRoles(staffRoles);
					bean.getStaffRoleBandboxExt().setStaffRole(staffRole);
				}

			}
		}
	}

	public void onOk() throws Exception {
		List<StaffRole> staffRoles = bean.getStaffRoleBandboxExt()
				.getStaffRoles();
		List<Staff> staffs = bean.getStaffBandboxExt().getStaffs();
		if (null == staffRoles || staffRoles.size() <= 0) {
			ZkUtil.showError("请选择角色。", "提示信息");
			return;
		}
		if (null == staffs || staffs.size() <= 0) {
			ZkUtil.showError("请选择员工。", "提示信息");
			return;
		}
		String msg2 = checkStaffRoles(staffRoles);
		if (msg2 != null) {
			Messagebox.show(msg2);
			return;
		}
		List<StaffRole> staffRoleList2 = staffRoleManager
				.queryStaffaRoles(staffs.get(0));
		if (staffRoleList2 != null && staffRoleList2.size() > 0) {
			for (StaffRole staffRole : staffRoleList2) {
				if (staffRoles.contains(staffRole)) {
					// Messagebox.show("您已经拥有角色"+staffRole.getRoleName());
					// return;
					staffRoles.remove(staffRole);
				}
			}
		}
		List<StaffRole> staffRoleList3 = new ArrayList<StaffRole>();
		staffRoleList3.addAll(staffRoles);
		staffRoleList3.addAll(staffRoleList2);
		String msg3 = checkStaffRoles(staffRoleList3);
		if (msg3 != null) {
			Messagebox.show(msg3);
			return;
		}
		staffRoleManager.saveStaffRoleRela(staffRoles, staffs);
		Events.postEvent("onOK", bean.getStaffRoleEditWindow(), staff);
		ZkUtil.showInformation("员工角色保存成功。", "系统提示");
		bean.getStaffRoleEditWindow().onClose();
	}

	public void onCancel() throws Exception {
		bean.getStaffRoleEditWindow().onClose();
	}

	private String checkStaffRoles(List<StaffRole> staffRoles) {
		if (staffRoles != null && staffRoles.size() > 0) {
			List<RoleRule> roleRules = roleRuleManager.getAllRules();
			HashMap<RoleRule, Integer> onlyRoleMap = new HashMap<RoleRule, Integer>();
			if (roleRules != null && roleRules.size() > 0) {
				for (RoleRule roleRule : roleRules) {
					if ("1".equals(roleRule.getRuleType())) {
						// 单选角色
						onlyRoleMap.put(roleRule, 0);
					}
				}
			}
			for (int i = 0; i < staffRoles.size(); i++) {
				StaffRole staffRole = staffRoles.get(i);
				for (Entry<RoleRule, Integer> entry : onlyRoleMap.entrySet()) {
					if (entry.getKey().getRole() == staffRole.getRoleParentId()) {
						entry.setValue(entry.getValue() + 1);
						if (entry.getValue() > 1) {
							return entry.getKey().getMessage();
						}
					}
				}
			}
		}
		return null;
	}
}
