package cn.ffcs.uom.syslist.action;

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
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staffrole.model.StaffRole;
import cn.ffcs.uom.syslist.action.bean.SysRoleListboxBean;
import cn.ffcs.uom.syslist.constants.SysListConstants;
import cn.ffcs.uom.syslist.manager.SysListManager;
import cn.ffcs.uom.syslist.model.SysList;
import cn.ffcs.uom.syslist.model.SysRoleRela;

@Controller
@Scope("prototype")
public class SysRoleListboxComposer extends BasePortletComposer implements IPortletInfoProvider {

	private static final long serialVersionUID = 1L;
	private SysRoleListboxBean bean = new SysRoleListboxBean();
	private SysRoleRela sysRoleRela;
	private SysRoleRela qrySysRoleRela;
	private SysList sysList;
	private StaffRole staffRole;
	@Autowired
	private SysListManager sysListManager;
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
		bean.getSysRoleListbox().setPageSize(10);
		/**
		 * 点击树控件查询出员工系统关系列表
		 */
		bean.getSysRoleMainWin().addForward(SysListConstants.ON_SELECT_TREE_SYSLIST, bean.getSysRoleMainWin(),
				SysListConstants.ON_SELECT_TREE_SYSLIST_RESPONSE);
		/**
		 * 删除树节点事件
		 */
		bean.getSysRoleMainWin().addForward(SysListConstants.ON_DEL_NODE_OK, bean.getSysRoleMainWin(),
				SysListConstants.ON_SELECT_TREE_SYSLIST_RESPONSE);
		/**
		 * 员工页面选择系统tab事件
		 */
		bean.getSysRoleMainWin().addForward(SffOrPtyCtants.ON_STAFF_PAGE_SELECT_FOR_SYS, bean.getSysRoleMainWin(),
				"onSelectSysResponse");
	}
	/**
	 * window初始化.
	 * 
	 * @throws Exception 异常
	 */
	public void onCreate$sysRoleMainWin() throws Exception {
		setPagePosition();
	}
	
	public void onSelectSysResponse(final ForwardEvent event) throws Exception{
		this.staffRole = (StaffRole)event.getOrigin().getData();
		this.querySysRoleListRela();
		this.setButtonValid(true, false);
	}
	
	public void onSelectTreeSysListResponse(final ForwardEvent event) throws Exception{
		this.sysList = (SysList)event.getOrigin().getData();
		this.querySysRoleListRela();
		this.setButtonValid(true, false);
	}
	
	public void onSysRoleSelectRequest() throws Exception {
		if (bean.getSysRoleListbox().getSelectedCount() > 0) {
			sysRoleRela = (SysRoleRela) bean.getSysRoleListbox().getSelectedItem().getValue();
			this.setButtonValid(true, true);
		}
	}
	
	public void onSysRoleListListPaging() throws Exception {
		this.querySysRoleListRela();
	}
	
	public void onQuery() throws Exception {
		this.bean.getSysRoleListboxPaging().setActivePage(0);
		this.querySysRoleListRela();
	}
	
	public void onReset() throws Exception {
		this.bean.getRoleName().setValue("");
		this.bean.getRoleCode().setValue("");
	}
	
	private void querySysRoleListRela() throws Exception {
		if(null == qrySysRoleRela){
			qrySysRoleRela = new SysRoleRela();			
		}
		if(null != sysList && null != sysList.getSysListId()){
			qrySysRoleRela.setSysListId(sysList.getSysListId());
		}
		if(null != staffRole && null != staffRole.getRoleId()){
			qrySysRoleRela.setRoleId(staffRole.getRoleId());
		}else{
			staffRole = new StaffRole();
		}
		staffRole.setRoleName(this.bean.getRoleName().getValue());
		staffRole.setRoleCode(this.bean.getRoleCode().getValue());
		qrySysRoleRela.setQryStaffRole(staffRole);
		ListboxUtils.clearListbox(bean.getSysRoleListbox());
		PageInfo pageInfo = sysListManager.querySysRoleRela(qrySysRoleRela, 
				this.bean.getSysRoleListboxPaging().getActivePage() + 1,
				this.bean.getSysRoleListboxPaging().getPageSize());
		ListModel dataList = new BindingListModelList(pageInfo.getDataList(), true);
		this.bean.getSysRoleListbox().setModel(dataList);
		this.bean.getSysRoleListboxPaging().setTotalSize(NumericUtil.nullToZero(pageInfo.getTotalCount()));
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void onAdd() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(this, ActionKeys.DATA_OPERATING))
			return;
		Map map = new HashMap();
		map.put("opType", "add");
		if(null == sysRoleRela){
			sysRoleRela = new SysRoleRela();
		}
		if(null != sysList){
			sysRoleRela.setSysListId(sysList.getSysListId());
		}
		if(null != staffRole){
			sysRoleRela.setRoleId(staffRole.getRoleId());
		}
		map.put("sysRoleRela", sysRoleRela);
		Window window = (Window) Executions.createComponents("/pages/syslist/sys_role_edit.zul", this.self, map);
		window.doModal();
		window.addEventListener("onOK", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getData() != null) {
					staffRole = (StaffRole)event.getData();
					if(null != staffRole){
						bean.getRoleName().setValue(staffRole.getRoleName());
						bean.getRoleCode().setValue(staffRole.getRoleCode());
					}
					querySysRoleListRela();
				}
			}
		});
	}
	
	public void onDel() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(this, ActionKeys.DATA_OPERATING))
			return;
		if (this.sysRoleRela != null && this.sysRoleRela != null) {
			ZkUtil.showQuestion("确定要删除吗?", "提示信息", new EventListener() {
				public void onEvent(Event event) throws Exception {
					Integer result = (Integer) event.getData();
					if (result == Messagebox.OK) {
						sysListManager.removeSysRoleRela(sysRoleRela);
						querySysRoleListRela();
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
			if (PlatformUtil.checkPermission(getThemeDisplay(), getPortletId(), ActionKeys.ROLE_SYS_LIST_RELA_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkPermission(getThemeDisplay(), getPortletId(), ActionKeys.ROLE_SYS_LIST_RELA_DEL)) {
				canDel = true;
			}
		}
		this.bean.getAddButton().setVisible(canAdd);
		this.bean.getDelButton().setVisible(canDel);
	}
}
