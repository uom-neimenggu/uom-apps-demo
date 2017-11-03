package cn.ffcs.uom.syslist.action;

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
import cn.ffcs.uom.staffrole.constants.StaffRoleConstants;
import cn.ffcs.uom.staffrole.model.StaffRole;
import cn.ffcs.uom.syslist.action.bean.SysRoleEditBean;
import cn.ffcs.uom.syslist.constants.SysListConstants;
import cn.ffcs.uom.syslist.manager.SysListManager;
import cn.ffcs.uom.syslist.model.SysList;
import cn.ffcs.uom.syslist.model.SysRoleRela;

@Controller
@Scope("prototype")
public class SysRoleEditComposer extends BasePortletComposer {
	private static final long serialVersionUID = 1L;
	private SysRoleEditBean bean = new SysRoleEditBean();
	private String opType;
	private StaffRole staffRole;
	private SysList sysList;
	private SysRoleRela sysRoleRela;
	@Autowired
	private SysListManager sysListManager;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}
	
	public void onCreate$sysRoleEditWindow() throws Exception {
		this.bindBean();
	}
	
	public void bindBean() throws Exception {
		opType = (String) arg.get("opType");
		if ("add".equals(opType)) {
			this.bean.getSysRoleEditWindow().setTitle("新增角色系统关系");
			sysRoleRela = (SysRoleRela) arg.get("sysRoleRela");
			if (sysRoleRela != null) {
				staffRole = sysRoleRela.getStaffRole();
				sysList = sysRoleRela.getSysList();
				if(null != staffRole && !StaffRoleConstants.IS_PARENT.equals(staffRole.getIsParent())){
					this.bean.getStaffRoleBandboxExt().setStaffRole(staffRole);
					List<StaffRole> staffRoles = new ArrayList<StaffRole>();
					staffRoles.add(staffRole);
					this.bean.getStaffRoleBandboxExt().setStaffRoles(staffRoles);
				}
				if(null != sysList && !SysListConstants.IS_PARENT.equals(sysList.getIsParent())){
					List<SysList> sysLists = new ArrayList<SysList>();
					sysLists.add(sysList);
					this.bean.getSysListBandboxExt().setSysList(sysList);
					this.bean.getSysListBandboxExt().setSysLists(sysLists);
				}
			}
		}
	}
	
	public void onOk() throws Exception {
		List<SysList> sysLists = this.bean.getSysListBandboxExt().getSysLists();
		List<StaffRole> staffRoles = this.bean.getStaffRoleBandboxExt().getStaffRoles();
		if(null == sysLists || sysLists.size() <= 0){
			ZkUtil.showError("请选择系统。", "提示信息");
			return;
		}
		if(null == staffRoles || staffRoles.size() <= 0){
			ZkUtil.showError("请选择角色。", "提示信息");
			return;
		}
		Events.postEvent("onOK", bean.getSysRoleEditWindow(), staffRole);
		sysListManager.saveSysRoleRelas(staffRoles, sysLists);
		ZkUtil.showInformation("角色系统关系保存成功。", "系统提示");
        bean.getSysRoleEditWindow().onClose();
	}
	
	public void onCancel() throws Exception {
        bean.getSysRoleEditWindow().onClose();
	}
	
}
