package cn.ffcs.uom.staffrole.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.staffrole.action.bean.RoleEditExtBean;
import cn.ffcs.uom.staffrole.constants.StaffRoleConstants;
import cn.ffcs.uom.staffrole.manager.StaffRoleManager;
import cn.ffcs.uom.staffrole.model.StaffRole;

@Controller
@Scope("prototype")
public class RoleEditComposer extends BasePortletComposer {

	private static final long serialVersionUID = 1L;
	private RoleEditExtBean bean = new RoleEditExtBean();
	private String opType = null;
	@Autowired
	private StaffRoleManager staffRoleManager;
	/**
	 * 角色
	 */
	private StaffRole staffRole;
	/**
	 * 修改的角色
	 */
	private StaffRole oldStaffRole;
	private boolean isNewInstance = true;
	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}
	
	public void onCreate$roleEditWindow() throws Exception {
		opType = StrUtil.strnull(arg.get("opType"));
		oldStaffRole = (StaffRole) arg.get("staffRole");
		if ("addRootNode".equals(opType)) {
			this.bean.getRoleEditWindow().setTitle("新增父角色");
		} else if ("addChildNode".equals(opType)) {
			this.bean.getRoleEditWindow().setTitle("新增子角色");
		}else {
			if ("view".equals(opType)) {
				this.bean.getRoleEditWindow().setTitle("查看角色");
				this.bean.getOkButton().setVisible(false);
				this.bean.getCancelButton().setVisible(false);
			}else{
				this.bean.getRoleEditWindow().setTitle("修改角色");
			}
			if (oldStaffRole != null) {
				this.bean.getRoleName().setValue(oldStaffRole.getRoleName());
				this.bean.getRoleCode().setValue(oldStaffRole.getRoleCode());
				isNewInstance = false;
			}
		}
	}
	
	public void onOk() {
		String roleName = this.bean.getRoleName().getValue();
		String roleCode = this.bean.getRoleCode().getValue();
		if (StrUtil.isEmpty(roleName)){
			ZkUtil.showError("角色名称不能为空。", "提示信息");
			return;
		}
		if (StrUtil.isEmpty(roleCode)){
			ZkUtil.showError("角色编码不能为空。", "提示信息");
			return;
		}
		if ("addRootNode".equals(opType)) {
			staffRole = new StaffRole();
			staffRole.setRoleParentId(StaffRoleConstants.ROOT_STAFF_ROLE_TREE);
			staffRole.setIsParent(StaffRoleConstants.IS_PARENT);
		} else if ("addChildNode".equals(opType)) {
			staffRole = new StaffRole();
			staffRole.setRoleParentId(oldStaffRole.getRoleId());
		} else if ("mod".equals(opType)) {
			staffRole = oldStaffRole;
		}
		staffRole.setRoleName(roleName);
		staffRole.setRoleCode(roleCode);
		if (isNewInstance) {
			staffRoleManager.saveStaffRole(staffRole);
		} else {
			staffRoleManager.updateStaffRole(staffRole);
		}
		// 抛出成功事件
		Events.postEvent("onOK", bean.getRoleEditWindow(), staffRole);
		bean.getRoleEditWindow().onClose();
	}
	
	public void onCancel() {
		bean.getRoleEditWindow().onClose();
	}

}
