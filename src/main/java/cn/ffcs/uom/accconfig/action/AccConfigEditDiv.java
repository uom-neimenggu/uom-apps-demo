package cn.ffcs.uom.accconfig.action;

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
import cn.ffcs.uom.accconfig.action.bean.AccConfigEditExtBean;
import cn.ffcs.uom.accconfig.constants.AccConfigConstants;
import cn.ffcs.uom.accconfig.manager.AccConfigManager;
import cn.ffcs.uom.accconfig.model.AccConfig;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.StrUtil;

@Controller
@Scope("prototype")
public class AccConfigEditDiv extends Div implements IdSpace {

	private static final long serialVersionUID = 1L;
	
	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;
	
	private AccConfigEditExtBean bean = new AccConfigEditExtBean();
	
	private final String zul = "/pages/accconfig/comp/acc_edit_ext.zul";
	/**
	 * 选中的树上AccConfig.
	 */
	private AccConfig accConfig;
	
	private AccConfigManager accConfigManager = (AccConfigManager)ApplicationContextUtil.getBean("accConfigManager");;

	public AccConfigEditDiv() throws Exception {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		this.addForward(AccConfigConstants.ON_SELECT_TREE_ACCCONFIG, this, AccConfigConstants.ON_SELECT_TREE_ACCCONFIG_RESPONSE);
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
	public void onSelectTreeAccConfigResponse(final ForwardEvent event) throws Exception {
		try {
			accConfig = (AccConfig)event.getOrigin().getData();			
		} catch (Exception e) {}
		if (this.accConfig == null){
			accConfig = new AccConfig();
		}
		this.bean.getAccName().setValue(accConfig.getAccName());
		this.bean.getEffDateStr().setValue(accConfig.getEffDateStr());
		this.bean.getExpDateStr().setValue(accConfig.getExpDateStr());
		this.bean.getStatusCdName().setValue(accConfig.getStatusCdName());
		this.bean.getAccName().setDisabled(true);
		this.setButtonValid(true, false, false);
	}
	
	public void onEdit() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(), ActionKeys.DATA_OPERATING))
			return;
		this.bean.getAccName().setDisabled(false);
		this.setButtonValid(false, true, true);
	}
	
	public void onSave() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(), ActionKeys.DATA_OPERATING))
			return;
		if(null != accConfig){
			String accName = this.bean.getAccName().getValue();
			if (StrUtil.isEmpty(accName)){
				ZkUtil.showError("配置名称不能为空。", "提示信息");
				return;
			}
			accConfig.setAccName(accName);
			accConfigManager.updateAccConfig(accConfig);
			this.bean.getAccName().setDisabled(true);
			this.setButtonValid(true, false, false);
			/**
			 * 抛出保存事件，用来在配置树中保存更改后的配置名称
			 */
			Events.postEvent(AccConfigConstants.ON_SAVE_ACCCONFIG, this, accConfig);
		}
	}
	
	public void onRecover() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(), ActionKeys.DATA_OPERATING))
			return;
		if(null != accConfig){
			this.bean.getAccName().setValue(accConfig.getAccName());
			this.bean.getAccName().setDisabled(true);
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
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(), ActionKeys.ACC_CONFIG_EDIT)) {
				canEdit = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(), ActionKeys.ACC_CONFIG_SAVE)) {
				canSave = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(), ActionKeys.ACC_CONFIG_RECOVER)) {
				canRecover = true;
			}
		}
		this.bean.getEditButton().setVisible(canEdit);
		this.bean.getSaveButton().setVisible(canSave);
		this.bean.getRecoverButton().setVisible(canRecover);
	}
}
