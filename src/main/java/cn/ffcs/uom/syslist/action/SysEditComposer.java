package cn.ffcs.uom.syslist.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.syslist.action.bean.SysListEditExtBean;
import cn.ffcs.uom.syslist.constants.SysListConstants;
import cn.ffcs.uom.syslist.manager.SysListManager;
import cn.ffcs.uom.syslist.model.SysList;

@Controller
@Scope("prototype")
public class SysEditComposer extends BasePortletComposer {

	private static final long serialVersionUID = 1L;
	private SysListEditExtBean bean = new SysListEditExtBean();
	private String opType = null;
	@Autowired
	private SysListManager sysListManager;
	/**
	 * 系统
	 */
	private SysList sysList;
	/**
	 * 修改的系统
	 */
	private SysList oldSysList;
	private boolean isNewInstance = true;
	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}
	
	public void onCreate$sysEditWindow() throws Exception {
		opType = StrUtil.strnull(arg.get("opType"));
		oldSysList = (SysList) arg.get("sysList");
		if ("addRootNode".equals(opType)) {
			this.bean.getSysEditWindow().setTitle("新增父系统");
		} else if ("addChildNode".equals(opType)) {
			this.bean.getSysEditWindow().setTitle("新增子系统");
		}else {
			if ("view".equals(opType)) {
				this.bean.getSysEditWindow().setTitle("查看系统");
				this.bean.getOkButton().setVisible(false);
				this.bean.getCancelButton().setVisible(false);
			}else{
				this.bean.getSysEditWindow().setTitle("修改系统");
			}
			if (oldSysList != null) {
				this.bean.getSysName().setValue(oldSysList.getSysName());
				this.bean.getClientCode().setValue(oldSysList.getClientCode());
				this.bean.getSysUrl().setValue(oldSysList.getSysUrl());
				this.bean.getTelcomRegionTreeBandbox().setTelcomRegion(sysList.getTelcomRegion());
				isNewInstance = false;
			}
		}
	}

	public void onOk() {
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
		if ("addRootNode".equals(opType)) {
			sysList = new SysList();
			sysList.setRelaDomainId(SysListConstants.ROOT_SYS_LIST_TREE);
			sysList.setIsParent(SysListConstants.IS_PARENT);
		} else if ("addChildNode".equals(opType)) {
			sysList = new SysList();
			sysList.setRelaDomainId(oldSysList.getSysListId());
		} else if ("mod".equals(opType)) {
			sysList = oldSysList;
		}
		sysList.setSysName(sysName);
		sysList.setClientCode(clientCode);
		sysList.setTelecomArea(this.bean.getTelcomRegionTreeBandbox().getTelcomRegion().getTelcomRegionId());
		sysList.setSysUrl(sysUrl);
		if (isNewInstance) {
			sysListManager.saveSysList(sysList);
		} else {
			sysListManager.updateSysList(sysList);
		}
		// 抛出成功事件
		Events.postEvent("onOK", bean.getSysEditWindow(), sysList);
		bean.getSysEditWindow().onClose();
	}
	
	public void onCancel() {
		bean.getSysEditWindow().onClose();
	}

}
