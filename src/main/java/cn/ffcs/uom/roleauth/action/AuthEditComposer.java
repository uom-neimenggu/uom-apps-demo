package cn.ffcs.uom.roleauth.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.roleauth.action.bean.AuthorityEditExtBean;
import cn.ffcs.uom.roleauth.constants.RoleAuthConstants;
import cn.ffcs.uom.roleauth.manager.AuthorityManager;
import cn.ffcs.uom.roleauth.model.StaffAuthority;

@Controller
@Scope("prototype")
public class AuthEditComposer extends BasePortletComposer {

	private static final long serialVersionUID = 1L;
	private AuthorityEditExtBean bean = new AuthorityEditExtBean();
	private String opType = null;
	@Autowired
	private AuthorityManager authorityManager;
	/**
	 * 权限
	 */
	private StaffAuthority authority;
	/**
	 * 修改的权限
	 */
	private StaffAuthority oldAuthority;
	private boolean isNewInstance = true;
	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}
	
	public void onCreate$authEditWindow() throws Exception {
		opType = StrUtil.strnull(arg.get("opType"));
		oldAuthority = (StaffAuthority) arg.get("authority");
		if ("addRootNode".equals(opType)) {
			this.bean.getAuthEditWindow().setTitle("新增父权限");
		} else if ("addChildNode".equals(opType)) {
			this.bean.getAuthEditWindow().setTitle("新增子权限");
		}else {
			if ("view".equals(opType)) {
				this.bean.getAuthEditWindow().setTitle("查看权限");
				this.bean.getOkButton().setVisible(false);
				this.bean.getCancelButton().setVisible(false);
			}else{
				this.bean.getAuthEditWindow().setTitle("修改权限");
			}
			if (oldAuthority != null) {
				this.bean.getAuthName().setValue(oldAuthority.getAuthorityName());
				isNewInstance = false;
			}
		}
	}

	public void onOk() {
		String sysName = this.bean.getAuthName().getValue();
		if (StrUtil.isEmpty(sysName)){
			ZkUtil.showError("权限名称不能为空。", "提示信息");
			return;
		}
		if ("addRootNode".equals(opType)) {
			authority = new StaffAuthority();
			authority.setAuthorityParentId(RoleAuthConstants.ROOT_ROLE_AUTH_TREE);
			authority.setIsParent(RoleAuthConstants.IS_PARENT);
		} else if ("addChildNode".equals(opType)) {
			authority = new StaffAuthority();
			authority.setAuthorityParentId(oldAuthority.getAuthorityId());
		} else if ("mod".equals(opType)) {
			authority = oldAuthority;
		}
		authority.setAuthorityName(sysName);
		if (isNewInstance) {
			authorityManager.saveAuthority(authority);
		} else {
			authorityManager.updateAuthority(authority);
		}
		// 抛出成功事件
		Events.postEvent("onOK", bean.getAuthEditWindow(), authority);
		bean.getAuthEditWindow().onClose();
	}
	
	public void onCancel() {
		bean.getAuthEditWindow().onClose();
	}

}
