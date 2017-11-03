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
import cn.ffcs.uom.roleauth.action.bean.SysAuthEditBean;
import cn.ffcs.uom.roleauth.constants.RoleAuthConstants;
import cn.ffcs.uom.roleauth.manager.AuthorityManager;
import cn.ffcs.uom.roleauth.model.StaffAuthority;
import cn.ffcs.uom.roleauth.model.SysAuthorityRela;
import cn.ffcs.uom.syslist.constants.SysListConstants;
import cn.ffcs.uom.syslist.model.SysList;

@Controller
@Scope("prototype")
public class SysAuthEditComposer extends BasePortletComposer {
	private static final long serialVersionUID = 1L;
	private SysAuthEditBean bean = new SysAuthEditBean();
	private String opType;
	private SysList sysList;
	private StaffAuthority authority;
	private SysAuthorityRela sysAuthRela;
	@Autowired
	private AuthorityManager authorityManager;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}
	
	public void onCreate$sysAuthEditWindow() throws Exception {
		this.bindBean();
	}
	
	public void bindBean() throws Exception {
		opType = (String) arg.get("opType");
		if ("add".equals(opType)) {
			this.bean.getSysAuthEditWindow().setTitle("新增系统权限关系");
			sysAuthRela = (SysAuthorityRela) arg.get("sysAuthRela");
			if (sysAuthRela != null) {
				sysList = sysAuthRela.getSysList();
				if(null != sysList && !SysListConstants.IS_PARENT.equals(sysList.getIsParent())){
					List<SysList> sysLists = new ArrayList<SysList>();
					sysLists.add(sysList);
					this.bean.getSysBandboxExt().setSysList(sysList);
					this.bean.getSysBandboxExt().setSysLists(sysLists);
				}
				authority = sysAuthRela.getAuthority();
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
		List<SysList> sysLists = this.bean.getSysBandboxExt().getSysLists();
		if(null == authoritys || authoritys.size() <= 0){
			ZkUtil.showError("请选择权限。", "提示信息");
			return;
		}
		if(null == sysLists || sysLists.size() <= 0){
			ZkUtil.showError("请选择系统。", "提示信息");
			return;
		}
		Events.postEvent("onOK", bean.getSysAuthEditWindow(), authority);
		authorityManager.saveSysAuthorityRela(authoritys, sysLists);
		ZkUtil.showInformation("系统权限关系保存成功。", "系统提示");
        bean.getSysAuthEditWindow().onClose();
	}
	
	public void onCancel() throws Exception {
        bean.getSysAuthEditWindow().onClose();
	}
	
}
