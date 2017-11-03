package cn.ffcs.uom.syslist.action;

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
import cn.ffcs.uom.syslist.action.bean.SysListEditExtBean;
import cn.ffcs.uom.syslist.constants.SysListConstants;
import cn.ffcs.uom.syslist.manager.SysListManager;
import cn.ffcs.uom.syslist.model.SysList;

@Controller
@Scope("prototype")
public class SysListEditDiv extends Div implements IdSpace {

	private static final long serialVersionUID = 1L;
	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;
	private SysListEditExtBean bean = new SysListEditExtBean();
	
	private final String zul = "/pages/syslist/comp/syslist_edit_ext.zul";
	/**
	 * 选中的树上SysList.
	 */
	private SysList sysList;
	private SysListManager sysListManager = (SysListManager)ApplicationContextUtil.getBean("sysListManager");;

	public SysListEditDiv() throws Exception {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		this.addForward(SysListConstants.ON_SELECT_TREE_SYSLIST, this, SysListConstants.ON_SELECT_TREE_SYSLIST_RESPONSE);
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
	
	public void onSelectTreeSysListResponse(final ForwardEvent event) throws Exception {
		try {
			sysList = (SysList)event.getOrigin().getData();			
		} catch (Exception e) {}
		if (this.sysList == null){
			sysList = new SysList();
		}
		this.bean.getSysName().setValue(sysList.getSysName());
		this.bean.getClientCode().setValue(sysList.getClientCode());
		this.bean.getSysUrl().setValue(sysList.getSysUrl());
		this.bean.getTelcomRegionTreeBandbox().setTelcomRegion(sysList.getTelcomRegion());
		this.bean.getEffDateStr().setValue(sysList.getEffDateStr());
		this.bean.getExpDateStr().setValue(sysList.getExpDateStr());
		this.bean.getStatusCdName().setValue(sysList.getStatusCdName());
		this.bean.getSysName().setDisabled(true);
		this.bean.getClientCode().setDisabled(true);
		this.bean.getSysUrl().setDisabled(true);
		this.bean.getTelcomRegionTreeBandbox().setDisabled(true);
		this.setButtonValid(true, false, false);
	}
	
	public void onEdit() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(), ActionKeys.DATA_OPERATING))
			return;
		this.bean.getSysName().setDisabled(false);
		this.bean.getClientCode().setDisabled(false);
		this.bean.getSysUrl().setDisabled(false);
		this.bean.getTelcomRegionTreeBandbox().setDisabled(false);
		this.setButtonValid(false, true, true);
	}
	
	public void onSave() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(), ActionKeys.DATA_OPERATING))
			return;
		if(null != sysList){
			String sysName = this.bean.getSysName().getValue();
			String clientCode = this.bean.getClientCode().getValue();
			String sysUrl = this.bean.getSysUrl().getValue();
			if (StrUtil.isEmpty(sysName)){
				ZkUtil.showError("系统名称不能为空。", "提示信息");
				return;
			}
			if (StrUtil.isEmpty(clientCode)){
				ZkUtil.showError("系统编码不能为空。", "提示信息");
				return;
			}
			if (StrUtil.isEmpty(sysUrl)){
				ZkUtil.showError("系统URl不能为空。", "提示信息");
				return;
			}
			if (this.bean.getTelcomRegionTreeBandbox() == null || this.bean.getTelcomRegionTreeBandbox().getTelcomRegion() == null){
				ZkUtil.showError("电信管理区域不能为空。", "提示信息");
				return;
			}
			sysList.setSysName(sysName);
			sysList.setClientCode(clientCode);
			sysList.setTelecomArea(this.bean.getTelcomRegionTreeBandbox().getTelcomRegion().getTelcomRegionId());
			sysList.setSysUrl(sysUrl);
			sysListManager.updateSysList(sysList);
			this.bean.getSysName().setDisabled(true);
			this.bean.getClientCode().setDisabled(true);
			this.bean.getSysUrl().setDisabled(true);
			this.bean.getTelcomRegionTreeBandbox().setDisabled(true);
			this.setButtonValid(true, false, false);/**
			 * 抛出保存事件，用来在系统树中保存更改后的系统名称
			 */
			Events.postEvent(SysListConstants.ON_SAVE_SYSLIST, this, sysList);
		}
	}
	
	public void onRecover() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(), ActionKeys.DATA_OPERATING))
			return;
		if(null != sysList){
			this.bean.getSysName().setValue(sysList.getSysName());
			this.bean.getClientCode().setValue(sysList.getClientCode());
			this.bean.getSysUrl().setValue(sysList.getSysUrl());
			this.bean.getTelcomRegionTreeBandbox().setTelcomRegion(sysList.getTelcomRegion());
			this.bean.getSysName().setDisabled(true);
			this.bean.getClientCode().setDisabled(true);
			this.bean.getSysUrl().setDisabled(true);
			this.bean.getTelcomRegionTreeBandbox().setDisabled(true);
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
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(), ActionKeys.SYS_LIST_EDIT)) {
				canEdit = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(), ActionKeys.SYS_LIST_SAVE)) {
				canSave = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(), ActionKeys.SYS_LIST_RECOVER)) {
				canRecover = true;
			}
		}
		this.bean.getEditButton().setVisible(canEdit);
		this.bean.getSaveButton().setVisible(canSave);
		this.bean.getRecoverButton().setVisible(canRecover);
	}
}
