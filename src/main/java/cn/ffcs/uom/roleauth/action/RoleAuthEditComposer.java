package cn.ffcs.uom.roleauth.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.roleauth.action.bean.RoleAuthEditBean;
import cn.ffcs.uom.roleauth.constants.RoleAuthConstants;
import cn.ffcs.uom.roleauth.manager.AuthorityManager;
import cn.ffcs.uom.roleauth.model.StaffAuthority;
import cn.ffcs.uom.roleauth.model.RoleAuthorityRela;
import cn.ffcs.uom.staffrole.constants.StaffRoleConstants;
import cn.ffcs.uom.staffrole.model.StaffRole;

@Controller
@Scope("prototype")
public class RoleAuthEditComposer extends BasePortletComposer {
	private static final long serialVersionUID = 1L;
	private RoleAuthEditBean bean = new RoleAuthEditBean();
	private String opType;
	private StaffRole staffRole;
	private StaffAuthority authority;
	private RoleAuthorityRela roleAuthRela;
	@Autowired
	private AuthorityManager authorityManager;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}
	
	public void onCreate$roleAuthEditWindow() throws Exception {
		this.bindBean();
	}
	
	public void bindBean() throws Exception {
		opType = (String) arg.get("opType");
		if ("add".equals(opType)) {
			this.bean.getRoleAuthEditWindow().setTitle("新增角色权限关系");
			roleAuthRela = (RoleAuthorityRela) arg.get("roleAuthRela");
			if (roleAuthRela != null) {
				staffRole = roleAuthRela.getStaffRole();
				if(null != staffRole && !StaffRoleConstants.IS_PARENT.equals(staffRole.getIsParent())){
					List<StaffRole> staffRoles = new ArrayList<StaffRole>();
					staffRoles.add(staffRole);
					this.bean.getStaffRoleBandboxExt().setStaffRole(staffRole);
					this.bean.getStaffRoleBandboxExt().setStaffRoles(staffRoles);
				}
				authority = roleAuthRela.getAuthority();
				if(null != authority && !RoleAuthConstants.IS_PARENT.equals(authority.getIsParent())){
					List<StaffAuthority> authoritys = new ArrayList<StaffAuthority>();
					authoritys.add(authority);
					this.bean.getAuthBandboxExt().setAuthority(authority);
					this.bean.getAuthBandboxExt().setAuthoritys(authoritys);
				}
			}
		}
	}
	
	public void onOk() throws Exception {
		List<StaffAuthority> authoritys = this.bean.getAuthBandboxExt().getAuthoritys();
		List<StaffRole> staffRoles = this.bean.getStaffRoleBandboxExt().getStaffRoles();
		if(null == authoritys || authoritys.size() <= 0){
			ZkUtil.showError("请选择权限。", "提示信息");
			return;
		}
		if(null == staffRoles || staffRoles.size() <= 0){
			ZkUtil.showError("请选择角色。", "提示信息");
			return;
		}
		Events.postEvent("onOK", bean.getRoleAuthEditWindow(), authority);
		authorityManager.saveRoleAuthorityRela(authoritys, staffRoles);
		ZkUtil.showInformation("角色权限关系保存成功。", "系统提示");
        bean.getRoleAuthEditWindow().onClose();
	}
	
	public void onCancel() throws Exception {
        bean.getRoleAuthEditWindow().onClose();
	}
	
}
