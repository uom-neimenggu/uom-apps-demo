package cn.ffcs.uom.roleauth.action;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.raptornuke.portal.theme.ThemeDisplay;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.roleauth.action.bean.RoleAuthListboxBean;
import cn.ffcs.uom.roleauth.constants.RoleAuthConstants;
import cn.ffcs.uom.roleauth.manager.AuthorityManager;
import cn.ffcs.uom.roleauth.model.RoleAuthorityRela;
import cn.ffcs.uom.roleauth.model.StaffAuthority;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staffrole.model.StaffRole;

@Controller
@Scope("prototype")
public class RoleAuthListboxComposer extends BasePortletComposer implements IPortletInfoProvider {

	private static final long serialVersionUID = 1L;
	private RoleAuthListboxBean bean = new RoleAuthListboxBean();
	private RoleAuthorityRela roleAuthorityRela;
	private RoleAuthorityRela qryRoleAuthorityRela;
	private StaffAuthority authority;
	private StaffRole staffRole;
	@Autowired
	private AuthorityManager authorityManager;
	@Override
	public String getPortletId() {
		return super.getPortletId();
	}

	@Override
	public ThemeDisplay getThemeDisplay() {
		return super.getThemeDisplay();
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
		this.setButtonValid(true, false);
		bean.getAuthListbox().setPageSize(10);
		/**
		 * 点击树控件查询出角色权限关系列表
		 */
		bean.getRoleAuthMainWin().addForward(RoleAuthConstants.ON_SELECT_TREE_AUTH, bean.getRoleAuthMainWin(),
				RoleAuthConstants.ON_SELECT_TREE_AUTH_RESPONSE);
		/**
		 * 删除树节点事件
		 */
		bean.getRoleAuthMainWin().addForward(RoleAuthConstants.ON_DEL_NODE_OK, bean.getRoleAuthMainWin(),
				RoleAuthConstants.ON_SELECT_TREE_AUTH_RESPONSE);
		/**
		 * 角色页面选择系统tab事件
		 */
		bean.getRoleAuthMainWin().addForward(SffOrPtyCtants.ON_STAFF_PAGE_SELECT_FOR_SYS, bean.getRoleAuthMainWin(),
				"onSelectAuthResponse");
	}
	
	/**
	 * window初始化.
	 * 
	 * @throws Exception 异常
	 */
	public void onCreate$roleAuthMainWin() throws Exception {
		setPagePosition();
	}
	
	public void onSelectAuthResponse(final ForwardEvent event) throws Exception{
		this.authority = (StaffAuthority)event.getOrigin().getData();
		this.queryAuthListRela();
		this.setButtonValid(true, false);
	}
	
	public void onSelectTreeAuthResponse(final ForwardEvent event) throws Exception{
		this.authority = (StaffAuthority)event.getOrigin().getData();
		this.queryAuthListRela();
		this.setButtonValid(true, false);
	}
	
	public void onAuthoritySelectRequest() throws Exception {
		if (bean.getAuthListbox().getSelectedCount() > 0) {
			roleAuthorityRela = (RoleAuthorityRela) bean.getAuthListbox().getSelectedItem().getValue();
			this.setButtonValid(true, true);
		}
	}
	
	public void onAuthorityListPaging() throws Exception {
		this.queryAuthListRela();
	}
	
	public void onQuery() throws Exception {
		this.bean.getAuthListboxPaging().setActivePage(0);
		this.queryAuthListRela();
	}
	
	public void onReset() throws Exception {
		this.bean.getRoleName().setValue("");
		this.bean.getRoleCode().setValue("");
	}
	
	private void queryAuthListRela() throws Exception {
		if(null == qryRoleAuthorityRela){
			qryRoleAuthorityRela = new RoleAuthorityRela();			
		}
		if(null != authority && null != authority.getAuthorityId()){
			qryRoleAuthorityRela.setAuthorityId(authority.getAuthorityId());
		}
		if(null != staffRole && null != staffRole.getRoleId()){
			qryRoleAuthorityRela.setRoleId(staffRole.getRoleId());
		}else{
			staffRole = new StaffRole();
		}
		staffRole.setRoleName(this.bean.getRoleName().getValue());
		staffRole.setRoleCode(this.bean.getRoleCode().getValue());
		qryRoleAuthorityRela.setQryStaffRole(staffRole);
		ListboxUtils.clearListbox(bean.getAuthListbox());
		PageInfo pageInfo = authorityManager.queryRoleAuthorityRela(qryRoleAuthorityRela, 
				this.bean.getAuthListboxPaging().getActivePage() + 1,
				this.bean.getAuthListboxPaging().getPageSize());
		ListModel dataList = new BindingListModelList(pageInfo.getDataList(), true);
		this.bean.getAuthListbox().setModel(dataList);
		this.bean.getAuthListboxPaging().setTotalSize(NumericUtil.nullToZero(pageInfo.getTotalCount()));
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void onAdd() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(this, ActionKeys.DATA_OPERATING))
			return;
		Map map = new HashMap();
		map.put("opType", "add");
		if(null == roleAuthorityRela){
			roleAuthorityRela = new RoleAuthorityRela();
		}
		if(null != authority){
			roleAuthorityRela.setAuthorityId(authority.getAuthorityId());
		}
		if(null != staffRole){
			roleAuthorityRela.setRoleId(staffRole.getRoleId());
		}
		map.put("roleAuthRela", roleAuthorityRela);
		Window window = (Window) Executions.createComponents("/pages/role_auth/role_auth_edit.zul", this.self, map);
		window.doModal();
		window.addEventListener("onOK", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getData() != null) {
					authority = (StaffAuthority)event.getData();
					queryAuthListRela();
				}
			}
		});
	}
	
	public void onDel() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(this, ActionKeys.DATA_OPERATING))
			return;
		if (this.roleAuthorityRela != null && this.roleAuthorityRela != null) {
			ZkUtil.showQuestion("确定要删除吗?", "提示信息", new EventListener() {
				public void onEvent(Event event) throws Exception {
					Integer result = (Integer) event.getData();
					if (result == Messagebox.OK) {
						authorityManager.removeRoleAuthorityRela(roleAuthorityRela);
						queryAuthListRela();
						setButtonValid(true, false);						
					}
				}
			});
		} else {
			ZkUtil.showError("请选择你要删除的记录。", "提示信息");
			return;
		}
	}
	
	private void setButtonValid(final Boolean canAdd, final Boolean canDelete) {
		if (canAdd != null) {
			this.bean.getAddButton().setDisabled(!canAdd);
		}
		this.bean.getDelButton().setDisabled(!canDelete);
	}
	
	
	/**
	 * 设置页面坐标
	 * 
	 * @throws SystemException
	 * @throws Exception
	 */
	public void setPagePosition()  throws Exception {
		boolean canAdd = false;
		boolean canDel = false;
		
		if (PlatformUtil.isAdmin()) {
			canAdd = true;
			canDel = true;
		} else {
			if (PlatformUtil.checkPermission(getThemeDisplay(), getPortletId(), ActionKeys.ROLE_AUTH_RELA_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkPermission(getThemeDisplay(), getPortletId(), ActionKeys.ROLE_AUTH_RELA_DEL)) {
				canDel = true;
			}
		}
		this.bean.getAddButton().setVisible(canAdd);
		this.bean.getDelButton().setVisible(canDel);
		
		
		
	}
}
