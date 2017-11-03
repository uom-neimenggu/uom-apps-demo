package cn.ffcs.uom.roleauth.action;

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
import cn.ffcs.uom.roleauth.action.bean.AuthorityEditExtBean;
import cn.ffcs.uom.roleauth.constants.RoleAuthConstants;
import cn.ffcs.uom.roleauth.manager.AuthorityManager;
import cn.ffcs.uom.roleauth.model.StaffAuthority;

@Controller
@Scope("prototype")
public class AuthorityEditDiv extends Div implements IdSpace {

	private static final long serialVersionUID = 1L;
	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;
	private AuthorityEditExtBean bean = new AuthorityEditExtBean();
	
	private final String zul = "/pages/role_auth/comp/authority_edit_ext.zul";
	/**
	 * 选中的树上Authority.
	 */
	private StaffAuthority authority;
	private AuthorityManager authorityManager = (AuthorityManager)ApplicationContextUtil.getBean("authorityManager");;

	public AuthorityEditDiv() throws Exception {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		this.addForward(RoleAuthConstants.ON_SELECT_TREE_AUTH, this, RoleAuthConstants.ON_SELECT_TREE_AUTH_RESPONSE);
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
	
	public void onSelectTreeAuthResponse(final ForwardEvent event) throws Exception {
		try {
			authority = (StaffAuthority)event.getOrigin().getData();			
		} catch (Exception e) {}
		if (this.authority == null){
			authority = new StaffAuthority();
		}
		this.bean.getAuthName().setValue(authority.getAuthorityName());
		this.bean.getEffDateStr().setValue(authority.getEffDateStr());
		this.bean.getExpDateStr().setValue(authority.getExpDateStr());
		this.bean.getStatusCdName().setValue(authority.getStatusCdName());
		this.bean.getAuthName().setDisabled(true);
		this.setButtonValid(true, false, false);
	}
	
	public void onEdit() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(), ActionKeys.DATA_OPERATING))
			return;
		this.bean.getAuthName().setDisabled(false);
		this.setButtonValid(false, true, true);
	}
	
	public void onSave() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(), ActionKeys.DATA_OPERATING))
			return;
		if(null != authority){
			String sysName = this.bean.getAuthName().getValue();
			if (StrUtil.isEmpty(sysName)){
				ZkUtil.showError("权限名称不能为空。", "提示信息");
				return;
			}
			authority.setAuthorityName(sysName);
			authorityManager.updateAuthority(authority);
			this.bean.getAuthName().setDisabled(true);
			this.setButtonValid(true, false, false);
			/**
			 * 抛出保存事件，用来在系统树中保存更改后的权限名称
			 */
			Events.postEvent(RoleAuthConstants.ON_SAVE_AUTH, this, authority);
		}
	}
	
	public void onRecover() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(), ActionKeys.DATA_OPERATING))
			return;
		if(null != authority){
			this.bean.getAuthName().setValue(authority.getAuthorityName());
			this.bean.getAuthName().setDisabled(true);
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
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(), ActionKeys.STAFF_AUTH_EDIT)) {
				canEdit = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(), ActionKeys.STAFF_AUTH_SAVE)) {
				canSave = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(), ActionKeys.STAFF_AUTH_RECOVER)) {
				canRecover = true;
			}
		}
		this.bean.getEditButton().setVisible(canEdit);
		this.bean.getSaveButton().setVisible(canSave);
		this.bean.getRecoverButton().setVisible(canRecover);
		
	}
}