package cn.ffcs.uom.staffrole.action;

import lombok.Getter;
import lombok.Setter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Div;

import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.staffrole.action.bean.RoleEditExtBean;
import cn.ffcs.uom.staffrole.constants.StaffRoleConstants;
import cn.ffcs.uom.staffrole.manager.StaffRoleManager;
import cn.ffcs.uom.staffrole.model.StaffRole;

@Controller
@Scope("prototype")
public class RoleEditDiv extends Div implements IdSpace {

	private static final long serialVersionUID = 1L;
	
	private RoleEditExtBean bean = new RoleEditExtBean();
	
	private final String zul = "/pages/staff_role/comp/role_edit_ext.zul";
	/**
	 * 选中的树上StaffRole.
	 */
	private StaffRole staffRole;
	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;
	private StaffRoleManager staffRoleManager = (StaffRoleManager)ApplicationContextUtil.getBean("staffRoleManager");;
	
	public RoleEditDiv() throws Exception {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		//this.addForward(StaffRoleConstants.ON_SAVE_ROLE, this, "onSaveRole");
		this.addForward(StaffRoleConstants.ON_SELECT_TREE_ROLE, this, StaffRoleConstants.ON_SELECT_TREE_ROLE_RESPONSE);
		this.setButtonValid(true, false, false);
	}
	
	/**
	 * window初始化.
	 * 
	 * @throws Exception 异常
	 */
	public void onCreate() throws Exception {
		setPagePosition();
	}
	
	public void onSelectTreeRoleResponse(final ForwardEvent event) throws Exception {
		try {
			staffRole = (StaffRole)event.getOrigin().getData();			
		} catch (Exception e) {}
		if (this.staffRole == null){
			staffRole = new StaffRole();
		}
		this.bean.getRoleName().setValue(staffRole.getRoleName());
		this.bean.getRoleCode().setValue(staffRole.getRoleCode());
		this.bean.getEffDateStr().setValue(staffRole.getEffDateStr());
		this.bean.getExpDateStr().setValue(staffRole.getExpDateStr());
		this.bean.getStatusCdName().setValue(staffRole.getStatusCdName());
		this.setButtonValid(true, false, false);
	}
	
	public void onEdit() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(), ActionKeys.DATA_OPERATING))
			return;
		this.bean.getRoleName().setDisabled(false);
		this.bean.getRoleCode().setDisabled(false);
		this.setButtonValid(false, true, true);
	}
	
	public void onSave() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(), ActionKeys.DATA_OPERATING))
			return;
		if(null != staffRole){
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
			staffRole.setRoleName(roleName);
			staffRole.setRoleCode(roleCode);
			staffRoleManager.updateStaffRole(staffRole);
			this.bean.getRoleName().setDisabled(true);
			this.bean.getRoleCode().setDisabled(true);
			this.setButtonValid(true, false, false);/**
			 * 抛出保存事件，用来在角色树中保存更改后的角色名称
			 */
			Events.postEvent(StaffRoleConstants.ON_SAVE_ROLE, this, staffRole);
		}
	}
	
	public void onRecover() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(), ActionKeys.DATA_OPERATING))
			return;
		if(null != staffRole){
			this.bean.getRoleName().setValue(staffRole.getRoleName());
			this.bean.getRoleCode().setValue(staffRole.getRoleCode());
			this.bean.getRoleName().setDisabled(true);
			this.bean.getRoleCode().setDisabled(true);
			this.setButtonValid(true, false, false);
		}
	}
	
	private void setButtonValid(final Boolean canEdit, final Boolean canSave, final Boolean canRecover) {
		if (canEdit != null) {
			this.bean.getEditButton().setDisabled(!canEdit);
		}
		this.bean.getSaveButton().setDisabled(!canSave);
		this.bean.getRecoverButton().setDisabled(!canRecover);
	}

	
	/**
	 * 设置页面坐标
	 * 
	 * @throws SystemException
	 * @throws Exception
	 */
	public void setPagePosition()  throws Exception {
		boolean canEdit = false;
		boolean canSave = false;
		boolean canRecover = false;
		
		if (PlatformUtil.isAdmin()) {
			canEdit = true;
			canSave = true;
			canRecover = true;
		} else {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(), ActionKeys.STAFF_ROLE_EDIT)) {
				canEdit = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(), ActionKeys.STAFF_ROLE_SAVE)) {
				canSave = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(), ActionKeys.STAFF_ROLE_RECOVER)) {
				canRecover = true;
			}
		}
		this.bean.getEditButton().setVisible(canEdit);
		this.bean.getSaveButton().setVisible(canSave);
		this.bean.getRecoverButton().setVisible(canRecover);
	}
}
