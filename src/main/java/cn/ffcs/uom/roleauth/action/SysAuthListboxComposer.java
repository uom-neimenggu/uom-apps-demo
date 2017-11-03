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
import cn.ffcs.uom.roleauth.action.bean.SysAuthListboxBean;
import cn.ffcs.uom.roleauth.constants.RoleAuthConstants;
import cn.ffcs.uom.roleauth.manager.AuthorityManager;
import cn.ffcs.uom.roleauth.model.StaffAuthority;
import cn.ffcs.uom.roleauth.model.SysAuthorityRela;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.syslist.model.SysList;

@Controller
@Scope("prototype")
public class SysAuthListboxComposer extends BasePortletComposer implements IPortletInfoProvider {

	private static final long serialVersionUID = 1L;
	private SysAuthListboxBean bean = new SysAuthListboxBean();
	private SysAuthorityRela sysAuthorityRela;
	private SysAuthorityRela qrySysAuthorityRela;
	private StaffAuthority authority;
	private SysList sysList;
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
		bean.getSysAuthListbox().setPageSize(10);
		/**
		 * 点击树控件查询出系统权限关系列表
		 */
		bean.getSysAuthMainWin().addForward(RoleAuthConstants.ON_SELECT_TREE_AUTH, bean.getSysAuthMainWin(),
				RoleAuthConstants.ON_SELECT_TREE_AUTH_RESPONSE);
		/**
		 * 删除树节点事件
		 */
		bean.getSysAuthMainWin().addForward(RoleAuthConstants.ON_DEL_NODE_OK, bean.getSysAuthMainWin(),
				RoleAuthConstants.ON_SELECT_TREE_AUTH_RESPONSE);
		/**
		 * 系统页面选择系统tab事件
		 */
		bean.getSysAuthMainWin().addForward(SffOrPtyCtants.ON_STAFF_PAGE_SELECT_FOR_SYS, bean.getSysAuthMainWin(),
				"onSelectAuthResponse");
	}
	/**
	 * window初始化.
	 * 
	 * @throws Exception 异常
	 */
	public void onCreate$sysAuthMainWin() throws Exception {
		setPagePosition();
	}
	
	public void onSelectAuthResponse(final ForwardEvent event) throws Exception{
		this.authority = (StaffAuthority)event.getOrigin().getData();
		this.querySysAuthListRela();
		this.setButtonValid(true, false);
	}
	
	public void onSelectTreeAuthResponse(final ForwardEvent event) throws Exception{
		this.authority = (StaffAuthority)event.getOrigin().getData();
		this.querySysAuthListRela();
		this.setButtonValid(true, false);
	}
	
	public void onSysAuthoritySelectRequest() throws Exception {
		if (bean.getSysAuthListbox().getSelectedCount() > 0) {
			sysAuthorityRela = (SysAuthorityRela) bean.getSysAuthListbox().getSelectedItem().getValue();
			this.setButtonValid(true, true);
		}
	}
	
	public void onSysAuthorityListPaging() throws Exception {
		this.querySysAuthListRela();
	}
	
	public void onQuery() throws Exception {
		this.bean.getSysAuthListboxPaging().setActivePage(0);
		this.querySysAuthListRela();
	}
	
	public void onReset() throws Exception {
		this.bean.getSysName().setValue("");
		this.bean.getClientCode().setValue("");
	}
	
	private void querySysAuthListRela() throws Exception {
		if(null == qrySysAuthorityRela){
			qrySysAuthorityRela = new SysAuthorityRela();			
		}
		if(null != authority && null != authority.getAuthorityId()){
			qrySysAuthorityRela.setAuthorityId(authority.getAuthorityId());
		}
		if(null != sysList && null != sysList.getSysListId()){
			qrySysAuthorityRela.setSysListId(sysList.getSysListId());
		}else{
			sysList = new SysList();
		}
		sysList.setSysName(this.bean.getSysName().getValue());
		sysList.setClientCode(this.bean.getClientCode().getValue());
		qrySysAuthorityRela.setQrySysList(sysList);
		ListboxUtils.clearListbox(bean.getSysAuthListbox());
		PageInfo pageInfo = authorityManager.querySysAuthorityRela(qrySysAuthorityRela, 
				this.bean.getSysAuthListboxPaging().getActivePage() + 1,
				this.bean.getSysAuthListboxPaging().getPageSize());
		ListModel dataList = new BindingListModelList(pageInfo.getDataList(), true);
		this.bean.getSysAuthListbox().setModel(dataList);
		this.bean.getSysAuthListboxPaging().setTotalSize(NumericUtil.nullToZero(pageInfo.getTotalCount()));
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void onAdd() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(this, ActionKeys.DATA_OPERATING))
			return;
		Map map = new HashMap();
		map.put("opType", "add");
		if(null == sysAuthorityRela){
			sysAuthorityRela = new SysAuthorityRela();
		}
		if(null != authority){
			sysAuthorityRela.setAuthorityId(authority.getAuthorityId());
		}
		if(null != sysList){
			sysAuthorityRela.setSysListId(sysList.getSysListId());
		}
		map.put("sysAuthRela", sysAuthorityRela);
		Window window = (Window) Executions.createComponents("/pages/role_auth/sys_auth_edit.zul", this.self, map);
		window.doModal();
		window.addEventListener("onOK", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getData() != null) {
					authority = (StaffAuthority)event.getData();
					querySysAuthListRela();
				}
			}
		});
	}
	
	public void onDel() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(this, ActionKeys.DATA_OPERATING))
			return;
		if (this.sysAuthorityRela != null && this.sysAuthorityRela != null) {
			ZkUtil.showQuestion("确定要删除吗?", "提示信息", new EventListener() {
				public void onEvent(Event event) throws Exception {
					Integer result = (Integer) event.getData();
					if (result == Messagebox.OK) {
						authorityManager.removeSysAuthorityRela(sysAuthorityRela);
						querySysAuthListRela();
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
			if (PlatformUtil.checkPermission(getThemeDisplay(), getPortletId(), ActionKeys.SYS_AUTH_RELA_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkPermission(getThemeDisplay(), getPortletId(), ActionKeys.SYS_AUTH_RELA_DEL)) {
				canDel = true;
			}
		}
		this.bean.getAddButton().setVisible(canAdd);
		this.bean.getDelButton().setVisible(canDel);
		
	}
}
